package battleships;

import battleships.game.*;
import battleships.model.*;
import battleships.network.*;
import java.io.IOException;
import java.util.Scanner;

public final class GameController {
    private final Board myBoard;
    private final NetworkConnector net;
    private final Scanner scanner = new Scanner(System.in);
    private final OpponentBoard opponentBoard = new OpponentBoard();


    public GameController(Board myBoard, NetworkConnector net) {

        this.myBoard = myBoard;
        this.net = net;
    }

    public void runAsClient() throws Exception {

        Coordinate firstShot = askPlayerForShot();
        Message start = new Message(CommandType.START, firstShot);
        net.send(start);
        gameLoop(start);
    }

    public void runAsServer() throws Exception {

        Message startMsg = null;
        while (startMsg == null)
            startMsg = net.receiveWithRetry(null);

        System.out.println("Serwer odebrał: " + startMsg);
        if (startMsg.getCommandType() != CommandType.START)
            throw new IOException("Oczekiwano komendy START, otrzymano: " + startMsg);


        Result res = myBoard.applyShot(startMsg.coordinate());

        if (res == Result.LAST_SUNK) {
            net.send(new Message(CommandType.LAST_SUNK, null));
            System.out.println("Mapa przeciwnika:");
            System.out.println(opponentBoard.outputBoard() + "\n");
            System.out.println("Moja mapa:");
            System.out.println(myBoard.outputBoard());
            return;
        }
        CommandType replyCommand = commandForResult(res);
        Coordinate myShot = askPlayerForShot();
        Message reply = new Message(replyCommand, myShot);
        net.send(reply);
        gameLoop(reply);
    }

    private Coordinate askPlayerForShot() {

        while (true) {
            System.out.print("Podaj współrzędne strzału: ");
            String input = scanner.nextLine();
            try {
                return Coordinate.parse(input);
            } catch (Exception e) {
                System.out.println("Błędny format, spróbuj ponownie.");
            }
        }
    }

    private void gameLoop(Message lastSent) throws Exception {

        Message lastMessage = lastSent;

        while (true) {

            Message incoming = net.receiveWithRetry(lastMessage);
            CommandType myResult = incoming.command();
            switch (myResult) {
                case MISS -> opponentBoard.markWater(lastMessage.coordinate());
                case HIT -> opponentBoard.markHit(lastMessage.coordinate());
                case SUNK, LAST_SUNK -> opponentBoard.markSunk(lastMessage.coordinate());
                default -> { }
            }

            if (myResult == CommandType.LAST_SUNK) {
                System.out.println("Wygrana");
                System.out.println("Mapa przeciwnika:");
                opponentBoard.revealAllUnknownAsWater();
                System.out.println(opponentBoard.outputBoard() + '\n');
                System.out.println("Moja mapa:");
                System.out.println(myBoard.outputBoard());
                return;
            }

            Result res = myBoard.applyShot(incoming.coordinate());

            if (res == Result.LAST_SUNK) {
                net.send(new Message(CommandType.LAST_SUNK, null));
                System.out.println("Przegrana");
                System.out.println("Mapa przeciwnika:");
                System.out.println(opponentBoard.outputBoard() + '\n');
                System.out.println("Moja mapa:");
                System.out.println(myBoard.outputBoard());
                return;
            }

            CommandType opponentResult = commandForResult(res);
            Coordinate myShot = askPlayerForShot();
            Message reply = new Message(opponentResult, myShot);
            net.send(reply);
            lastMessage = reply;
        }
    }

    private CommandType commandForResult(Result res) {

        return switch (res) {
            case MISS -> CommandType.MISS;
            case HIT -> CommandType.HIT;
            case SUNK -> CommandType.SUNK;
            case LAST_SUNK -> CommandType.LAST_SUNK;
        };
    }

}

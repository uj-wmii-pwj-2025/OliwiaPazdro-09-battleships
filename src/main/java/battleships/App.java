package battleships;

import battleships.game.Board;
import battleships.network.NetworkConnector;
import battleships.util.*;

public final class App {
    public static void main(String[] args) {

        try {
            Configuration cfg = Configuration.parseArgs(args);

            Board myBoard;
            try {
                myBoard = Board.fromFile(cfg.mapPath);
            } catch (Exception e) {
                System.out.println("Nie udało się wczytać mapy, generowanie losowej: " + e.getMessage());
                BattleshipGenerator gen = BattleshipGenerator.defaultInstance();
                myBoard = Board.fromString(gen.generateMap());
            }
            System.out.println("Moja mapa:");
            System.out.println(myBoard.outputBoard());

            NetworkConnector net = (cfg.mode == Configuration.Mode.SERVER) ? NetworkConnector.server(cfg.port, cfg.TIMEOUT)
                                    : NetworkConnector.client(cfg.host, cfg.port, cfg.TIMEOUT);

            GameController game = new GameController(myBoard, net);
            if (cfg.mode == Configuration.Mode.CLIENT)
                game.runAsClient();
            else
                game.runAsServer();

            net.close();
        } catch (Exception e) {
            System.out.println("Błąd komunikacji");
            e.printStackTrace();
        }
    }
}

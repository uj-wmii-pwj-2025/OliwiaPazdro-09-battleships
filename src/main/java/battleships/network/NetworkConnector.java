package battleships.network;

import battleships.model.Message;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public final class NetworkConnector implements Closeable {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[95m";
    private static final String RESET = "\u001B[0m";

    NetworkConnector(Socket socket, int timeout) throws IOException {

        this.socket = socket;
        this.socket.setSoTimeout(timeout);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public static NetworkConnector server(int port, int timeout) throws IOException {

        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();
        ss.close();
        return new NetworkConnector(s, timeout);
    }

    public static NetworkConnector client(String host, int port, int timeout) throws IOException {

        Socket s = new Socket();
        s.connect(new InetSocketAddress(host, port), 30000);
        return new NetworkConnector(s, timeout);
    }

    public synchronized void send(Message msg) throws IOException {

        String line = msg.serialize();
        System.out.print(BLUE + "SEND: " + line + RESET);
        out.write(line);
        out.flush();
    }

    public synchronized Message receiveWithRetry(Message lastSent) throws IOException {

        int attempts = 0;

        while (attempts < 3) {
            try {
                String line = in.readLine();
                if (line == null)
                    throw new SocketException("Rozłączenie");

                System.out.println(MAGENTA + "RECV: " + line + RESET);
                return Message.parse(line);

            } catch (SocketTimeoutException | IllegalArgumentException e) {

                attempts++;
                if (lastSent != null)
                    send(lastSent);
            }
        }

        System.out.println("Błąd komunikacji");
        throw new IOException("Błąd komunikacji");
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}

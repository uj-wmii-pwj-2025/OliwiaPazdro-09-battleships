package battleships.util;

import java.util.*;

public final class Configuration {
    public enum Mode {
        SERVER,
        CLIENT
    }

    public final Mode mode;
    public final int port;
    public final String host;
    public final String mapPath;
    public final int TIMEOUT = 60000;


    private Configuration(Mode mode, int port, String host, String mapPath) {
        this.mode = mode;
        this.port = port;
        this.host = host;
        this.mapPath = mapPath;
    }

    public static Configuration parseArgs(String[] args) {

        Map<String,String> p = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2)
            p.put(args[i], args[i+1]);

        Mode mode = "server".equalsIgnoreCase(p.getOrDefault("-mode","")) ? Mode.SERVER : Mode.CLIENT;

        int port = Integer.parseInt(p.getOrDefault("-port", "5000"));
        String host = p.getOrDefault("-host", "localhost");
        String map = p.getOrDefault("-map", "map.txt");

        if (mode == Mode.CLIENT && (host == null || host.isEmpty()))
            throw new IllegalArgumentException("Client wymaga -host");
        return new Configuration(mode, port, host, map);
    }
}

package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        int inputPort;
        if (args.length < 1) {
            throw new IllegalArgumentException();
        }
        try {
            inputPort = Integer.decode(args[0]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException();
        }

        Database data=Database.getInstance();
        data.initialize("./Courses.txt");
        Server tcp=Server.threadPerClient(inputPort, BGRSProtocol::new, BGRSEncoderDecoder::new);
        tcp.serve();
    }
}

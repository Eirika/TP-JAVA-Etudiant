package parserEtud;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurHTTPSax {

    static String path = System.getProperty("user.dir") + "/html/";
    static File listeEtudiant = new File(path + "/listeEtudiantsXML.xml");

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        // args 0 : l'adresse locale en notation point�e
        // args 1 : le port local
        InetSocketAddress sa = new InetSocketAddress("127.0.0.1", 80);//port 80 pour le protocole HTTP
        ServerSocket ss = null;
        try {
            ss = new ServerSocket();
            ss.bind(sa);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                Socket sock = ss.accept();
                ThreadServeurHTTPSax serv = new ThreadServeurHTTPSax(sock);
                serv.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ClientHandler.ClientHandler;

public class Server {


    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){

        try{

            System.out.println("server: "+serverSocket.getInetAddress().getHostAddress()+":");

            while(!serverSocket.isClosed()){

                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected. ");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clienteThread = new Thread(clientHandler);
                clienteThread.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public void stopServer(){


        try{

            if(serverSocket != null){

                serverSocket.close();

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) throws IOException {


        ServerSocket server = new ServerSocket(1234);
        Server server1 = new Server(server);
        server1.startServer();

    }





}

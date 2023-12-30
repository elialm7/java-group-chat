package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private String username;


    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
            try{
                if(bufferedReader != null ){
                    bufferedWriter.close();
                }
                if(bufferedWriter != null){
                    bufferedReader.close();
                }
                if(socket!= null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void sendMessage(){

        try{
            writer.write(username);
            writer.newLine();
            writer.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messagetosend = scanner.nextLine();
                writer.write(username+": "+ messagetosend);
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
           closeEverything(socket, reader, writer);
        }


    }


    public void listenForMessage(){
        new Thread(() -> {
            String message;
            while(socket.isConnected()){
                try{
                    message = reader.readLine();
                    System.out.println(message);
                } catch (IOException e) {
                    closeEverything(socket, reader, writer);
                }

            }
        }).start();
    }


    public static void main(String[] args) throws IOException {


        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the chatgroup: ");
        String name = scanner.nextLine();

        Socket sck = new Socket("localhost", 1234);
        Client client = new Client(sck, name);
        client.listenForMessage();
        client.sendMessage();


    }


}

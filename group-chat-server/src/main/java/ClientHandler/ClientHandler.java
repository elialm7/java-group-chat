package ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{




    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket){
        this.socket = socket;
        try{
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: "+clientUserName + "has entered the chat");
        } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void broadcastMessage(String message){
        for(ClientHandler clientHandler: clientHandlers){
            try{
              if(!clientHandler.clientUserName.equalsIgnoreCase(clientUserName)){
                  clientHandler.bufferedWriter.write(message);
                  clientHandler.bufferedWriter.newLine();
                  clientHandler.bufferedWriter.flush();
              }
            } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
       removeClientHandler();

        try {
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

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server: "+clientUserName+ "has left the chat. ");
    }



    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;
                // close and break
            }
        }

    }
}

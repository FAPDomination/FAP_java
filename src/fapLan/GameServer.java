package fapLan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import java.net.Socket;

public class GameServer extends Thread {
    private Socket socket;
    private Host host;
    public GameServer(Socket s, Host host){
        socket = s;
        this.host = host;
    }
    public void run(){
        traitement();
    }
    
    public void traitement(){
        // Gestion des messages
        try{
            String message = "";
            //System.out.println("Message de "+socket.getInetAddress());
            // In c'est ce qui arrive du client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out c'est ce que je lui renvoie
            //PrintStream out = new PrintStream(socket.getOutputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            message = in.readLine();
            // Teste le type de message
            if(message!=null){
                if(message.charAt(0) == 'g'){
                    out.writeObject(host.getGame());
                    //out.close();
                    //out.print(host.getGame());
                }
            }
            out.close();
            socket.close();
        }
        catch (ClassCastException cc){
            System.out.println("Couldn't cast");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
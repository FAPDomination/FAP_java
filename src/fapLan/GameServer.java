package fapLan;

import fap_java.Cell;
import fap_java.Player;

import fap_java.Team;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.net.Socket;

import java.nio.channels.SocketChannel;

import java.util.ArrayList;

public class GameServer extends Thread {
    private SocketChannel  socket;
    private Host host;
    public GameServer(SocketChannel s, Host host){
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.socket().getInputStream()));
            // out c'est ce que je lui renvoie
            //PrintStream out = new PrintStream(socket.getOutputStream());
            OutputStream out = null;
            message = in.readLine();
            //System.out.println("Arriving "+message);
            // Teste le type de message
            if(message!=null){
                if(message.charAt(0) == 'g'){
                    out = new ObjectOutputStream(socket.socket().getOutputStream());
                    ((ObjectOutputStream)out).writeObject(host.getGame());
                    //out.close();
                    //out.print(host.getGame());
                }
                else if(message.charAt(0) == 'a'){
                    ArrayList<Player> players = host.getGame().getPlayers();
                    int charac = 1;
                    int pid = players.size();
                    Cell c = host.getGame().getStartCell(pid);
                    Team team = host.getGame().getTeams().get(0);
                    int ai = 0;
                    Player p = host.getGame().generatePlayer(charac, pid, c, team, ai, 0,host.getGame());

                    players.add(p);
                    
                    out = new PrintStream(socket.socket().getOutputStream());
                    ((PrintStream)out).println(pid);
                }
                else if(message.charAt(0) == 'k'){
                    int pid = Integer.parseInt(""+message.charAt(1));
                    int keyID = Integer.parseInt(""+message.charAt(2));
                    int pressed = Integer.parseInt(""+message.charAt(3));
                    int[][] keys = host.getGame().getPlayers().get(pid).getKeys();
                    keys[keyID][1] = pressed;
                }
                else if(message.charAt(0) == 's'){
                    System.out.println("Asked if I'm a server");
                    out = new PrintStream(socket.socket().getOutputStream());
                    ((PrintStream)out).print(true);
                }
                else if(message.charAt(0)=='h'){
                    out = new PrintStream(socket.socket().getOutputStream());
                    ((PrintStream)out).println(host.getGame().getPlayers().size());
                }
            }
            if(out != null){
                out.close();
            }
            socket.close();
        }
        catch (ClassCastException cc){
            System.out.println("Couldn't cast in GameServer.java");
        }
        catch(Exception e){
            System.out.println("Exception in GameServer.java : "+e.toString());
        }
        
    }
}
package fapLan;

import fap_java.Game;

import fap_java.Params;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Host implements Runnable {
    private Game game;
    public Host(Game game) {
        super();
        this.game = game;
    }


    public void run() {
        try{
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            System.out.println("Serveur lancé");
            ssChannel.configureBlocking(true);
            ssChannel.socket().bind(new InetSocketAddress(Params.port));
            while(true){
                SocketChannel sChannel = ssChannel.accept();
                GameServer t = new GameServer(sChannel,this);
                t.start();
            }
        }
        catch(Exception e){
            System.out.println("Exception in Host.java : "+e.toString());
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}

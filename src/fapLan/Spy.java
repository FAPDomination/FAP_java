package fapLan;

import java.net.InetAddress;

public class Spy implements Runnable{
    private String i;
    private Thread th;
    private FindServersPanel client;
    private int delay;

    public Spy(String i,FindServersPanel client, int delay) {
        this.i = i;
        this.client = client;
        this.delay=delay;
    }

    public void run() {
        try {
            InetAddress in;
            in = InetAddress.getByName(i);
            if (in.isReachable(delay)) {
                System.out.println("IP: "+i + " Hostname: " + in.getHostName());
                client.askServer(i);
                th.interrupt();
            }
            else{
                th.interrupt(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTh(Thread th) {
        this.th = th;
    }

    public Thread getTh() {
        return th;
    }
}
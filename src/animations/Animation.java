package animations;

import fap_java.TheComputingThread;

import java.awt.Graphics;


public abstract class Animation {

    protected int x;
    protected int y;
    protected int duration; // in ms
    protected int position; // in ms
    protected TheComputingThread thread;
    protected boolean running;

    public Animation(int x, int y, int duration, TheComputingThread thread) {
        this.x = x;
        this.y = y;

        this.duration = duration;
        this.position = 0;

        this.thread = thread;
        thread.getMyGame().addAnim(this);
        running = true;
    }

    public abstract String toString();

    public abstract void paintComponent(Graphics g);

    public void executeAnimation() {
        position += thread.getDelay();
        if (position >= duration) {
            endAnimation();
        }
    }

    public void endAnimation() {
        running = false;
        thread.getMyGame().deleteAnim(this);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void setThread(TheComputingThread thread) {
        this.thread = thread;
    }

    public TheComputingThread getThread() {
        return thread;
    }
}

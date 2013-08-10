package fap_java;

public class Tools {
    /**
     * Picks a random int between two value
     * @param min : min boundary
     * @param max : max boundary
     * @return : a random integer between min and max
     */
    public static int randRange(int min, int max) {
        int randomNum = (int)(Math.random() * (max - min + 1) + min);
        return randomNum;
    }
}

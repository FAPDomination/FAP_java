package fap_java;

public class MapTools {
    public static final int FAC = 2;
    public static final int TH = 60/FAC;
    public static final int TW = 35/FAC;
    public static final int OFFMAP = 0;
    
   public static int[] giveTalePosition(int i, int j) {
            int[] arr = new int[2];
            // calculate the corresponding position
            arr[0] = j*TW+(TW/2)*(i%2);
            arr[1] = i*(TH)*(1-1/4)+OFFMAP;
            return arr;
    };
    
   public static int[] givePositionTale(int x, int y) {
        int[] arr = new int[2];
            // Undo the calculus of the position
            arr[0] = Math.round(((y-OFFMAP)/TH)*(4/3));
            if (arr[0]%2 == 0) {
                    arr[1] = (x/TW);
            } else {
                    arr[1] = ((x/TW)-1/2);
            }
            return arr;
    };
}

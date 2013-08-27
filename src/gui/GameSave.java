package gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;

public class GameSave implements Serializable {
    ArrayList<int[]> mapValues;
    ArrayList<Boolean> gameSwitches;
    ArrayList<Boolean> unlockedChars;
    
    public GameSave() {
        mapValues =  new ArrayList<int[]>();
        gameSwitches = new ArrayList<Boolean>();
        unlockedChars = new ArrayList<Boolean>();
        for(int i=0; i<Constants.listAdvMaps.length;i++){
            int[] table = new int[2];
            table[0] = Constants.listAdvMaps[i];
            //System.out.println(table[0]);
            if(table[0] == 0){
                table[1] = 2;
            }
            else{
                table[1] = 0;
            }
            mapValues.add(table);
            //System.out.println(table[0]+"-"+table[1]);
        }
        for(int j=0;j<Constants.numberOfAdvSwitches;j++){
            gameSwitches.add(false);
        }
        for(int k=0;k<Constants.numberOfAdvChars;k++){
            unlockedChars.add(false);
        }
        unlockedChars.set(1, true);
        unlockedChars.set(7, true);
    }

    public void setMapValues(ArrayList<int[]> mapValues) {
        this.mapValues = mapValues;
    }

    public ArrayList<int[]> getMapValues() {
        return mapValues;
    }

    public void setGameSwitches(ArrayList<Boolean> gameSwitches) {
        this.gameSwitches = gameSwitches;
    }

    public ArrayList<Boolean> getGameSwitches() {
        return gameSwitches;
    }

    public void setUnlockedChars(ArrayList<Boolean> unlockedChars) {
        this.unlockedChars = unlockedChars;
    }

    public ArrayList<Boolean> getUnlockedChars() {
        return unlockedChars;
    }
}

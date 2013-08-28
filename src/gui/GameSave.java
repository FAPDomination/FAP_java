package gui;

import java.awt.Image;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameSave implements Serializable {
    Map<Integer, Integer> mapValues;
    ArrayList<Boolean> gameSwitches;
    ArrayList<Boolean> unlockedChars;
    
    public GameSave() {
        mapValues =  new HashMap<Integer, Integer>();
        gameSwitches = new ArrayList<Boolean>();
        unlockedChars = new ArrayList<Boolean>();
        for(int i=0; i<Constants.listAdvMaps.length;i++){
            int mapID = Constants.listAdvMaps[i];
            mapValues.put(mapID,0);
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

    public void setMapValues(Map<Integer, Integer> mapValues) {
        this.mapValues = mapValues;
    }

    public Map<Integer, Integer> getMapValues() {
        return mapValues;
    }
}

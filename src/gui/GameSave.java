package gui;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GameSave implements Serializable {
    Map<String, Integer> mapValues;
    ArrayList<Boolean> gameSwitches;
    ArrayList<Boolean> unlockedChars;

    public GameSave() {
        mapValues = new HashMap<String, Integer>();
        gameSwitches = new ArrayList<Boolean>();
        unlockedChars = new ArrayList<Boolean>();
        for (int i = 0; i < Constants.listAdvMaps.length; i++) {
            String cellHash = Constants.listAdvMaps[i];
            mapValues.put(cellHash, 0);
            //System.out.println(table[0]+"-"+table[1]);
        }
        for (int j = 0; j < Constants.numberOfAdvSwitches; j++) {
            gameSwitches.add(false);
        }
        for (int k = 0; k < Constants.numberOfAdvChars; k++) {
            unlockedChars.add(false);
        }
        //unlockedChars.set(1, true);
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

    public void setMapValues(Map<String, Integer> mapValues) {
        this.mapValues = mapValues;
    }

    public Map<String, Integer> getMapValues() {
        return mapValues;
    }
}

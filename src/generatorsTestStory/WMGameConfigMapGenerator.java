package generatorsTestStory;

import fap_java.GameConfig;

import gui.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

public class WMGameConfigMapGenerator implements Serializable{

    @SuppressWarnings("compatibility:-3331448763819377516")
    private static final long serialVersionUID = 1L;

    public static void main(String[] args){
        Map<String,GameConfig> mapList = new HashMap<String,GameConfig>();
        mapList.put("18,10", new GameConfig(30,"18,10"));
        mapList.put("19,9", new GameConfig(31,"19,9"));
        mapList.put("17,9", new GameConfig("1,7","0,1","0,1","0,1",false,32,1200,0,0,1,"17,9"));
        mapList.put("17,8", new GameConfig(33,"17,8"));
        //mapList.put("17,9", new GameConfig("1,1,1","0,1,1","0,1,1","0,1,1",false,22,1000,0,0,1,"17,9"));
        
        
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.wmGameConfigMap);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mapList);
            out.close();
            fileOut.close();
            System.out.println("Saved map cell linking in "+Constants.wmGameConfigMap);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

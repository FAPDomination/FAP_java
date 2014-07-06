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
        mapList.put("17,7", new GameConfig("1,7","0,1","0,1","0,1",false,34,1200,0,0,1,"17,7"));
        mapList.put("17,6", new GameConfig(35,"17,6"));
        mapList.put("16,9", new GameConfig("1,7","0,1","0,1","0,1",false,36,1200,0,0,1,"16,9"));
        mapList.put("17,10", new GameConfig("1,7","0,1","0,1","0,1",false,37,1200,0,0,1,"17,10"));
        mapList.put("16,10", new GameConfig("1,7","0,1","0,1","0,1",false,38,1200,0,0,1,"16,10"));
        mapList.put("15,9", new GameConfig("1,7","0,1","0,1","0,1",false,39,1200,0,0,1,"15,9"));
        mapList.put("15,10", new GameConfig("1,7","0,1","0,1","0,1",false,40,1200,0,0,1,"15,10"));
        mapList.put("14,10", new GameConfig("1,1,7,7","0,1,0,1","0,1,1,1","0,1,1,1",false,41,2000,0,0,1,"14,10"));
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

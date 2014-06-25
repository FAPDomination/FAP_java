package generators;

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
        mapList.put("18,11", new GameConfig("1,1","0,1","0,1","0,2",false,20,500,0,0,1,"18,11"));
        mapList.put("18,10", new GameConfig("1,5","0,1","0,1","0,2",false,21,1000,0,0,1,"18,10"));
        mapList.put("17,9", new GameConfig("1,1,1","0,1,1","0,1,1","0,1,1",false,22,1000,0,0,1,"17,9"));
        mapList.put("17,10", new GameConfig("1,1","0,1","0,1","0,2",false,23,1000,0,0,1,"17,10"));
        mapList.put("16,10", new GameConfig("1,1,1","0,1,2","0,1,1","0,1,2",false,24,1000,0,0,1,"16,10"));
        mapList.put("18,9", new GameConfig(25,"18,9"));
        
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

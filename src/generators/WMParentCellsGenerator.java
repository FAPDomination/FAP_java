package generators;

import gui.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WMParentCellsGenerator {
    public static void main(String[] args){
        Map<String,ArrayList<String>> mapParents = new HashMap<String,ArrayList<String>>();
        
        for(int i=0;i<Constants.listAdvMaps.length;i++){
            mapParents.put(Constants.listAdvMaps[i], new ArrayList<String>());
        }
        // Fill map parents
        
        mapParents.get("17,9").add("18,11");
        
        mapParents.get("17,10").add("18,10");
        mapParents.get("17,10").add("17,9");
        
        mapParents.get("16,10").add("17,10");
        
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.wmParentCells);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mapParents);
            out.close();
            fileOut.close();
            System.out.println("Saved map cell linking in "+Constants.wmParentCells);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

package generatorsTestStory;

import gui.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class WMParentCellsGenerator {
    public static void main(String[] args) {
        Map<String, ArrayList<String>> mapParents = new HashMap<String, ArrayList<String>>();

        for (int i = 0; i < Constants.listAdvMaps.length; i++) {
            mapParents.put(Constants.listAdvMaps[i], new ArrayList<String>());
        }
        // Fill map parents
        mapParents.get("19,9").add("18,10");

        mapParents.get("17,9").add("18,10");
        mapParents.get("17,10").add("18,10");

        mapParents.get("16,9").add("17,9");
        mapParents.get("16,11").add("17,10");

        mapParents.get("16,10").add("17,9");
        mapParents.get("16,10").add("17,10");

        mapParents.get("15,9").add("16,9");
        //mapParents.get("15,9").add("16,11");

        mapParents.get("15,10").add("16,10");
        //mapParents.get("15,10").add("16,11");

        mapParents.get("14,10").add("15,9");
        mapParents.get("14,10").add("15,10");

        mapParents.get("13,9").add("14,10");
        mapParents.get("13,10").add("14,10");

        mapParents.get("12,10").add("13,9");
        mapParents.get("12,10").add("13,10");

        // Side quest
        mapParents.get("17,8").add("17,9");
        mapParents.get("17,7").add("17,8");
        mapParents.get("17,6").add("17,7");
        /*
        mapParents.get("17,9").add("18,11");

        mapParents.get("17,10").add("18,10");
        mapParents.get("17,10").add("17,9");

        mapParents.get("16,10").add("17,10");
        */
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.wmParentCells);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(mapParents);
            out.close();
            fileOut.close();
            System.out.println("Saved map cell linking in " + Constants.wmParentCells);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

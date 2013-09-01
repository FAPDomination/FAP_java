package fap_java;

import java.util.ArrayList;

import npcs.NPCExit;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class process all the datas concerning a map.
 * It creates the map and the cell by parsing the XML
 * @see XMLparser
 * @see CMap
 * @see Cell
 */
public class MapHandler extends DefaultHandler {
    /**
     * The resulting map
     */
    private CMap map;
    /**
     * A cell field that will be used all along
     */
    private Cell c;
    /**
     * Line index in the grid
     */
    private int i = 0;
    /**
     * Column index in the grid
     */
    private int j = 0;
    /**
     * The game that you lost
     */
    private Game game;

    /** Flags for the position of the parser */
    private boolean inFile, inI, inJ, inStartCell;
    /** buffer for retreiving datas */
    private StringBuffer buffer;

    /**
     * Initializes a map Handler
     * @param game the game where it happens
     */
    public MapHandler(Game game, int fileID) {
        super();
        this.game = game;
        map = new CMap(game,fileID);
    }

    /**
     * Detects an opening tag in the XML
     * @param uri Unused
     * @param localName Unused
     * @param qName The name of the tag
     * @param attributes Unused
     * @throws SAXException Throw exception if the tag is unknown
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String regex1 = "st[0-9]";
        // For each tag do something according to its name
        if (qName.equals("root")) {
            inFile = true;
        } else if (qName.equals("ligne")) {
            inI = true;
        } else if (qName.equals("map")) {
        } else if (qName.equals("persos")) {
        } else if (qName.matches(regex1)) { // ie <stX> where X is 0->9
            inStartCell = true;
        } else if (qName.equals("i")) {
        } else if (qName.equals("j")) {
        } else {
            buffer = new StringBuffer();
            if (qName.equals("case")) {
                inJ = true;
            } else {
                // Error
                throw new SAXException("Unknown tag " + qName + ".");
            }
        }
        buffer = new StringBuffer();
    }
    
    /**
     * Closing tag
     * @param uri Unused
     * @param localName Unused
     * @param qName The name of the tag
     * @throws SAXException Thrown exception if the tag is unknown
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String regex1 = "st[0-9]";
        // For each tag do something according to its name
        if (qName.equals("root")) {
            inFile = false;
        } else if (qName.equals("ligne")) {
            // End of a line, update indexes
            i++;
            j = 0;
            inI = false;
        } else if (qName.equals("case")) {
            // End of a case in a line, get datas
            String content = buffer.toString();
            // Parse the Type, Did and additional param of the cell
            String param = "";
            int t = 0;
            int did = 0;

            String[] tabS = content.split(",", 2);
            did = Integer.parseInt(tabS[0]);
            if (tabS.length > 1) {
                param = tabS[1];
            }
            
            t = setTypeWithDid(did, param);
            
            // This is an exit NPC
            inJ = false;
            if (did != 0) {
                // Create the cell and add it to the map
                c = new Cell(i, j, t, param, did,game);
                if(did == 12){
                    game.getListNPCs().add(new NPCExit(c,game));
                }
                map.addElement(c);
            }
            j++;
        } else if (qName.equals("map")) {
        } else if (qName.equals("persos")) {
        } else if (qName.equals("i")) {
            i = Integer.parseInt(buffer.toString());
        } else if (qName.equals("j")) {
            j = Integer.parseInt(buffer.toString());
        } else if (qName.matches(regex1)) {
            //If it's a starting point
            Cell c = map.getCell(i, j);
            i = 0;
            j = 0;
            int pid = Integer.parseInt("" + qName.charAt(2));
            map.addStartCell(c, pid - 2);
            inStartCell = false;
        } else {
            //Error
            throw new SAXException("Unknown tag " + qName + ".");
        }
    }
    
    /**
     * This method detetcs the characters. BlackBox
     * @param ch BlackBox
     * @param start BlackBox
     * @param length BlackBox
     * @throws SAXException BlackBox
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        String lecture = new String(ch, start, length);
        if (buffer != null)
            buffer.append(lecture);
    }
    
    /**
     * Actions executed when starting parsing
     * @throws SAXException throws exception
     */
    public void startDocument() throws SAXException {
    }
    
    /**
     * Actions executed when ending parsing
     * @throws SAXException throws exception
     */
    public void endDocument() throws SAXException {
        //System.out.println("End of parsing");
    }

    public void setMap(CMap map) {
        this.map = map;
    }

    public CMap getMap() {
        return map;
    }
    
    /**
     * Computes the type of the cell with the Did and the special parameters of the cell
     * @param did the design id of the cell
     * @param param the additional param of the cell
     * @return the calculated type
     */
    public static int setTypeWithDid(int did,String param){
        int t=0;
        // If blocking high
        if (did >= Params.idBlockingHigh && did < Params.idblockingLow) {
            t = 20;
        // If blocking low
        } else if (did >= Params.idblockingLow) {
            t = 19;
        } else if (param.equals("")) {
            t = 1;
        }
        // If special tile (healthy, frozen, unstable)
        else if (param.indexOf(',') == -1) {
            if(param.matches("[a-z]")){ // Healthy Healthy and special tiles
                t=1;
            }
            else{
                // countdown cell
                t = 2;
            }
        } else if (did == 10 ||did == 11) {
        // Warp or switch
            t = did;
        }
        else {

        }
        return t;
    }
}

package fap_java;

import gui.MapListHandler;

import gui.Minimap;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This Class handles everything that is XML related.
 * It opens and close the files, read the content and give them to a handler that will process the datas
 */
public class XMLparser {

    /**
     * Not used
     */
    public XMLparser() {
        super();
    }
    
    /**
     * Parses a map from a XML file and return the designated map
     * @see MapHandler
     * @see CMap
     * @param mapID The ID of the XML file to read
     * @param game The game where it's supposed to take place
     * @return The created map
     */
    public static CMap parseMap(int mapID, Game game){
        // Creates a Handler that will process the datas
        DefaultHandler dh = new MapHandler(game, mapID);
        // Call the parse method
        parseXML("Maps/map"+mapID+".xml", dh);

        // Stores and returns the datas
        MapHandler dhm = (MapHandler) dh;
        return dhm.getMap();
    }
    
    
    /**
     * Parses the map list in order to create minimaps
     * @see gui.MapListHandler
     * @return An ArrayList of Minimaps
     */
    public static ArrayList<Minimap> parseMapList(){
        // Creates a Handler that will process the datas
        MapListHandler dh = new MapListHandler();
        // Call the parse method
        parseXML("maps.xml", dh);
        return dh.getList();
    }
    
    /**
     * Parses the Big Table Parameter.
     * @see ParamTableHandler
     */
    public static void parseParams(){
        // Creates a Handler that will process the datas
        DefaultHandler dh = new ParamTableHandler();
        // Call the parse method
        parseXML("paramTable.xml", dh);
    }
    
    /**
     * This method is meant to parse a file and give the datas to a handler that will treat them
     * @param f The path and name of the file
     * @param gestionnaire The Handler that will process the datas
     */
    private static void parseXML(String f, DefaultHandler gestionnaire){
                        try{
                                // create a parser factory
                                SAXParserFactory fabrique = SAXParserFactory.newInstance();

                                // create a parser
                                SAXParser parseur = fabrique.newSAXParser();

                                // Reading an XML File with a handler
                                File fichier = new File("resources/"+f);
                                parseur.parse(fichier, gestionnaire);
                        }catch(ParserConfigurationException pce){
                                System.out.println("Erreur de configuration du parseur");
                                System.out.println("Lors de l'appel a  newSAXParser()");
                        }catch(SAXException se){
                                System.out.println("Erreur de parsing");
                                System.out.println("Lors de l'appel a  parse()");
                            String message = "Message : "+se.getMessage()+"\n";
                            System.out.println(message);
                                    
                        }catch(IOException ioe){
                                System.out.println("Erreur d'entree/sortie");
                                System.out.println("Lors de l'appel a  parse()");
                                
                            String message = "Message : "+ioe.getMessage()+"\n";
                            //message += "Ligne "+ioe.getLineNumber()+", colonne "+ioe.getColumnNumber()+"\n";
                            System.out.println(message);
                        }
    }
}

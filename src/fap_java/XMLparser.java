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

public class XMLparser {
    
    // Sert à charger des fichiers dans les ressources : faire "load.getResource(chemin relatif depuis 'ressources')".
    private static final ClassLoader load = XMLparser.class.getClassLoader();
    private Game game;
    
    
    
    public XMLparser(Game game) {
        super();
        this.game = game;
    }
    
    public static CMap parseMap(int mapID, Game game){
        DefaultHandler dh = new MapHandler(game);
        parseXML("Maps/map"+mapID+".xml", dh);
        //System.out.println(dh);
        MapHandler dhm = (MapHandler) dh;
        return dhm.getMap();
    }
    
    public static void parseOptions(){
        OptionsParser dh = new OptionsParser();
        parseXML("options.xml", dh);
    }
    
    public static ArrayList<Minimap> parseMapList(){
        MapListHandler dh = new MapListHandler();
        parseXML("maps.xml", dh);
        return dh.getList();
    }
    
    public static void parseParams(){
        DefaultHandler dh = new ParamTableHandler();
        parseXML("paramTable.xml", dh);
    }
    
    private static void parseXML(String f, DefaultHandler gestionnaire){
                        try{
                                // création d'une fabrique de parseurs SAX
                                SAXParserFactory fabrique = SAXParserFactory.newInstance();

                                // création d'un parseur SAX
                                SAXParser parseur = fabrique.newSAXParser();

                                // lecture d'un fichier XML avec un DefaultHandler
                                //File fichier = new File(load.getResource(f).toString());
                                File fichier = new File("resources/"+f);
                                parseur.parse(fichier, gestionnaire);
                        }catch(ParserConfigurationException pce){
                                System.out.println("Erreur de configuration du parseur");
                                System.out.println("Lors de l'appel à newSAXParser()");
                        }catch(SAXException se){
                                System.out.println("Erreur de parsing");
                                System.out.println("Lors de l'appel à parse()");
                            String message = "Message : "+se.getMessage()+"\n";
                            System.out.println(message);
                                    
                        }catch(IOException ioe){
                                System.out.println("Erreur d'entrée/sortie");
                                System.out.println("Lors de l'appel à parse()");
                                
                            String message = "Message : "+ioe.getMessage()+"\n";
                            //message += "Ligne "+ioe.getLineNumber()+", colonne "+ioe.getColumnNumber()+"\n";
                            System.out.println(message);
                        }
    }
}

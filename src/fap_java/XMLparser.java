package fap_java;

import java.io.File;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLparser {
    
    // Sert à charger des fichiers dans les ressources : faire "load.getResource(chemin relatif depuis 'ressources')".
    private static final ClassLoader load = XMLparser.class.getClassLoader();
    
    public XMLparser() {
        super();
    }
    
    public static CMap parseMap(int mapID){
        DefaultHandler dh = new MapHandler();
        parseXML("map"+mapID+".xml", dh);
        //System.out.println(dh);
        MapHandler dhm = (MapHandler) dh;
        return dhm.getMap();
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

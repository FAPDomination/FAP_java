package gui;

import fap_java.Params;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MapListHandler extends DefaultHandler {
    
    // Flags for the position of the parser
    private boolean inFile;
    //buffer for retreiving datas
    private StringBuffer buffer;
    private ArrayList<Minimap> list;
    private String title;
    private int fileNumber;
    
    public MapListHandler() {
        list = new ArrayList<Minimap>();
    }
    
    // Detecting tag opening

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("root")) {
            inFile = true;
        } else if (qName.equals("map") || qName.equals("title") || qName.equals("fileNumber")) {
        }
        else {
            buffer = new StringBuffer();
            // Error
            throw new SAXException("Unknown tag " + qName + ".");
        }
        buffer = new StringBuffer();
    }
    //Detecting tag closing

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("root")) {
            inFile = false;
        } else if (qName.equals("map")) {
            Minimap mini = new Minimap(fileNumber,title);
            list.add(mini);
        }
        else if(qName.equals("title")){
            title = buffer.toString();    
        }
        else if(qName.equals("fileNumber")){
            this.fileNumber = Integer.parseInt(buffer.toString());
        }
        else {
            //Error
            throw new SAXException("Unknown tag " + qName + ".");
        }
    }
    //Detecting chars

    public void characters(char[] ch,int start, int length)
                    throws SAXException{
            String lecture = new String(ch,start,length);
            if(buffer != null) buffer.append(lecture);       
    }
    //Parsing starting

    public void startDocument() throws SAXException {
    }
    //End of parsing

    public void endDocument() throws SAXException {
        //System.out.println("End of parsing");
    }

    public void setList(ArrayList<Minimap> list) {
        this.list = list;
    }

    public ArrayList<Minimap> getList() {
        return list;
    }
}

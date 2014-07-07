package gui;

import fap_java.Params;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class ClassesDescListHandler extends DefaultHandler {

    // Flags for the position of the parser
    private boolean inFile;
    //buffer for retreiving datas
    private StringBuffer buffer;
    private int fileNumber;
    String thumb;
    String desc;
    int counter = 0;

    public ClassesDescListHandler() {
    }

    // Detecting tag opening

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("root")) {
            inFile = true;
        } else if (qName.equals("br") || qName.equals("h2") || qName.equals("p") || qName.equals("class") ||
                   qName.equals("thumbnail") || qName.equals("right") || qName.equals("center")) {
        } else if (qName.equals("desc")) {
            desc = "";
        } else {
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
        } else if (qName.equals("class")) {
            Params.waitingMessages[counter][0] = thumb;
            Params.waitingMessages[counter][1] = desc;
            desc = "";
            counter++;
        } else if (qName.equals("thumbnail")) {
            thumb = buffer.toString();
        } else if (qName.equals("desc")) {
            desc += buffer.toString();
        } else if (qName.equals("br") || qName.equals("h2") || qName.equals("p") || qName.equals("right") ||
                   qName.equals("center")) {
            desc += "<" + qName + ">" + buffer.toString() + "</" + qName + ">";
        } else {
            //Error
            throw new SAXException("Unknown tag " + qName + ".");
        }
        buffer = new StringBuffer();
    }
    //Detecting chars

    public void characters(char[] ch, int start, int length) throws SAXException {
        String lecture = new String(ch, start, length);
        if (buffer != null)
            buffer.append(lecture);
    }
    //Parsing starting

    public void startDocument() throws SAXException {
    }
    //End of parsing

    public void endDocument() throws SAXException {
        //System.out.println("End of parsing");
    }
}

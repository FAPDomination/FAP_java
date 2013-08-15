package fap_java;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OptionsParser extends DefaultHandler {

    // Flags for the position of the parser
    private boolean inFile;
    private boolean inKey;
    //buffer for retreiving datas
    private StringBuffer buffer;
    private int controlNumber;
    private int keyNumber;
    private String regex1 = "player[0-9]";

    public OptionsParser() {
        super();
    }

    // Detecting tag opening

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equals("root")) {
            inFile = true;
        } else if (qName.equals("keys")) {
            this.inKey = true;
        } else if (qName.matches(regex1)) {
            controlNumber = Integer.parseInt(""+(qName.charAt(qName.length() - 1)))-1;
        } else if (qName.equals("up")) {
            keyNumber = 0;
        } else if (qName.equals("down")) {
            keyNumber = 1;
        } else if (qName.equals("right")) {
            keyNumber = 2;
        } else if (qName.equals("left")) {
            keyNumber = 3;
        } else if (qName.equals("skill")) {
            keyNumber = 4;
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
        } else if (qName.equals("keys")) {
            // You lost the FAP
            this.inKey = false;
        } else if (qName.matches(regex1)) {
        } else if (qName.equals("up") || qName.equals("down") || qName.equals("right") || qName.equals("left") || qName.equals("skill")) {
            Params.controlsList[this.controlNumber][this.keyNumber] = Integer.parseInt(buffer.toString());
        } else {
            //Error
            throw new SAXException("Unknown tag " + qName + ".");
        }
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

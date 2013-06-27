package fap_java;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParamTableHandler extends DefaultHandler {

    //flags nous indiquant la position du parseur
    private boolean inFile;
    private String whereAreWe;
    //buffer nous permettant de récupérer les données
    private StringBuffer buffer;
    private double[] datas;

    public ParamTableHandler() {
        super();
    }

    //détection d'ouverture de balise

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("root")) {
            inFile = true;
        } else if (qName.equals("skillTime") || qName.equals("dispSpeed") || qName.equals("decLifeForced") ||
                   qName.equals("specParam") || qName.equals("maxHP") || qName.equals("recovLifeAuto")) {
            whereAreWe = qName;
            datas = new double[10];
        } else if (qName.equals("admin")) {
        } else if (qName.equals("knight")) {
        } else if (qName.equals("magician")) {
        } else if (qName.equals("miner")) {
        } else if (qName.equals("warlock")) {
        } else if (qName.equals("archer")) {
        } else if (qName.equals("vampire")) {
        } else if (qName.equals("Nmagician")) {
        } else if (qName.equals("booster")) {
        } else if (qName.equals("NoChar")) {
        } else {
            buffer = new StringBuffer();
            //erreur, on peut lever une exception
            throw new SAXException("Balise " + qName + " inconnue.");
        }
        buffer = new StringBuffer();
    }
    //détection fin de balise

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("root")) {
            inFile = false;
        } else if (qName.equals("skillTime") || qName.equals("dispSpeed") || qName.equals("decLifeForced") ||
                   qName.equals("specParam") || qName.equals("maxHP") || qName.equals("recovLifeAuto")) {
            Params.paramTable.put(whereAreWe, datas);
        }else if(whereAreWe != "specParam"){
            if (qName.equals("admin")) {
                datas[0] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("knight")) {
                datas[1] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("magician")) {
                datas[2] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("miner")) {
                datas[3] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("warlock")) {
                datas[4] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("archer")) {
                datas[5] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("vampire")) {
                datas[6] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("Nmagician")) {
                datas[8] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("booster")) {
                datas[9] = Double.parseDouble(buffer.toString());
            } else if (qName.equals("NoChar")) {
                datas[7] = Double.parseDouble(buffer.toString());
            } 
        }else if(whereAreWe == "specParam"){
            if (qName.equals("admin")) {
               
            } else if (qName.equals("knight")) {
                String[] stab = buffer.toString().split(",");
                int fac = Integer.parseInt(stab[0]);
                double pow = Double.parseDouble(stab[1]);
                Params.warriorDammage = fac/Math.pow(pow*1000, 2);
            } else if (qName.equals("Nmagician")) {
                String[] stab = buffer.toString().split(",");
                Params.howManyRingsIstheMagicianActive = Integer.parseInt(stab[0]);
                Params.howLongBlockingMagician = Integer.parseInt(stab[1]);
            } else if (qName.equals("miner")) {
                Params.minerNCells = Integer.parseInt(buffer.toString());
            } else if (qName.equals("warlock")) {
                Params.nBlastedTiles = Integer.parseInt(buffer.toString());
            } else if (qName.equals("archer")) {
                String[] stab = buffer.toString().split(",");
                int speed = Integer.parseInt(stab[0]);
                int dam = Integer.parseInt(stab[1]);
                Params.archerDammage = dam;
                Params.arrowSpeed = speed;
            } else if (qName.equals("vampire")) {
                String[] stab = buffer.toString().split(",");
                int nRings = Integer.parseInt(stab[0]);
                int gain = Integer.parseInt(stab[1]);
                
                Params.ringsVampirismTakes = nRings;
                Params.rateVampirismGains = gain;
            } else if (qName.equals("booster")) {
                String[] stab = buffer.toString().split(",");
                int newSpeed = Integer.parseInt(stab[0]);
                int time = Integer.parseInt(stab[1]);
                
                Params.boosterSpeed = newSpeed;
                Params.boosterTime = time;
            } else if (qName.equals("NoChar")) {
                // You lost the FAP
            } 
        }
        else {
            //erreur, on peut lever une exception
            throw new SAXException("Balise " + qName + " inconnue.");
        }
    }
    //détection de caractères

    public void characters(char[] ch,int start, int length)
                    throws SAXException{
            String lecture = new String(ch,start,length);
            if(buffer != null) buffer.append(lecture);       
    }
    //début du parsing

    public void startDocument() throws SAXException {
        //System.out.println("Début du parsing");
    }
    //fin du parsing

    public void endDocument() throws SAXException {
        //System.out.println("Fin du parsing");
        //System.out.println("Resultats du parsing");
    }
}

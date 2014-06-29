package generatorsTestStory;

import fap_java.NPC;

import gui.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import npcs.actions.*;

public class WPNPCBankGenerator {
    public static void main(String[] args){
        Map<Integer,ArrayList<NPC>> npcBank = new HashMap<Integer,ArrayList<NPC>>();
        Map<Integer,ArrayList<NPC>> npcBacklogBank = new HashMap<Integer,ArrayList<NPC>>();
        
        //TODO Initialize the list of all NPCs according to the nmap
        // Last action to first action (chained list)
        // Map 30:
        /*
        {
            ArrayList<NPC> theList = new ArrayList<NPC>();
            ArrayList<NPC> theBLList = new ArrayList<NPC>();
            Action a;
            Action b;
            Action c;
            Action d;
            Action e;
            Action f;
            NPC npc;
            
            
            npcBank.put(30, theList);
            npcBacklogBank.put(30, theBLList);
        }
        */
        {
            ArrayList<NPC> theList = new ArrayList<NPC>();
            ArrayList<NPC> theBLList = new ArrayList<NPC>();
            Action a;
            Action b;
            Action c;
            Action d;
            Action e;
            Action f;
            NPC npc;
            
            b = new ADisplayMessage("Alors que vous vous approchez de l'une d'entre elles, vous réalisez que ces colonnes sont vivantes ! Elles ont quelque chose à vous dire...",null);
            a = new ADisplayMessage("Vous vous réveillez dans une étrange salle. Des colonnes tout autour de vous.",b);
            e = new ASetSwitch(15,true,a);
            d = new ATestSwitch(15,e,null);
            NPC npc21 = new NPC(d);
            theList.add(npc21);
            
            // Columns
            //17,14, 17,8
            a = new ASetMapValue("18,10",2,null);
            e = new ADisplayMessage("Aide-nous à sauver Le Graphiste et à rétablir la paix visuelle de FAP-Land® (Un pays où il fait bon fapper)!",a);
            f =  new ADisplayMessage("Un affreux sorcier a capturé le graphiste et l'a enfermé dans sa forteresse des montagnes ! Il a ensuite remplacé tous les graphismes des personnages du jeu par ces affreux batons !",e);
            c = new ADisplayMessage("Au secours ! Un mal terrible s'est attaqué au royaume !",f);
            
            npc = new NPC("17,8",false,false,"NPC_sample",6,-17,c);
            theList.add(npc);
            npc = new NPC("19,9",false,false,"NPC_sample",6,-17,c);
            theList.add(npc);
            npc = new NPC("19,12",false,false,"NPC_sample",6,-17,c);
            theList.add(npc);
            
            c = new ASetSwitch(1,true,null);
            b = new ADisplayMessage("Tiens, je t'enseigne mon savoir, puisse-t-il t'aider dans ta quête et te permettre de délivrer le royaume de cette horrible malédiction",c);
            a = new ADisplayMessage("Avant d'être réduit à l'état de baton, j'étais un puissant chevalier.",b);
            npc = new NPC("17,13",false,false,"NPC_sample",6,-17,a);
            theList.add(npc);
            
            npcBank.put(30, theList);
            npcBacklogBank.put(30, theBLList);
        }
        // Map 31:
        {
            ArrayList<NPC> theList = new ArrayList<NPC>();
            ArrayList<NPC> theBLList = new ArrayList<NPC>();
            Action a;
            Action b;
            Action c;
            Action d;
            Action e;
            Action f;
            Action g;
            Action h;
            NPC npc;
            
            // Columns
            //17,14, 17,8
            c = new ADisplayMessage("C'est affreux, qu'allons-nous faire ?",null);
            b = new ADisplayMessage("Merci d'avoir libéré Le Graphiste ! Maintenant tout va pouvoir rentrer dans l'ordre !",null);
            a = new ATestSwitch(16,c,b);
            npc = new NPC("19,11",false,false,"NPC_sample",6,-17,a);
            theList.add(npc);
            
            c = new ADisplayMessage("Mais laisse-moi donc tranquille, j'ai à faire !",null);
            
            d = new ADisplayMessage("J'ai une mission pour toi. De l'autre côté du pont se trouve un marchand, qui détient une fiole. Va me la chercher et je te récompenserai",null);
            b = new ADisplayMessage("Ah, enfin te voilà ! J'ai réparé le pont, tu n'as plus qu'à l'emprunter !",null);
            
            g = new ADisplayMessage("Quoi ? Une récompense... Hmmm... Tiens, je t'apprends les secrets de l'archérie ! Tu pourras maintenant combattre tel un vrai archer", null);
            f = new ADisplayMessage("Ah voilà ma fiole ! Merci bien, jeune aventurier. Maintenant ouste, disparaît !",g);
            
            e = new ATestSwitch(18,b,f);
            a = new ATestSwitch(17,c,e);
            npc = new NPC("14,7",false,false,"NPC_sample",6,-17,a);
            theList.add(npc);
            
            
            npcBank.put(31, theList);
            npcBacklogBank.put(31, theBLList);
        }
        /*
        //Map 21:
            {
            ArrayList<NPC> theList = new ArrayList<NPC>();
            ArrayList<NPC> theBLList = new ArrayList<NPC>();
            Action c = new ADisplayMessage("Ah mais chui con ! Tu viens de le faire !",null);
            Action b = new ADisplayMessage("Appuie sur SKILL pour continuer",c);
            Action a = new ADisplayMessage("Willkommen dans l'aventure, jeune fougeux !",b);
            NPC npc21 = new NPC(a);
            
            theList.add(npc21);
            npcBank.put(21, theList);
            npcBacklogBank.put(21, theBLList);
            }
        //Map 25:
            {
                ArrayList<NPC> theList = new ArrayList<NPC>();
                ArrayList<NPC> theBLList = new ArrayList<NPC>();
            Action a;
            Action b;
            Action c;
            Action d;
            Action e;
            Action f;
            NPC npc;
            
            b = new AModifyCell("17,10","100",null);
            a = new ATestSwitch(20,null,b);
            npc = new NPC(null,false,false,null,0,0,a);
            theBLList.add(npc);
            
            a =  new ASetMapValue("18,9",2,null);
            npc = new NPC(null,false,false,null,0,0,a);
            theBLList.add(npc);
            
            b = new AModifyCell("18,13","100",null);
            a =  new ATestMapValue("18,11",null,b);
            npc = new NPC(null,false,false,null,0,0,a);
            theBLList.add(npc);
                
            e = new ADisplayMessage("You're missing something here, you now ?!",null);
            f =  new ADisplayMessage("Wat a sheime",e);
            c = new ADisplayMessage("I'ma doing it right now !",null);
            b = new ASetSwitch(0,true,c);
            a = new AAsk("Ya want da switch ?","Yeah","Nup", f,b);
            
            npc = new NPC("10,10",false,false,"NPC_sample",6,-17,a);
            theList.add(npc);
            
            
            f = new ADisplayMessage("Ya do not interrest-a me",null);
            c = new ASetSwitch(20,true,null);
            b = new ADisplayMessage("Ye have da switch !! Gloria !",c);
            a = new ATestSwitch(0,f,b);
        
            NPC npc2 = new NPC("15,8",false,false,"NPC_sample",6,-17,a);
            theList.add(npc2);
            
            
            
            e = new ASetSwitch(1,true,null);
            d = new ADisplayMessage("I'll activate the ooh yeah for ya",e);
            c = new ADisplayMessage("I'll activate the ah aon for ya",d);
            b = new ADisplayMessage("I'll activate the bullshit for ya",c);
            a = new ADisplayMessage("I'll activate the knight for ya",b);
        
            NPC npc4 = new NPC("20,9",false,false,"NPC_sample",6,-17,a);
            theList.add(npc4);
        
            b = new ASetSwitch(5,true,null);
            a = new ADisplayMessage("I'll activate the arrrcher for ya",b);
        
            NPC npc5 = new NPC("27,19",false,false,"NPC_sample",6,-17,a);
            theList.add(npc5);
            
                npcBank.put(25, theList);
                npcBacklogBank.put(25, theBLList);
            }
        */
        // Save regular bank
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.wmNPCBank);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(npcBank);
            out.close();
            fileOut.close();
            System.out.println("Saved NPC Bank in "+Constants.wmNPCBank);
        } catch (IOException i) {
            i.printStackTrace();
        }
        // Save backlog bank
        try {
            FileOutputStream fileOut = new FileOutputStream(Constants.wmNPCBacklogBank);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(npcBacklogBank);
            out.close();
            fileOut.close();
            System.out.println("Saved Trigger NPCs bank in "+Constants.wmNPCBacklogBank);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}

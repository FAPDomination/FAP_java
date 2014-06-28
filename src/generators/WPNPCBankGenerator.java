package generators;

import fap_java.NPC;

import gui.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import npcs.actions.AAsk;
import npcs.actions.ADisplayMessage;
import npcs.actions.AModifyCell;
import npcs.actions.ASetMapValue;
import npcs.actions.ASetSwitch;
import npcs.actions.ATestMapValue;
import npcs.actions.ATestSwitch;
import npcs.actions.Action;

public class WPNPCBankGenerator {
    public static void main(String[] args){
        Map<Integer,ArrayList<NPC>> npcBank = new HashMap<Integer,ArrayList<NPC>>();
        Map<Integer,ArrayList<NPC>> npcBacklogBank = new HashMap<Integer,ArrayList<NPC>>();
        
        //TODO Initialize the list of all NPCs according to the nmap
        // Last action to first action (chained list)
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

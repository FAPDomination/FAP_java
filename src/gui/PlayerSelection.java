package gui;

import fap_java.NPC;
import fap_java.Params;

import fap_java.Tools;

import java.awt.Graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;


import java.awt.Color;
import java.awt.event.MouseEvent;

public class PlayerSelection extends FAPanel {
    private JButton btnNext = new JButton();
    private JButton btnAdd = new JButton();
    
    private ArrayList<PlayerSelect> players;
    private ArrayList<JComboBox> controlSelecters;
    private ArrayList<JComboBox> teamSelecters;
    private ArrayList<JButton> eraseSelecters;
    private String[] listControls = {"Player 1","Player 2","Player 3","AI Low", "AI Middle", "AI High"};
    private int maxPlayers = Params.maxPlayers;
    
    private int displayOrigX = 140;
    private int displayOrigY = 200;
    private int displayIncrementY = 55;
    private int displayHeight = 30;
    
    private boolean error;
    private String message;

    public PlayerSelection(TheFrame theFrame, JPanel jPanel) {
        super(theFrame, jPanel);
        
        players = new ArrayList<PlayerSelect>();
        controlSelecters = new ArrayList<JComboBox>(); 
        teamSelecters = new ArrayList<JComboBox>(); 
        eraseSelecters = new ArrayList<JButton>();
        // testing

        //
        this.addPlayerSelecter();
        this.addPlayerSelecter();
        //---------
        
        swordX = minxS;
        cloudsX = minxC;
        
        message = "";
        
        this.setLayout(null);
        this.setSize(Constants.frameDimension);

        btnGoBack.setText("Retour");
        btnGoBack.setSize(120,60);
        btnGoBack.setLocation(origX-5, origY-5);
        
        btnNext.setText("Suivant");
        btnNext.setSize(120, 60);
        btnNext.setUI(new Button_SampleUI());
        ((Button_SampleUI)btnNext.getUI()).setHover(false);
        btnNext.setOpaque(false);
        btnNext.setLocation(this.getWidth()-30-btnNext.getWidth(), 20);
        
        //btnAdd.setText("+");
        btnAdd.setSize(48,48);
        btnAdd.setOpaque(false);
        btnAdd.setUI(new Button_AddRemoveUI("btn_add"));
        ((Button_AddRemoveUI)btnAdd.getUI()).setHover(false);
        btnAdd.setLocation(displayOrigX,displayOrigY-displayIncrementY);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPlayerSelecter();
                ((Button_AddRemoveUI)btnAdd.getUI()).setHover(false);
            }
        });
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_AddRemoveUI)btnAdd.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_AddRemoveUI)btnAdd.getUI()).setHover(false);
            }
        });
        
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextFrame();
                ((Button_SampleUI)btnNext.getUI()).setHover(false);
            }
        });
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                //LectureFichierSon.lire(Design.sonChtk);
                ((Button_SampleUI)btnNext.getUI()).setHover(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ((Button_SampleUI)btnNext.getUI()).setHover(false);
            }
        });
        
        this.repaint();
    }
    
    public void nextFrame(){
        if(!error){
            ArrayList<Integer> listTeams = new ArrayList<Integer>();
            for(int i=0;i<Params.maxPlayers;i++){
                listTeams.add(null);
            }
            // Parse the PlayerSelecters
            for(int i=0;i<players.size();i++){
                PlayerSelect ps = players.get(i);
                if(ps.getControler() > Params.nPlayersOn1Computer-1){
                    int isFSM = ps.getControler()-1;    // So they get lvl 2,3 and 4
                    ps.setIsFSM(isFSM);
                }
                else{
                    ps.setIsFSM(0);
                }
                // Security for number of teams :
                int team = ps.getTeam();
                if(listTeams.contains(team)){}
                else{
                    listTeams.set(team, team);
                }
            }
            // Security for number of teams :
            listTeams = Tools.removeNull(listTeams);
            for(int i=0;i<players.size();i++){
                PlayerSelect ps = players.get(i);
                ps.setTeam(listTeams.indexOf(ps.getTeam()));
            }
            
            // Proceeding to next panel
            JPanel nextPanel = new CharacterSelection(parent,this);
            parent.changePanel(nextPanel);
        }
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(!message.equals("")){
            //TODO multiline
            //TODO pretty text
            g.setColor(Color.black);
            g.drawString(message, 600, 300);
        }
    }

    public void setPlayers(ArrayList<PlayerSelect> players) {
        this.players = players;
    }

    public ArrayList<PlayerSelect> getPlayers() {
        return players;
    }
    
    public void organizePlayerSelect(){
        //TODO Placements of tout ce joli petit monde
        // Reinit buttons and lists
        this.removeAll();
        this.add(btnNext);
        this.add(btnGoBack);
        this.add(btnAdd);
        this.validate();
        this.repaint();
        
        for(int i=0;i<this.players.size();i++){
            // PlayerSelect
            PlayerSelect ps = players.get(i);
            // Associated controller
            JComboBox combo = this.controlSelecters.get(i);
            combo.setSelectedIndex(ps.getControler());
            combo.setBounds(displayOrigX+80, displayOrigY + (displayIncrementY) * i + 10, 150, displayHeight);
            //this.remove(combo);
            this.add(combo);
            combo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    combo_ActionPerformed(e);
                }
            });
            // Associated Team
            JComboBox team = this.teamSelecters.get(i);
            team.setSelectedIndex(ps.getTeam());
            team.setBounds(displayOrigX+270, displayOrigY+(displayIncrementY)*i + 10, 150, displayHeight);
            //this.remove(team);
            this.add(team);
            team.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    team_ActionPerformed(e);
                }
            });
            // Delete button
            JButton jb = this.eraseSelecters.get(i);
            //jb.setText("X");
            jb.setSize(48,48);
            jb.setOpaque(false);
            jb.setUI(new Button_AddRemoveUI("btn_remove"));
            ((Button_AddRemoveUI)jb.getUI()).setHover(false);
            jb.setLocation(displayOrigX,displayOrigY-displayIncrementY);
            jb.setBounds(displayOrigX, displayOrigY+displayIncrementY*i, 48,48);
            this.add(jb);
            
            jb.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    //LectureFichierSon.lire(Design.sonChtk);
                    jb_rollover(evt);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    jb_rollout(evt);
                }
            });
            
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jb_ActionPerformed(e);
                }
            });
        }
        
        updateErrorText();
        
        this.validate();
        this.repaint();
    }
    
    public void jb_ActionPerformed(ActionEvent e){
        JButton jb = (JButton) e.getSource();
        ((Button_AddRemoveUI)jb.getUI()).setHover(false);
        this.removePlayerSelecter(this.eraseSelecters.indexOf(jb));
    }
    
    public void jb_rollover(MouseEvent e){
        JButton jb = (JButton) e.getSource();
        ((Button_AddRemoveUI)jb.getUI()).setHover(true);
        
    }
    
    public void jb_rollout(MouseEvent e){
        JButton jb = (JButton) e.getSource();
        ((Button_AddRemoveUI)jb.getUI()).setHover(false);
    }
    
    
    
    public void combo_ActionPerformed(ActionEvent e){
        JComboBox combo = (JComboBox)e.getSource();
        int controler = combo.getSelectedIndex();
        PlayerSelect ps = players.get(this.controlSelecters.indexOf(combo));
        ps.setControler(controler);
        updateErrorText();
    }
    
    public void team_ActionPerformed(ActionEvent e){
        JComboBox combo = (JComboBox)e.getSource();
        int team = combo.getSelectedIndex();
        PlayerSelect ps = players.get(this.teamSelecters.indexOf(combo));
        ps.setTeam(team);
        updateErrorText();
    }
    
    public void addPlayerSelecter(){
        if(players.size()<this.maxPlayers){
            int ctrl = players.size();
            if(ctrl>Params.nPlayersOn1Computer-1){
                ctrl = 3;
            }
            players.add(new PlayerSelect(this,ctrl,players.size()));
            controlSelecters.add(new JComboBox(listControls));
            JComboBox teamS = new JComboBox();
            for(int i=0;i<maxPlayers;i++){
                teamS.addItem("Team "+(i+1));
            }
            teamSelecters.add(teamS);
            eraseSelecters.add(new JButton());
            
            this.organizePlayerSelect();
        }
    }
    
    public void removePlayerSelecter(int id){
        if(players.size()>2 && id!=-1){
            if(players.get(id) != null){
                players.remove(id);
                controlSelecters.remove(id);
                teamSelecters.remove(id);
                eraseSelecters.remove(id);
                
                this.organizePlayerSelect();
            }
        }
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public void updateErrorText(){
        int onlyTeam=0;
        error = true;
        message = "Vous devez avoir au moins deux equipes !";
        int fsmCounter = 0;
        for(int i=0;i<players.size();i++){
            PlayerSelect ps = players.get(i);
            if(i==0){
                onlyTeam = ps.getTeam();
            }
            else if(ps.getTeam() != onlyTeam){
                message = "";
                error = false;
            }
            if(ps.getControler() > Params.nPlayersOn1Computer-1){
                fsmCounter++;
            }
        }
        if(!error && fsmCounter == players.size()){
            message = "Vous avez le droit de regarder des ordinateurs se battre.\nMais c'est moins fun.";
        }
        if(!error){
            int[] controlers = new int[Params.nPlayersOn1Computer];
            for(int i=0;i<Params.nPlayersOn1Computer;i++){
                controlers[i] = 0;
            }
            for(int i=0;i<players.size();i++){
                PlayerSelect ps = players.get(i);
                if(ps.getControler()<Params.nPlayersOn1Computer){
                    controlers[ps.getControler()]++;
                    if(controlers[ps.getControler()]>1){
                        message = "Vous ne pouvez controler qu'un personage !";
                        error = true;
                        break;
                    }
                }
            }
        }
        this.btnNext.setEnabled(!error);
        this.repaint();
    }
}

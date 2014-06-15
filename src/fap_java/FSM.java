package fap_java;

import characters.Miner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pathFinder.pathFinder;

public class FSM {

    private FSM_State currentState;
    private FSM_State prevState;
    private FSM_Event currentEvent;

    private Cell nextCell;
    private boolean cellWasTaken;
    private int tryOut;
    private int maxTryOut = 5;

    private Player body;
    private int level;

    private boolean fsmGo;
    private Object fsm_param;
    private Object fsm_secParam;

    private int nRings;
    private int reactTime;

    //States
    public static FSM_State picking = new FSM_State(0, "pickCell");
    public static FSM_State shifting = new FSM_State(1, "shiftToPicked");
    public static FSM_State analysing = new FSM_State(2, "analyseCurCell");
    public static FSM_State waiting = new FSM_State(3, "waitForIt");
    public static FSM_State staying = new FSM_State(4, "stay");
    public static FSM_State pathDefine = new FSM_State(5, "definePath");
    public static FSM_State pathFollow = new FSM_State(6, "followPath");
    //Events
    public static FSM_Event ev_done = new FSM_Event(0);
    public static FSM_Event ev_error = new FSM_Event(1);
    public static FSM_Event ev_secDone = new FSM_Event(2);
    public static FSM_Event ev_thirdDone = new FSM_Event(3);
    public static FSM_Event ev_fourthDone = new FSM_Event(4);


    public FSM(Player p, int level) {
        //    super(id, c, game, pc, t);
        fsmGo = true;
        currentState = picking;
        body = p;
        this.level = level;
        initFSM();
    }

    public void getSkill() {
    }

    // FSM Configuration
    /*
    private void fsm_configure_transition(Object fstate, Object fevent, Object nextState){
            fsm_transition_table[fstate][fevent] = nextState;
    }

    private void fsm_configure_action(Object fstate, Object action){
            fsm_action_table[fstate] = action;
    }
    */

    public String toString() {
        return "Level " + this.level + " FSM";
    }


    public void pickCell() {
        ArrayList<Cell> neighborHoodList = this.body.getGame().getMap().surroundingCells(this.body.getCurrent());
        Map<Cell, Integer> weights = new HashMap<Cell, Integer>();
        for (int i = 0; i < neighborHoodList.size(); i++) {
            Cell c = neighborHoodList.get(i);
            if (c != null) {
                int w = this.getWeight(c);
                weights.put(c, w);
            }
        }
        ArrayList<Integer> weightList = new ArrayList(weights.values());
        Collections.sort(weightList);

        ArrayList<Cell> bestCells = new ArrayList<Cell>();
        for (int k = 0; k < neighborHoodList.size(); k++) {
            Cell c = neighborHoodList.get(k);
            if (c != null && this.getWeight(c) == weightList.get(weightList.size() - 1)) {
                bestCells.add(c);
            }
        }
        if (bestCells.size() == 0) {
            fsm_param = "No walkable cell";
            this.fsm_receive_event(ev_error);
        } else {
            Cell c = bestCells.get(Tools.randRange(0, bestCells.size() - 1));
            nextCell = c;
            this.fsm_receive_event(ev_done);
        }
    }

    public void shiftToPicked() {
        //Set ori
        Cell current = body.getCurrent();
        ArrayList<Cell> list = body.getGame().getMap().surroundingCells(current);
        for (int i = 0; i < list.size(); i++) {
            Cell w = list.get(i);
            if (w == this.nextCell) {
                body.setOri(i);
            }
        }
        //---

        boolean event = false;
        body.shiftStick(0, 0);
        if (body.getCurrent() == nextCell) {
            this.tryOut = 0;
        } else {
            tryOut++;
            if (tryOut >= maxTryOut && prevState == pathFollow) {
                System.out.println("MAXTRYOUT");
                event = true;
                Cell k = findGoodCell();
                fsm_param = k;
                this.fsm_receive_event(ev_thirdDone);
            }
        }
        if (!event) {
            if (prevState == pathFollow && fsm_secParam == null) {
                this.fsm_receive_event(ev_secDone);
            } else {
                this.fsm_receive_event(ev_done);
            }
        }
    }

    public void analyseCurCell() {
        //currentEvent = null;
        //System.out.println(body.getCurrent());
        //Analyses
        //check area
        int areaWeight = this.areaWeight(body.getCurrent(), nRings);
        //Weight toggle for changin area
        int weightToggle = 6 - level;
        if (weightToggle < 2) {
            weightToggle = 2;
        }
        if (areaWeight < weightToggle && fsm_secParam == null) {
            //Find good Cell system
            if (body instanceof Miner) {
                body.getSkill();
                Miner me = (Miner)body;
                int maxW =0;
                Cell selected = null;
                for(int i =0;i<me.getRandCells().size();i++){
                    Cell c = me.getRandCells().get(i);
                    int w = areaWeight(c, nRings);
                    if(w>=maxW){
                        maxW = w;
                        selected = c;
                    }
                }
                me.setCursor(me.getRandCells().indexOf(selected));
                body.keyLow(4);
                fsm_param = (6 - level) * reactTime;
                this.fsm_receive_event(ev_secDone);
            } else {
                //Ersatz system : find Cell with ennemy
                //Cell k = body.getGame().getPlayers().get(0).getCurrent();
                // The right thing
                Cell k = findGoodCell();
                fsm_param = k;
                this.fsm_receive_event(ev_thirdDone);
            }
        } else {
            boolean skillWorth = false;
            int ts = body.getGame().getThread().getCount() - body.getLastSkill();
            Cell c = body.getCurrent();
            ArrayList<Cell> neighbour = body.getGame().getMap().surroundingCells(c);
            int averageHP = 0;
            int nCells = 0;
            if (ts >= body.getSkillTime()) {
                switch (body.getPc()) {
                case 1: //Knight
                    //Get the average HP of the tiles surrounding
                    for (int i = 0; i < neighbour.size(); i++) {
                        Cell k = neighbour.get(i);
                        if (k != null && k.getType() == 1 &&
                            (k.getOwner() != body.getTeam() || k.getOwner() == null)) {
                            int hpAmount = (int)k.getHp();
                            if (k.getOwner() == null) {
                                hpAmount = 90;
                            }
                            averageHP += hpAmount;
                            nCells++;
                        }
                    }
                    if (nCells > 0) {
                        averageHP /= nCells;
                    }
                    //Calculate the amount of damage he can do
                    int randBound = 140 - 20 * level;
                    double randError = ((double)Tools.randRange(-randBound, randBound)) / 10;
                    int dammage = (int)(Params.warriorDammage * Math.pow((ts + randError), 2));
                    //Triggah'
                    //Totally arbitrary : should also depend on levels
                    int triggNCells = 4;
                    if (nCells >= triggNCells && dammage >= averageHP) {
                        skillWorth = true;
                    }
                    break;
                    //Note : for the Miner (pc = 3) see above
                case 4: //Warlock
                    Cell k = body.getCurrent();
                    neighbour = body.getGame().getMap().surroundingCells(k);
                    averageHP = 0;
                    nCells = 0;
                    for (int i = 0; i < neighbour.size(); i++) {
                        if (neighbour.get(i) != null && neighbour.get(i).getType() == 1) {
                            nCells++;
                        }
                    }
                    //Totally arbitrary : should also depend on levels
                    if (nCells < 6 - level) {
                        skillWorth = true;
                    }
                    break;
                case 5: // Archer
                    ArrayList<Cell> path =
                        this.body.getGame().getMap().tileOnPath(this.body.getCurrent(), this.body.getOri());
                    //Totally arbitrary : should also depend on levels
                    if (path.size() >= level + 2) {
                        skillWorth = true;
                    }
                    break;
                case 6: //Vampire
                    nCells = 0;
                    int randVNum = Tools.randRange(0, 10);
                    int randVRing = 0;
                    if (randVNum == 0) {
                        randVRing = 1;
                    } else if (randVNum == 1) {
                        randVRing = -1;
                    } else {
                        randVRing = 0;
                    }
                    int nVRg = randVRing + Params.ringsVampirismTakes;
                    Map<Integer, ArrayList<Cell>> ringsVOfCells = body.getGame().getMap().ringsSurrounding(c, nVRg);
                    //per ring...
                    for (int i = 0; i <= nVRg; i++) {
                        ArrayList<Cell> theRing = ringsVOfCells.get(i);
                        for (int j = 0; j < theRing.size(); j++) {
                            Cell a = theRing.get(j);
                            //c.setOwner(this.getTeam());
                            //c.setHp(this.getInitHP());
                            if (a != null && a.getType() == 1 && a.getOwner() != null &&
                                a.getOwner() != body.getTeam()) {
                                nCells++;
                            }
                        }
                    }

                    //Totally arbitrary : should depend on levels
                    if (nCells >= 9) {
                        skillWorth = true;
                    }
                    break;
                case 8: //Magician
                    Game game = body.getGame();
                    int randNum = Tools.randRange(0, 5);
                    int randRing = 0;
                    if (randNum == 0) {
                        randRing = 1;
                    } else if (randNum == 1) {
                        randRing = -1;
                    } else {
                        randRing = 0;
                    }
                    int nRg = Params.howManyRingsIstheMagicianActive + randRing;
                    Map<Integer, ArrayList<Cell>> ringsOfCells = game.getMap().ringsSurrounding(c, nRg);
                    for (int i = 0; i <= nRg; i++) {
                        ArrayList<Cell> theRing = ringsOfCells.get(i);
                        for (int j = 0; j < theRing.size(); j++) {
                            Cell a = theRing.get(j);
                            //c.setOwner(this.getTeam());
                            //c.setHp(this.getInitHP());
                            Player p = game.isOccupied(a);
                            if (p != null) {
                                skillWorth = true;
                            }
                        }
                    }
                    break;
                default:
                    break;
                }
            }

            if (skillWorth && fsm_secParam == null) {
                //System.out.println("sop");
                body.getSkill();
                fsm_param = reactTime;
                this.fsm_receive_event(ev_secDone);
            } else {
                //Define if it's time to go :
                c = body.getCurrent();
                if (c.getOwner() != null && c.getOwner() != body.getTeam()) {
                    cellWasTaken = true;
                } else {
                    if (cellWasTaken) {
                        if (fsm_secParam != null) {
                            fsm_secParam = reactTime;
                        } else {
                            fsm_param = reactTime;
                        }
                        this.fsm_receive_event(ev_secDone);
                    } else {
                        if (fsm_secParam == null) {
                            this.fsm_receive_event(ev_done);
                        } else {
                            this.fsm_receive_event(ev_fourthDone);
                        }
                    }
                    cellWasTaken = false;
                }
            }
        }
    }

    public void waitForIt() {
        if (fsm_secParam != null) {
            body.makeHimWait((Integer)fsm_secParam);
            this.fsm_receive_event(ev_secDone);
        } else {
            body.makeHimWait((Integer)fsm_param);
            this.fsm_receive_event(ev_done);
        }
    }

    public void definePath() {
        fsm_secParam = null;
        //Dis is da path, yo
        //System.out.println("Begin path");
        Cell c = (Cell)fsm_param;
        Cell s = body.getCurrent();
        ArrayList<Cell> map = body.getGame().getMap().getMyMap();
        ArrayList<Cell> path = pathFinder.findPath(map, c, s);

        fsm_param = path;

        //have to define if the path will be direct (no waiting on cell for conquering) or  not
        int nCells = 0;
        for (int i = 0; i < path.size(); i++) {
            Cell k = path.get(i);
            if (k.getType() == 1 && k.getOwner() != body.getTeam()) {
                nCells++;
            }
        }
        //Should depend on level
        if (nCells >= 2 + level) {
            fsm_secParam = true;
        }
        if (path.size() > 0) {
            this.fsm_receive_event(ev_done);
        } else {
            fsm_param = "You shall not path";
            this.fsm_receive_event(ev_error);
        }
    }

    public void followPath() {
        ArrayList<Cell> path = (ArrayList<Cell>)fsm_param;
        Cell c = path.get(path.size() - 1);
        if (c != body.getCurrent()) { // Have to shift to cell
            // go to shiftCell
            nextCell = c;
            this.fsm_receive_event(ev_done);
        } else {
            if (path.size() <= 1) { // Path is over
                // Escape to pickCell
                //System.out.println("End of Path");
                fsm_secParam = null;
                this.fsm_receive_event(ev_thirdDone);
            } else { // Cell is shifted

                path.remove(c);
                fsm_param = path;
                this.fsm_receive_event(ev_secDone);
            }
        }
    }

    public void stay() {
        System.out.println("An error occured, FSM crashed");
        System.out.println((String)fsm_param);

        fsmGo = false; //
    }

    public void fsm_receive_event(FSM_Event ev) {
        currentEvent = ev;
        //trace(currentState+"-"+currentEvent);
        prevState = currentState;
        FSM_State nextState = currentState.getNextState(ev);
        //trace(currentState+"-"+currentEvent+"-"+nextState);
        currentState = nextState;
        //trace("transition "+currentState);
        //fsmGo = 1;
        //System.out.println(prevState+"-"+ev+"-"+currentState);
    }

    public void executeMethod() {
        if (fsmGo) {
            try {
                Method method = this.getClass().getMethod(currentState.getAssociatedMethod());
                method.invoke(this);
            } catch (SecurityException e) {
                System.out.println("Security Exception");
            } catch (NoSuchMethodException e) {
                System.out.println("No such Method : " + currentState.getAssociatedMethod());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void initFSM() {
        //ev_done
        picking.addTransition(ev_done, shifting);
        shifting.addTransition(ev_done, analysing);
        analysing.addTransition(ev_done, picking);
        waiting.addTransition(ev_done, picking);
        pathDefine.addTransition(ev_done, pathFollow);
        pathFollow.addTransition(ev_done, shifting);
        //ev_error
        picking.addTransition(ev_error, staying);
        shifting.addTransition(ev_error, staying);
        analysing.addTransition(ev_error, staying);
        pathDefine.addTransition(ev_error, staying);
        waiting.addTransition(ev_error, staying);
        pathFollow.addTransition(ev_error, staying);
        //ev_secDone
        analysing.addTransition(ev_secDone, waiting);
        pathFollow.addTransition(ev_secDone, pathFollow);
        shifting.addTransition(ev_secDone, pathFollow);
        waiting.addTransition(ev_secDone, pathFollow);
        //ev_thirdDone
        analysing.addTransition(ev_thirdDone, pathDefine);
        shifting.addTransition(ev_thirdDone, pathDefine);
        pathFollow.addTransition(ev_thirdDone, picking);
        //ev_fourthDone
        analysing.addTransition(ev_fourthDone, pathFollow);

        // COnstants
        nRings = level;
        if (nRings > 3) {
            nRings = 3;
        }

        reactTime = 60 - Params.fsmReactionTime * level;
    }

    public void setNextCell(Cell nextCell) {
        this.nextCell = nextCell;
    }

    public Cell getNextCell() {
        return nextCell;
    }

    public int getWeight(Cell c) {
        int w;
        if (!c.isWalkable()) {
            w = 0;
        } else if (c.getOwner() != null) {
            if (c.getOwner() == body.getTeam()) {
                w = 2;
            } else {
                Team te = c.getOwner();
                if (te.getScore() > body.getTeam().getScore()) {
                    w = 8;
                } else {
                    w = 7;
                }
            }
        } else {
            switch (c.getType()) {
            case 1:
                w = 9;
                break;
            case 10: //It's a warp

                int[] tab = new int[2];
                String[] tabS = new String[2];
                tabS = c.getAddParam().split(",");
                tab[0] = Integer.parseInt(tabS[0]);
                tab[1] = Integer.parseInt(tabS[1]);

                Cell warpedCell = body.getGame().getMap().getCell(tab);
                w = areaWeight(warpedCell, nRings + 1);
            case 11:
                // if no one switched that switch, the weight is high, else very low
                if (c.isWalked()) {
                    w = 1;
                } else {
                    w = 12;
                }
                break;

            default:
                w = 0;
                break;
            }
        }
        return w;
    }

    public int areaWeight(Cell cell, int nRings) {
        int average = 0;
        int nCells = 0;
        Map<Integer, ArrayList<Cell>> ringsOfCells = this.body.getGame().getMap().ringsSurrounding(cell, nRings);
        for (int i = 1; i <= nRings; i++) {
            ArrayList<Cell> theRing = ringsOfCells.get(i);
            for (int j = 0; j < theRing.size(); j++) {
                Cell c = theRing.get(j);
                if (c != null) {
                    average += this.getWeight(c);
                    nCells++;
                }
            }
        }
        average /= nCells;
        return average;
    }

    public Cell findGoodCell() {
        Cell c = null;
        //Constants
        int count = 0;
        int minWeight = 8;
        int tries = 10 + level;
        ArrayList<Cell> list = body.getGame().getMap().getMyMap();
        while (c == null) {
            Cell k = list.get(Tools.randRange(0, list.size() - 1));
            if (k != null && k.getType() == 1) {
                int w = areaWeight(k, nRings);
                ArrayList<Cell> path = pathFinder.findPath(list, body.getCurrent(), k);
                if (w >= minWeight && path.size() > 0) {
                    c = k;
                } else {
                    if (count >= tries) {
                        count = 0;
                        minWeight--;
                    } else {
                        count++;
                    }
                }
            }
        }
        return c;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

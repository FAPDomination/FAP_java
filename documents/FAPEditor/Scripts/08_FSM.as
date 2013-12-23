/*
	Title: Final State Machine
	
		The Artificial Intelligence of the game is a Final States Machine. It means that it can
		take a finite number of states. For each state is associated an action, that will be
		executed during the time where the FSM is at this state.
		
		The FSM also contains a list of event (End of state, Problem, Command, etc...) that
		can occur during the game.
		
		For each state is defined the behavior of the FSM when a particular event occurs. Meaning
		for each state, each event, the FSM knows what state will come next.
		
		So when an event occurs, the FSM reads the next state to take, and takes it.
*/
/*
	Title: FSM Variables
*/
/*
	Variable: fsmGo
	Allows the fsm to continue
*/
var fsmGo = 0;
/*
	Variable: fsm_action_table
	Table that will contain the function associated to each state. During a state, the fsm
	will execute the function associated until the next state.
*/
var fsm_action_table:Array = new Array();
/*
	Variable: fsm_transition_table
	Table that will contain the nextState that will be set when a particular event occurs
	during a particular state
*/
var fsm_transition_table:Array = new Array();
/*
	Variable: currentState
	The state that the FSM is experiencing
*/
var currentState;

/*
	Variable: prevState
	The state that the FSM had just before the currentState.
*/
var prevState = null;

/*
	Variable: currentEvent
	The event that the FSM is experiencing. An event's life is not very long, except for
	"EV_NO_EV"
*/
var currentEvent;

/*
	Variable: fsm_param
	A parameter that can be passed during a transition
*/
var fsm_param = -1;

/*
	Variable: fsm_path
	The path that the FSMStick has to follow. If this variable is not null, the FSM will be
	forced to follow this path until the end.
*/
var fsm_path:Array = null;

/*
	Variable: fsmTimer
	A timer to allow the FSM to count things (such as the time it stays on a cell before moving)
*/
var fsmTimer = 0;

/*
	Variable: cellWasNeutral
	Count if a cell was neutral as the FSMStick walked in
*/
var cellWasNeutral = 0;

// list of events
/*
	Variable: listEvents
	List of all the events that can occur during FSM state
*/
var listEvents:Array = ["EV_Done", "EV_Error","EV_Escape","EV_GotoTimer", "EV_NO_EV" ,"EV_GotoSkill"];
for(i=0;i<listEvents.length;i++){
	this["ev"+(i+1)] = listEvents[i];
}
// List of states
/*
	Variable: listStates
	List of all the states that the FSM can take
*/
var listStates:Array = ["PickingCell","Shifting","Analysing","DefinePath","FSMWait","FSMSkill"];
for(j=0;j<listStates.length;j++){
	this["st"+(j+1)] = listStates[j];
	fsm_transition_table[this["st"+(j+1)]] = new Array();
}

/*
	Title: FSM Actions
*/
/*
	Function: pickCell
	
		This is the function associated with the state PickingCell.
		It defines wich tile the AI-Player wants to go on, trying to execpt steping on his
		own tiles.
	
	Parameters:
		none
		
	Returns:
		none
*/
function pickCell(){
	lowerExpectation = 0;
	currentEvent = ev5;
	//trace(fsm_param+"-"+fsm_path);
	if(fsm_param != -1 && fsm_path != null){
		if(fsm_param < fsm_path.length){
			var randCell:Array = [fsm_path[fsm_param].m_line,fsm_path[fsm_param].m_col];
			
			fsm_wantedCell = randCell;
			fsm_param++;
			//fsm_direction = 
		}
		if(fsm_param == fsm_path.length){
			fsm_resetParam();
			fsm_path = null;
		}
		fsm_receive_event(ev1,null);
	}
	else{
		//Get current Tile 
		var fsm_talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
		var surroundLet:Array = surroundingCellsCoords(myMap, fsm_talArr[0], fsm_talArr[1]);
		indexNum = 0;
		var surroundNum:Array = new Array();
		for(m in surroundLet){
			if(surroundLet[m]!="noCell"){
				surroundNum[indexNum] = surroundLet[m];
				indexNum++;
			}
		}
		
			//fsm_receive_event(ev2,null);
		var weights:Array = new Array();
		for(j in surroundNum){
			//trace(surroundNum[j]);
			var cell:Array = surroundNum[j];
			var type = myMap[cell[0]][cell[1]];
			var w:Number = getWeight(type,fsmStick);
			weights.push([w,cell]);
		}
		weights.sort();
		weights.reverse();
		//trace("w"+weights);
		maxWeight = weights[0][0];
		//Escape to pathFinding
		if(maxWeight <=2){
			fsm_receive_event(ev3,null);
			break;
		}
		//trace(maxWeight);
		//trace("m"+maxWeight);
		for(i=0;i<weights.length;i++){
			//trace(weights[i][0]+"-"+ maxWeight);
			if(weights[i][0] < maxWeight){
				weights.splice(i);
			}
		}
		//trace(weights);
		//trace(randCell);
		randNum = randRange(0,weights.length);
		randCell = weights[randNum][1];
		if(!randCell || currentEvent != ev5){}
		else{
			var surroundLet:Array = surroundingCellsCoords(myMap, fsm_talArr[0], fsm_talArr[1]);
			fsmStick.ori = arrayContainsCoords(surroundLet, randCell);
			fsm_wantedCell = randCell;
			fsmGo=1;
			//fsm_direction = 
			fsm_receive_event(ev1,null);
		}
	}
	fsmGo = 1;
}

function definePath(){
	currentEvent = ev5;
	// Pathfinder
	var fsm_talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
	var cell = findGoodCell(myMap, fsm_talArr[0], fsm_talArr[1], 3, fsmStick);
		var chemin:Array = pf_findPath(myMap, fsmStick.pp, cell);
		if(chemin.length == 0){
			trace("You shall not path !");
			fsm_receive_event(ev2,null);
		}
		else{
			fsm_path = chemin;
			fsmGo=1;
			fsm_param = 0;
			fsm_receive_event(ev1,0);
		}
	//
}

/*
	Function: shiftToPicked
	
		This is the function associated with the state Shifting.
		it executes the displacement onto the wanted cell picked by PickingCell.
	
	Parameters:
		none
		
	Returns:
		none
		
	See Also:
	
		<pickCell>
*/
function shiftToPicked(){
	currentEvent = ev5;
	if(fsmStick.t >= fsmStick.tmax){
		//trace(myMap[fsm_wantedCell[0]][fsm_wantedCell[1]]);
		shiftStick(0,0,fsmStick);
		fsmStick.t = 0;
		fsm_receive_event(ev1,null);
	}
	fsmGo=1;
}

/*
	Function: analyseCurrCell
	
		This is the function associated with the state Analysing.
		It analyses the cell on wich the AI-Player stands, and decide either to execute another
		displacement if the tile is his (State PickingCell back),
		either to stand on it until he own the tile.
	
	Parameters:
		none
		
	Returns:
		none
		
	See Also:
	
		<shiftToPicked>
		
		<pickCell>
*/
function analyseCurrCell(){
	currentEvent = ev5;
	var fsm_talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
	fsm_cellType = myMap[fsm_talArr[0]][fsm_talArr[1]];
	if(fsm_cellType == fsmStick.idEq || fsm_cellType >=10){
		if(cellWasNeutral == 42){
			cellWasNeutral = 0;
			fsm_receive_event(ev4,fsmReactionTime);
		}
		else{
			cellWasNeutral = 0;
			fsm_receive_event(ev1,null);
		}
	}
	else{
		cellWasNeutral = 42;
	}
	//Area analysing (get average weight of the area)
	var surround:Array = ringsSurrounding(myMap, fsm_talArr[0], fsm_talArr[1], 2);
	var average = 0;
	var nCells = 0;
	for(i in surround){
		for(j in surround[i]){
			var cell = surround[i][j];
			var type = myMap[cell[0]][cell[1]];
			if(type){
				var w = getWeight(type,fsmStick);
				average+=w;
				nCells++;
			}
		}
	}
	average/=nCells;
	average = Math.round(average);
	
	randNum = randRange(0,4);
	if(fsmStick.ts >= castSkillTime[fsmStick.pc]*fpsa && fsmStick.tmax <=20 && !gamePaused && randNum != 0){
		var skb:Boolean = false;
		switch(fsmStick.pc){
			case 1:
				//Get the average HP of the tiles surrounding
				var talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
				var cells:Array = surroundingCellsCoords(myMap, talArr[0], talArr[1]);
				var averageHP = 0;
				var nCells =0;
				for(i in cells){
					vi = cells[i][0];
					vj = cells[i][1];
					var type = parseInt(myMap[vi][vj]);
					if(type>=1 && type<=9 && type!=stick.idEq){
						hpAmount = healthPoints[(1000*vi)+vj][1];
						if(type == 1){
							hpAmount = 90;
						}
						averageHP += hpAmount;
						nCells++;
					}
				}
				averageHP/=nCells;
				//Calculate the amount of damage he can do
				randError = randRange(-100,100)/10;
				dammage =(warriorDammage*Math.pow((fsmStick.ts+randError),2));
				//Triggah'
				//Totally arbitrary : should also depend on levels
				triggNCells = 4;
				if(nCells >= triggNCells && averageHP && dammage >= averageHP){
					skb = true;
				}
			break;
			case 3:
				if(average < 5 && fsm_param == -1 && fsm_path == null){
					skb = true;
				}
			break;
			case 4:
				var talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
				var cells:Array = surroundingCellsCoords(myMap, talArr[0], talArr[1]);
				var nCells = 0;
				for(i in cells){
					var type = myMap[cells[i][0]][cells[i][1]];
					if(type != 0 && type <20){
						nCells++;
					}
				}
				//Totally arbitrary : should also depend on levels
				if(nCells < 4){
					skb = true;
				}
			break;
			case 5:
				//Find the tiles that are on the arrow's path :
				var talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
				var pathTiles:Array = tilesOnPath(myMap,talArr[0],talArr[1],fsmStick.ori);
				var nCells = 0;
				for(i in pathTiles){
					type = parseInt(myMap[pathTiles[i][0]][pathTiles[i][1]]);
					if(type >= 1 && type <20 && type !=fsmStick.id){
						nCells++;
					}
				}
				//add errors:
				randNum = randRange(0,10);
				if(randNum <=2){
					nCells +=1;
				}
				else if(randNum <= 4){
					nCells -=1;
				}
				else{
				}
				//Totally arbitrary : should also depend on levels
				if(nCells > 3){
					skb = true;
				}
			break;
			case 6:
				var talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
				var nCells = 0;
				//Find wich tiles, by rings, are around
				randNum = randRange(0,10);
				randRing = 0;
				if(randNum <=1){
					randRing = 1;
				}
				else if(randNum <= 3){
					randRing = -1;
				}
				else{
					randRing = 0;
				}
				var affectedCells = ringsSurrounding(myMap, talArr[0], talArr[1], ringsVampirismTakes);
				//per ring...
				for(i = 1 ; i < affectedCells.length ; i++){
					//per cell...
					for(j = 0 ; j < affectedCells[i].length ; j++){
						afci = affectedCells[i][j][0];
						afcj = affectedCells[i][j][1];
						cellType = myMap[afci][afcj];
						//test if it's ok to take HPs from it
						if(cellType <= 9 && cellType>1 && cellType!=stick.idEq){
							nCells++;
						}
					}
				}
				
				//Totally arbitrary : should depend on levels
				if(nCells >= 9){
					skb = true;
				}
			break;
			case 8:
				var talArr:Array = givePositionTale(fsmStick._x-offx, fsmStick._y-offy);
				//Find wich tiles, by rings, are around
				//Add error
				randNum = randRange(0,10);
				randRing = 0;
				if(randNum <=1){
					randRing = 1;
				}
				else if(randNum <= 3){
					randRing = -1;
				}
				else{
					randRing = 0;
				}
				var MGaffectedCells = ringsSurrounding(myMap, talArr[0], talArr[1], (howManyRingsIstheMagicianActive+randRing));
				//per ring...
				for(i = 1 ; i < MGaffectedCells.length ; i++){
					//per cell...
					for(j = 0 ; j < MGaffectedCells[i].length ; j++){
						afCell = MGaffectedCells[i][j];
						// Find if occuped
						if(pl=isOccupied(afCell)){
							//If yes, block the player
							skb = true;
						}
					}
				}
			break;
			case 9:
				if(fsm_param != -1 && fsm_path != null){
					skb = true;
				}
			break;
			default:
			skb = false;
			break;
		}
		if(skb == true){
			fsm_receive_event(ev6,null);
		}
	}

	fsmGo=1;
}

function fsmWait(){
	//trace(fsm_param)
	currentEvent = ev5;
	fsmTimer ++;
	if(fsmTimer >= fsm_param){
		fsm_param = -1;
		fsmTimer = 0;
		fsmGo = 1;
		fsm_receive_event(ev1,null);
	}
	fsmGo = 1;
}

function fsmSkill(){
	currentEvent = ev5;
	getSkill(fsmStick);
	fsm_receive_event(ev1,null);
	if(fsmStick.pc != 3){
		fsmGo =1;
	}
}

/*
	Title: FSM Configurations
*/
/*
	Function: fsm_configure_transition
	
		Configures the fsm by setting wich state will be set when a particulare event occurs
		during a particular state.
		
	Parameters:
	
	fstate - The state  to configure
	fevent - The event for this state to configure
	nextState - The sate that will be set when fevent occurs during fstate
	
	Returns:
		void
		
	Example of use:
	
	>fsm_configure_transition(st1, ev1, st2);
*/
function fsm_configure_transition(fstate, fevent, nextState){
	fsm_transition_table[fstate][fevent] = nextState;
	
}
/*
	Function: fsm_configure_action
	
		Configures the fsm by setting wich action (function) will correspond to wich state
		
	Parameters:
	
	fstate - The state  to configure
	function action - The function wich will be use during this state
	
	Returns:
		void
		
	Example of use:
	
	>fsm_configure_action(st1,pickCell);
*/
function fsm_configure_action(fstate, action){
	fsm_action_table[fstate] = action;
}

/*
	Function: fsm_configure_all
	
		Executes the configuration functions for the FSM
		
	Parameters:
	
		none	
	
	Returns:
		void
*/
function fsm_configure_all(){
	
	fsm_configure_action(st1,pickCell);
	fsm_configure_action(st2,shiftToPicked);
	fsm_configure_action(st3,analyseCurrCell);
	fsm_configure_action(st4,definePath);
	fsm_configure_action(st5,fsmWait);
	fsm_configure_action(st6,fsmSkill);
	
	fsm_configure_transition(st1, ev1, st2);
	fsm_configure_transition(st1, ev3, st4);
	fsm_configure_transition(st1, ev5, st1);
	fsm_configure_transition(st1, ev6, st6);
	
	fsm_configure_transition(st2, ev1, st3);
	fsm_configure_transition(st2, ev5, st2);
	fsm_configure_transition(st2, ev6, st6);
	
	fsm_configure_transition(st3, ev1, st1);
	fsm_configure_transition(st3, ev3, st4);
	fsm_configure_transition(st3, ev4, st5);
	fsm_configure_transition(st3, ev5, st3);
	fsm_configure_transition(st3, ev6, st6);
	
	fsm_configure_transition(st4, ev1, st1);
	fsm_configure_transition(st4, ev2, st1);
	fsm_configure_transition(st4, ev6, st6);
	
	fsm_configure_transition(st5, ev1, st1);
	fsm_configure_transition(st5, ev6, st6);
	
	fsm_configure_transition(st6, ev1, st1);
}

/*
	Function: initFSM
	
		Initializes FSM. Including define wich stick is the ai, configure the fsm, firstState
		
	Parameters:
	
		none	
	
	Returns:
		void
*/
function initFSM(){
	fsm_configure_all();

	currentState = st1;
	currentEvent = ev5;
	fsmGo = 1;

	fsmStick = this["stick"+(5)];
	fsmStick.ai = true;
	
	
	do{
		char = randRange(1,9);
	}
	while(char == 2 || char == 7 || char == 3 || char == 5);
	fsmStick.pc = char;
	fsmStick.skillTime = charParam["skillTime"][fsmStick.pc];
	fsmStick.tmax = charParam["dispSpeed"][fsmStick.pc];
}

/* 
	Title: FSM Execute
*/

/*
	Function: fsm_receive_event
	
		Set the next sate, according to what event occurs and wich state the FSM is experiencing
		
	Parameters:
	
	fevent - The event that occured
	param - a parameter (information) for this event
	
	Returns:
		void
		
	Example of use:
	
	>fsm_receive_event(ev1,null);
*/
function fsm_receive_event(fevent,param){
	currentEvent = fevent;
	//trace(currentState+"-"+currentEvent);
	prevState = currentState;
	nextState = fsm_transition_table[currentState][currentEvent];
	//trace(currentState+"-"+currentEvent+"-"+nextState);
	currentState=nextState;
	if(param != null){
		fsm_param = param;
	}
	//trace("transition "+currentState);
	//fsmGo = 1;
}

function fsm_resetParam(){
	fsm_param = null;
}

function getWeight(type, stick){
	var w:Number;
	if(type == 0 || type >= 20){
		w = 0;
	}
	else if(type == 1){
		w = 9;
	}
	/*for(i=2;i<10;i++){
		if(type == i){
			w = 8;
		}
	}*/
	for(j in players){
		i = parseInt(j)+2;
		if(type == i){
			w = 8;
		}
	}
	if(type == stick.id){
		w = 2;
	}
	if(type[0]){
		w=3;
	}
	return w;
}


function findGoodCell(map:Array, i, j, minCellWeight, stick){
    var ringsOfCells = new Array();
    ringsOfCells[0] = [[i, j]];        // Ring 0 : the chosen cell
    var ring = 1;
    continueRingLoop = true;        // For stopping the loop after a certain number of rings
    while(continueRingLoop){
        ringsOfCells[ring] = new Array();
        indexRing = 0;            // Index of the array for the current ring
         
        // Loop : gets the cells of the inferior ring and checks their neighbour cells
        for(i in ringsOfCells[ring-1]){
            cell = ringsOfCells[ring-1][i];        // Inferior ring cell
            aroundCurrentCell = surroundingCellsCoords(map, cell[0], cell[1]);
             
            // Loop : checking if the cells must be added to the current ring array
            for(j in aroundCurrentCell){
                tempCell = aroundCurrentCell[j];
                var rejectCell = false;
                // Loop : is this cell already in the returned array ?
                for(k = ring-2 ; k <= ring ; k++){
                    if(k < 0){ k = 0; } // Prevents error if ring = 1
                    // use of other function arrayContainsCoords(array, coords)
                    if((tempCell == "noCell") || arrayContainsCoords(ringsOfCells[k], tempCell) != -1){
                        rejectCell = true;
                    }
                }
                if(!rejectCell){
                    typeTempCell = parseInt(map[tempCell[0]][tempCell[1]]);
                    if(getWeight(typeTempCell, stick) >= minCellWeight){
                        return tempCell;
                    }
                    // Update of returned array and current ring index
                    ringsOfCells[ring][indexRing] = tempCell;
                    indexRing++;
                }
            }
        }
        ring++;
         
        // Stop condition
        if(ring > 15){
            continueRingLoop = false;
        }
    }

    return -1;

}

this.onEnterFrame = function(){
	if(fsmGo == 1){
		fsmGo=0;
		//trace("loop "+currentState);
		fsm_action_table[currentState]();
	}
}
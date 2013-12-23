/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						Parameters, second sheet				*
*																*
* - Authors :	Léonard 										*
* - Description : list and values of variables will be passed 	*
*							by the loader /configuration swf	*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
	Variable: gameOfLife
		Activate the "GameOfLife" mode. It includes the fact that a cell slowly dies when Isolated.
		(Reference to Conway's rules)
*/
var gameOfLife = 1;

/*
	Variable: nNeighboursConwell
		Set the minimum number of neighbours a cell must have to be considered as 
		"not isolated" (no loosing HPs)
*/
var nNeighboursConwell = 1;
/*
	Variable: nPlayer
		Number of players
*/
var nPlayer = 0;

/*
	Variable: teamPlay
		Enable teamPlay. The even players will be together, odd together. 'nuff said
*/
var teamPlay = 0;

/*
	Variable: fsmEnable
		Enable the FSM. The AI will occupate the last slot of player of nPlayer.
*/
var fsmEnable:Boolean = false;


// Player controls 

var playerControls:Array = [[Key.UP, Key.DOWN, Key.RIGHT, Key.LEFT, Key.END],
							[90, 83, 68, 81, 69],
							[89,72,74,71,85],
							[104,101,102,100,96]];
						/*	
var playerControls:Array = new Array();
playerControls = copyArray(originalPlayerControls);*/


var victScore:Number = 100000;
var victTile:Number = 0;
var victTime:Number = 0; // in sec

var gameType=null;

var classNames:Array = ["Admin","Guerrier",null,"Mineur","Enchanteur","Archer","Vampire","Ordinateur","Magicien","Booster"];

/*
	Variable: pvc
	The ID of character that the player has before the game starts
*/
var pvc:Array = new Array();

//ADV Define things

var ADVMapValues:Array = new Array();
//Maps values :
//nPlayers - players - fsmEnable - mixStartPoints - FSMSkill - victScore - victTile - victTime
ADVMapValues[20] = [2,[1,0,0,1],true,false,9,1000,0,90];
ADVMapValues[21] = [2,[1,0,0,1],true,true,1,2000,0,0];
ADVMapValues[22] = [2,[1,0,0,1],true,true,6,4000,0,20];
ADVMapValues[23] = [2,[1,0,0,1],true,false,6,2000,0,0];
ADVMapValues[24] = [2,[1,0,0,1],true,true,6,1000,0,0];
ADVMapValues[25] = [0];
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*							Parameters							*
*																*
* - Authors :	Léonard 										*
* - Description : list and values of variables that define the	*
*				  entire game.									*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// Size of the tales

var fac = 2;

offyMap = 10;

var idBlockingHigh = 100;
var idblockingLow = 200;

/*
	Variable: tw
	the width of a tile
*/
var tw = 60/fac;
/*
	Variable: th
	the height of a tile
*/
var th = 35/fac;

// Offset for the position of a stick on a tale
/*
	Variable: offx
	Offset on the x axis to place the stick at the center of a tile
*/
var offx = 15;
/*
	Variable: offy
	Offset on the y axis to place the stick at the center of a tile
*/
var offy = 8;

// v6 : the character parameters table (ParamTable) :
/*
charParam["skillTime"] = [1, 5, 1.5, 2, 2, 2, 2];					// time to cast a spell
charParam["dispSpeed"] = [2, 10, 10, 10, 10, 10, 10];				// Char's Displacement speed
charParam["decLifeForced"] = [10, 5, 5, 5, 5, 5, 5];				// Damage rate to enemy celle
charParam["specParam"] = [null, 140, 4, null, 5, 6, [10,1], null];	// Parameter used by skill
charParam["maxHP"] = [230, 120, 130, 130, 130, 130, 130];			// Max HP you can Regen (after 100)
charParam["recovLifeAuto"] = [1,0.2,0.2,0.2,0.2,0.2,0.2];			// regen rate
*/
/*
	Variable: charParam
		This is a huge parameter Table used to define most of the parameters concerning a player.
		It is set so these parameters can be modified for each different class of character.
		Here the line is a parameter, each collumn is the value for a character (see below)
	
	List of characters and IDs:
		
		0 - Admin (lol & test character)
		1 - Knight
		2 - Old Magician (/!\ Not used anymore)
		3 - Miner
		4 - Warlock
		5 - Archer
		6 - Vampire
		7 - No Power Guy (AI)
		8 - Magician (v8)
		9 - Booster
		
	Contains:
	
		skillTime - time to cast a spell
		dispSpeed - Char's Displacement speed
		decLifeForced - Damage rate to enemy cell
		specParam - Parameter used by skill
		maxHP - Max HP you can Regen (after 100)
		recovLifeAuto - regen rate
		
	Example of use:
		The displacement speed of the magician is stored so :
		
> charParam["dispSpeed"][2]
*/
var charParam:Array = new Array();

/*
	Variable: characters
	Array that contains the name of each character, indexed with his ID
*/
var characters:Array = ["!Admin","Knight", "!OldMagician", "Miner", "Warlock", "Archer", "Vampire", "!AI", "Magician", "Booster"];

// Loading ParamTable :
/*
	Variable: paramXML
		The XML that contains the parameters for the characters. Will be interpreted to define 
		charParam
	
	See Also:
	
		<charParam>
*/
var paramXML:XML = new XML();
paramXML.ignoreWhite = true;
paramXML.onLoad = function(success)
{
	for (i = 0; i < this.firstChild.childNodes.length; i++)
	{
		chName = this.firstChild.childNodes[i].nodeName;
		//trace(chName);
		//charParam[chName] = 
		arrProv = new Array(7);
		for (j = 0; j < this.firstChild.childNodes[i].childNodes.length; j++)
		{
			nValue = this.firstChild.childNodes[i].childNodes[j].firstChild.nodeValue;
			if (nValue == "null")
			{
				fValue = null;
			}
			else
			{
				n2Value = Number(nValue);
				if (n2Value != nValue)
				{
					nArr = nValue.split(",");
					for(k=0;k<nArr.length;k++){
						nArr[k] = parseInt(nArr[k]);
					}
					fValue = nArr;
				}
				else
				{
					//trace(n2Value);
					fValue = n2Value;
				}
			}
			arrProv[j] = fValue;
		}
		_root.charParam[chName] = arrProv;
	}
	
	_root.lifeTimeMagicianBlockingTile = _root.charParam["specParam"][2]; // in sec, OLD magician
	_root.howLongBlockingMagician = _root.charParam["specParam"][8][1];
	_root.howManyRingsIstheMagicianActive = _root.charParam["specParam"][8][0];
	_root.nBlastedTiles = _root.charParam["specParam"][4]; // number of tiles that get blasted by the warlock PER PLAYER
	_root.ringsVampirismTakes = _root.charParam["specParam"][6][0]; // The number of HP the vampire takes PER ENEMY CELL
	_root.rateVampirismGains = _root.charParam["specParam"][6][1];
	_root.arrowSpeed = _root.charParam["specParam"][5][0];
	_root.archerDammage = _root.charParam["specParam"][5][1];
	_root.warriorDammage = _root.charParam["specParam"][1][0]; // Set an amount of HP the warrior bashes
	_root.warriorDammage /= Math.pow((_root.charParam["specParam"][1][1]*_root.fpsa),2);
	// Array to set the time it takes for a character to cast
	_root.castSkillTime = _root.charParam["skillTime"]; // Set in sec
	_root.boosterSpeed=_root.charParam["specParam"][9][0];
	_root.boosterTime=_root.charParam["specParam"][9][1];
	launchGameOk++;
};//end load XML
paramXML.load("paramTable.xml");

/*
	Variable: initHP
	initial amount of HP
	
	See Also:
	
		<stickActivateTale>
		
		<refreshHealthPoints>
*/
var initHP = 100;
// Rate of decreasing of the HP of a cell
/*
	Variable: decLifeAuto
	When isolated, a cell starts to die, slowly decreasing HPs. this is the rate of HP decreasing
	per 1/24 sec.
	
	See Also:
	
		<initHP>
		
		<stickActivateTale>
		
		<refreshHealthPoints>
*/
var decLifeAuto = 1;
/*
	Variable: gainLife
	After recovering quickly up to 'initHP' HPs, a cell gains slowly HPs up to a maxHP limit.
	This is the rate of HP increasing per 1/24 sec.
	
	See Also:
	
		<initHP>
		
		<charParam>
		
		<stickActivateTale>
		
		<refreshHealthPoints>
*/
var gainLife = 0.01;
/*
	Variable: giveScore
	Rate of getting your score refreshed (getting new points). In sec
	
	See Also:
	
	<02_scoreNTalesMethods.as>
*/
var giveScore = 2;
/*
	Variable: rPoints
	Number of point you got per tale you own
*/
var rPoints = 2;
/*
	Variable: fpsa
		Framerate per second of the animation
*/
var fpsa = 24;

/*
	Variable: gamePaused
	Boolean that indicates (and sets) the pause of the game on/off
*/
var gamePaused:Boolean = false;

/*
	Variable: endGameb
	Boolean that indicates (and sets) the end of the game on.
*/
var endGameb:Boolean = false;

/*
	Variable: cntdownDuration
	The amount of time in second to wait after a pause before the game is resumed
*/
var cntdownDuration = 2;

/*
	Variable: dateG
	The duration in frames of the game. This variable is incremented every frame of the game,
	except during pause times.
*/
var dateG:Number = 0;

var totalTile:Number = 0;

var startPoints:Array = new Array();

/*
	Variable: healthPoints
		healthPoints is an Array meant to store all the HPs of each tile.
	
	Structure:
		The index of a tile with (i,j) coordinates is [(1000*i)+j]. In this case, each healpoint
	value is unique.
		
		Each healthpoint value includes an array with two values :
		- The ID of the player (can be 1 for "neutral" if it's a countdown cell)
		- The amount of HPs for this tile
		
		> healthpoint[(1000*i)+j] = [IDPlayer][AmountHP];
		
	See Also:
		
		<02_scoreNTalesMethods.as>
*/
var healthPoints:Array = new Array();
/*
	Variable: myMap
	
		The Map is the structure that includes all datas concerning the tiles : wich type they are,
		owner, position, etc.
		It is a 2D Array wich represents each tile.
		
		The Map will take care of :
			- Color of the owned tiles
			- The changing tiles (switches)
			- Every calculus map-related (special tiles with array)
		
		It is automatically computed when the games loads the DMap
		
	Example of structure:
	
	> var myMap:Array = [[0, 0, 0, 1, 1, 1, 0, 0], 
	>					  [0, 0, 1, 1, 1, 1, 1, 0], 
	>					 [0, 0, 1, 1, 1, 1, 1, 0], 
	>					  [0, 0, 0, 1, 1, 1, 0, 0]];
	
	Content: 
	
	- Tales 0 : empty
	- Tales 1 to 9 : neutral or owned tale. Conquerable
	- Tales from 1 to 19 : walkable
	- Tales above 20 : obstacles;
	
	See Also:
		
		<myDMap>
		
		<02_mapMethods.as>
	
*/
var myMap:Array = new Array();
/*
	Variable: myDMap
	
		DMap, as for "Design Map", is a background map wich will support the graphics of the environment. It is not meant to be changed during the game.
	The DMap will take care of displaying the general graphics for the environment (grass, moutain, water, whatever)
	
	The DMap has the exact same structure as the Map, so they can be stacked properly.

	Editor:
	
	The editor produces a DMap as XML.
	The FAP_D program compiles it into a DMap and a myMap.

	Notes: 
	
		t 1 -> 20 : walkable cell
		
		t > 20 : blocking cell
		
		In the case of a switch, it is MANDATORY that the design of the "after" cell is 1 or 20. Nothing special about the "before" cell. Same in case of CountdownCell

	See Also:
		
		<myMap>
		
		<02_mapMethods.as>
*/
var myDMap:Array = new Array();

/*
	Title: About Skills
*/

/*
	Variable: lifeTimeMagicianBlockingTile
		Duration of the life of a magical tile;
	
	Warning:
		this counts for the OLD magician !! ID = 2 !
		
	See Also:
	
		<charParam>
		
		<getSkill>
*/
var lifeTimeMagicianBlockingTile:Number; // in sec


/*
	Variable: howLongBlockingMagician
		Duration of the blocking of the players the magician hit.
	
	Warning:
		this counts for the NEW magician !! ID = 8 !
		
		This also counts for the archer, as an arrow touches an ennemy. the arrow blocks this
		ennemy for howLongBlockingMagician secs
		
	See Also:
	
		<charParam>
		
		<getSkill>
*/
var howLongBlockingMagician:Number;

/*
	Variable: howManyRingsIstheMagicianActive
		how many rings the Magician will hit with his blocking spell
	
	Warning:
		this counts for the NEW magician !! ID = 8 ! 
		
	See Also:
	
		<charParam>
		
		<getSkill>
*/
var howManyRingsIstheMagicianActive:Number;
/*
	Variable: nBlastedTiles
		number of tiles that get blasted by the warlock PER PLAYER

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var nBlastedTiles:Number;
/*
	Variable: ringsVampirismTakes
		Number of rings around the Vampire on wich he sucks HPs

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var ringsVampirismTakes:Number;
/*
	Variable: rateVampirismGains
		The number of HP the vampire gains per enemy HP sucked.
		It can be set to 1/2 so that the vampire only recieves a fraction of what he sucks.
		Vampires suck anyway.

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var rateVampirismGains:Number;
/*
	Variable: arrowSpeed
		Seriously ? Does that need a description ?

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var arrowSpeed:Number;
/*
	Variable: warriorDammage
		Set an amount of HP the warrior bashes per 1/24 sec. The new version of the warrior is
		set so that he can bashes whenever he wants, but the more he waits, the more powerful
		his bash gets.
		warriorDammage is a coefficient to help with the enpowering.
		
	Note:
		In the paramTable, the specParam of the warrior is an array containing :
		
		0 - The amount of HP to be bashed if he waits a certain amount of time
		1 - this specific amount of time in secs.
		
		Example : he one wants the warrior to bash 140 HPs if he waits 5 secs, one will write
		140,5 in the array

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var warriorDammage:Number;


/*
	Variable: boosterSpeed
		The speed that the Booster gets when he uses his skill

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var boosterSpeed:Number;

/*
	Variable: boosterTime
		The amount of time during wich the Booster gets his speed increased.

	See Also:
	
		<charParam>
		
		<getSkill>
*/
var boosterTime:Number;

/*
	Variable: archerDammage
		The amount of HP an arrow takes on each tile
		
	See Also:
	
		<charParam>
		
		<getSkill>
*/
var archerDammage:Number;

/*
	Variable: warlockCanDisableTraps
		A boolean that toggles the ability of disabling trap cells for the warlock.
		If it's set true, he can disable them, else he gets blasted as every other player.
		#InYourFace
		
	See Also:
	
		<charParam>
		
		<getSkill>
*/
var warlockCanDisableTraps:Boolean = true;

/*
	Variable: listTrapCells
	A list of all the trap cells the warlocks of the game planted.
	
	Use:
	the indexes are such as :
	
	0 : i - the i coordinate of the trap cell 
	1 : j - the j coordinate of the trap cell 
	2 : p - the id of the warlock who planted it
	
	See also:
	
	<getSkill>
*/
var listTrapCells:Array = new Array();

/*
	Title: Final State Machine Parameters
	Note : are listed here only parameters that can be changed depending on the level chosen
	for the FSM. The variables needed for the FSM to work are directly documented in the FSM
	code.
	
	See Also:
	
	<Final State Machine>
*/

/*
	Variable: fsmReactionTime
	The time needed for the FSM to react after conquering an enemy cell. To seem more human,
	the FSM waits for *fsmReactionTime* frames after killing a cell before moving again.
*/
var fsmReactionTime:Number = 4;

//Interactions with the DMap

var iceSpeedDecrease = 2;

var higherMaxHP = 200;
var gainLifeFactorMultiplier = 1.5;

var listNPC:Array = new Array();

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						Diverse Functions						*
*																*
* - Authors :	Léonard											*
* - Description : Diverse functions that will be needed 		*
*																*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
	Function: attachAnimation
	
	Attaches the movieClip of an animation where asked to
	
	Parameters: 
	
		String s - the library name of the clip you want to attach
		Number x - position on the x-axis (in pixel)
		Number y - position on the y-axis (in pixel)
		
	Note: 
	
 		Very powerful ! No security, can attach anything anywhere ! Careful.
		
	Returns: 
	
 		void
		
*/
function attachAnimation (s:String,x:Number,y:Number){
	var anim:MovieClip = this.attachMovie(s,s,this.getNextHighestDepth());
	anim._x = x;
	anim._y = y;
}

/*
   Function: randRange

   Defines a random int between the two paramters min<max.

   Parameters:
   
      Number min - inferior bound
      Number max - superior bound

	Note:
   
   min < max !
   
   Returns:

	randomNum - A random int between min and max
*/
function randRange(min:Number, max:Number):Number {
     var randomNum:Number = Math.floor(Math.random() * (max - min + 1)) + min;
     return randomNum;
}

/*
	Function: copyArray
	Takes an array an returns a copy of this array (different pointer)
	
	Parmaters:
	
		Array origArray - the Array one wants to copy 
		
	Returns:
		
		Array copiedArray - A copy of the original array
*/
function copyArray(origArray:Array){
    var copiedArray = new Array();
    for(var i = 0 ; i < origArray.length ; i++){
        if(typeof origArray[i] == "array"){
            copiedArray[i] = copyArray(origArray[i]);
        } else {
            copiedArray[i] = origArray[i];
        }
    }
   
    return copiedArray;
}

/*
	Function: testVictory
	Tests the condition of victory, and if someone is winning, return the ID of this player.
	The victory is based on three variables : <victTime> , <victScore> , <victTile>
	
	The first is the max time to win the game. Above this time, the game is lost. Note : if
	victTime = 0, the time is "infinite"
	
	VictScore is about the amount of points one must possess in order to win. Note : if
	victScore = 0, no need to have any points to win (cf victTile, then).
	
	the last one, victTile, sets a percentage of the total takable cells the player must
	have in order to win. Note : if victTile = 0, no need to have any tiles to win, just score
	(cf victScore, then).
	
	Returns:
	
	Number p - the ID of the winner
*/
function testVictory(){
	p=-1;
	//test for each player
	for(i=0;i<10;i++){
		if(players[i] == 1){
			var stick:MovieClip = _root["stick"+(i+2)];
			//place here victory condition
			score = stick.score;
			var nArr:Array = _root.countCellsByType(_root.myMap);
			tilesOwned = nArr[stick.id];
			i=stick.id-2;
			//pass the time test
			//if(victTime!=0 && dateG <= victTime*fpsa){
				//Pass the score test
				if(score && score>=victScore){
					//Pass the tile test
					if(tilesOwned/totalTile >= victTile){
						p=i;
					}
				}
			//}
			if(victTime!=0 && dateG >= victTime*fpsa){
				endGame((-1));
			}
			else{}
		}
	}
	return p;
}

/*
	Function: endGame
	Manages the end of the game (launch the pauseScreen with end mode)
*/
function endGame(p){
	//trace("Victoire de "+p);
	gamePaused = true;
	endGameb = true;
	var ps:MovieClip = _root.attachMovie("pauseScreen","pauseScreen",_root.getNextHighestDepth());
	ps.p = p;
	ps.stick = _root["stick"+(p+2)];
	ps.gotoAndStop(3);
	Key.removeListener(spyPause);
	delete ps;
}

/*
	Function: blast
	Blasts a player, meaning strike with a lightning *nBlastedTiles* tiles from the player 
	given as parameter, these tiles die instantly
	
	Parameters:
	
	Number idPlayer - the ID of the player to blast
*/
function blast(idPlayer){
	// Get the list of the array he owns
	var exArr:Array = new Array();
	for (j=1; j<=9; j++) {
		if (j != idPlayer) {
			exArr.push(j);
		}
	}
	var listArr:Array = findPlayableCells(myMap, exArr);
			//Blast
			for (i=0; i<nBlastedTiles; i++) {
				//Pick random cell
				j = randRange(0, (listArr.length)-1);
				randCell = listArr[j];
				//KILL IT WITH FIRE !
				healthPoints[(randCell[0]*1000+randCell[1])] = -1;
				//Add animation
				var posArr:Array = giveTalePosition(randCell[0], randCell[1]);
				rl = randRange(1,4);
				attachAnimation("aBlast"+rl, posArr[0]+offx, posArr[1]+offy);
			}
}

/*
	Function: backgroundFade
	makes the background (the sky from the upper animation) to fade.
	
	Parameters:
	
	Number speed - the rate at wich the alpha of the background has to decrease
	
	Warning:
	
	Has to be use on the right level of movieClip !
*/
function backgroundFade(speed){
	bgnd.speede = speed;
	bgnd.onEnterFrame = function(){
		this._alpha -= this.speede;
	}
}

function changeParameter(param, nvalue, time, stick){
	//trace(stick+"-"+time+"-"+nvalue);
	var chMc = _root.createEmptyMovieClip("changeParam"+_root.getNextHighestDepth(),_root.getNextHighestDepth());
	chMc.resetValue = charParam[param][stick.pc];
	chMc.tmax = time;
	chMc.stick = stick;
	chMc.t = 0;
	chMc.param = param;
	//Set value
	if(param == "dispSpeed"){
		stick.tmax = nvalue;
	}
	else{
		stick[param] = nvalue;
	}
	//trace(stick.tsmax);
	chMc.onEnterFrame = function(){
		this.t++;
		if(this.t>= this.tmax*_root.fpsa){
			if(this.param == "dispSpeed"){
			this.stick.tmax = this.resetValue;
			}
			else{
				this.stick[this.param] = this.resetValue;
			}
			this.removeMovieClip();
		}
	}
}
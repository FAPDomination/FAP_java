/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						Methods for the score					*
*																*
* - Authors :	Léonard											*
* - Description : functions that control the HealthPoints of	*
*				the tales and the tales.						*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
	Function: stickActivateTale
	
	this function changes the color of a tale depending on who is on. If the tale is already the property of the player, nothing happens. If it's the property of noone, it becomes
the color of the player instantly. If it's another player's property, the healthpoints
of the tale decrease to 0 and then the color changes.

	Parameters:
	
	Stick stick - the player wich is on the tale
	
	Returns:
		void
*/
function stickActivateTale(stick) {
	IDplayer = stick.id;
	// Get the position of the player
	var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
	/*
		Variable: isTakable
		Tests if the tale is already owned by the player
		
		Results:
			Is true if the tile is either  the tile is a 1 to 9 tile not owned by the player
			
			Or if it's a takable countdown cell (not implemented yet)
	*/
	var isTakable:Boolean = myMap[talArr[0]][talArr[1]] != stick.idEq && (!myMap[talArr[0]][talArr[1]][0] || (myMap[talArr[0]][talArr[1]][0] == 1 && myMap[talArr[0]][talArr[1]][2] == 0));
	if (isTakable) {
		//if(myMap[talArr[0]][talArr[1]][0] == 1){
		//	trace('ok');
		//}
		// if not, tests if the tale is neutral
		if (healthPoints[talArr[0]*1000+talArr[1]][1]<=0 || !healthPoints[talArr[0]*1000+talArr[1]][1]) {
			// When a timer-cell comes back to a non-walkable cell, restore the player's former coordinates
			if (myMap[talArr[0]][talArr[1]] == 20) {
				//var nPos:Array = giveTalePosition(_root["stick"+IDplayer].inii,_root["stick"+IDplayer].inij);
				kickBack(IDplayer);
			} else {
				// The tale is empty, sets it as the property of the player, gives HP and draw the according map
				myMap[talArr[0]][talArr[1]] = stick.idEq;
				_root["t"+((1000*talArr[0])+talArr[1])].gotoAndStop(stick.idEq);
				healthPoints[1000*talArr[0]+talArr[1]] = new Array(stick.idEq, initHP);
				//drawMap();
			}
			//refreshHealthPoints();
		} else {
			// Else forces the healthpoints of the tale to decrease (Attack)
			healthPoints[talArr[0]*1000+talArr[1]][1] -= stick.decLifeForced;
		}
	}
	//Special Tile that slows you down (ice) :
	if(myDMap[talArr[0]][talArr[1]] == 9){
		changeParameter("dispSpeed", stick.tmax+iceSpeedDecrease, 4, stick);
	}
};
/*	
	Function: countCellsByType
	
		Method that counts, for every type, the number of concerned cells existing on the map.
	
	Parameters:
	
		map - the map you want to count the cells by type on.
		
	Returns:
	
		countArray - Array containing the number of occurrences for each tile type. For example, countArray[3] gives you the number of "type 3" cells on the map.

*/
function countCellsByType(map:Array) {
	var countArray = new Array();
	for (i=0; i<map.length; i++) {
		for (j=0; j<map[i].length; j++) {
			if (!countArray[map[i][j]]) {
				countArray[map[i][j]] = 0;
				// if not defined yet, set as 0.
			}
			countArray[map[i][j]]++;
		}
	}
	return countArray;
};
/*
	Function: refreshHealthPoints
	
		Complicated function wich calculates the healthpoints of tale depending on several factors
such as the number of neighbours of the tale.

	Parameters:
	
		none
		
	Note:
	
		the Healthpoints are stocked in an Array healthPoints[]. The index of each tale is set so:
i*1000 + j where i&j are the coordinate of the tale for wich the HP are calculated

	Returns:
	
		void
*/
function refreshHealthPoints() {
	//trace(_root.kco);
	// Checks all HP
	for (i in healthPoints) {
		// Refreshes text in the tale
		_root["t"+i].pvt.text = Math.floor(healthPoints[i][1]);
		// Finds the coordinates of the tale depengin on the index
		vi = Math.floor(i/1000);
		vj = i-vi*1000;
		// Counts the neighbours of the same type of the tale
		//Note : MyDMap != 8 is for lava floor and unstable cells
		var recovB:Boolean = myDMap[vi][vj] != 8 && healthPoints[i][0] !=1 && countNeighbours(myMap, vi, vj, healthPoints[i][0])>=nNeighboursConwell;
		if (recovB) {
			// If the cell is wounded (under initHP HPs)
			if (healthPoints[i][1]<initHP) {
				//_root["t"+i].onEnterFrame = function() {
					// The HP will recover slowly up to initHP
					healthPoints[((vi*1000)+vj)][1] += _root["stick"+healthPoints[((vi*1000)+vj)][0]].recovLifeAuto;
					this.pvt.text = Math.floor(healthPoints[((vi*1000)+vj)][1]);
				//};
				// between initHP and maxHP
			} else if (healthPoints[i][1]<charParam["maxHP"][_root["stick"+healthPoints[i][0]].pc] || (healthPoints[i][1]<higherMaxHP && myDMap[vi][vj] == 13)) {
				//_root["t"+i].onEnterFrame = function() {
					// The HP will very slowly increase up to the max limit
					var gainLifeFactor;
					if(myDMap[vi][vj] == 13){
						gainLifeFactor = gainLifeFactorMultiplier;
					}
					else{
						gainLifeFactor = 1;
					}
					healthPoints[((vi*1000)+vj)][1] += gainLife*gainLifeFactor;
					this.pvt.text = Math.floor(healthPoints[((vi*1000)+vj)][1]);
				//};
			} else {
				// If the tale isn't lonely or anything, do nothing
				//delete _root["t"+i].onEnterFrame;
			}
		} else {
			/* Only enabled when "GameOfLife" level 1 or more is on :
			the goal here is to decrease the HP of the tale because it's alone.
			Cells need to be in groups to survive
			*/
			if (gameOfLife>=1 && healthPoints[i][0] != 1) {
					//_root["t"+i].onEnterFrame = function() {
						// The HP will decrease until the cell is dead OR not alone anymore
						healthPoints[((vi*1000)+vj)][1] -= decLifeAuto;
						this.pvt.text = Math.floor(healthPoints[((vi*1000)+vj)][1]);
					//};
			}
		}
		// Testing if the tale is a neutral tale with HP (Countdown Cell) :
		if (healthPoints[i][0] == 1) {
			//if(_root["t"+i].onEnterFrame){}
			//else{
				_root["t"+i].vi = vi;
				_root["t"+i].vj = vj;
				//_root["t"+i].onEnterFrame = function() {
					// The HP will decrease until the cell is dead
					healthPoints[((vi*1000)+vj)][1] -= decLifeAuto;
					this.pvt.text = Math.floor(healthPoints[((vi*1000)+vj)][1]);
				//};
			//}
		}
		// If a cell dies, it goes back to normal un-owned tale  
		if (healthPoints[i][1]<=0) {
			// if it's a timer-cell
			if (healthPoints[i][0] == 1) {
				//test if it's an occupied- takable cell :
				cvi = _root["t"+i].vi;
				cvj = _root["t"+i].vj;
				var talArr:Array = new Array(cvi,cvj);
				if(myMap[cvi][cvj][2] == 0 && isOccupied(talArr)){
					myMap[cvi][cvj] = isOccupied(talArr);
					_root["t"+i].gotoAndStop(isOccupied(talArr));
				}
				else{
				// it becomes blocking again
					m = 20; 			// v6-Point : Doesnt block v6.
					_root["d"+i].gotoAndStop(idBlockingHigh);
				}
			} else {
				//else it goes back to neutral
				m = 1;
			}
			// set the changes in the different variables and clips
			myMap[vi][vj] = m;
			_root["t"+i].gotoAndStop(m);
			_root["t"+i].pvt.text = "";
			delete healthPoints[i];
			delete _root["t"+i].onEnterFrame;
		}
	}
};
// --------------- About the score :

/*
	Function: countDiffScore
	
		Calculates the number of points a player gets for his tales
		
	Parameters:
	
		Number IDplayer - ID of the player one wants to calculate the score
		
	Returns:
	
		void, automatically adds the points to the score textfield.
		
*/
function countDiffScore(IDplayer) {
	var healthPoints = healthPoints;
	// Method 1 : counts rPoints per tale owned
	s = 0-rPoints;
	for (i in healthPoints) {
		if (healthPoints[i][0] == IDplayer && healthPoints[i][1]>0) {
			s += rPoints;
		}
	}
	score = parseInt(scoreMc["score"+IDplayer+"Player"].scoret.text)+s;
	_root["stick"+IDplayer].score = score;
	scoreMc["score"+IDplayer+"Player"].scoret.text = score;
};

/*
	Function: findPlayableCells
	
		This method returns an array of all the "normal" playable cells of the map, except those wich are
		given in parameters
		
	Example of use: 
	
		teleport skill.
		
	Parameters:
	
		Array map - the map you want to analyze.
		Array excludedCells - Array of cells types you don't want to take into count
		
	Returns:
	
		playableCellsArray - the array that contains the coordinates of every "playable" cell of the map.
*/
function findPlayableCells(map:Array, excludedCells:Array) {
	var playableCellsArray = new Array();
	var currentIndex:Number = 0;
	for (i=0; i<map.length; i++) {
		for (j=0; j<map[i].length; j++) {
			if (0<map[i][j] && map[i][j]<10) {
				if (!excludedCells) {
					playableCellsArray[currentIndex] = [i, j];
					currentIndex++;
				} else {
					var saveThisCell:Boolean = true;
					for (var elem in excludedCells) {
						if (excludedCells[elem] == map[i][j]) {
							saveThisCell = false;
						}
					}
					if (saveThisCell) {
						playableCellsArray[currentIndex] = [i, j];
						currentIndex++;
					}
				}
			}
		}
	}
	return playableCellsArray;
};

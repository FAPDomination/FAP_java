/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						Methods for the displacement			*
*																*
* - Authors :	Léonard											*
* - Description : Function allow to move the sticks on the map	*
*																*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
	Function: shiftStick
	
	Changes position of a specified stick depending on the displacement needed
	
	Parameters: 
	
	Number dx - number of tiles for displacement "x"-axis
	Number dy - number of tiles for displacement "y"-axis
	Stick stick - the stick you want to displace
	
	Note: 
	
	dx & dy can both be negative !
	
	Returns:
	
	void
*/
function shiftStick(dx:Number, dy:Number, stick:MovieClip) {
	// Get the position of the stick
	var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
	// Get the supposed new position of the stick
	if(!stick.ai){
		var posArr:Array = giveTalePosition(talArr[0]+dy, talArr[1]+dx);
		var tal2Arr:Array = givePositionTale(posArr[0], posArr[1]);
	}
	else{
		var posArr:Array = giveTalePosition(fsm_wantedCell[0], fsm_wantedCell[1]);
		var tal2Arr:Array = fsm_wantedCell;
	}
	//trace(myMap[tal2Arr[0]][tal2Arr[1]][0]);
	/* 
		Variable: walkable
		tests if it's allowed to walk on the tile
		
		Results:
			Is true if the tile type is t and 1<= t < 20, and the tile is not occupied.
	*/
	var walkable:Boolean = myMap[tal2Arr[0]][tal2Arr[1]]>=1 && myMap[tal2Arr[0]][tal2Arr[1]]<20 || myMap[tal2Arr[0]][tal2Arr[1]][0];
	// Adds colisions : one does not simply walk into an occupied tale
	if (isOccupied(tal2Arr)) {
		walkable = false;
	}
	var n:NPC=isNPC(tal2Arr[0],tal2Arr[1]);
	if(n!=null){
		if(f != 0){
			n.interract();
		}
		if(n.getWalkable() < 1){
			walkable = false;
		}
	}
	//Apply walkable
	if (walkable) {
		// Move the stick
		stick.prevTale = talArr;
		stick._x = posArr[0]+offx;
		stick._y = posArr[1]+offy;
		//Controls if it's a special tale (like a warp)
		if (myMap[tal2Arr[0]][tal2Arr[1]][0]) {
			// Test for a warp portal
			if (myMap[tal2Arr[0]][tal2Arr[1]][0] == 10) {
				var wantedPos:Array = [myMap[tal2Arr[0]][tal2Arr[1]][1], myMap[tal2Arr[0]][tal2Arr[1]][2]];
				//trace(enemyPos);
				if(!isOccupied(wantedPos)){
					var posArr:Array = giveTalePosition(myMap[tal2Arr[0]][tal2Arr[1]][1], myMap[tal2Arr[0]][tal2Arr[1]][2]);
					stick._x = posArr[0]+offx;
					stick._y = posArr[1]+offy;
					var tal2Arr:Array = [myMap[tal2Arr[0]][tal2Arr[1]][1], myMap[tal2Arr[0]][tal2Arr[1]][2]];
					// add a little animation :p
					attachAnimation("aWarp", stick._x, stick._y);
				}
				else{
					var pPos:Array = stick.prevTale;
					var nPos:Array = giveTalePosition(pPos[0], pPos[1]);
					stick._x = nPos[0]+offx;
					stick._y = nPos[1]+offy;
				}
			// Test for a switch
			} else if (myMap[tal2Arr[0]][tal2Arr[1]][0] == 11) {
				// get the informations about the switch
				arr = myMap[tal2Arr[0]][tal2Arr[1]];
				vi = parseInt(arr[1]);
				vj = parseInt(arr[2]);
				// don't make it a neutral cell back if it's already taken
					if (!arr[4]) {
						// If it's a "normal" switch, just switch
						myMap[vi][vj] = parseInt(arr[3]);
						delete healthPoints[(1000*vi)+vj];
						_root[(1000*vi)+vj].pvt.text = "";
					} else {
						var arr2:Array = new Array();
						arr2[0] = arr[3];
						arr2[1] = arr[4];
						arr2[2] = arr[5];
						myMap[vi][vj] = arr2;
						healthPoints[(1000*vi)+vj] = arr2;
					}
					//kick any player standing on the tile :
					pOcc = isOccupied([[vi],[vj]]);
					if(pOcc != 0){
						kickBack(pOcc);
					}
					
					myDMap[vi][vj] = parseInt(arr[3]);
					_root["d"+((1000*vi)+vj)].gotoAndStop(myDMap[vi][vj]);
					/* Redraw the map.
						The map isn't supposed to get drawn every frame, but a switch is 
						something special that needs a big, stable drawing.
					*/
					drawMap(myMap,0);
			} else {
			}
		}
		else if(myMap[tal2Arr[0]][tal2Arr[1]] == 12){
			npQuit.interract();
		}
		//Test if it's a trapCell
		for(i in listTrapCells){
			if(listTrapCells[i][0] == tal2Arr[0] && tal2Arr[1] == listTrapCells[i][1]){
				//Yes, it's a trap
				if(listTrapCells[i][2] == stick.id && warlockCanDisableTraps == true){
					//Add animation for removing
					listTrapCells.splice(i,1);
					attachAnimation("aWarlRemove", stick._x, stick._y);
				}
				else{
					listTrapCells.splice(i,1);
					blast(stick.id);
				}
			}
		}
		
		if (talArr[0] != tal2Arr[0]) {
			// controls the depth (to be in front or behind the objects)
			stick.swapDepths(((tal2Arr[0]+1)*1000)-(stick.id));
		}
	}
};
/*
	Function: getDisplacements
	
		Using the function above, controls the displacement of the stick depending on the control keys
	
	Parameters: 
	
		Stick stick - stick to be controlled.
	
	Returns:
	
		void

*/
function getDisplacements(stick) {
	for(k=0;k<4;k++){
		this["key"+(k+1)] = stick.keys[k];
	}
	//[Key.UP, Key.DOWN, Key.RIGHT, Key.LEFT, Key.END]
	
	//Check for double keys
	if (Key.isDown(key4) && Key.isDown(key2)) { //DL
		// reset the timer for this stick so the player is not able to move for a little while
		stick.t = 0;
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		if (talArr[0]%2 == 0) {
			// Move the stick
			shiftStick(-1, 1, stick);
			
		} else {
			shiftStick(0, 1, stick);
		}
		stick.ori = "bl";
	}
	else if (Key.isDown(key1) && Key.isDown(key3)) { //UR
		// reset the timer for this stick so the player is not able to move for a little while
		stick.t = 0;
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		if (talArr[0]%2 == 0) {
			// Move the stick
			shiftStick(0, -1, stick);
			
		} else {
			shiftStick(1, -1, stick);
		}
		stick.ori = "tr";
	}
	else if (Key.isDown(key1) && Key.isDown(key4)) { //UL
		// reset the timer for this stick so the player is not able to move for a little while
		stick.t = 0;
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		if (talArr[0]%2 == 0) {
			// Move the stick
			shiftStick(-1, -1, stick);
			
		} else {
			shiftStick(0, -1, stick);
		}
		stick.ori = "tl";
	}
	else if (Key.isDown(key3) && Key.isDown(key2)) { //DR
		// reset the timer for this stick so the player is not able to move for a little while
		stick.t = 0;
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		if (talArr[0]%2 == 0) {
			// Move the stick
			shiftStick(0, 1, stick);
			
		} else {
			shiftStick(1, 1, stick);
		}
		stick.ori = "br";
	}
	// If the key LEFT is pressed
	else if (Key.isDown(key4)) {
		// reset the timer for this stick so the player is not able to move for a little while
		stick.t = 0;
		// Move the stick
		shiftStick(-1, 0, stick);
		// Update the stick's orientation :
		stick.ori = "l";
	} else if (Key.isDown(key3)) { // If key RIGHT is pressed
		stick.t = 0;
		shiftStick(1, 0, stick);
		// If the key1 is pressed
		stick.ori = "r";
	} else if (Key.isDown(key1)) { // If key UP is pressed
		stick.t = 0;
		// reset the timer for this stick so the player is not able to move for a little while
		// Because of the hexa-grid to movings up and down are complicated
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		// find if the stick is on an odd or even number of line
		if (talArr[0]%2 == 0) {
			// Move the stick
			shiftStick(-1, -1, stick);
			
		} else {
			shiftStick(0, -1, stick);
		}
		stick.ori = "tl";
	} else if (Key.isDown(key2)) {// If key DOWN is pressed
		stick.t = 0;
		var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
		if (talArr[0]%2 == 0) {
			shiftStick(0, 1, stick);
		} else {
			shiftStick(1, 1, stick);
		}
		stick.ori = "br";
	}
};

/*
	Function: getStickPosition
	
		Using the functions givePositionTale, returns the coordinates of an certain player
	
	Parameters: 
	
		Number IDplayer - the ID of the player you want to know the coordinates
	
	Returns: 
	
		Array talArr - an array with the coordinates of the stick asked	
	
	See Also:
	
	<giveTalePosition>
*/
function getStickPosition(IDplayer) {
	var stick:MovieClip = _root["stick"+IDplayer];
	var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
	return talArr;
};

/*
	Function: isOccupied
	
		Computes wether or not the tile given in parameter is occupied. If yes, returns the 
		ID of the player occupying.
	
	Parameters: 
	
		Array talArr - The tile one wants to test
		
	Returns: 
	
		Number res - the number of the player standing on the tile
		
	See Also:
	
	<getStickPosition>
*/
function isOccupied(talArr){
	var res = 0;
	for(n in players){
		var i = parseInt(n);
		//trace(n+"-"+players[n])
		if(players[i] == 1){
			tal2Arr = getStickPosition((i+2));
			if(tal2Arr[0] == talArr[0] && tal2Arr[1] == talArr[1]){
				res = (i+2);
			}
		}
	}
	return res;
}
/*
	Function: kickBack
		Sends a player back to the previous tile where he was
		
	Parameters:
		Number IDplayer - the ID of the player to kick back
*/
function kickBack(IDplayer){
	var pPos:Array = _root["stick"+IDplayer].prevTale;
	var nPos:Array = giveTalePosition(pPos[0], pPos[1]);
	_root["stick"+IDplayer]._x = nPos[0]+offx;
	_root["stick"+IDplayer]._y = nPos[1]+offy;
	_root["stick"+IDplayer].swapDepths(((pPos[0]+1)*1000)-(_root["stick"+IDplayer].id));
}

function initStartPoints(){
	// Find initial positions
	for(i=0; i<leXML.firstChild.childNodes[1].childNodes.length; i++){
		istc = parseInt(leXML.firstChild.childNodes[1].childNodes[i].childNodes[0].firstChild.nodeValue);
		jstc = parseInt(leXML.firstChild.childNodes[1].childNodes[i].childNodes[1].firstChild.nodeValue);

		startPoints.push([istc,jstc]);
	}
}
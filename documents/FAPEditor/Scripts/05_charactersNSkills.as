/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						GetSkills								*
*																*
* - Authors :	Léonard, Félix									*
* - Description : 	Manages all the uses of the special skills	*
*																*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
	Title: Special Skills
		Each character (Warrior, Magician, Archer, ...) has his own special skill. Each skill
		has its own cast Time and effects.
		
		List of characters and Skills
		
		- Admin (lol & test character)
		- Knight.
		- Old Magician (/!\ Not used anymore)
		- Miner
		- Warlock
		- Archer
		- Vampire
		- No Power Guy (AI)
		- Magician (v8)
		- Booster
		
*/
/*
   Function: getSkill

   Excecutes the skill of the character (called when the player types his skill-key)

   Parameters:

      movieClip stick - The player's stick

   Returns:

      void
*/
function getSkill(stick:MovieClip) {
	// get the code of the skill key for this player
	key = stick.keys[4];
	ids = stick.pc;
	if (Key.isDown(key) || stick.ai) {
		switch (ids) {
		// Admin : lol character
		case 0 :
		/*
			 // blow up skill
			if(stick.id == 2){
				target = 3;
			}
			else{
				target = 2;
			}
			
			trgt = _root["stick"+target];
			trgt._visible = false;
			if(trgt.id){
				attachAnimation("anim_Expl",trgt._x,trgt._y);
				trgt.removeMovieClip();
			}
		*/
			//trace(isOccupied([4,12]));
		break;
		// Knight (Warrior)
		case 1 :
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			var neighborHoodList:Array = surroundingCellsCoords(myMap, talArr[0], talArr[1]);
			for (i in neighborHoodList) {
				vi = neighborHoodList[i][0];
				vj = neighborHoodList[i][1];
				if (parseInt(myMap[vi][vj])>=1 && parseInt(myMap[vi][vj])<=9 && parseInt(myMap[vi][vj])!=stick.idEq) {
					// if he kills
					/*
					myMap[vi][vj] = stick.id;
					healthPoints[1000*vi+vj] = new Array(stick.id, initHP);
					*/
					//if he hurts
					dammage =(warriorDammage*Math.pow(stick.ts,2));
					healthPoints[(1000*vi)+vj][1] -= dammage;
					if((healthPoints[1000*vi+vj][1] && healthPoints[1000*vi+vj][1] <=0) || (parseInt(myMap[vi][vj])==1 && dammage>=90)){
						myMap[vi][vj] = stick.idEq;
						healthPoints[1000*vi+vj] = new Array(stick.idEq, initHP);
						_root["t"+((vi*1000)+vj)].gotoAndStop(stick.idEq);
					}
				}
			}
			stick.ts = 0;
			stick.t = -10;
			// Add animation
			attachAnimation("sKnight", stick._x, stick._y);
			break;
		//OLDMagician
		// OLD Version of the magician. Not used anymore but still available
		case 2 :
			//Position
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			// Find the orientation of the player :
			var ori = surroundingCellsCoords(myMap, talArr[0], talArr[1])[stick.ori];
			//trace(ori);
			if (myMap[ori[0]][ori[1]]>=1 && myMap[ori[0]][ori[1]]<=9) {
				stick.ts = 0;
				stick.t = -10;
				myMap[ori[0]][ori[1]] = 21;
				_root["t"+((ori[0]*1000)+ori[1])].gotoAndStop(21);
				delete healthPoints[((ori[0]*1000)+ori[1])];
				//healthPoints[((ori[0]*1000)+ori[1])] = [stick.id,100];
				//Add animation :
				attachAnimation("sMagician", stick._x, stick._y);
			}
			break;
		//Miner
		case 3 :
		/* Old Version of the miner
			//Position
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			// Get the list of the array he can teleport to
			var listArr:Array = findPlayableCells(myMap);
			randNum = randRange(0, (listArr.length)-1);
			randCell = listArr[randNum];
			var posArr:Array = giveTalePosition(randCell[0], randCell[1]);
			stick._x = posArr[0]+offx;
			stick._y = posArr[1]+offy;
			//if (randCell[1] != talArr[1]) {
				stick.swapDepths(((randCell[0]+1)*1000)-(stick.id));
			//}
			stick.ts = 0;
			stick.t = -10;
			//Add animation
			attachAnimation("aWarp", stick._x, stick._y);
		*/
		//Miner : Reloaded
		//trace(this["miner"+stick.id+"selec"]+"-"+stick.id);
		if(!this["miner"+stick.id+"selec"]){
			// Get the list of the array he can teleport to
			var listArr:Array = findPlayableCells(myMap);
			var listCell:Array = new Array();
			for(i=0;i<3;i++){
				listCell[i] = new Array();
				randNum = randRange(0, (listArr.length)-1);
				randCell = listArr[randNum];
				listCell[i] = randCell;
				//Show warpable cells
				_root["t"+(1000*randCell[0]+randCell[1])].gotoAndStop(19);
			}
			var minerSelec:MovieClip = this.createEmptyMovieClip("miner"+stick.id+"selec",this.getNextHighestDepth());
			//trace(minerSelec._name);
			minerSelec.stick = stick;
			minerSelec.listCell = listCell;
			minerSelec.selec = 0;
			minerSelec.t=0;
			minerSelec.tmax = 10;
			
			delete listArr;
			delete listCell;
			delete stick;
			
			minerSelec.onEnterFrame = function(){
				this.stick.ts = 0;
				this.stick.t = 0;
				if(Key.isDown(this.stick.keys[4])){
					this.stick.ts = 0;
					this.stick.t = 0;
					this.t++;
					if(Key.isDown(this.stick.keys[2]) && this.t>=this.tmax){ //right
						this.selec++;
						this.t=0;
					}
					else if(Key.isDown(this.stick.keys[3]) && this.t>=this.tmax){ //left
						this.selec--;
						this.t=0;
					}
					
					if(this.selec<0){
						this.selec = 2;
					}
					this.selec = this.selec%3;
					
					for(i=0;i<listCell.length;i++){
						if(i==this.selec){
							_root["t"+(1000*listCell[i][0]+listCell[i][1])].mselect.gotoAndStop(2);
						}
						else{
							_root["t"+(1000*listCell[i][0]+listCell[i][1])].mselect.gotoAndStop(1);
						}
					}
				}
				else{
					for(i=0;i<listCell.length;i++){
						_root["t"+(1000*listCell[i][0]+listCell[i][1])].gotoAndStop(myMap[listCell[i][0]][listCell[i][1]]);
					}
					this.stick.ts = 0;
					this.stick.t = 0;
					
					//Warp
					var posArr:Array = giveTalePosition(listCell[this.selec][0], listCell[this.selec][1]);
					if(!isOccupied(posArr)){
						this.stick._x = posArr[0]+offx;
						this.stick._y = posArr[1]+offy;
						if(this.stick.ai){
							_root.fsmGo = 1;
						}
						//if (randCell[1] != talArr[1]) {
						this.stick.swapDepths(((listCell[this.selec][0]+1)*1000)-(this.stick.id));
						//}
						//Add animation
						attachAnimation("aWarp", this.stick._x, this.stick._y);
					}
					delete this.stick;
					this.removeMovieClip();
					delete this.onEnterFrame;
				}
			}
			delete minerSelec;
		}
		else{
				this.stick.ts = 0;
				this.stick.t = 0;
		}
			break;
		//Warlock
		case 4 :
			var talArr:Array = stick.pp;
			listTrapCells.push([talArr[0],talArr[1],stick.id]);
			//Add animation
			attachAnimation("aWarlPlace", stick._x, stick._y);
			stick.ts = 0;
			stick.t = -10;
			break;
		//Archer
		case 5:
			// YAY ! ARCHERYYYYY
			//Position
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			//Attach the arrow :
			this["a"+stick.id].removeMovieClip();
			var arro:MovieClip = this.attachMovie("arrow","a"+stick.id,myMap.length*1000+(12*stick.id));
			arro._x = stick._x;
			arro._y = stick._y;
			arro.id = stick.idEq;
			ori = stick.ori;
			approxAngle = -0.85832;
			if(ori == "l"){
				angle = -Math.PI/2;
			}
			else if(ori == "r"){
				angle = Math.PI/2;
				arro.gotoAndStop(2);
			}
			else if(ori == "tl"){
				angle = approxAngle; // In rad, approximation with Maple
			}
			else if(ori=="br"){
				angle = (Math.PI)+approxAngle; // In rad, approximation with Maple
				arro.gotoAndStop(2);
			}
			else if(ori == "tr"){
				angle = -approxAngle; // In rad, approximation with Maple
				arro.gotoAndStop(2);
			}
			else if(ori == "bl"){
				angle = (Math.PI)-approxAngle; // In rad, approximation with Maple
				arro.gotoAndStop(2);
			}
			else{
				
			}
			
			//Find the tiles that are on the arrow's path :
			var pathTiles:Array = tilesOnPath(myMap,talArr[0],talArr[1],ori);
			/*for(k in pathTiles){
				myMap[pathTiles[k][0]][pathTiles[k][1]] = 21;
				_root["t"+(pathTiles[k][0]*1000+pathTiles[k][1])].gotoAndStop(21);
			}*/
			
			
			arro._rotation = angle*180/Math.PI+90;
			//arro.tmax = 2.8;
			arro.t = arro.tmax+1;
			arro.k = 0;
			arro.angle =angle;
			arro.onEnterFrame = function(){
				this._y -= arrowSpeed*Math.cos(this.angle);
				this._x += arrowSpeed*Math.sin(this.angle);
				var taleArr = givePositionTale(this._x-offx+6,this._y-offy);
					if(taleArr[0]!=this.vi || taleArr[1]!=this.vj){
						this.t=0;
						this.swapDepths(((taleArr[0]+1)*1000)-(12)); // 12 is to avoid messing with the other depths
						this.vi=taleArr[0];
						this.vj=taleArr[1];
						if(!this.vi || !this.vj || this.vi<2 || this.vj <2 || this.vi>myMap.length+1 || this.vj>myMap[0].length+1 || !pathTiles[this.k]){
							this.removeMovieClip();
						}
						var type = parseInt(myMap[pathTiles[this.k][0]][pathTiles[this.k][1]]);
						if(type >1 && type<=9 || (type == 1 && !myMap[pathTiles[this.k][0]][pathTiles[this.k][1]][1]) ){
							healthPoints[(pathTiles[this.k][0]*1000+pathTiles[this.k][1])][1] -= archerDammage;
							if(healthPoints[(pathTiles[this.k][0]*1000+pathTiles[this.k][1])][1] <=0){
							//
								myMap[pathTiles[this.k][0]][pathTiles[this.k][1]] = this.id;
								_root["t"+(pathTiles[this.k][0]*1000+pathTiles[this.k][1])].gotoAndStop(this.id);
								healthPoints[(pathTiles[this.k][0]*1000+pathTiles[this.k][1])] = [this.id,initHP];
							//
							}
							//Block arrow against ennemy
							if(en=isOccupied(pathTiles[this.k])){
								if(en!=stick.id){
										var stEnn:MovieClip = _root["stick"+en];
										stEnn.tmax = ((howLongBlockingMagician)/1.5)*fpsa;
										stEnn.t = 0;
										attachAnimation("aArrowEnnemy", stEnn._x, stEnn._y);
										this.removeMovieClip();
								}
							}
						}
						this.k++;
					}
			}
			delete arro;
			stick.ts = 0;
			stick.t = -30;
			break;
		//Vampire
		case 6 :
			//Collect amount of HP from enemy
			// Get the list of the array he can vampirise
			var listArr:Array = findPlayableCells(myMap, [1, stick.idEq]);
			// Get the list of the array he owns
			var exArr:Array = new Array();
			for (j=1; j<=9; j++) {
				if (j != stick.idEq) {
					exArr.push(j);
				}
			}
			var ownArr:Array = findPlayableCells(myMap, exArr);
			
			amount = 0;
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			//Find wich tiles, by rings, are around
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
						// Count how much HPs to take for this cell (depending on wich ring it is on)
						drainedLife = Math.pow((affectedCells.length-i), 2) + 3;
						//map[afci][afcj] -= drainedLife;
						healthPoints[(1000*afci)+afcj][1] -= drainedLife;
						//Store in amount
						amount += drainedLife;
			
					}
				}
			}
			
			// Redsitribute HPs from "amount"
			amProCell = Math.floor(amount/ownArr.length*rateVampirismGains);
			for (k=0; k<ownArr.length; k++) {
				healthPoints[(ownArr[k][0]*1000+ownArr[k][1])][1] += amProCell;
			}
			stick.ts = 0;
			stick.t = -20;
			break;
		// AI
		case 7:
			break;
		// Magician
		// new Version of the magician. See n°2 for old version
		case 8:
			stick.ts = 0;
			stick.t = -20;
			var talArr:Array = givePositionTale(stick._x-offx, stick._y-offy);
			//Find wich tiles, by rings, are around
			var MGaffectedCells = ringsSurrounding(myMap, talArr[0], talArr[1], howManyRingsIstheMagicianActive);
			//per ring...
			for(i = 1 ; i < MGaffectedCells.length ; i++){
				//per cell...
				for(j = 0 ; j < MGaffectedCells[i].length ; j++){
					afCell = MGaffectedCells[i][j];
					// Find if occuped
					if(pl=isOccupied(afCell)){
						//If yes, block the player
						var stEnn:MovieClip = _root["stick"+pl];
						stEnn.tmax = howLongBlockingMagician*fpsa;
						stEnn.t = 0;
						attachAnimation("aWater", stEnn._x, stEnn._y);
						/*
							Note : the blocking is simply setting the displacement time of
							the player higher (the amount of time he's gotta be blocked, that 
							mofo). the tmax will automatically be reset at his next displacement
						*/
					}
				}
			}
			break;
		// Booster : can boost his displacement time for a while
		case 9:
			stick.ts = 0;
			stick.t = 0;
			// Boost speed
			stick.tmax = boosterSpeed;
			//initialize countdown
			var boostCount:MovieClip = this.createEmptyMovieClip("booCn"+stick.id,this.getNextHighestDepth());
			boostCount.stick = stick;
			// Get the time the speed is boosted
			boostCount.tt = boosterTime;
			boostCount.t = 0;
			boostCount.onEnterFrame = function(){
				//Count
				this.t++;
				if(this.t >= this.tt*fpsa){
					//When it's time, set speed back to normal
					this.stick.tmax = _root.charParam["dispSpeed"][9];
					this.removeMovieClip();
				}
			}
			delete boostCount;
		break;
		default :
			break;
		}
	}
};
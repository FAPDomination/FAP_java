/*
   Function: initPlayer

   Defines, attaches, places, initializes a player. Takes care of :
   - Creating the "stick" object for this player
   - Initialize its datas (from XML and others)
   - Initialize managing of displacements, skills, TileActivation

   Parameters:

      Number pID - Id of the player you want to initialize
	  
   Returns:

      void
*/
function initPlayer(pID){
	// Initialize movieClip
	var stick:MovieClip = this.attachMovie("stick", "stick"+pID, this.getNextHighestDepth());
	// Initial Positions
	/*
	istc = parseInt(leXML.firstChild.childNodes[1].childNodes[pID-2].childNodes[0].firstChild.nodeValue);
	jstc = parseInt(leXML.firstChild.childNodes[1].childNodes[pID-2].childNodes[1].firstChild.nodeValue);
	*/
	//a.splice(2,1);
	if(gameType == "vs" && mixStartPoints == true){
		var randIndex = randRange(0,startPoints.length-1);
		var randPos = startPoints[randIndex];
		 startPoints.splice(randIndex,1);
		istc = randPos[0];
		jstc = randPos[1];
	}
	else{
		istc = parseInt(leXML.firstChild.childNodes[1].childNodes[pID-2].childNodes[0].firstChild.nodeValue);
		jstc = parseInt(leXML.firstChild.childNodes[1].childNodes[pID-2].childNodes[1].firstChild.nodeValue);
	}
	stick.inii = istc;
	stick.inij = jstc;
	// Place it
	var posArr:Array = giveTalePosition(istc, jstc);
	stick._x = posArr[0]+offx;
	stick._y = posArr[1]+offy;
	//Datas initialization
	stick.id = pID;
	stick.stColor.gotoAndStop(pID-1);
	if(teamPlay!=1){
		stick.idEq = pID;
	}
	else{
		stick.idEq = (pID%2)+2;
	}
	stick.ori = "br";
	//Init skills
	stick.pc = _root["p"+pID+"c"];
	stick.t = 0;
	stick.ts =0;
	stick.tmax = charParam["dispSpeed"][stick.pc];
	stick.skillTime = castSkillTime[stick.pc];
	stick.decLifeForced = charParam["decLifeForced"][stick.pc];
	stick.recovLifeAuto = charParam["recovLifeAuto"][stick.pc];
	
	
	fsmStick.ai = false;
	
	stick.keys = playerControls[pID-2];
	
	
	//------------ To control the game
	stick.onEnterFrame = function()
	{
		if(!gamePaused){
			this.t++;
			this.ts++;
		}
		// Update the values of the positions of the players
		_parent["p" + this.id + "p"] = getStickPosition(this.id);
		this.pp = getStickPosition(this.id);

		// Allow displacements
		if (this.t >= this.tmax && !gamePaused)
		{
			// Set speed back to normal if the guy was frozen
			if(this.tmax > _root.charParam["dispSpeed"][this.pc]){
				this.tmax = _root.charParam["dispSpeed"][this.pc];
			}
			getDisplacements(this);
			//refreshHealthPoints();
		}
		if(gameType == "vs"){
		//Skills 
		// Get them if your castTime is done or if you're a warrior
		if (this.ts >= this.skillTime*fpsa && this.tmax <=20 && !gamePaused && !this.ai)
		{
			getSkill(this);
		}
		//Control the tales :   
		stickActivateTale(this);
		}
	};
}
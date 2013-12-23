// Display Message
function broadcast(messA,up,j){
	mess = messA[j];
	if(mess!=null){
		this.broadcast.removeMovieClip();
		var broadcast:MovieClip = this.attachMovie("broadcast","broadcastMC",this.getNextHighestDepth());
		if(up){
			broadcast._y = 5;
		}
		else{
			broadcast._y = ha-5-broadcast._height;
		}
		broadcast.i = 0;
		broadcast.mess = mess;
		broadcast.strArr = new Array();
		broadcast.strArr = mess.split("");
		broadcast.textf.text ="";
		broadcast.tmax = 10;
		broadcast.twait = broadcast.tmax;
		broadcast.onEnterFrame = function(){
			if(this.i<this.strArr.length){
				this.textf.text += this.strArr[this.i];
				this.i++;
				if(Key.isDown(Key.SPACE) || Key.isDown(stick2.keys[4])){
					this.twait=0;
					this.i = this.strArr.length;
					this.textf.text =this.mess;
				}
			}
			else if((Key.isDown(Key.SPACE) || Key.isDown(stick2.keys[4])) && this.twait >= this.tmax){
				if(messA.length>1 && j<messA.length){
					_root.broadcast(messA,up,j+1);
				}
				this.removeMovieClip();
			}
			else{
				this.twait++;
			}
		}
		delete broadcast;
	}
}

//broadcastStr("Bienvenue dans l'aventure !",0);

function broadcastStr(mess,up){
	if(mess != null){
		var messA = mess.split("\p");
		broadcast(messA,up,0);
	}
}

function askQuestion(mess,yesStr,noStr,up,npc:NPC){
	boradcastMC.removeMovieClip();
	var broadcast:MovieClip = this.attachMovie("broadcast","broadcastMC",this.getNextHighestDepth());
	broadcast.gotoAndStop(2);
	if(up){
		broadcast._y = 5;
	}
	else{
		broadcast._y = ha-5-broadcast._height;
	}
	broadcast.i = 0;
	broadcast.mess = mess;
	broadcast.strArr = new Array();
	broadcast.strArr = mess.split("");
	broadcast.textf.text ="";
	broadcast.tmax = 10;
	broadcast.twait = broadcast.tmax;
	broadcast.choice = 0;
	broadcast.noText.text = noStr;
	broadcast.yesText.text = yesStr;
	broadcast.npc = npc;
	broadcast.onEnterFrame = function(){
		if(this.i<this.strArr.length){
			this.textf.text += this.strArr[this.i];
			this.i++;
			if(Key.isDown(Key.SPACE) || Key.isDown(stick2.keys[4])){
				this.twait=0;
				this.i = this.strArr.length;
				this.textf.text =this.mess;
			}
		}
		else if((Key.isDown(Key.SPACE) || Key.isDown(stick2.keys[4])) && this.twait >= this.tmax){
			this.twait=0;
			if(messA.length>1 && j<messA.length){
				_root.broadcast(messA,up,j+1);
			}
			npc.choice = this.choice;
			//npc._parent.setChoice(this.choice);
			this.removeMovieClip();
			
		}
		else if((Key.isDown(Key.LEFT) || Key.isDown(Key.RIGHT) || Key.isDown(stick2.keys[3])  || Key.isDown(stick2.keys[2])) && this.twait >= this.tmax){
			this.choice^=1;
			this.twait=0;
			this.select._x = this.choice*180+50;
		}
		else{
			this.twait++;
		}
	}
	delete broadcast;
}

function isNPC(i,j){
	for(k=0;k<listNPC.length;k++){
		var npci:NPC = listNPC[k];
		var ni = npci.getI();
		var nj = npci.getJ();
		if(ni == i && nj == j){
			return npci;
		}
	}
	return null;
}

//----------------- Transitions

function exitAndGoto(advI){
	delete _root.onEnterFrame;
	delete _root.kcount.onEnterFrame;
	
	_root._parent.gotoAndStop("preLoad");
	if(advI != null){
		_root._parent.advIndicate = advI;
	}
	else{
		_root._parent.advIndicate = null;
	}
	_root._parent.conten.removeMovieClip();
}

function exitToMap(f,pt){
	_root._parent.f = f;
	if(pt == "vs"){
		exitAndGoto("ADV_prepVS");
	}
	else{
		exitAndGoto("ADV_PlayAdv");
	}
}

function exitToADV(f){
	exitToMap(f,"adv");
}
function exitToVS(f){
	exitToMap(f,"vs");
}
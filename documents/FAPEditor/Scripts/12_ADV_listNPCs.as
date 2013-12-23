var worldNPCs:Array = new Array();
worldNPCs[20] = [18,11];
worldNPCs[21] = [16,10];
worldNPCs[22] = [17,9];
worldNPCs[23] = [17,10];
worldNPCs[24] = [16,10];
worldNPCs[25] = [18,9];

function initNPCs(){
	npQuitac = [["ask","Etes-vous sûr de vouloir quitter ? (retour au monde)","Oui","Non",null],["test","choice",0, null],["function",exitAndGoto,"ADV_preLoadWorld"]];
	_root.npQuit = new NPC(this,0,-1,"npcTile",npQuitac,1);
	//broadcastStr("Bienvenue dans l'aventure !",0);
	switch(f){
		case 0: //World Map

		for(i=20;i<(20+_root._parent.nVSMap);i++){
			listNPC.push(initWorldTile(i));
		}
		break;
		case 25:
			// Store Actions
			npiac = [["test","goldSwitch",true, "Grmbl..."],"Oh ! Tu as complété la quête !\pVoilà ta princesse !"];
			npjac = ["ProutProut","Comment ça va ?",["function",trace,"salut"],"et voilà",["ask","Aimes-tu les peperonis","peut-être","tropa","lolilol"],"tu pues"];
			// Initiate Variables
			var npi:NPC = new NPC(this,10,10,"stick",npiac,0);
			var npj:NPC = new NPC(this,20,8,"stick",npjac,0);
			// Special things
			npi.mc.stick.stColor.gotoAndStop(2);
			npj.mc.stick.stColor.gotoAndStop(4);
			// List pushes
			listNPC.push(npi);
			listNPC.push(npj);
		break;
		default:
		break;
	}

}

function initWorldTile(n){
	if(_root._parent.ADVMapValues[n][0] == 0){
		npac = [["ask","Chargement map "+n+" ?","Oui","Non",null],["test","choice",0, null],["function",_root.exitToADV,n]];
	}
	else{
		npac = [["ask","Chargement map "+n+" ?","Oui","Non",null],["test","choice",0, null],["function",_root.exitToVS,n]];
	}
	var npc = new NPC(this,worldNPCs[n][0],worldNPCs[n][1],"npcTile",npac,_root._parent.gameData[n]);
	npc.mc.npcTile.gotoAndStop(_root._parent.gameData[n]+1);
	return npc;
}
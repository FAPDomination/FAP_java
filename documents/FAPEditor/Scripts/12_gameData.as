var mapParents:Array = new Array();
mapParents[20] = [0];
mapParents[21] = [0];
mapParents[22] = [1,21,20];
mapParents[23] = [1,21,22];
mapParents[24] = [2,23,20];
mapParents[25] = [0];

function computeMapValue(n){
	var numParents = mapParents[n][0];
	var sum = 0;
	for(j=1;j<mapParents[n].length;j++){
		if(_root.gameData[mapParents[n][j]] == 2){
			sum ++;
		}
	}
	if(sum>=numParents && _root.gameData[n] == 0){
		val = 1;
	}
	else{
		val = _root.gameData[n];
	}
	return val;
}
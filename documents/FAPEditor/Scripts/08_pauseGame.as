//Pause the Game
Key.removeListener(spyPause);
delete spyPause;
/*
	Variable: spyPause
	A listener that will wpy on the KeyBoard for pressing Pause keys
*/
var spyPause:Object = new Object();

//When the Pause keys are pressed, toggle the pause mode of the game
spyPause.onKeyDown = function(){
	if((Key.isDown(Key.ESCAPE) || Key.isDown(80)) && !endGameb){
		pauseGame();
	}
}
Key.addListener(spyPause);

/*
	Function: pauseGame
	Toggles the pause mode
*/
function pauseGame(){
	if(gamePaused && _root.pauseScreen){
		//Unset the pause
		_root.pauseScreen.gotoAndStop(2);
	}
	else{
		//Set it (back)
		gamePaused = true;
		_root.attachMovie("pauseScreen","pauseScreen",_root.getNextHighestDepth());
		_root.pauseScreen.gotoAndStop(1);
	}
}
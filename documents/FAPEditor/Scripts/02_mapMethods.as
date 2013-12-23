/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * 
*																*
*						Methods for the map						*
*																*
* - Authors :	Léonard, Félix									*
* - Description : Useful functions to be able to read and write	*
*				the map											*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
/*
	Function: initMap
	
		Initializes the map variable according to the content of the XML
		
	Parameters:
	
		none
		
	Returns:
	
		void
	
	See Also:
	
		<leXML>

*/
function initMap() {
	//reading the XML :
	for (i=0; i<leXML.firstChild.firstChild.childNodes.length; i++) {
		// Create an extra array for each line. It's provisional
		var arrProvisoire:Array = new Array();
		var arrDProvisoire:Array = new Array();
		for (j=0; j<leXML.firstChild.firstChild.childNodes[i].childNodes.length; j++) {
			// The cell of the XML is copied in the array
			arrDProvisoire[j] = parseInt(leXML.firstChild.firstChild.childNodes[i].childNodes[j].firstChild.nodeValue);
			if(arrDProvisoire[j] !=0){
				_root.totalTile++;
			}
			//Test for warp of switch :
			if (arrDProvisoire[j] != leXML.firstChild.firstChild.childNodes[i].childNodes[j].firstChild.nodeValue) {
				// if so, copy the array with the information directly in the map
				str = leXML.firstChild.firstChild.childNodes[i].childNodes[j].firstChild.nodeValue;
				arr = str.split(",");
				for (k=0; k<arr.length; k++) {
					arr[k] = parseInt(arr[k]);
				}
				//if it's a special tile, the designe shall be empty
				if(arr[0] == 10 || arr[0] == 11){
					arrDProvisoire[j] = 19;
				}
				else{
					// else (countdown tile)
					arrDProvisoire[j] = arr[0];
					arr[0] = 1;
				}
				/*ri = arr[1];
				rj = arr[2];
				t = arr[0];*/
				arrProvisoire[j] = arr;
				// if it's a neutral tale
				if (arr[0] == 1) {
					// And it has an array, it's because it has HP, wich must be initialized
					healthPoints[(i*1000)+j] = [1, arr[1]];
				}
				
			}
			//Simplifications for myMap (not myDMap)
			else{
				if(arrDProvisoire[j] == 0){
					arrProvisoire[j] = 0;
				}
				else if(arrDProvisoire[j] == 12){
					arrProvisoire[j] = arrDProvisoire[j];
				}
				else if(arrDProvisoire[j]<idBlockingHigh){
					arrProvisoire[j] = 1;
				}
				else if(arrDProvisoire[j]>=idBlockingLow){
					arrProvisoire[j] = 22;
				}
				else{
					arrProvisoire[j] = 20;
				}
			}
			// end if  
		}
		// end for j
		// Copy the provisional array in the map variable
		myDMap[i] = arrDProvisoire;
		myMap[i] = arrProvisoire;
	}
	// end for i
	return myDMap;
};

/*
	Function: drawMap
	
	Draw the map according to the external variable myMap
	
	Parameters: 
	
		Array myCMap - Array of the map one wants to draw (myMap, or myDMap)
		Number ty - type of the map, 0 for myMap, 1 for myDMap
		
	Returns:
	
		void
		
*/
function drawMap(myCMap:Array,ty:Number) {
	// Decide with movieClip to call depending on wich map is being drawn
	if(ty==0){
		tclip = "tale";
		// doff is an offset for the layer number. It is on purpose either 0 or >myMap.length
		doff = 50;
		nt = "t";
	}
	else{
		tclip = "Dtile";
		doff = 0;
		nt = "d";
	}
	// read the lines
	for (i=0; i<myCMap.length; i++) {
		// read the columns
		for (j=0; j<myCMap[i].length; j++) {
			// test of not empty
			//Warning : this makes the function not redraw the magic tiles (invoked by the magician)
			if (myCMap[i][j]>=1 && myCMap[i][j]!=21 ) {
				// attach the movieClip, set design, position and intern variable
				var clp:MovieClip = this.attachMovie(tclip, nt+((1000*i)+j), 1000*i+j+doff);
				// If it's a tale with an array and not a single number
				if (myCMap[i][j][0]) {
					clp.gotoAndStop(myCMap[i][j][0]);
				} else {
					clp.gotoAndStop(myCMap[i][j]);
				}
				clp._xscale *= 1/fac;
				clp._yscale *= 1/fac;
				// hard to calculate : the position in the hexa grid
				clp._x = j*tw+(tw/2)*(i%2);
				clp._y = i*(th)*(1-1/4)+offyMap;
				clp.i = i;
				clp.j = j;
			}
			// endif    
		}
		// endfor j
	}
	// endfor i
};
// endfunction
/*
	Function: giveTalePosition

		Takes the coordinates (i,j) of an object and returns the position in pixel where it is
	
	Parameters: 
	
		Number i - x axis coordinate (in Tiles)
		Number j - y axis coordinate (in Tiles)
	
	Returns: 
	
		Array arr - An array containing the position of the object. index 0 is x, 1 is y

	See Also:
	
		<givePositionTale>
		
*/
function giveTalePosition(i, j) {
	var arr:Array = new Array();
	// calculate the corresponding position
	arr[0] = j*tw+(tw/2)*(i%2);
	arr[1] = i*(th)*(1-1/4)+offyMap;
	return arr;
};
/*
	Function: givePositionTale

		Takes the position in pixels of an object and returns the coordinates of the tale on wich it is
	
	Note:
	
		It's exactly the inverse of the function above.
	
	Parameters: 
	
		Number x - x axis position (in pixels)
		Number y - y axis position (in pixels)
		
	Returns: 
	
		Array arr - An array containing the coordinates of the tile. index 0 is i, 1 is j

	See Also:
	
		<giveTalePosition>
	
*/
function givePositionTale(x, y) {
	var arr:Array = new Array();
	// Undo the calculus of the position
	arr[0] = Math.round(((y-offyMap)/th)*(4/3));
	if (arr[0]%2 == 0) {
		arr[1] = Math.floor(x/tw);
	} else {
		arr[1] = Math.floor((x/tw)-1/2);
	}
	return arr;
};
/*
	Function: countNeighbours
	
		function that returns the number of cells of a specified type surrounding the chosen cell.
	
	Parameters:
	
		Array map - the map you're playing on.
		Number i,j - coordinates of the chosen cell.
		Number type - the type of cell you want to count.
		
	Returns:
	
		Number count - number of neighbours (result of the analysis)
		
*/
countNeighbours = function (map:Array, i, j, type) {
	count = 0;
	// cells from the superior line
	if (i != 0) {
		// not the first line
		if (i%2 == 0) {
			// line with even index
			if (j != 0) {
				// not the first column
				if (map[i-1][j-1] == type) {
					count++;
				}
			}
			if (map[i-1][j] == type) {
				count++;
			}
		} else {
			// line with odd index
			if (j != map[i].length-1) {
				// not the last column
				if (map[i-1][j+1] == type) {
					count++;
				}
			}
			if (map[i-1][j] == type) {
				count++;
			}
		}
	}
	// cells from the same line     
	if (j != 0) {
		if (map[i][j-1] == type) {
			count++;
		}
	}
	if (j != map[i].length-1) {
		if (map[i][j+1] == type) {
			count++;
		}
	}
	// cells from the inferior line     
	if (i != map.length-1) {
		if (i%2 == 0) {
			if (j != 0) {
				if (map[i+1][j-1] == type) {
					count++;
				}
			}
			if (map[i+1][j] == type) {
				count++;
			}
		} else {
			if (j != map[i].length-1) {
				if (map[i+1][j+1] == type) {
					count++;
				}
			}
			if (map[i+1][j] == type) {
				count++;
			}
		}
	}
	return count;
	// You just lost the game.
};
/*
	Function: surroundingCellsCoords
	
		Method that returns the coordinates of the cells surrounding a chosen cell (given as parameter).
	
	Parameters:
	
		map - the map you want to work on.
		i, j - coordinates of the cell from which you want to know the neighbours cells.
		
	Returns:
	
		surroundingCells - is an array with 6 elements with following indexes
		'tl' - for "top left". Contains an array with the [i, j] coordinates of the cell that is top left from the chosen cell.
		'tr' - same for "top right".
		'l' - same for "left".
		'r' - same for "right".
		'bl' - same for "bottom left".
		'br' - same for "bottom right".
		
	Particular value: 
	
		if one of these neighbour cells doesn't exist (border of the map), the coordinates array will be replaced with the string "noCell".
	
	Accessing the values: 
	
		simply use surroundingCells['tl']; for instance.
*/
function surroundingCellsCoords(map:Array, i, j) {
	var surroundingCells = new Array();
	// top cells
	if (i != 0) {
		// not the first line
		if (i%2 == 0) {
			// line with even index
			if (j != 0) {
				// not the first row
				surroundingCells['tl'] = [i-1, j-1];
			} else {
				surroundingCells['tl'] = "noCell";
			}
			surroundingCells['tr'] = [i-1, j];
		} else {
			// line with odd index
			if (j != map[i].length-1) {
				// not the last row
				surroundingCells['tr'] = [i-1, j+1];
			} else {
				surroundingCells['tr'] = "noCell";
			}
			surroundingCells['tl'] = [i-1, j];
		}
	} else {
		surroundingCells['tl'] = "noCell";
		surroundingCells['tr'] = "noCell";
	}
	// cells from the same line
	if (j != 0) {
		surroundingCells['l'] = [i, j-1];
	} else {
		surroundingCells['l'] = "noCell";
	}
	if (j != map[i].length-1) {
		surroundingCells['r'] = [i, j+1];
	} else {
		surroundingCells['r'] = "noCell";
	}
	// bottom cells (see top cells)
	if (i != map.length-1) {
		if (i%2 == 0) {
			if (j != 0) {
				surroundingCells['bl'] = [i+1, j-1];
			} else {
				surroundingCells['bl'] = "noCell";
			}
			surroundingCells['br'] = [i+1, j];
		} else {
			// line with odd index
			if (j != map[i].length-1) {
				// not the last row
				surroundingCells['br'] = [i+1, j+1];
			} else {
				surroundingCells['br'] = "noCell";
			}
			surroundingCells['bl'] = [i+1, j];
		}
	} else {
		surroundingCells['bl'] = "noCell";
		surroundingCells['br'] = "noCell";
	}
	return surroundingCells;
};

/*
   Function: tilesOnPath
   
       function that returns coordinates of the cells in a given direction from the chosen cell.
   
   Parameters:
   
       Array map - the map you're playing on.
       Number i,j - coordinates of the chosen cell.
       String pathDirection - the direction you want to compute the path.
       
   Returns:
   
       Array pathTiles - tiles on the path. Each index contains array[i, j] of coordinates.
       
*/
function tilesOnPath(map:Array, i, j, pathDirection){
    var pathTiles:Array = new Array();
    var currentTile;
    var nextTile = [i, j];
    do{
        currentTile = nextTile;
        pathTiles.push(currentTile);
        nextTile = surroundingCellsCoords(map, currentTile[0], currentTile[1])[pathDirection];
    }
	// Iterate until blocking tile or end of the map
	while (map[nextTile[0]][nextTile[1]] != 20 && map[nextTile[0]][nextTile[1]] != 21 && (map[nextTile[0]][nextTile[1]] || map[nextTile[0]][nextTile[1]]==0));
    return pathTiles;
}


/*
  Function: ringsSurrounding
  
      Makes lists of coordinates for the tiles that are on the rings surrounding a cell.
      The array first index will be the number of the ring.
      This ring is an array of coordinates [i,j] of the tiles being on this ring.
      
  Parameters:
  
      Array map - the map you're playing on.
      Number i,j - coordinates of the chosen cell.
      Number numberOfRings - The number of ring one wants to know the neighboors
      
      >ringsOfCells[ring][indexOfCellInTheRing][1 or 0]
      
   Warning:
   
       /!\ The number of tiles (and then calculus time) is exponential when the number of rings
       increases /!\
      
  Returns:
  
      Array ringsOfCells - The array containing the rings containing the coordinates
  
  See Also:
  
          <surroundingCellsCoords>
       
       <arrayContainsCoords>
*/
function ringsSurrounding(map:Array, i, j, numberOfRings){
    var ringsOfCells = new Array();
    ringsOfCells[0] = [[i, j]];        // Ring 0 : the chosen cell
    var ring = 1;
    continueRingLoop = true;        // For stopping the loop after a certain number of rings
    while(continueRingLoop){
        ringsOfCells[ring] = new Array();
        indexRing = 0;            // Index of the array for the current ring
         
        // Loop : gets the cells of the inferior ring and checks their neighbour cells
        for(i in ringsOfCells[ring-1]){
            cell = ringsOfCells[ring-1][i];        // Inferior ring cell
            aroundCurrentCell = surroundingCellsCoords(map, cell[0], cell[1]);
             
            // Loop : checking if the cells must be added to the current ring array
            for(j in aroundCurrentCell){
                tempCell = aroundCurrentCell[j];
                var rejectCell = false;
                // Loop : is this cell already in the returned array ?
                for(k = ring-2 ; k <= ring ; k++){
                    if(k < 0){ k = 0; } // Prevents error if ring = 1
                    // use of other function arrayContainsCoords(array, coords)
                    if((tempCell == "noCell") || arrayContainsCoords(ringsOfCells[k], tempCell) != -1){
                        rejectCell = true;
                    }
                }
                if(!rejectCell){
                    // Update of returned array and current ring index
                    ringsOfCells[ring][indexRing] = tempCell;
                    indexRing++;
                }
            }
        }
        ring++;
         
        // Stop condition
        if(ring > numberOfRings){
            continueRingLoop = false;
        }
    }

    return ringsOfCells;

}

/*
   Function: arrayContainsCoords
   
       function that returns a boolean telling wether or not an array of coordinates (list of
	   [i,j]) contains a certain 2-uplet [i,j]
   
   Parameters:
   
       Array varray - the list of [i,j] one wants to test
       Array coords - a couple of [i,j] one wants to know if it is contained in varray
       
   Returns:
   
       Boolean - [i,j] is in the list or not
       
*/
function arrayContainsCoords(varray, coords) {
    var isContained = -1;
    for (i in varray) {
        if (varray[i][0] == coords[0] && varray[i][1] == coords[1]) {
            isContained = i;
        }
    }
    return isContained;
}

/*
	Function: minimap
	Generates a mini-map (miniature) for a given map.
	
	Parameters:
	
	myCMap - The map that is to be miniaturized
	vname - The name of the clip that will contain the map (to be created)
	fac - a factor of size for the tiles
	
	Note:
	fac = 1 <=> 2px per tile
	
	Return:
	
	The movieClip that contains the drawing
	
	See Also:
	
	<generateMinimap>
*/
function minimap(myCMap,vname,fac){
	tw = fac*2;
	th = 2*fac;
	fac = 1/fac;
	var mov:MovieClip = this.createEmptyMovieClip(vname,this.getNextHighestDepth());
	// Decide with movieClip to call depending on wich map is being drawn
		tclip = "Dmin";
		doff = 0;
		nt = "d";
	// read the lines
	for (i=0; i<myCMap.length; i++) {
		// read the columns
		for (j=0; j<myCMap[i].length; j++) {
			// test of not empty
			//Warning : this makes the function not redraw the magic tiles (invoked by the magician)
			if (myCMap[i][j]>=1 && myCMap[i][j]!=21) {
				// attach the movieClip, set design, position and intern variable
				var clp:MovieClip = mov.attachMovie(tclip, nt+((1000*i)+j), 1000*i+j+doff);
				// If it's a tale with an array and not a single number
				if (myCMap[i][j][0]) {
					clp.gotoAndStop(myCMap[i][j][0]);
				} else {
					clp.gotoAndStop(myCMap[i][j]);
				}
				clp._xscale *= 1/fac;
				clp._yscale *= 1/fac;
				// hard to calculate : the position in the hexa grid
				clp._x = j*tw+(tw/2)*(i%2);
				clp._y = i*(th)*(1-1/4);
				clp.i = i;
				clp.j = j;
			}
			// endif    
		}
		// endfor j
	}
	// endfor i
	
	return mov;
}

/*
	Function: generateMinimap
	This function is able to load the file of a map and draw a minimap from it.
	
	Parameters:
	
	f - the ID of the map file to load
	
	Return:
	
	The movieClip that contains the drawing
	
	See Also:
	
	<minimap>
*/
function generateMinimap(f){
	var myDMap:Array = new Array();
	var th:MovieClip = this;
	var mov:MovieClip = new MovieClip();
	// Define the XML
	var leXML:XML = new XML();
	leXML.ignoreWhite = true;
	leXML.onLoad = function(success)
	{
		//reading the XML :
	for (i=0; i<leXML.firstChild.firstChild.childNodes.length; i++) {
		// Create an extra array for each line. It's provisional
		var arrDProvisoire:Array = new Array();
		for (j=0; j<leXML.firstChild.firstChild.childNodes[i].childNodes.length; j++) {
			arrDProvisoire[j] = parseInt(leXML.firstChild.firstChild.childNodes[i].childNodes[j].firstChild.nodeValue);
		}
		myDMap[i] = arrDProvisoire;
	}
		mov = th.minimap(myDMap,"cont",3);
	};//end load XML
	leXML.load("Maps/map" + f + ".xml");
	delete myDMap;
	return mov;
}
/*
	Title: Pathfinding
	Here are listed the functions allowing to use the pathfinding algorytm properly
	
	See Also:
	
	<Node>
	
	<Pathfinder>
	
	<http://forums.mediabox.fr/wiki/tutoriaux/flashplatform/jeux/pathfinder_algorithme_astar>
*/
import Node;
import Pathfinder;

/*
	Function: pf_convertMap
	Converts the myMap array into an array of Nodes, in order to get the pathfinding algo
	to work.
	
	Parameters:
	Array myMap - the myMap one wants to convert
	
	Returns:
	
	Array realGraphe - the array of node corresponding, so that the pathfinding algo can work
*/
function pf_convertMap(myMap:Array){
	//Initialize needed variables
	var realGraphe:Array = new Array();
	var graphe:Array = myMap;
	var ligne:Array = graphe[0];
	
	var maxcol:Number = ligne.length;
	var maxline:Number = graphe.length;
	 
	 //Read the myMap array
	for ( var i:Number = 0; i < maxline; i++ ) 
		  {
			var line:Array = new Array();
	 
			for ( var j:Number = 0; j < maxcol; j++ )
			{
			//Create a node corresponding to the myMap[i][j] value
			  var node:Node = new Node();
			  //Check if it's walkable
	 		  var walkable:Boolean = (myMap[i][j]>=1 && myMap[i][j]<20 || myMap[i][j][0]) && myMap[i][j][0]!=10;
			  if (walkable)
			  {
				node.walkable = true;
			  }
			  else{
				 node.walkable = false;
			  }
	 
			  node.col = j;
			  node.line = i;
	 		  // Push it in the line of the new array
			  line.push( node );
			}
	 		//Push the line in the node array
			realGraphe.push( line );
	}
		//Return the array of node for the pathfinding
		return realGraphe;
}

/*
	Function: pf_findPath
	Finds the path between two tiles
	
	Parameters:
	myMap:Array - The map on wich the pathfinding occurs
	tileStart:Array - The start tile for the path
	tileEnd:Array - The end tile for the path
	
	Returns:
	chemin:Array - the array of nodes on the path
*/
function pf_findPath(myMap:Array, tileStart:Array, tileEnd:Array){
	
	//Convert Map
	var realGraphe:Array = pf_convertMap(myMap);
	//Convert tiles into nodes
	var start:Node = realGraphe[tileStart[0]][tileStart[1]];
	var end:Node = realGraphe[tileEnd[0]][tileEnd[1]];
	
	//FIND DAH PATH !
	var chemin:Array = Pathfinder.findPath( realGraphe, start, end ); 
	
	
	return chemin;
}
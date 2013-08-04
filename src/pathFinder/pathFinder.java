package pathFinder;

import com.sun.org.apache.xpath.internal.operations.Number;

import fap_java.CMap;
import fap_java.Cell;

import java.util.ArrayList;
import java.util.Collections;

/*
	Class: Pathfinder
	The pathfinding algorithm. It is based on the A* algorithm.
	
	Link to the tutorial it is extract from (FR) :
	http://forums.mediabox.fr/wiki/tutoriaux/flashplatform/jeux/pathfinder_algorithme_astar
	
	The goal is to list the nodes around the current node, find the one wich has the smallest
	distance to end point and repeat. This algo also takes care of checking if it's possible to
	walk on the tile.
	
	The principle of the algorithm:
	There's a close list and an open list. There is a node that is currently examinated
	on the path, named CURRENT.
	
	Add the start node to the open list
		- Entering the following loop :
			- Get the node with the smallest F in the open list. name it CURRENT
			- Place CURRENT in the closed list
			- For each of the 6 surrounding nodes, apply the following tests :
			
				.If the node is non walkable or is in the closed list, Ignore and analyse
				an other node
				
				.If the node isn't in the open list, add it to this list and make CURRENT
				its parent. Calculate and save its F,G and H properties.
				
				.If the node is already in open list, calculate its new G, and if it's
				less than the old one, make CURRENT its parent, and re-calculate its
				F and H properties
				
				.Stop the loop if the end node is added to closed list or if the open list
				is empty. In this last case, there's no path from start to end.
				
		- Take the end node and get each parent until getting back to start node
	
	Same thing in french:
	
	Ajout du node de départ à la liste ouverte.
        - Entrée dans la boucle suivante :
          - Récupération du node avec le plus petit F contenu dans la liste ouverte.
		  	On le nommera CURRENT.
          - Basculer CURRENT dans la liste fermée.
          - Pour chacun des 6 nodes adjacents à CURRENT appliquer la méthode suivante:

			  .Si le node est un obstacle ou est dans la liste fermée ignorez-le et
			passer à l'analyse d'un autre node.
	
			  .Si le node n'est pas dans la liste ouverte, ajoutez-le à ladite liste
				et faites de CURRENT son parent(P). Calculez et enregistrez
				ses propriétés F, G et H.
	
			  .Si le node est déjà dans la liste ouverte, recalculez son G, s'il est inférieur
				à l'ancien, faites de CURRENT son parent(P) et recalculez et enregistrez
				ses propriétés F et H.
	
			  .Stopper la boucle de recherche si vous ajoutez le node d'arrivée à la liste fermée ou si la liste ouverte est vide,
			dans ce dernier cas, il n'y a pas de chemin possible entre A et B.

        - Prenez le node d'arrivée et reconstruisez le chemin à rebours, càd en bouclant sur les propriétés parentes
        jusqu'à ce que le parent soit CURRENT.
	
	See Also:
	<http://forums.mediabox.fr/wiki/tutoriaux/flashplatform/jeux/pathfinder_algorithme_astar>
*/

public class pathFinder {
    
    private static int ndv = 10;
    private static ArrayList<Node> openList;
    private static ArrayList<Node> closeList;
    
    public static ArrayList<Cell> findPath(ArrayList<Cell> map,Cell start, Cell end){
        NMap nmap = new NMap(map);
        Node nstart = new Node(start);
        Node nend = new Node(end);
        ArrayList<Node> nodePath = findPath(nmap,nstart,nend);
        ArrayList<Cell> cellPath = new ArrayList<Cell>();
        for(int i=0; i < nodePath.size(); i++){
            Node n = nodePath.get(i);
            cellPath.add(n.getCell());
        }
        return cellPath;
    }
    
    
    public static ArrayList<Node> findPath( NMap param_graphe, Node param_start, Node param_end )
        {
              // Creating closed and open lists
          // on crée les listes fermées et les listes ouvertes
          openList = new ArrayList<Node>();
          closeList = new ArrayList<Node>();
     
          for(int i=0;i<param_graphe.getMyMap().size();i++){
              Node n = param_graphe.getMyMap().get(i);
              if(n.equals(param_end)){
                  param_end = n;
              }
              else if(n.equals(param_start)){
                  param_start = n;
              }
          }
     
              // Creating the variable that will host the final path
          // on crée la variable qui va accueillir le chemin final
     
          ArrayList<Node> finalPath = new ArrayList<Node>();
     
          addToOpenList( param_start );
          Node currentNode = null;
     
                    //Stop the loop of the open list is empty
                    //  stopper la boucle si la liste ouverte est vide
          while( openList.size() > 0 ) 
          {
                    // a. get the node with the smallest F in the open list. name it CURRENT
            // a. Récupération du node avec le plus petit F contenu dans la liste ouverte. On le nommera CURRENT.
            currentNode = getCurrentNode(); 
                    
                    //  stop the loop if the end node is added to closed list
            //  stopper la boucle si n ajoute le noeud d'arrivée à la liste fermée
            if( currentNode == param_end ) 
              break;
     
                    // b. Place CURRENT in the closed list
            // b. Basculer CURRENT dans la liste fermée.
            addToCloseList( currentNode ); 
     
                    //  get neighbours of CURRENT
            //  récupération des voisins de CURRENT
            ArrayList<Node> neighbours = param_graphe.surroundingNodes(currentNode); 
            int maxi = neighbours.size();
                    // For each of the 6 surrounding nodes, apply the following
            // Pour chacun des 6 nodes adjacents à CURRENT appliquer la méthode suivante:
            for( int i = 0; i < maxi; i++ )
            {
            
              Node node = neighbours.get(i);
                if(node != null){
                      //if the node is an obstacle, ignore
              //Si le node est un obstacle ou est dans la liste fermée ignorez-le et passer à l'analyse d'un autre node.
              if(closeList.contains( node ) || node != null && !node.getCell().isWalkable()) 
                continue;
                            
                      // Calculate new g
              /* on calcule le nouveau g */
              int newG;
              newG = (int)(node.getParent().getG() + pathFinder.ndv);
                    
                      //Calculate new h
              /*on calcule le nouveau h */
             // var newH:Number = ( Math.abs( param_end.line - node.line ) + Math.abs( param_end.col - node.col ) ) * ndv;
                            double dx = Math.abs(param_end.getLine() - node.getLine());
                            double dy = Math.abs( param_end.getCol() - node.getCol() );
                            double newH = Math.sqrt(dx*dx+dy*dy) * ndv;
                            
                      //Calculate new F
              /*on calcule le nouveau F*/
              double newF = newH + newG;
     
      
              if( openList.contains( node ) )
              {
                             /*
                             If the node is already in the open list, re-calculate its G, and, if it is
                             less than the former G, make CURRENT its parent and re-calculate its F and H
                             */
                //Si le node est déjà dans la liste ouverte, recalculez son G, s'il est inférieur à l'ancien, 
                //faites de CURRENT  son parent(P) et recalculez et enregistrez ses propriétés F et H.
     
                if( newG < node.getG() )
                {
                  node.setParent(currentNode);
                  node.setG(newG);
                  node.setH(newH);
                  node.setF(newF);
                }
     
              }
              else 
              {
                            //If the node isn't in the open list, add it to this list. Calculate F,G,H
                //Si le node n'est pas dans la liste ouverte, ajoutez-le à la dite liste et faites de CURRENT son parent(P). 
                //Calculez et enregistrez ses propriétés F, G et H.
                addToOpenList( node );
                node.setParent(currentNode);
                node.setG(newG);
                node.setH(newH);
                node.setF(newF);
              }
            }
            }
     
          }
              // end of the list. Either there's no path and the list is empty
          // on est sorti de la liste, on a deux solutions, soit la liste ouverte est vide dans ces cas là il 
          // n'y a pas de solutions et on retoure directement finalPath;
     
              // There's no path
          if( openList.size() == 0 )
            return finalPath;
     
              //Either there's a path and we build it backwards from the end of the list, using the parent property
          // Soit on maintenant on construit le chemin à rebours;
     
          Node lastNode = param_end;
          while( lastNode != param_start )
          {
           // trace( lastNode.parent );
            finalPath.add( lastNode );
            lastNode = lastNode.getParent();
          }
    
              // Return final path
          // on retourne le chemin final
          Collections.reverse(finalPath);
          return finalPath;
        }
    
    
    private static void addToCloseList( Node param_node )
        {
          openList.remove(param_node);
          closeList.add( param_node );
        }
     
            /*
                    Function: addToOpenList
                    Add a node to the open list
            */
        private static void addToOpenList( Node param_node )
        {
          closeList.remove( param_node );
          openList.add( param_node );
        }
    
    private static Node getCurrentNode()
        {
          int maximum = openList.size();
          int minF = 1000000;
          Node curNode = null;
     
          for( int i = 0; i < maximum; i++ )
          {
            Node node = openList.get(i);
     
            if( (int)node.getF() < minF )
            {
              minF = (int)node.getF();
              curNode = node;
            }
          }
     
          return curNode;
        }
}

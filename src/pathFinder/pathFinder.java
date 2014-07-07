package pathFinder;

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
	
	Ajout du node de depart a la liste ouverte.
        - Entree dans la boucle suivante :
          - Recuperation du node avec le plus petit F contenu dans la liste ouverte.
		  	On le nommera CURRENT.
          - Basculer CURRENT dans la liste fermee.
          - Pour chacun des 6 nodes adjacents a CURRENT appliquer la methode suivante:

			  .Si le node est un obstacle ou est dans la liste fermee ignorez-le et
			passer a l'analyse d'un autre node.
	
			  .Si le node n'est pas dans la liste ouverte, ajoutez-le a ladite liste
				et faites de CURRENT son parent(P). Calculez et enregistrez
				ses proprietes F, G et H.
	
			  .Si le node est deja dans la liste ouverte, recalculez son G, s'il est inferieur
				a l'ancien, faites de CURRENT son parent(P) et recalculez et enregistrez
				ses proprietes F et H.
	
			  .Stopper la boucle de recherche si vous ajoutez le node d'arrivee a la liste fermee ou si la liste ouverte est vide,
			dans ce dernier cas, il n'y a pas de chemin possible entre A et B.

        - Prenez le node d'arrivee et reconstruisez le chemin a rebours, cad en bouclant sur les proprietes parentes
        jusqu'a ce que le parent soit CURRENT.
	
	See Also:
	<http://forums.mediabox.fr/wiki/tutoriaux/flashplatform/jeux/pathfinder_algorithme_astar>
*/

public class pathFinder {

    private static int ndv = 10;

    private static NMap theMap;
    private static long lastGenerationDate;
    public static final long pathfindingMapGenerationFrequency = 5*1000;

    /**
     * Find the path from a starting cell to another
     * @param map : the map where the game plays
     * @param start : the start cell
     * @param end : the end cell
     * @return : the path
     */
    public static ArrayList<Cell> findPath(ArrayList<Cell> map, Cell start, Cell end) {
        //Convert the Cell map into Node Map
        long diff = System.currentTimeMillis() - lastGenerationDate;
        if(theMap == null || diff > pathfindingMapGenerationFrequency){
            lastGenerationDate = System.currentTimeMillis();
            theMap = new NMap(map);
        }
        
         
        
       
        Node nstart = new Node(start);
        Node nend = new Node(end);
        // Get the path
        ArrayList<Node> nodePath = findPath(theMap, nstart, nend);
        ArrayList<Cell> cellPath = new ArrayList<Cell>();
        //Convert into cell
        for (int i = 0; i < nodePath.size(); i++) {
            Node n = nodePath.get(i);
            cellPath.add(n.getCell());
        }
        return cellPath;
    }

    /**
     * Finds the path between a start node and an end node
     * @param param_graphe : the Node map where it happens
     * @param param_start : the starting node
     * @param param_end : the end node
     * @return : the node path
     */
    public static ArrayList<Node> findPath(NMap param_graphe, Node param_start, Node param_end){
        // Creating closed and open lists
        // on cree les listes fermees et les listes ouvertes
        ArrayList<Node> openList = new ArrayList<Node>();
        ArrayList<Node> closeList= new ArrayList<Node>();

        for (int i = 0; i < param_graphe.getMyMap().size(); i++) {
            Node n = param_graphe.getMyMap().get(i);
            if (n.equals(param_end)) {
                param_end = n;
            } else if (n.equals(param_start)) {
                param_start = n;
            }
        }

        // Creating the variable that will host the final path
        // on cree la variable qui va accueillir le chemin final

        ArrayList<Node> finalPath = new ArrayList<Node>();

        addToOpenList(param_start,openList,closeList);
        Node currentNode = null;

        //Stop the loop of the open list is empty
        //  stopper la boucle si la liste ouverte est vide
        while (openList.size() > 0) {
            // a. get the node with the smallest F in the open list. name it CURRENT
            // a. Recuperation du node avec le plus petit F contenu dans la liste ouverte. On le nommera CURRENT.
            currentNode = getCurrentNode(openList,closeList);

            //  stop the loop if the end node is added to closed list
            //  stopper la boucle si n ajoute le noeud d'arrivee a la liste fermee
            if (currentNode == param_end)
                break;

            // b. Place CURRENT in the closed list
            // b. Basculer CURRENT dans la liste fermee.
            addToCloseList(currentNode,openList,closeList);

            //  get neighbours of CURRENT
            //  recuperation des voisins de CURRENT
            ArrayList<Node> neighbours = param_graphe.surroundingNodes(currentNode);
            int maxi = neighbours.size();
            // For each of the 6 surrounding nodes, apply the following
            // Pour chacun des 6 nodes adjacents a CURRENT appliquer la methode suivante:
            for (int i = 0; i < maxi; i++) {

                Node node = neighbours.get(i);
                if (node != null) {
                    //if the node is an obstacle, ignore
                    //Si le node est un obstacle ou est dans la liste fermee ignorez-le et passer a l'analyse d'un autre node.
                    if (closeList.contains(node) || node != null && !node.getCell().isWalkable())
                        continue;

                    // Calculate new g
                    /* on calcule le nouveau g */
                    int newG;
                    newG = (int)(node.getParent().getG() + pathFinder.ndv);

                    //Calculate new h
                    /*on calcule le nouveau h */
                    // var newH:Number = ( Math.abs( param_end.line - node.line ) + Math.abs( param_end.col - node.col ) ) * ndv;
                    double dx = Math.abs(param_end.getLine() - node.getLine());
                    double dy = Math.abs(param_end.getCol() - node.getCol());
                    double newH = Math.sqrt(dx * dx + dy * dy) * ndv;

                    //Calculate new F
                    /*on calcule le nouveau F*/
                    double newF = newH + newG;


                    if (openList.contains(node)) {
                        /*
                             If the node is already in the open list, re-calculate its G, and, if it is
                             less than the former G, make CURRENT its parent and re-calculate its F and H
                             */
                        //Si le node est deja dans la liste ouverte, recalculez son G, s'il est inferieur a l'ancien,
                        //faites de CURRENT  son parent(P) et recalculez et enregistrez ses proprietes F et H.

                        if (newG < node.getG()) {
                            node.setParent(currentNode);
                            node.setG(newG);
                            node.setH(newH);
                            node.setF(newF);
                        }

                    } else {
                        //If the node isn't in the open list, add it to this list. Calculate F,G,H
                        //Si le node n'est pas dans la liste ouverte, ajoutez-le a la dite liste et faites de CURRENT son parent(P).
                        //Calculez et enregistrez ses proprietes F, G et H.
                        addToOpenList(node,openList,closeList);
                        node.setParent(currentNode);
                        node.setG(newG);
                        node.setH(newH);
                        node.setF(newF);
                    }
                }
            }

        }
        // end of the list. Either there's no path and the list is empty
        // on est sorti de la liste, on a deux solutions, soit la liste ouverte est vide dans ces cas la il
        // n'y a pas de solutions et on retoure directement finalPath;

        // There's no path
        if (openList.size() == 0)
            return finalPath;

        //Either there's a path and we build it backwards from the end of the list, using the parent property
        // Soit on maintenant on construit le chemin a rebours;

        Node lastNode = param_end;
        long i=0;
        while (lastNode != param_start) {
            // trace( lastNode.parent );
            finalPath.add(lastNode);
            lastNode = lastNode.getParent();
        }

        // Return final path
        // on retourne le chemin final
        Collections.reverse(finalPath);
        return finalPath;
    }


    private static void addToCloseList(Node param_node, ArrayList<Node> openList, ArrayList<Node> closeList) {
        openList.remove(param_node);
        closeList.add(param_node);
    }

    private static void addToOpenList(Node param_node, ArrayList<Node> openList, ArrayList<Node> closeList) {
        closeList.remove(param_node);
        openList.add(param_node);
    }

    private static Node getCurrentNode(ArrayList<Node> openList, ArrayList<Node> closeList) {
        int maximum = openList.size();
        int minF = Integer.MAX_VALUE;
        Node curNode = null;

        for (int i = 0; i < maximum; i++) {
            Node node = openList.get(i);

            if ((int)node.getF() < minF) {
                minF = (int)node.getF();
                curNode = node;
            }
        }

        return curNode;
    }

    public static void setTheMap(NMap theMap) {
        pathFinder.theMap = theMap;
    }

    public static NMap getTheMap() {
        return theMap;
    }
}

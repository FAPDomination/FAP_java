package pathFinder;

import fap_java.Cell;

import java.util.ArrayList;

public class NMap {
    public NMap(ArrayList<Cell> map) {
        this.convertMap(map);
    }
    
    private ArrayList<Node> myMap = new ArrayList<Node>();
    
    public ArrayList<Node> getMyMap() {
        return myMap;
    }

    public void addElement(Node c) {
        if (containsNode(c) != -1) {
            myMap.remove(containsNode(c));
        }
        myMap.add(c);
    }
    
    public void removeElement(Node c) {
        myMap.remove(c);
    }
    
    public int containsNode(Node c) {
        int b = (-1);
        for (int k = 0; k < myMap.size(); k++) {
            Node o = myMap.get(k);
            if (o.equals(c)) {
                b = k;
                break;
            }
        }
        return b;
    }
    
    public Node getNode(int[] tab) {
        Node c;
        Node o = new Node(new Cell(tab[0],tab[1],1,1));
        if (tab.length == 2 && containsNode(o) != (-1)) {
            c = myMap.get(containsNode(o));
        } else {
            c = null;
        }
        return c;
    }

    public Node getNode(int i, int j) {
        int[] tab = new int[2];
        tab[0] = i;
        tab[1] = j;
        return getNode(tab);
    }
    
    public ArrayList<Node> surroundingNodes(Node c) {
        ArrayList<Node> surroundingNodes = new ArrayList<Node>(6);
        int i = c.getLine();
        int j = c.getCol();
        Node o;
        // top Nodes
        // not the first line
        if (i % 2 == 0) {
            o = this.getNode(i - 1, j - 1);
            surroundingNodes.add(o);
            //surroundingNodes['tr'] = [i-1, j];
            o = this.getNode(i - 1, j);
            surroundingNodes.add(o);
        } else {
            
            //surroundingNodes['tl'] = [i-1, j];
            o = this.getNode(i - 1, j);
            surroundingNodes.add(o);
            
            //surroundingNodes['tr'] = [i-1, j+1];
            o = this.getNode(i - 1, j + 1);
            surroundingNodes.add(o);
        }
        // Nodes from the same line
        //surroundingNodes['l'] = [i, j-1];
        o = this.getNode(i, j - 1);
        surroundingNodes.add(o);
        //surroundingNodes['r'] = [i, j+1];
        o = this.getNode(i, j + 1);
        surroundingNodes.add(o);
        // bottom Nodes (see top Nodes)
        if (i % 2 == 0) {
            //surroundingNodes['bl'] = [i+1, j-1];
            o = this.getNode(i + 1, j - 1);
            surroundingNodes.add(o);
            //surroundingNodes['br'] = [i+1, j];
            o = this.getNode(i + 1, j);
            surroundingNodes.add(o);
        } else {
            //surroundingNodes['br'] = [i+1, j+1];
            o = this.getNode(i + 1, j + 1);
            surroundingNodes.add(o);
            //surroundingNodes['bl'] = [i+1, j];
            o = this.getNode(i + 1, j);
            surroundingNodes.add(o);
        }
        return surroundingNodes;
    };
    
    private ArrayList<Node> convertMap(ArrayList<Cell> map){
        ArrayList<Node> list = new ArrayList<Node>();
        //Do stuff
        for(int i=0;i<map.size();i++){
            Cell c = map.get(i);
            if(c!=null){
            Node n = new Node(c);
            this.myMap.add(n);
            }
        }
        return list;
    }

    public void setMyMap(ArrayList<Node> myMap) {
        this.myMap = myMap;
    }
}

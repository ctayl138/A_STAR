package a_star;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;


public class A_STAR {
    
    private Node [][] map = new Node[15][15]; // holds node objects
    private Random rand = new Random( ); // rng
    private Node start; //stores start node
    private Node end; //stores end node
    private Node current; //stores currently selected node
    private int blockCount = 0; //stores number of blocked nodes
    private final PriorityQueue openList = new PriorityQueue<>(225, (Node n, Node m) -> {
        return n.getF() < m.getF() ? - 1: n.getF() > m.getF() ? 1 :0; // determines how the priority queue (min heap) sorts the nodes
    });
    private ArrayList closedList = new ArrayList(225); //stores the visited nodes
    
    public A_STAR( ){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                map[i][j] = new Node(i, j, 0); //creates blank nodes
            }
        }
        while(blockCount < 24){
            int m = rand.nextInt(15);
            int n = rand.nextInt(15);

            if(map[m][n].type != 1){
                map[m][n].type = 1; //stores the blocked nodes
                blockCount++; //counts blocked nodes
            }
        }
        
        System.out.println("Initialized Board:"); //outputs board
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                System.out.print(map[i][j].type + " ");
            }
            System.out.print("\n");
        }
    }
    
    public static void main(String[] args) {
        A_STAR astar = new A_STAR( );
        Scanner scan = new Scanner(System.in);
        int m;
        int n;
        char choice = 'y';
        
        
        while(choice=='y'||choice=='Y'){
            System.out.println("\nEnter the row of the starting position:");
            m = scan.nextInt( );
            System.out.println("\nEnter the column of the starting position:");
            n = scan.nextInt( );
        
            astar.start = astar.map[m][n];
        
            System.out.println("\nEnter the row of the starting position:");
            m = scan.nextInt( );
            System.out.println("\nEnter the column of the starting position:");
            n = scan.nextInt( );
        
            astar.end = astar.map[m][n];
        
            astar.start.type = 2;
            astar.end.type = 4;
        
            System.out.println("\nBoard with Terminals:");
            System.out.println("0 => traverseable");
            System.out.println("1 => blocked");
            System.out.println("2 => start");
            System.out.println("4 => end");
            System.out.println("8 => path");
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    System.out.print(astar.map[i][j].type + " ");
                }
                System.out.print("\n");
            }
        
            astar.move( ); //initiates A* algorithm
            System.out.println("Do you want to choose different start and end points?");
            choice = scan.next().charAt(0);
            astar.mapReset();
            if(choice == 'y'||choice == 'Y'){
            System.out.println("\nMap reset: ");
            for(int i = 0; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    System.out.print(astar.map[i][j].type + " ");
                }
                System.out.print("\n");
            }
            }
        }
        
        System.out.println("\n\nHave a nice day! :)");
        
        
        
    }
    
    public void move( ){
        openList.add(start); // adds start node
        
        while(true){ //iterating block

            current = (Node) openList.poll(); //pops first node 
            closedList.add(current); //adds current node to closed list

            Node n;  
            
            if(current.getRow() - 1 >= 0){ // checks nodes above current node
                n = map[current.getRow()-1][current.getCol()];
                update(n, 10); 

                if(current.getCol() - 1 >= 0){ //check diagonal node to upper left                     
                    n = map[current.getRow()-1][current.getCol()-1];
                    update(n, 14); 
                }

                if(current.getCol()+1 < 15){ //check diagonal node to the right
                    n = map[current.getRow()-1][current.getCol()+1];
                    update(n, 14); 
                }
            } 

            if(current.getCol() - 1 >= 0){ //checks node to the left
                n = map[current.getRow()][current.getCol()-1];
                update(n, 10); 
            }

            if(current.getCol()+1< 15){ // checks node to the right
                n = map[current.getRow()][current.getCol()+1];
                update(n, 10); 
            }

            if(current.getRow()+1< 15){ //checks node to the bottom
                n = map[current.getRow()+1][current.getCol()];
                update(n, 10); 

                if(current.getCol()-1 >= 0){ // get node to the bottom left
                    n = map[current.getRow()+1][current.getCol()-1];
                    update(n, 14); 
                }
                
                if(current.getCol()+ 1 < 15){ //get node to the bottom right
                   n = map[current.getRow()+1][current.getCol()+1];
                   update(n, 14); 
                }
            
            }
            if(openList.isEmpty()){//terminating condition
                trace(end);
                System.out.println("\nFinal Path:");
                System.out.println("0 => traverseable");
                System.out.println("1 => blocked");
                System.out.println("2 => start");
                System.out.println("4 => end");
                System.out.println("8 => path");
                for(int i = 0; i < 15; i++){
                    for(int j = 0; j < 15; j++){
                        System.out.print(map[i][j].getType() + " ");
                    }
                    System.out.print("\n");
                }
                break;
            }
        }
    }
    public void update(Node n, int cost){
        if(n == null||closedList.contains(n)||n.getType() == 1){return;} //if node is null, already visited, or blocked, exit
        n.setH(heuristic(n));
        if(openList.contains(n)){ //if node is in open list
           if(n.getG() + cost < current.getG()){
               current.setParent(n);
               current.setG(n.getG()+cost);
               current.setF();
           }
        }
        if(!openList.contains(n)){ //if node is not in the open list
            n.setParent(current);
            n.setG(current.getG() + cost);
            n.setF();
            openList.add(n);
            
        }
        
    }
    public void mapReset( ){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if((map[i][j].getType() == 2)||(map[i][j].getType() == 3)||(map[i][j].getType()==4)||(map[i][j].getType()==8)){
                    map[i][j].setType(0);
                    map[i][j].setParent(null);
                    map[i][j].setG(0);
                    map[i][j].setH(0);
                    map[i][j].setF();
                }
                
            }
        }
        openList.clear();
        closedList.clear();
    }
    
    public int heuristic(Node n){
        int h;
        h = (abs((n.getCol()-end.getCol()))+ abs((n.getRow() - end.getRow())))*10;//calculates manhattan score
        return h;
    }
    
    public void trace(Node n){ //traces the path from the end node to the start node
        current = n.getParent();
        if(current == null){System.out.println("\nNo path could be found!");return;}
        while(current != start){
            current.type = 8;
            current = current.getParent();
        }
    }
    
    class Node {
        private int row, col, f, g, h, type; private Node parent;
  
        public Node(int r, int c, int t){
            row = r; col = c; type = t; parent = null; //type 0 is traverseable, 1 is not
        }
        //mutator methods to set values 
        public void setF(){
            f = g + h;
        } 
        public void setG(int value){
            g = value;
        } 
        public void setH(int value){
            h = value;
        } 
        public void setParent(Node n){
            parent = n;
        }
        public void setType(int n){
            type = n;
        }
        //accessor methods to get values 
        public int getF(){return f;} public int getType() {return type;} public int getG(){ return g; } public int getH(){ return h; } public Node getParent(){ return parent; } public int getRow(){ return row; } public int getCol(){ return col; }
        @Override
        public boolean equals(Object in){ //typecast to Node 
            Node n = (Node) in;
            return row == n.getRow() && col == n.getCol();
        }   
        @Override
        public String toString(){
            return "Node: " + row + "_" + col;
        }
}
}

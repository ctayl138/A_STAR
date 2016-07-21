package a_star;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;


public class A_STAR {
    
    private static final int DCOST = 14;
    private static final int VHCOST = 10;
    private Node [][] map = new Node[15][15];
    private Random rand = new Random( );
    private Node start;
    private Node end;
    private Node current;
    private int blockCount = 0;
    private final PriorityQueue openList = new PriorityQueue<>(225, (Node n, Node m) -> {
        return n.getF() < m.getF()?-1: n.getF()>m.getF()?1 :0;
    });
    private ArrayList closedList = new ArrayList(225);
    
    
    public A_STAR( ){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                map[i][j] = new Node(i, j, 0);
            }
        }
        while(blockCount < 24){
            int m = rand.nextInt(15);
            int n = rand.nextInt(15);
            
            if(map[m][n].type != 1){
                map[m][n].type = 1;
                blockCount++;
            }
        }
        
        System.out.println("Initialized Board:");
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                System.out.print(map[i][j].type + " ");
            }
            System.out.print("\n");
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
    
    public static void main(String[] args) {
        A_STAR astar = new A_STAR( );
        Scanner scan = new Scanner(System.in);
        int m;
        int n;
        
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
        
        System.out.println("Board with Terminals:");
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                System.out.print(astar.map[i][j].type + " ");
            }
            System.out.print("\n");
        }
        
        astar.move( );
        
        
    }
    
    public void move( ){
        openList.add(start);
        
        while(true){

            current = (Node) openList.poll();
            closedList.add(current);

            Node n;  
            
            if(current.getRow()-1>=0){
                n = map[current.getRow()-1][current.getCol()];
                update(n, 10); 

                if(current.getCol()-1>=0){                      
                    n = map[current.getRow()-1][current.getCol()-1];
                    update(n, 14); 
                }

                if(current.getCol()+1 < map[0].length){
                    n = map[current.getRow()-1][current.getCol()+1];
                    update(n, 14); 
                }
            } 

            if(current.getCol()-1>=0){
                n = map[current.getRow()][current.getCol()-1];
                update(n, 10); 
            }

            if(current.getCol()+1<map[0].length){
                n = map[current.getRow()][current.getCol()+1];
                update(n, 10); 
            }

            if(current.getRow()+1<map.length){
                n = map[current.getRow()+1][current.getCol()];
                update(n, 10); 

                if(current.getCol()-1>=0){
                    n = map[current.getRow()+1][current.getCol()-1];
                    update(n, 14); 
                }
                
                if(current.getCol()+ 1 < map[0].length){
                   n = map[current.getRow()+1][current.getCol()+1];
                    update(n, 14); 
                }
            
            }
            if(closedList.contains(end)){
                trace(end);
                System.out.println("Final Path:");
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
        if(n == null||closedList.contains(n)||n.getType() == 1){return;}
        n.setH(heuristic(n));
        if(openList.contains(n)){
           if(n.getG() + cost < current.getG()){
               current.setParent(n);
               current.setG(n.getG()+cost);
               current.setF();
           }
        }
        if(!openList.contains(n)){
            n.setParent(current);
            n.setG(current.getG() + cost);
            openList.add(n);
            n.setF();
        }
        
    }
    
    public int heuristic(Node n){
        int h;
        h = (abs((end.getCol()-n.getCol()))+ abs((end.getRow() - n.getRow())))*10;
        return h;
    }
    
    public void trace(Node n){
        current = n.getParent();
        if(current == null){return;}
        while(current != start){
            current.type = 8;
            current = current.getParent();
        }
    }
    
}

package Rental;


class Node {
    
    
    Car   item;   // the Car
    Node  next;  // next node
    
    
    Node ( Car c, Node n ) {
        
        item = c;
        next = n;
        
    };  // constructor
    
    
}  //Node
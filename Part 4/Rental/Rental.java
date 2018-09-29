package Rental;


import BasicIO.*;
import static BasicIO.Formats.*;


public class Rental {
    
    
    private Node            avail;    // list of available cars
    private Node            rented;   // list of rented cars
    private ASCIIDisplayer  display;  // for display of lists
    private BasicForm       form;     // form for user interaction
    
    
    public Rental ( ) {
        
        int  button;  // button pressed
        
        display = new ASCIIDisplayer();
        form = new BasicForm("Rent","Return","List","Quit");
        avail = null;
        rented = null;
        loadCars();
        setupForm();
        for ( ; ; ) {
            form.clearAll();
            button = form.accept();
        if ( button == 3 ) break;  // Quit
            switch ( button ) {
                case 0: {          // Rent
                    doRent();
                    break;
                }
                case 1: {          // Return
                    doReturn();
                    break;
                }
                case 2: {          // List
                    doList();
                    break;
                }
            };
            form.accept("OK");
        };
        form.close();
        display.close();
        
    }; // constructor
    
    
    private void doRent ( ) {
        
        int  cat;   // rental category
        Car  aCar;  // car to be rented
        
        cat = form.readInt("cat");
        aCar = removeAvail(cat);
        if ( aCar == null ) {
            form.writeString("msg","No cars available in that category");
        }
        else {
            fillForm(aCar);
            addRented(aCar);
            form.writeString("msg","Rented: "+aCar.getLicence());
        }
        
    };  // doRent
    
    
    private void doReturn ( ) {
        
        String  licence;  // licence plate
        Car     aCar;     // car being returned
        int     mileage;  // mileage at return
        double  cost;     // cost of rental
        
        licence = form.readString("licence");
        aCar = removeRented(licence);
        if ( aCar == null ) {
            form.writeString("msg","Licence: "+licence+" is not currently rented");
        }
        else {
            fillForm(aCar);
            form.accept("OK");
            mileage = form.readInt("nMileage");
            cost = aCar.returned(mileage);
            form.writeDouble("amt",cost);
            addAvail(aCar);
            form.writeString("msg","Returned: "+aCar.getLicence());
        };
        
    };  // doReturn
    
    
    private void doList ( ) {
        
        display.writeLine("Available");
        listAvail();
        display.writeLine("Rented");
        listRented();
        form.writeString("msg","Listed");
        
    };  // doList
    
    
    private void loadCars ( ) {
        
        ASCIIDataFile  carFile;  // file of car info
        Car            aCar;
        
        carFile = new ASCIIDataFile();
        for ( ; ; ) {
            aCar = new Car(carFile);
        if ( carFile.isEOF() ) break;
            addAvail(aCar);
        };
        
    };  // loadCars
    
    
    private void setupForm ( ) {
        
        form.setTitle("Acme Rentals");
        form.addTextField("licence","Licence",8,10,10);
        form.addRadioButtons("cat","Category",true,10,40,Car.CATEGORIES);
        form.addTextField("rate","Rate",getCurrencyInstance(),8,294,40);
        form.setEditable("rate",false);
        form.addTextField("oMileage","Rental Mileage",getIntegerInstance(),
                          8,10,160);
        form.setEditable("oMileage",false);
        form.addTextField("nMileage","Returned Mileage",getIntegerInstance(),
                          8,222,160);
        form.addTextField("amt","Amount",getCurrencyInstance(),10,10,190);
        form.setEditable("amt",false);
        form.addTextField("msg","Message",45,10,220);
        form.setEditable("msg",false);
        
    };  // setupForm
    
    
    private void fillForm ( Car aCar ) {
        
        form.writeString("licence",aCar.getLicence());
        form.writeInt("oMileage",aCar.getMileage());
        form.writeInt("cat",aCar.getCategory());
        form.writeDouble("rate",aCar.getRate());
        
    };  // fillForm
    
    
    private void addAvail ( Car aCar ) {
        
        Node  p;
        Node  q;
        
        q = null;
        p = avail;
        while ( p != null ) {
            q = p;
            p = p.next;
        };
        if ( q == null ) {
            avail = new Node(aCar,null);
        }
        else {
            q.next = new Node(aCar,null);
        };
        
    };  // addAvail
    
    
    private Car removeAvail ( int cat ) {
        
        Car   result;  // car being rented
        Node  p;
        Node  q;
        
        q = null;
        p = avail;
        while ( p != null
                && p.item.getCategory() != cat ) {
            q = p;
            p = p.next;
        };
        if ( p == null ) {
            result = null;
        }
        else {
            result = p.item;
            if ( q == null ) {
                avail = p.next;
            }
            else {
                q.next = p.next;
            };
        };
        return result;
        
    };  // removeAvail
    
    
    private void listAvail ( ) {
        
        Node  p;
        
        p = avail;
        while ( p != null ) {
            display.writeString(p.item.getLicence());
            display.writeInt(p.item.getMileage());
            display.writeInt(p.item.getCategory());
            display.newLine();
            p = p.next;
        };
        display.newLine();

    };  // listAvail
    
    
    private void addRented ( Car aCar ) {
        
        rented = new Node(aCar,rented);
        
    };  // addRented
    
    
    private Car removeRented ( String licence ) {
        
        Car   result;  // car being returned
        Node  p;
        Node  q;
        
        q = null;
        p = rented;
        while ( p != null
                   && p.item.getLicence().compareTo(licence) != 0 ) {
            q = p;
            p = p.next;
        };
        if ( p == null ) {
            result = null;
        }
        else {
            result = p.item;
            if ( q == null ) {
                rented = p.next;
            }
            else {
                q.next = p.next;
            };
        };
        return result;
        
    }; // removeRented
    
    
    private void listRented ( ) {
        
        Node  p;
        
        p = rented;
        while ( p != null ) {
            display.writeString(p.item.getLicence());
            display.writeInt(p.item.getMileage());
            display.writeInt(p.item.getCategory());
            display.newLine();
            p = p.next;
        };
        display.newLine();
        
    };  // listRented
    
    
    public static void main ( String[] args ) { Rental r = new Rental(); };
    
    
}  // Rental
package phase1;

import java.util.ArrayList;
import java.util.List;

import physics.Angle;
import physics.Geometry.DoublePair;
import physics.LineSegment;
import physics.Circle;

public class LeftFlipper extends Flipper{
    
    private static final int SIDE_LENGTH = 2; 
    
    public LeftFlipper(double xloc, double yloc, Angle orientation, List<Gadget> triggeredGadgets) {
        super(xloc, yloc, orientation, triggeredGadgets);
        if (this.orientation.equals(Angle.ZERO)) {
            this.endpoint = new Circle(xloc, yloc + SIDE_LENGTH, 0);
            this.pivot= new Circle(xloc, yloc,0);
            this.flipper = new LineSegment(xloc, yloc, xloc, yloc + SIDE_LENGTH);
            this.isVerticle=true;
            this.x=xloc;
            this.y=yloc;    
        } else if (this.orientation.equals(Angle.DEG_90)) {
            this.endpoint = new Circle(xloc, yloc, 0);
            this.pivot= new Circle(xloc + SIDE_LENGTH, yloc,0);
            this.flipper = new LineSegment(xloc, yloc, xloc + SIDE_LENGTH, yloc);
            this.isVerticle=false;
            this.x=xloc+SIDE_LENGTH;
            this.y=yloc;  
        } else if (this.orientation.equals(Angle.DEG_180)) {
            this.endpoint = new Circle(xloc+SIDE_LENGTH,yloc,0);
            this.pivot=new Circle(xloc+SIDE_LENGTH,yloc+SIDE_LENGTH,0);
            this.flipper = new LineSegment(xloc+SIDE_LENGTH, yloc, xloc+SIDE_LENGTH,yloc+SIDE_LENGTH);
            this.isVerticle=true;
            this.x=xloc+SIDE_LENGTH;
            this.y=yloc+SIDE_LENGTH;  
        } else {
            this.endpoint = new Circle(xloc+SIDE_LENGTH,yloc+SIDE_LENGTH,0);
            this.pivot= new Circle(xloc,yloc+SIDE_LENGTH,0);
            this.flipper = new LineSegment(xloc, yloc+SIDE_LENGTH, xloc+SIDE_LENGTH, yloc+SIDE_LENGTH);
            this.isVerticle=false;
            this.x=xloc;
            this.y=yloc+SIDE_LENGTH;  
        }
    }
    
    public LeftFlipper(double xloc, double yloc, Angle orientation) {
        super(xloc, yloc, orientation, new ArrayList<Gadget>());
        if (this.orientation.equals(Angle.ZERO)) {
            this.endpoint = new Circle(xloc, yloc + SIDE_LENGTH, 0);
            this.pivot= new Circle(xloc, yloc,0);
            this.flipper = new LineSegment(xloc, yloc, xloc, yloc + SIDE_LENGTH);
            this.isVerticle=true;
            this.x=xloc;
            this.y=yloc;     
        } else if (this.orientation.equals(Angle.DEG_90)) {
            this.endpoint = new Circle(xloc, yloc, 0);
            this.pivot= new Circle(xloc + SIDE_LENGTH, yloc,0);
            this.flipper = new LineSegment(xloc, yloc, xloc + SIDE_LENGTH, yloc);
            this.isVerticle=false;
            this.x=xloc+SIDE_LENGTH;
            this.y=yloc;  
        } else if (this.orientation.equals(Angle.DEG_180)) {
            this.endpoint = new Circle(xloc+SIDE_LENGTH,yloc,0);
            this.pivot=new Circle(xloc+SIDE_LENGTH,yloc+SIDE_LENGTH,0);
            this.flipper = new LineSegment(xloc+SIDE_LENGTH, yloc, xloc+SIDE_LENGTH,yloc+SIDE_LENGTH);
            this.isVerticle=true;
            this.x=xloc+SIDE_LENGTH;
            this.y=yloc+SIDE_LENGTH;   
        } else {
            this.endpoint = new Circle(xloc+SIDE_LENGTH,yloc+SIDE_LENGTH,0);
            this.pivot= new Circle(xloc,yloc+SIDE_LENGTH,0);
            this.flipper = new LineSegment(xloc, yloc+SIDE_LENGTH, xloc+SIDE_LENGTH, yloc+SIDE_LENGTH);
            this.isVerticle=false;
            this.x=xloc;
            this.y=yloc+SIDE_LENGTH;  
        }
    }
    
    public LeftFlipper(double xloc, double yloc) {
        super(xloc, yloc, Angle.ZERO, new ArrayList<Gadget>());
        this.endpoint = new Circle(xloc, yloc + SIDE_LENGTH, 0);
        this.pivot= new Circle(xloc, yloc,0);
        this.flipper = new LineSegment(xloc, yloc, xloc, yloc + SIDE_LENGTH);
        this.isVerticle=true;
        this.x=xloc;
        this.y=yloc;    
    }
    
    public LeftFlipper(double xloc, double yloc, List<Gadget> triggeredGadgets) {
        super(xloc, yloc, Angle.ZERO, triggeredGadgets);
        this.endpoint = new Circle(xloc, yloc + SIDE_LENGTH, 0);
        this.pivot= new Circle(xloc, yloc,0);
        this.flipper = new LineSegment(xloc, yloc, xloc, yloc + SIDE_LENGTH);
        this.isVerticle=true;
        this.x=xloc;
        this.y=yloc;     
    }
    
    @Override
    public List<DoublePair> toConsoleCoordinates() {
        int xLoc = (int) (Math.ceil(center.x()+1)); 
        if (xLoc>20) xLoc = 20;
        int yLoc = (int) (Math.ceil(center.y()+1));
        if (yLoc>20) yLoc = 20;
        
        List<DoublePair> coordinates= new ArrayList<DoublePair>();
        if (isVerticle){
            if (orientation == Angle.DEG_90 || orientation == Angle.DEG_180) xLoc+=1;
            coordinates.add(new DoublePair(xLoc, yLoc));
            coordinates.add(new DoublePair(xLoc, yLoc+1));

        } else {
            if (orientation == Angle.DEG_180 || orientation == Angle.DEG_270) yLoc+=1;
            coordinates.add(new DoublePair(xLoc, yLoc));
            coordinates.add(new DoublePair(xLoc+1, yLoc));
        }
        return coordinates;
    }
   
}

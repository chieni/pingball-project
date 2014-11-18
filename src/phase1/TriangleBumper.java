package phase1;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Geometry.DoublePair;
import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.Vect;
import physics.LineSegment;


/** 
 * Immutable gadget representing a square bumper of side-length 1. 
 * This bumper reflects any balls that collide with it with a reflection
 * coefficient of 1.0. Orientation must be 0, 90, 180 or 270
 * 
 */
public class TriangleBumper implements Gadget {

    private static final int SIDE_LENGTH = 1;
    private final int xLocation;
    private final int yLocation;
    private final List<LineSegment> lines;
    private final List<Circle> corners;
    private Angle orientation;
    private final List<Gadget> triggeredGadgets;

    
    public TriangleBumper(int x, int y) {
        this.xLocation = x;
        this.yLocation = y;
        this.orientation = Angle.ZERO; 
        this.triggeredGadgets = new ArrayList<Gadget>(); 
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        setupHelper(x, y);
        checkRep();
    }
    
    public TriangleBumper(int x, int y, List<Gadget> triggeredGadgets) {
        this.xLocation = x;
        this.yLocation = y;
        this.orientation = Angle.ZERO;
        this.triggeredGadgets = triggeredGadgets; 
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        setupHelper(x, y);
        checkRep();

    }

    //Precondition: orientation must be 0, 90, 180 or 270
    public TriangleBumper(int x, int y, Angle orientation) {
        this.xLocation = x;
        this.yLocation = y;
        this.orientation = orientation;
        this.triggeredGadgets = new ArrayList<Gadget>(); 
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        setupHelper(x, y);
        checkRep();
    }
    
    public TriangleBumper(int x, int y, Angle orientation, List<Gadget> triggeredGadgets ) {
        this.xLocation = x;
        this.yLocation = y;
        this.orientation = orientation;
        this.triggeredGadgets = triggeredGadgets;
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        setupHelper(x, y);
        checkRep();
    }
    
    /**
     * Checks the representation invariant of the TriangleBumper, namely that the position of the TriangleBumper is in a 
     * reasonable position (within the bounds of the board) and that its orientation is within the specifications 
     * (Angles of degree 0, 90, 180, or 270)
     * 
     */
    public void checkRep() {
        boolean correctRep = (new ArrayList<Angle>(Arrays.asList(Angle.ZERO, Angle.DEG_90,
                Angle.DEG_180, Angle.DEG_270)).contains(this.orientation) && this.xLocation > -1 && this.yLocation > -1 && this.xLocation < 21 && this.yLocation < 21);
        if (!correctRep) {
            throw new RuntimeException("TriangleBumper is not initialized correctly.");
        }
    }
    
    private void setupHelper(int x, int y) {
        if (orientation.equals(Angle.ZERO)){  //   |-/
            lines.add(new LineSegment(x, y, x+SIDE_LENGTH, y));
            lines.add(new LineSegment(x, y, x, y+1));
            lines.add(new LineSegment(x, y+SIDE_LENGTH, x+SIDE_LENGTH, y));
            corners.add(new Circle(x, y, 0));
            corners.add(new Circle(x, y+SIDE_LENGTH, 0));
            corners.add(new Circle(x+SIDE_LENGTH, y, 0));
        } 
        else if (orientation.equals(Angle.DEG_90)) {  //  \-|
            lines.add(new LineSegment(x, y, x+SIDE_LENGTH, y));
            lines.add(new LineSegment(x+SIDE_LENGTH, y, x+SIDE_LENGTH, y+SIDE_LENGTH));
            lines.add(new LineSegment(x, y, x+SIDE_LENGTH, y+SIDE_LENGTH));
            corners.add(new Circle(x, y, 0));
            corners.add(new Circle(x+SIDE_LENGTH, y, 0));
            corners.add(new Circle(x+SIDE_LENGTH, y+SIDE_LENGTH, 0));   
        }
        else if (orientation.equals(Angle.DEG_180)){ //  /_|
            lines.add(new LineSegment(x+SIDE_LENGTH, y, x+SIDE_LENGTH, y+SIDE_LENGTH));
            lines.add(new LineSegment(x, y+SIDE_LENGTH, x+SIDE_LENGTH, y+SIDE_LENGTH));
            lines.add(new LineSegment(x, y+SIDE_LENGTH, x+SIDE_LENGTH, y));
            corners.add(new Circle(x+SIDE_LENGTH, y, 0));
            corners.add(new Circle(x, y+SIDE_LENGTH, 0));
            corners.add(new Circle(x+SIDE_LENGTH, y+SIDE_LENGTH, 0));
        }
        else { // orientation == 270    |_\
            lines.add(new LineSegment(x, y, x+SIDE_LENGTH, y+SIDE_LENGTH));
            lines.add(new LineSegment(x, y, x, y+SIDE_LENGTH));
            lines.add(new LineSegment(x, y+SIDE_LENGTH, x+SIDE_LENGTH, y+SIDE_LENGTH));
            corners.add(new Circle(x, y, 0));
            corners.add(new Circle(x, y+SIDE_LENGTH, 0));
            corners.add(new Circle(x+SIDE_LENGTH, y+SIDE_LENGTH, 0));
        }
    }
    
    @Override
    public String toString() {
        return "TriangleBumper<Location:("+this.xLocation + ", " + this.yLocation + "), Orientation: " + this.orientation +">";
    }
    
    @Override
    public int hashCode() {
        if (this.orientation.equals(Angle.ZERO)){
            return this.xLocation + this.yLocation + 0 + this.triggeredGadgets.hashCode();
        }
        else if (this.orientation.equals(Angle.DEG_90)){
            return this.xLocation + this.yLocation + 90 + this.triggeredGadgets.hashCode();
        }
        else if (this.orientation.equals(Angle.DEG_180)){
            return this.xLocation + this.yLocation + 180 + this.triggeredGadgets.hashCode();
        }
        else{
            return this.xLocation + this.yLocation + 270 + this.triggeredGadgets.hashCode();
        }
         
    }
    
    @Override 
    public boolean equals(Object other) {
        if (other instanceof TriangleBumper) {
            if (((TriangleBumper) other).getLocation().equals(new DoublePair(this.xLocation, this.yLocation))){
                if (((TriangleBumper) other).getOrientation().equals(this.orientation)) {
                    if (((TriangleBumper) other).getTriggeredGadgets().equals(this.triggeredGadgets)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * @return the orientation of this TriangleBumper
     */
    public Angle getOrientation() {
        return this.orientation;
    }
    
    /**
     * @return the location of this TriangeBumper as a DoublePair
     */
    public DoublePair getLocation() {
        return new DoublePair(this.xLocation, this.yLocation);
    }
    
    /**
     * @return the list of Gadgets that this bumper triggers
     * do not mutate the objects in the list
     */
    public List<Gadget> getTriggeredGadgets() {
        return new ArrayList<Gadget>(this.triggeredGadgets);
    }
    
    /**
     * Determines whether this gadget represents a ball
     * @return true if it is a ball, otherwise false
     */
    @Override
    public boolean isBall() {
        return false;
    }
    
    /**
     * Used to determine time until a collision between a given ball and this gadget.
     * The return value should adhere to what the physics library methods would return.
     * 
     * @param ball the ball for which the time until this ball collides with 
     * this gadget is calculated
     * @return the time in seconds until the given ball collides with this gadget
     */
    @Override
    public double getTimeUntilCollision(Ball ball) {
        DoublePair minTimeUntilLineSegmentCollisionInfo = minTimeUntilLineSegmentHit(ball);
        DoublePair minTimeUntilCornerCollisionInfo = minTimeUntilCornerHit(ball);
        return Math.min(minTimeUntilLineSegmentCollisionInfo.d2, minTimeUntilCornerCollisionInfo.d2);
    }
    
    private DoublePair minTimeUntilLineSegmentHit(Ball ball) {
        double minTimeUntilCollisionLineSegment = Geometry.timeUntilWallCollision(lines.get(0), ball.getShape(), ball.getVelocity()); 
        int indexForLineWithMinTime = 0;
        for (int i=1; i<lines.size(); i++) {
            double timeUntilCollisionCurrent = Geometry.timeUntilWallCollision(lines.get(i), ball.getShape(), ball.getVelocity());
            if (timeUntilCollisionCurrent < minTimeUntilCollisionLineSegment) {
                minTimeUntilCollisionLineSegment = timeUntilCollisionCurrent;
                indexForLineWithMinTime = i;
            }
        }
        return new DoublePair(indexForLineWithMinTime, minTimeUntilCollisionLineSegment);
    }
    
    private DoublePair minTimeUntilCornerHit(Ball ball) {
        double minTimeUntilCollisionCorner = Geometry.timeUntilCircleCollision(corners.get(0), ball.getShape(), ball.getVelocity());
        int indexForCornerWithMinTime = 0;
        for (int i=1; i<corners.size(); i++) {
            double timeUntilCollisionCurrent = Geometry.timeUntilCircleCollision(corners.get(i), ball.getShape(), ball.getVelocity());
            if (timeUntilCollisionCurrent < minTimeUntilCollisionCorner) {
                minTimeUntilCollisionCorner = timeUntilCollisionCurrent;
                indexForCornerWithMinTime = i;
            }
        }
        return new DoublePair(indexForCornerWithMinTime, minTimeUntilCollisionCorner);
    }
    
    /**
     * Compute the new velocity of the given ball once it reflects off this gadget.
     * The return value should adhere to what the physics library methods would return,
     * and ignores friction.
     * 
     * @param ball the which will be reflecting off this gadget 
     * @return the new velocity of the given ball once it "reflects" off this wall
     */
    @Override
    public Vect getReflectionVector(Ball ball) {
        DoublePair minTimeUntilLineSegmentCollisionInfo = minTimeUntilLineSegmentHit(ball);
        DoublePair minTimeUntilCornerCollisionInfo = minTimeUntilCornerHit(ball);
        triggerGadgets();
        if (minTimeUntilLineSegmentCollisionInfo.d2 < minTimeUntilCornerCollisionInfo.d2) {
            return Geometry.reflectWall(lines.get((int)minTimeUntilLineSegmentCollisionInfo.d1), ball.getVelocity());
        }
        else {
            return Geometry.reflectCircle(corners.get((int)minTimeUntilCornerCollisionInfo.d1).getCenter(),
                                                            ball.getShape().getCenter(), ball.getVelocity());
        }
    }
    
    /**
     * Provides the coordinates that this gadget should occupy in the console display.
     * These coordinates are always the position of the gadget rounded up.
     * 
     * @return A list of DoublePair objects with the locations as doubles of grid locations
     * that contain any part of this gadget
     */
    @Override
    public List<DoublePair> toConsoleCoordinates() {
        List<DoublePair> consoleCoords = new ArrayList<DoublePair>();
        double x = Math.ceil(this.xLocation)+1;
        double y = Math.ceil(this.yLocation)+1;
        if (x > 20) x = 20; 
        if (y > 20) y = 20; 
        consoleCoords.add(new DoublePair(x, y));
        return consoleCoords;
    }

    /**
     * @return a String representing the console print format of this gadget
     */
    @Override
    public String getSymbol() {
        if (orientation.equals(Angle.ZERO) || orientation.equals(Angle.DEG_180)){
            return "/";
        }
        else {
            return "\\";
        }
    }
    
    /**
     * Calls gadgetAction() for each Gadget in it's triggeredGadgets list. 
     */
    @Override
    public void triggerGadgets() {
        for (Gadget gadget : this.triggeredGadgets) {
            gadget.gadgetAction();
        }
    }
    
    /**
     * Changes the position of this gadget based on the parameter time
     * 
     * @param time the time interval that has passed based off which the position
     * of this gadget should be changed.
     */
    @Override
    public void changePosition(double time) {
        //Can't be changed        
    }
    
    /**
     * Called when a gadget is "triggered" and causes this gadget to perform its
     * appropriate action
     */
    @Override
    public void gadgetAction() {
        //None
    }
    
    /**
     * Creates a triggee gadget relationship. Whenever this gadget is hit, triggee
     * becomes triggered
     * @param triggee the trigee gadget
     */
    @Override
    public void addTrigger(Gadget triggee) {
        this.triggeredGadgets.add(triggee);        
    }
    
    /**
     * Makes this gadget be a trigger for itself
     * 
     */
    @Override
    public void makeSelfTriggering() {
        
        
    }    
    
}

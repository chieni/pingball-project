package phase1;

import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.Geometry.DoublePair;
import physics.Geometry;
import physics.Vect;
import physics.LineSegment;


/** 
 * Immutable gadget representing a square bumper of side-length 1. 
 * This bumper reflects any balls that collide with it with a reflection
 * coefficient of 1.0. 
 * 
 */

public class SquareBumper implements Gadget {

    private static final int SIDE_LENGTH = 1;
    private final int xLocation;
    private final int yLocation;
    private final List<LineSegment> lines;
    private final List<Circle> corners;
    private final List<Gadget> triggeredGadgets;
    /**
     * SquareBumper constructor. Creates a square bumper of width and height 1L.
     * Stores the square's edges as LineSegments and its corners as Circles
     * RI: ball cannot be on top of bumper or pass thru bumper
     * @param origin the top-left corner of the square
     */
    
    public SquareBumper(int x, int y) {
        this.xLocation = x;
        this.yLocation = y;
        lines = new ArrayList<LineSegment>(); 
        this.triggeredGadgets = new ArrayList<Gadget>(); 
        corners = new ArrayList<Circle>();
        setupHelper(x,y);  
    }
    
    public SquareBumper(int x, int y, List<Gadget> triggeredGadgets) {
        this.xLocation = x;
        this.yLocation = y;
        lines = new ArrayList<LineSegment>(); 
        this.triggeredGadgets = triggeredGadgets; 
        corners = new ArrayList<Circle>();
        setupHelper(x,y);
    }
    
    /**
     * Checks the representation invariant of the SquareBumper, namely that the position of the SquareBumper is in a 
     * reasonable position (within the bounds of the board)
     * 
     */
    public void checkRep() {
        boolean correctRep = (this.xLocation > -1 && this.yLocation > -1 && this.xLocation < 21 && this.yLocation < 21);
        if (!correctRep) {
            throw new RuntimeException("SquareBumper is not initialized correctly.");
        }
    }
    
    
    private void setupHelper(int x, int y) {
        lines.add(new LineSegment(x, y, x+SIDE_LENGTH, y));
        lines.add(new LineSegment(x, y, x, y+SIDE_LENGTH));
        lines.add(new LineSegment(x+SIDE_LENGTH, y, x+SIDE_LENGTH, y+SIDE_LENGTH));
        lines.add(new LineSegment(x, y+SIDE_LENGTH, x+SIDE_LENGTH, y+SIDE_LENGTH));
        corners.add(new Circle(x, y, 0));
        corners.add(new Circle(x, y+SIDE_LENGTH, 0));
        corners.add(new Circle(x+SIDE_LENGTH, y, 0));
        corners.add(new Circle(x+SIDE_LENGTH, y+SIDE_LENGTH, 0));
    }
    
    @Override
    public String toString() {
        return "SquareBumper<Location("+this.xLocation+ ", " + this.yLocation +")>";
    }
    
    @Override
    public int hashCode() {
        return this.xLocation + this.yLocation + this.triggeredGadgets.hashCode(); 
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof SquareBumper) {
            if (((SquareBumper) other).getLocation().equals(new DoublePair(this.xLocation, this.yLocation))) {
                if (((SquareBumper) other).getTriggeredGadgets().equals(this.triggeredGadgets)) {
                    return true;
                }
            }
        }
        return false;
    }
    
   /**
    * @return the location of this SquareBumper
    */
    public DoublePair getLocation(){
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
        return "#";
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

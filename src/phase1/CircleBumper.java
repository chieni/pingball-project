package phase1;

import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.Geometry.DoublePair;
import physics.Vect;

/** 
 * Immutable gadget representing a square bumper of side-length 1. 
 * This bumper reflects any balls that collide with it with a reflection
 * coefficient of 1.0. 
 * 
 */
public class CircleBumper implements Gadget {

    private static final double RADIUS = 0.5; 
    private final int xLocation;
    private final int yLocation;
    private final Circle shape; 
    private final List<Gadget> triggeredGadgets;

    /**
     * CircleBumper constructor. Creates a circle bumper of radius 0.5L for a diameter of 1L.
     * circle bumpers have exactly one "corner," which is the entire circle bumper
     * RI: ball cannot be on top of bumper or pass thru bumper
     * @param position the center of the circle
     */
    public CircleBumper(int x, int y) {
        this.xLocation = x;
        this.yLocation = y;
        shape = new Circle(x+RADIUS, y+RADIUS, RADIUS);
        this.triggeredGadgets = new ArrayList<Gadget>();
        
        checkRep();
    }
    
    public CircleBumper(int x, int y, List<Gadget> triggeredGadgets) {
        this.xLocation = x;
        this.yLocation = y;
        shape = new Circle(x+RADIUS, y+RADIUS, RADIUS);
        this.triggeredGadgets = triggeredGadgets;
        
        checkRep();
    }
    
    /**
     * Checks the representation invariant of the CircleBumper, namely that the position of the CircleBumper is in a 
     * reasonable position (within the bounds of the board)
     * 
     */
    public void checkRep() {
        boolean correctRep = (this.xLocation > -1 && this.yLocation > -1 && this.xLocation < 21 && this.yLocation < 21);
        if (!correctRep) {
            throw new RuntimeException("CircleBumper is not initialized correctly.");
        }
    }
    
    @Override
    public String toString() {
        return "CircleBumper<Location("+this.xLocation+ ", " + this.yLocation +")>";
    }
    
    @Override
    public int hashCode() {
        return this.xLocation + this.yLocation + this.triggeredGadgets.hashCode(); 
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof CircleBumper) {
            if (((CircleBumper) other).getLocation().equals(new DoublePair(this.xLocation, this.yLocation))) {
                if (((CircleBumper) other).getTriggeredGadgets().equals(this.triggeredGadgets)) {
                    return true;
                }
            }
        }
        return false;
    }
    
   /**
    * @return the location of this CircleBumper
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
        return Geometry.timeUntilCircleCollision(this.shape, ball.getShape(), ball.getVelocity());
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
        triggerGadgets();
        return Geometry.reflectCircle(this.shape.getCenter(), ball.getShape().getCenter(), ball.getVelocity());
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
        return "O";
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
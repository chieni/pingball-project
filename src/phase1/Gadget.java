package phase1;

import java.util.List;

import physics.Geometry;
import physics.Vect;

/**
 * A mutable structured gadget. 
 * 
 * Gadgets consist of Balls, Bumpers (Square, Circle, Triangle, Walls), Flippers, and Absorbers.
 * Gadgets cannot occupy the same locations. All gadgets have a trigger and an action, actions 
 * may be to do nothing. All gadgets also have coefficients of reflection and somehow interact
 * with a ball. 
 * 
 */
public interface Gadget {
        
    /**
     * Determines whether this gadget represents a ball
     * @return true if it is a ball, otherwise false
     */
    public boolean isBall();
    
    /**
     * Changes the position of this gadget based on the parameter time
     * 
     * @param time the time interval that has passed based off which the position
     * of this gadget should be changed.
     */
    public void changePosition(double time);
    
    /**
     * Used to determine time until a collision between a given ball and this gadget.
     * The return value should adhere to what the physics library methods would return.
     * 
     * @param ball the ball for which the time until this ball collides with 
     * this gadget is calculated
     * @return the time in seconds until the given ball collides with this gadget
     */
    public double getTimeUntilCollision(Ball ball);
    
    /**
     * Compute the new velocity of the given ball once it reflects off this gadget.
     * The return value should adhere to what the physics library methods would return,
     * and ignores friction.
     * 
     * @param ball the which will be reflecting off this gadget 
     * @return the new velocity of the given ball once it "reflects" off this wall
     */
    public Vect getReflectionVector(Ball ball);
    
    /**
     * Calls gadgetAction() for each Gadget in it's triggeredGadgets list. 
     */
    public void triggerGadgets();
    
    /**
     * Creates a triggee gadget relationship. Whenever this gadget is hit, triggee
     * becomes triggered
     * @param triggee the trigee gadget
     */
    public void addTrigger(Gadget triggee);
    
    /**
     * Makes this gadget be a trigger for itself
     * 
     */
    public void makeSelfTriggering();
    
    /**
     * Provides the coordinates that this gadget should occupy in the console display.
     * These coordinates are always the position of the gadget rounded up.
     * 
     * @return A list of DoublePair objects with the locations as doubles of grid locations
     * that contain any part of this gadget
     */
    public List<Geometry.DoublePair> toConsoleCoordinates();
    
    /**
     * Called when a gadget is "triggered" and causes this gadget to perform its
     * appropriate action
     */
    public void gadgetAction(); 
    
    /**
     * @return a String representing the console print format of this gadget
     */
    public String getSymbol();
    
}

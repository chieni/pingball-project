package phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import physics.Geometry.DoublePair;
import physics.Angle;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
/**
 * Size and shape: A generally-rectangular rotating shape with bounding box of size 2L × 2L
 * Orientation: For a left flipper, the default orientation (0 degrees) places the flipper’s pivot point in the northwest corner.
 * For a right flipper, the default orientation puts the pivot point in the northeast corner.
 * Coefficient of reflection: 0.95 
 * Trigger: generated whenever the ball hits it
 * Action: rotates 90 degrees, as described below
 * 
 *  A left flipper begins its rotation in a counterclockwise direction
 *  A right flipper begins its rotation in a clockwise direction.
 *  
 *  When a flipper is first triggered, it sweeps 90 degrees in the direction indicated by the arrows. 
 *  If triggered again, the flipper sweeps back 90 degrees to the initial position
 *  
 *  When a flipper’s action is triggered, the flipper rotates at a constant angular velocity of 
 *  1080 degrees per second to a position 90 degrees away from its starting position. 
 *  When its action is triggered a second time, the flipper rotates back to its original position 
 *  at an angular velocity of 1080 degrees per second.
 *  
 *  RI: flipper must be bounded by a 2x2 box and can only rotate within a 90 degree sweep.
     Valid orientations are 0, 90, 180, and 360.
 */
public class Flipper implements Gadget {
    protected final double COEFF_REFLECTION = 0.95;
    protected final double ANGULAR_VELOCITY = 1080;
    protected static final double ERROR_TOLERANCE = 0.05;
    private static final int FLIPPER_SPEED = 2;


    protected final List<Gadget> triggeredGadgets;
    protected final Vect center;
    protected Angle orientation;
    protected Angle currentAngle;
    protected LineSegment flipper;
    protected Circle pivot;
    protected Circle endpoint;
    

    protected boolean isVerticle;
    protected double velocity;
    protected double x;
    protected double y;
    private boolean flipForward;

    public Flipper(double xloc, double yloc, Angle orientation, List<Gadget> triggeredGadgets) {
        this.flipForward = true;
        this.center = new Vect(xloc, yloc);
        this.orientation = orientation;
        this.currentAngle = orientation.minus(Angle.DEG_90);
        this.triggeredGadgets = triggeredGadgets;
        this.setVelocity(0);
        
        checkRep();
    }
    
    /**
     * Checks the representation invariant of the Flipper, namely that the position of the Flipper is in a 
     * reasonable position (within the bounds of the board) and that its orientation is within the specifications 
     * (Angles of degree 0, 90, 180, or 270)
     * 
     */
    public void checkRep() {
        boolean correctRep = (new ArrayList<Angle>(Arrays.asList(Angle.ZERO, Angle.DEG_90,
                Angle.DEG_180, Angle.DEG_270)).contains(this.orientation) && this.center.x() > -1 && this.center.y() > -1 && this.center.x() < 21 && this.center.y() < 21);
        if (!correctRep) {
            throw new RuntimeException("Flipper is not initialized correctly.");
        }
    }
    
    /**
     * Sets the velocity of the flipper to new velocity
     * @param velocity: the new velocity for the flipper
     */
    protected void setVelocity(double velocity){
        this.velocity=velocity;
    }
    /**
     * @return a double representing the current velocity of the flipper
     */      
    protected double getVelocity(){
        return this.velocity;
    }

    
    @Override
    public boolean isBall() {
        return false;
    }


    /**
     * Flips left flipper counterclockwise if it's in its starting position, or clockwise back to its
     *   starting position. Flips right flipper clockwise if it's in its starting position, or
     *   counterclockwise back to its starting position.
     */
    @Override
    public void gadgetAction() {
        if (flipForward){
            setVelocity(-6*Math.PI);
        }
        else{
            setVelocity(6*Math.PI);
        }
        flipForward = !flipForward;
    }

    @Override
    public void changePosition(double time) {
      //System.out.println(this.shape);
        if (getVelocity()!=0 && isVerticle){
            if (this.flipper.p1().y() > this.flipper.p2().y() - ERROR_TOLERANCE &&  this.flipper.p1().y() < this.flipper.p2().y() + ERROR_TOLERANCE){
                setVelocity(0);
                this.flipper= new LineSegment(Math.round(this.flipper.p1().x()), Math.round(this.flipper.p1().y()),
                                                        Math.round(this.flipper.p2().x()), Math.round(this.flipper.p2().y()));
                isVerticle=false;
            }
            else {
                this.flipper= Geometry.rotateAround(this.flipper, new Vect(x,y), new Angle(getVelocity()*time*FLIPPER_SPEED));                
            }
        }
        else if (getVelocity()!=0 && !isVerticle){
            if (this.flipper.p1().x() > this.flipper.p2().x() - ERROR_TOLERANCE && this.flipper.p1().x() < this.flipper.p2().x() + ERROR_TOLERANCE){
                setVelocity(0);
                this.flipper= new LineSegment(Math.round(this.flipper.p1().x()), Math.round(this.flipper.p1().y()),
                        Math.round(this.flipper.p2().x()), Math.round(this.flipper.p2().y()));
                isVerticle=true;
            }
            else {
                this.flipper= Geometry.rotateAround(this.flipper, new Vect(x,y), new Angle(getVelocity()*time*FLIPPER_SPEED));
            }
        }  
        this.endpoint = new Circle(this.flipper.p2().x(), this.flipper.p2().y(),0);
    }

    @Override
    public double getTimeUntilCollision(Ball ball) {
        double wallTime = Geometry.timeUntilWallCollision(this.flipper,
                ball.getShape(), ball.getVelocity());
        double centerTime = Geometry.timeUntilCircleCollision(this.pivot, ball.getShape(),
                ball.getVelocity());
        double endpointTime = Geometry.timeUntilCircleCollision(this.endpoint, ball.getShape(),
                ball.getVelocity());
        return Math.min(wallTime, Math.min(centerTime, endpointTime)); 
    }

    @Override
    public Vect getReflectionVector(Ball ball) {
        // note that collision with end points will send the ball back in its original direction
        // collisions with the wall itself will send the ball in a near-perpendicular direction
        // (not perfectly perpendicular due to effects of gravity and friction)
        
        double wallTime = Geometry.timeUntilWallCollision(this.flipper,
                ball.getShape(), ball.getVelocity());
        double centerTime = Geometry.timeUntilCircleCollision(this.pivot, ball.getShape(),
                ball.getVelocity());
        double endpointTime = Geometry.timeUntilCircleCollision(this.endpoint, ball.getShape(),
                ball.getVelocity());
        
        triggerGadgets();
        Vect returnVect;

        if (centerTime < Math.min(wallTime, endpointTime)) {
            returnVect= Geometry.reflectCircle(this.pivot.getCenter(),
                    ball.getCenter(), ball.getVelocity(), COEFF_REFLECTION);
        } else if (endpointTime < Math.min(wallTime, centerTime)) {
            returnVect= Geometry.reflectCircle(this.endpoint.getCenter(),
                    ball.getCenter(), ball.getVelocity(), COEFF_REFLECTION);
        } else { // WallTime is the smallest
            returnVect= Geometry.reflectWall(this.flipper,
                ball.getVelocity(), COEFF_REFLECTION);
        }
        this.gadgetAction();
        
        return returnVect;
    }

    @Override
    public void triggerGadgets() {
        for (Gadget trigger: triggeredGadgets){
            trigger.gadgetAction();
        }
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
    
    @Override
    public void makeSelfTriggering() {

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


    @Override
    public String getSymbol() {
        if (!isVerticle){
            return "-";
        }else{return "|";}
    }
    
    @Override
    public boolean equals(Object other){
        if (other instanceof LeftFlipper ){
            LeftFlipper myother = (LeftFlipper) other;
            if (myother.flipper.equals(this.flipper) && myother.orientation==this.orientation && myother.triggeredGadgets.equals(this.triggeredGadgets)){
                return true;
            }
        }
        if (other instanceof RightFlipper ){
            RightFlipper myother = (RightFlipper) other;
            if (myother.flipper.equals(this.flipper) && myother.orientation==this.orientation && myother.triggeredGadgets.equals(this.triggeredGadgets)){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return flipper.hashCode()+orientation.hashCode()+triggeredGadgets.hashCode();
    }

}

package phase1;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import phase1.Wall.WallDescription;
import physics.Circle;
import physics.Geometry;
import physics.Geometry.VectPair;
import physics.Vect;
/**
 * The ball by default must have a diameter of approximately 0.5L. Ball velocities must range 
 * at least from 0.01 L/sec to 200 L/sec and can cover a larger range if you wish. 0 L/sec (stationary) 
 * must also be supported.
 * 
 * The ball should interact reasonably with the playing area. That is, the ball should 
 * bounce in the direction and with the resulting velocity that you would expect it to bounce in a physical pinball game.
 * 
 * The velocity of the ball should continually change to account 
 * for the effects of gravity. The default gravity value should be 25 L/sec2, which resembles a 
 * pinball game with a slightly tilted playing surface, but your Pingball boards should be able to be configured 
 * with a different value for gravity.
 * 
 * The ball velocity should also continually change to account for the effects of friction. You should model 
 * friction by scaling the velocity of the ball using the frictional constants mu and mu2. For sufficiently small deltat’s 
 * you can model friction as Vnew = Vold × ( 1 - mu × deltat - mu2 × |Vold| × deltat ). The default value of mu should be 
 * 0.025 per second. The default value of mu2 should be 0.025 per L. Pingball boards should be able to be configured with 
 * different values for mu and mu2.
 * 
 * The ball is represented by an asterisk.
 */
public class Ball implements Gadget {
    
    public Circle ball;     //represents the physical ball 
    private Vect velocity;  //direction of ball, and length of vector is speed
    private static final double RADIUS=0.25;
    private final List<Gadget> triggeredGadgets;
    private double x;
    private double y;
    public String ballTrajectoryMessage;

    public Ball(Vect velocity, double x, double y) {
        this.x=x;
        this.y=y;
        this.velocity = velocity; 
        this.ball = new Circle(x, y, RADIUS);
        this.triggeredGadgets = new ArrayList<Gadget>();
        this.ballTrajectoryMessage="";
        
        checkRep();
    }

    /**
     * Checks the representation invariant of the Ball, namely that the velocity and position of the ball is are in a 
     * reasonable position (within the bounds of the board)
     * 
     */
    public void checkRep() {
        boolean correctRep = (this.x > -1 && this.y > -1 && this.x < 21 && this.y < 21 && this.velocity != null);
        if (!correctRep) {
            throw new RuntimeException("Ball is not initialized correctly.");
        }
    }
    
    /**
     * Update the ball's location for the board it's going to depending on whether the wall 
     * is a top, bottom, left, or right wall. Also makes a call to board.removeBallfromBoard().
     * @return <Ball, String> key, value pair of ball: the name of the board that this ball is going to
     */
    public void hitInvisibleWall(WallDescription wallDescription, String connectedWallName){
        int xLoc;
        int yLoc;
        if (wallDescription==WallDescription.TOP){
            xLoc=(int) this.ball.getCenter().x();
            yLoc=19;
        }
        else if (wallDescription==WallDescription.BOTTOM){
            xLoc=(int) this.ball.getCenter().x();
            yLoc=1;
        }
        else if  (wallDescription==WallDescription.RIGHT){
            xLoc=1;
            yLoc= (int) this.ball.getCenter().y();
        }
        else{
            xLoc=19;
            yLoc= (int) this.ball.getCenter().y();
        }
        ballTrajectoryMessage= "--board " + connectedWallName+ " --velocity " + (int) this.velocity.x()+","+(int) this.velocity.y()+ " --position "+ xLoc+","+yLoc;
    }
    
    /**
     * @return a message of where the ball needs to be reallocated if it hits an invisible wall
     */
    public String getBallTrajectoryMessage(){
        return this.ballTrajectoryMessage;
    }
    @Override
    public String toString() {
        return "Ball: \\--x "+ this.x + " --y "+ this.y + " --velocity " + velocity.toString();
    }

    /**
     * @return a String representing the console print format of this gadget
     */
    @Override
    public String getSymbol() {
        return "*";
    }
    
    /**
     * Determines whether this gadget represents a ball
     * @return true if it is a ball, otherwise false
     */
    @Override
    public boolean isBall() {
        return true; 
    }
    
    @Override
    public boolean equals(Object other) {
        Gadget otherball=(Gadget) other;
        if ((otherball).isBall()){
            Ball otherball2= (Ball) otherball;
            if (Math.abs(this.getCenter().x()-otherball2.getCenter().x())<.01 && Math.abs(this.getCenter().y()-otherball2.getCenter().y())<.01){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return (int) (this.getCenter().x()+this.getCenter().y());
    }
    
    /**
     * Changes the velocity of this ball
     * 
     * @param newVelocity the new velocity of this ball
     */
    public void setVelocity(Vect newVelocity) {
        velocity = newVelocity;
    }
    
    /**
     * Changes the velocity of this gadget accounting for the 
     * acceleration due to gravity conforming to this formula:
     * newVelocity = oldVelocity + gravity * time, where gravity > 0
     * Default value of gravity is 25L/sec^2
     * 
     * @param time interval of time over which acceleration occurred
     */
    public void accelerateDueToGravity(double time, double gravity) {
        this.setVelocity(this.getVelocity().plus(new Vect(0,gravity*time)));
    }
    
    /**
     * Changes the position of this ball
     * @param x is new x value
     * @param y is new y value
     */
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.ball = new Circle(x, y, RADIUS);
    }
    
    /**
     * Changes the position of this ball
     * 
     * @param time the interval in seconds for which the change 
     * in position occurs
     */
     public void changePosition(double time) {
        double xDistance = (velocity.x()*time);
        double yDistance = (velocity.y() *time);
        this.ball = new Circle(this.ball.getCenter().x()+xDistance, 
                this.ball.getCenter().y()+yDistance, RADIUS);
        this.x = this.ball.getCenter().x()+xDistance;
        this.y = this.ball.getCenter().y()+yDistance;
    }

    /**
     * 
     * @return a Vect representing the velocity of this ball
     */
    public Vect getVelocity() {
        return velocity;
    }

    /**
     * 
     * @return a Circle which represents this ball 
     */
    public Circle getShape() {
        return ball; 
    }
    
    /**
     * @return the center location/coordinates of of the ball
     */
    public Vect getCenter() {
        return ball.getCenter();
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
    public double getTimeUntilCollision(Ball otherBall) {
        return Geometry.timeUntilBallBallCollision(this.getShape(), this.getVelocity(), otherBall.getShape(), otherBall.getVelocity());
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
        VectPair vects=Geometry.reflectBalls(this.getShape().getCenter(), 1, this.getVelocity(), ball.getShape().getCenter(), 1, ball.getVelocity());
        return vects.v2;
    }
    
    /**
     * Provides the coordinates that this gadget should occupy in the console display.
     * These coordinates are always the position of the gadget rounded up.
     * 
     * @return A list of DoublePair objects with the locations as doubles of grid locations
     * that contain any part of this gadget
     */
    @Override
    public List<Geometry.DoublePair> toConsoleCoordinates() {
        List<Geometry.DoublePair> locations = new ArrayList<Geometry.DoublePair>();
        int x=(int) Math.ceil(ball.getCenter().x())+1;
        if (x > 20) x=20;
        if (x< 0){
            x=0;
            this.ball=new Circle(0, this.getCenter().y(), RADIUS);
            double newVelocity=-1*this.velocity.x();
            this.velocity=new Vect(newVelocity, this.velocity.y());
            this.x=0;
        }

        
        int y=(int) Math.ceil(ball.getCenter().y())+1;
        if (y>20) y=20;
        if (y<0){
            y=0;
            this.ball=new Circle(this.getCenter().x(), 0, RADIUS);
            double newVelocity=-1*this.velocity.y();
            this.velocity=new Vect(this.velocity.x(), newVelocity);
            this.y=0;
        }

        Geometry.DoublePair position = new Geometry.DoublePair(x,y);
        locations.add(position);

        return locations;
    }
    
    /**
     * Calls gadgetAction() for each Gadget in it's triggeredGadgets list. 
     */
    @Override
    public void triggerGadgets() {
        //None
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
        //None
    }

}

package phase1;

import java.util.ArrayList;
import java.util.List;

import physics.Geometry.DoublePair;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

/**
 * AF: An absorber simulates the ball-return mechanism in a pinball game. 
 * When a ball hits an absorber, the absorber stops the ball and holds it 
 * (unmoving) in the bottom right-hand corner of the absorber. The ball’s
 *  center is .25L from the bottom of the absorber and .25L from the right
 *   side of the absorber.

 * If the absorber is holding a ball, then the action of an absorber, when 
 * it is triggered, is to shoot the ball straight upwards in the direction 
 * of the top of the playing area. By default, the initial velocity of the 
 * ball should be 50L/sec. With the default gravity and the default values 
 * for friction, the value of 50L/sec gives the ball enough energy to lightly 
 * collide with the top wall, if the bottom of the absorber is at y=20L. 
 * If the absorber is not holding the ball, or if the previously ejected ball 
 * has not yet left the absorber, then the absorber takes no action when it 
 * receives a trigger signal.

 * An absorber can be made self-triggering by connecting its trigger to its own action. 
 * When the ball hits a self-triggering absorber, it should be moved to the bottom 
 * right-hand corner as described above, and then be shot upward as described above. 
 * There may or may not be a short delay between these events, at your discretion.
 * 
 * Specs: @param: position: Vect position of the top left component of the Absorber
 * RI: 0 < width <= 20, 0 < height <=20
 *      coefficient of reflection = N/A
 *      orientation = N/A
 *      triggered by itself or by the ball
 *  
 */
public class Absorber implements Gadget{
    private final int WIDTH;
    private final int HEIGHT; 
    private final int xLocation;
    private final int yLocation;
    private final List<LineSegment> lines; 
    private final List<Circle> corners;
    private final List<Gadget> triggeredGadgets;
    private Ball ball; 
    private boolean hasBall;
    private boolean isSelfTriggering=false;
    
    public Absorber(int x, int y, int k, int m) {
        this.xLocation = x;
        this.yLocation = y;
        this.WIDTH=k;
        this.HEIGHT=m;
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        this.triggeredGadgets = new ArrayList<Gadget>(); 
        setupHelper(x,y);
        hasBall = false; 
        checkRep();
    }
    
    public Absorber(int x, int y, int k, int m, List<Gadget> triggeredGadgets) {
        this.xLocation = x;
        this.yLocation = y;
        this.WIDTH=k;
        this.HEIGHT=m;
        lines = new ArrayList<LineSegment>(); 
        corners = new ArrayList<Circle>();
        this.triggeredGadgets = triggeredGadgets;
        setupHelper(x,y);
        hasBall = false; 
        checkRep();
    }
    
    /**
     * checks the rep invariant of absorber, namely the size
     */
    public void checkRep() {
        boolean correctSize = this.WIDTH > 0 && this.WIDTH <= 20 && this.HEIGHT > 0 && this.HEIGHT <= 20;
        if (!correctSize) {
            throw new RuntimeException("absorber is not the correct size");
        }
    }
    
    private void setupHelper(int x, int y) {
        lines.add(new LineSegment(x, y, x+WIDTH, y));
        lines.add(new LineSegment(x, y, x, y+HEIGHT));
        lines.add(new LineSegment(x+WIDTH, y, x+WIDTH, y+HEIGHT));
        lines.add(new LineSegment(x, y+HEIGHT, x+WIDTH, y+HEIGHT));
        corners.add(new Circle(x, y, 0));
        corners.add(new Circle(x, y+HEIGHT, 0));
        corners.add(new Circle(x+WIDTH, y, 0));
        corners.add(new Circle(x+WIDTH, y+HEIGHT, 0));
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
    /**
     * Finds the line segment in square bumper that is closest to the ball
     * @param ball: the ball that has the potential of colliding with the wall
     * @return the closest linesegment to the ball represented by a DoublePair 
     * composed of the index of the linesegment and the time until collision
     */
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
    /**
     * Finds the corner (circle) in square bumper that is closest to the ball
     * @param ball: the ball that has the potential of colliding with the wall
     * @return the closest corner (circle) to the ball represented by a DoublePair 
     * composed of the index of the circle and the time until collision
     */
    
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
        if (hasBall) {
            this.gadgetAction();
        }
        
        ball.setPosition(this.xLocation + (WIDTH-.25), this.yLocation + (HEIGHT-.25));
        this.ball = ball;
        hasBall = true;
        triggerGadgets();
        
        if (isSelfTriggering) {
            this.gadgetAction();
            return this.ball.getVelocity();
        }
        return new Vect(0,0);
        
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
     * Provides the coordinates that this gadget should occupy in the console display.
     * These coordinates are always the position of the gadget rounded up.
     * 
     * @return A list of DoublePair objects with the locations as doubles of grid locations
     * that contain any part of this gadget
     */
    @Override
    public List<DoublePair> toConsoleCoordinates() {
        List<DoublePair> consoleCoords = new ArrayList<DoublePair>();
        for (int i=0; i<WIDTH; i++) {
            for (int j=0; j<HEIGHT; j++) {
                double x = Math.ceil(this.xLocation)+i+1;
                double y = Math.ceil(this.yLocation)+j+1;
                if (x > 20) x = 20; 
                if (y > 20) y = 20; 
                if (i != WIDTH-1 || j != HEIGHT-1 || !hasBall)
                    consoleCoords.add(new DoublePair(x, y));
            }
        }
        return consoleCoords;
    }

    /**
     * @return a String representing the console print format of this gadget
     */
    @Override
    public String getSymbol() {
        return "=";
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
     * Changes the position of this gadget based on the parameter time
     * 
     * @param time the time interval that has passed based off which the position
     * of this gadget should be changed.
     */
    @Override
    public void changePosition(double time) {
        //can't be changed
    }
    
    /**
     * Called when a gadget is "triggered" and causes this gadget to perform its
     * appropriate action
     */
    @Override
    public void gadgetAction() { 
        this.ball.setPosition(this.ball.getShape().getCenter().x(), this.ball.getShape().getCenter().y() - HEIGHT);
        this.ball.setVelocity(new Vect(0, -50));
        hasBall = false;  
    }
    
    /**
     * Makes this gadget be a trigger for itself
     * 
     */
    @Override
    public void makeSelfTriggering() {
        isSelfTriggering=true;      
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
    public String toString() {
        String text = "Absorber: k = " + WIDTH;
        text += "m = " + HEIGHT;
        text += "at " + xLocation + ", " + yLocation;
        return text;
    }
    
    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Absorber)) {
            return false;
        }
        Absorber thatAbsorber = (Absorber) thatObject;
        return (lines.equals(thatAbsorber.lines) &&
                corners.equals(thatAbsorber.corners) &&
                triggeredGadgets.equals(thatAbsorber.triggeredGadgets)
                );
    }
    
    @Override
    public int hashCode() {
        return lines.hashCode();
    }

}

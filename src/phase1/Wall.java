package phase1;

import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.Geometry.DoublePair;
import physics.Vect;
import physics.LineSegment;


/** 
 * Immutable gadget representing a square bumper of side-length 1. 
 * This bumper reflects any balls that collide with it with a reflection
 * coefficient of 1.0. Either it is horizontal or vertical - it cannot be slanted. 
 * 
 * AF: The border walls surrounding the playfield.

 * A Pingball game supports exactly one set of outer walls, which lie just outside the playing area:

        one horizontal wall just above the y=0L coordinate
        one horizontal wall just below the y=20L coordinate
        one vertical wall just to the left of the x=0L coordinate
        one vertical wall just to the right of the x=20L coordinate
 * Specs: @param position: position of the wall gadget
 * RI: corners are circles, rest of the wall is a line segment
 *      walls are solid (balls reflect off of walls)
 *  
 */
public class Wall implements Gadget {

    private final DoublePair startCoord;
    private final DoublePair endCoord;
    private final LineSegment line; 
    private final Circle corner1;
    private final Circle corner2; 
    private String connectedBoardName;
    private boolean isInvisible;
    private final WallDescription wallDescrip;
    public enum WallDescription{TOP, BOTTOM, LEFT, RIGHT, VERTICLE, HORIZONTAL;
    private WallDescription orientation;
        static {
            TOP.orientation=HORIZONTAL;
            BOTTOM.orientation=HORIZONTAL;
            LEFT.orientation=VERTICLE;
            RIGHT.orientation=VERTICLE;
        }
    }
    private final Board board;
    
    
    /**
     * Precondition: either x1 == x2 or y1 == y2
     * @param x1 the starting x coord
     * @param y1 the starting y cood
     * @param x2 the ending x coord
     * @param y2 the ending y coord
     * @param wallDescription the location of the wall (top, bottom, left, right)
     */
    public Wall(int x1, int y1, int x2, int y2, WallDescription wallDescription, Board board) {
        this.startCoord = new DoublePair(x1, y1);
        this.endCoord = new DoublePair(x2, y2);
        line = new LineSegment(x1, y1, x2, y2);
        corner1 = new Circle(x1, y1, 0); 
        corner2 = new Circle(x2, y2, 0); 
        this.connectedBoardName="";
        this.wallDescrip= wallDescription;
        this.isInvisible=false;
        this.board=board;
    }
    
    public Wall(int x1, int y1, int x2, int y2, WallDescription wallDescription) {
        this.startCoord = new DoublePair(x1, y1);
        this.endCoord = new DoublePair(x2, y2);
        line = new LineSegment(x1, y1, x2, y2);
        corner1 = new Circle(x1, y1, 0); 
        corner2 = new Circle(x2, y2, 0); 
        this.connectedBoardName="";
        this.wallDescrip= wallDescription;
        this.isInvisible=false;
        this.board=null;
    }
    
    /**
     * @return the wall description enum
     */
    public WallDescription getWallDescription(){
        return this.wallDescrip;
    }
    
    /**
     * Updates the wall with the name of the board that its connected to.
     * @param name: Name of the board it's connected to. If not connected 
     * to any board, the name will be "".
     */
    public void setConnectedBoardName(String name){
        this.connectedBoardName=name;
        if (name==""){
            setIsInvisible(false);
        }else{
            setIsInvisible(true);
        }
    }
    
    /**
     * @return name of board wall is connected to. If none, will return "".
     */
    public String getConnectedBoardName(){
        return this.connectedBoardName;
    }
    
    /**
     * Sets the wall to be or not be invisible
     * @param invisible: a boolean stating whether a wall is invisible or not
     */
    public void setIsInvisible(boolean invisible){
        this.isInvisible=invisible;
    }
    
    /**
     * @return true if a wall is invisible, false if it is solid
     */
    public boolean getIsInvisible(){
        return this.isInvisible;
    }
    
    @Override
    public String toString() {
        return connectedBoardName+" Wall<Start"+this.startCoord.toString()+ ", End" + this.endCoord +">";
    }
    
    @Override 
    public int hashCode() {
        return this.startCoord.hashCode() + this.endCoord.hashCode();
    }
   
    @Override
    public boolean equals(Object other) {
        if (other instanceof Wall) {
            if (((Wall) other).getStartCoord().equals(this.startCoord)){
                if(((Wall) other).getEndCoord().equals(this.endCoord)) {
                    return true;
                }
            }
        }   
        return false;
    }
    
    /**
     * @return the start point of this wall
     */
    public DoublePair getStartCoord() {
        return startCoord;
    }
    
    /**
     * @return the start point of this wall
     */
    public DoublePair getEndCoord() {
        return endCoord;
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
        double timeUntilCollisionWithCorner1 = Geometry.timeUntilCircleCollision(this.corner1, ball.getShape(), ball.getVelocity());
        double timeUntilCollisionWithCorner2 = Geometry.timeUntilCircleCollision(this.corner2, ball.getShape(), ball.getVelocity());
        double timeUntilCollisionWithLine = Geometry.timeUntilWallCollision(this.line, ball.getShape(), ball.getVelocity());
        return Math.min(Math.min(timeUntilCollisionWithCorner1, timeUntilCollisionWithCorner2),
                        Math.min(timeUntilCollisionWithCorner2, timeUntilCollisionWithLine));
    }

    private String helperWhichPartOfWallHit(Ball ball) {
        double timeUntilCollisionWithCorner1 = Geometry.timeUntilCircleCollision(this.corner1, ball.getShape(), ball.getVelocity());
        double timeUntilCollisionWithCorner2 = Geometry.timeUntilCircleCollision(this.corner2, ball.getShape(), ball.getVelocity());
        double timeUntilCollisionWithLine = Geometry.timeUntilWallCollision(this.line, ball.getShape(), ball.getVelocity());
        if (timeUntilCollisionWithCorner1 < timeUntilCollisionWithCorner2){
            if (timeUntilCollisionWithCorner1 < timeUntilCollisionWithLine) return "corner1";
        }
        else if (timeUntilCollisionWithCorner2 < timeUntilCollisionWithLine) return "corner2";
        return "line";
    }
    
    /**
     * Compute the new velocity of the given ball once it reflects off this gadget.
     * The return value should adhere to what the physics library methods would return,
     * and ignores friction.
     * 
     * If the wall is invisible, velocity will not change and Ball.hitInvisibleWall(wallLocation, connectedBoardName)
     * is called
     * 
     * @param ball the which will be reflecting off this gadget 
     * @return the new velocity of the given ball once it "reflects" off this wall
     */
    @Override
    public Vect getReflectionVector(Ball ball) {
        if (!isInvisible){
            String partOfWallHit = helperWhichPartOfWallHit(ball);
            Vect reflectionVector;
            if (partOfWallHit == "corner1") {
                reflectionVector = Geometry.reflectCircle(this.corner1.getCenter(), ball.getShape().getCenter(), ball.getVelocity());
            }
            else if (partOfWallHit == "corner2") {
                reflectionVector = Geometry.reflectCircle(this.corner2.getCenter(), ball.getShape().getCenter(), ball.getVelocity());
            }
            else { //ball is hitting the line
                reflectionVector = Geometry.reflectWall(line, ball.getVelocity());
            }
            return reflectionVector;
        }else{
            ball.hitInvisibleWall(this.wallDescrip, this.connectedBoardName);
            //board.removeBallfromBoard(ball);
            Vect reflectionVector= ball.getVelocity();
            return reflectionVector;
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
        List<DoublePair> listOfCoords = new ArrayList<DoublePair>();
        if (wallDescrip.orientation==WallDescription.VERTICLE){
            for (int i= (int) Math.ceil(startCoord.d2); i<=(int) Math.ceil(endCoord.d2); i++){
                listOfCoords.add(new DoublePair((int) Math.ceil(startCoord.d1), i));
            }
        }else{
            for (int i= (int) Math.ceil(startCoord.d1); i<=(int) Math.ceil(endCoord.d1); i++){
                listOfCoords.add(new DoublePair(i, (int) Math.ceil(startCoord.d2)));
            }
        }
        return listOfCoords;
    }
    

    @Override
    public void gadgetAction() {
        //None
    }
    
    /**
     * Creates a string representation of the wall. 
     * 
     * @return a String representing the console print format of this gadget
     */
    @Override
    public String getSymbol() {
        return ".";
    }
    
    /**
     * @return a string of length 21 representing an invisible wall
     */
    public String getStringRepresentationInvsibleWall(){
        int WALL_LENGTH=21;
        String nameRepresentation="";
        if (connectedBoardName.length()>WALL_LENGTH){
            return connectedBoardName.substring(0, WALL_LENGTH);
        }
        int lengthOfName= connectedBoardName.length();
        int leftOverOneSide= (WALL_LENGTH-lengthOfName)/2;
        for (int i=0; i<leftOverOneSide;i++){
            nameRepresentation+=".";
        }
        nameRepresentation+=connectedBoardName;
        while (nameRepresentation.length()<WALL_LENGTH){
            nameRepresentation+=".";
        }
        return nameRepresentation;
    }
    
    /**
     * Calls gadgetAction() for each Gadget in it's triggeredGadgets list. 
     */
    @Override
    public void triggerGadgets() {
        //None
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
     * Creates a triggee gadget relationship. Whenever this gadget is hit, triggee
     * becomes triggered
     * @param triggee the trigee gadget
     */
    @Override
    public void addTrigger(Gadget triggee) {
        //does nothing
    }
    
    /**
     * Makes this gadget be a trigger for itself
     * 
     */
    @Override
    public void makeSelfTriggering() {
        // there are no triggeredGadgets
        
    }
    
}
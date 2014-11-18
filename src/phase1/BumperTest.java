package phase1;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import phase1.Wall.WallDescription;
import physics.Angle;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;
import physics.Geometry;
import physics.Geometry.DoublePair;

public class BumperTest {
    /*
     * Square():
     *  getTimeUntilCollision(Ball ball)
     *      -Path location: not in path, <.05 seconds in front, >.05 seconds in front
     *      -Side (check that 4 balls placed at equal distance from each side return same time)
     *     
     *  getReflectionVector(Ball ball)
     *      -angle (perpendicular, other)
     *      -side (check each side returns the same reflection vector length)
     *      -corner
     *      
     *  toConsoleCoordinates()
     *      -call toConsoleCoordinates(), return correct coordinates
     *      
     *  gadgetAction()
     *      -call gadgetAction()
     *      
     *  getSymbol()
     *      -call getSymbol(), return symbol
     *      
     *  triggerGadgets()
     *      -trigger flipper correctly
     * 
     * Circle():
     * 
     * Triangle():
     *  -toConsoleCoordinates()
     *      -check orientation(default vs 0, output for 0,180 vs 90, 270)
     * 
     * Wall():
     *  ifInvisible:
     *      -getStringRepresentationInvsibleWall (connectedBoardName="Board2" && connectedBoardName="BOARDBOARDBOARDBOARDBOARD")
     *      -getReflectionVector (prpendicular, other)
     * 
     */
    
    @Test public void testCirclegetTimeUntilCollisionNotinPath(){
        Ball myBall= new Ball(new Vect(1,1), 5, 5);    
        assertTrue(Double.isInfinite((new CircleBumper(2,2)).getTimeUntilCollision(myBall)));        
    }
    
    @Test public void testCirclegetTimeUntilCollisionCloseInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 5.75);
        double actualTime= Geometry.timeUntilCircleCollision(new Circle(6.5,6.5, .5), new Circle(6.5, 5.75, .25), new Vect(0,1));
        double predictedTime=(new CircleBumper(6,6)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testCirclegetTimeUntilCollisionFarInPath(){
        Ball myBall= new Ball(new Vect(0,1), 12.5, 1);
        double actualTime= Geometry.timeUntilCircleCollision(new Circle(12.5,12.5,.5), new Circle(12.5, 1,.25), new Vect(0,1));
        double predictedTime=(new CircleBumper(12,12)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
     
    @Test public void testCirclegetTimeUntilCollisionTreatsSidesAlltheSameWay(){
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        double predictedTime1=(new CircleBumper(12,12)).getTimeUntilCollision(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        double predictedTime2=(new CircleBumper(12,12)).getTimeUntilCollision(ball2);
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        double predictedTime3=(new CircleBumper(12,12)).getTimeUntilCollision(ball3);
        Ball ball4= new Ball(new Vect(-1,0), 16, 12.5);
        double predictedTime4=(new CircleBumper(12,12)).getTimeUntilCollision(ball4);
        double epsilon=.0001;
        assertEquals(predictedTime1,predictedTime2, epsilon);
        assertEquals(predictedTime1,predictedTime3, epsilon);
        assertEquals(predictedTime1,predictedTime4, epsilon);      
    }
    
    @Test public void testCirclegetReflectionVectorPredictedvsActual() {
        Ball ball1= new Ball(new Vect(0,1), 11.6, 11.6);
        Vect predictedVector=(new CircleBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectCircle(new Vect(12,12), new Vect(11.6,11.6), new Vect(0,1));
        assertEquals(predictedVector, actualVector);
    }
    
    
    @Test public void testCirclegetReflectionVectorTreatsSidesAlltheSameWay() {
        Ball ball1= new Ball(new Vect(0,-1), 9, 12.5);
        Vect predictedVector1=(new CircleBumper(12,12)).getReflectionVector(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        Vect predictedVector2=(new CircleBumper(12,12)).getReflectionVector(ball2);
        Ball ball3= new Ball(new Vect(1,0), 16, 12.5);
        Vect predictedVector3=(new CircleBumper(12,12)).getReflectionVector(ball3);
        Ball ball4= new Ball(new Vect(-1,0), 12.5, 16);
        Vect predictedVector4=(new CircleBumper(12,12)).getReflectionVector(ball4);
        double epsilon=.0005;
        assertEquals(predictedVector1.length(),predictedVector2.length(),epsilon);
        assertEquals(predictedVector1.length(),predictedVector3.length(),epsilon);
        assertEquals(predictedVector1.length(),predictedVector4.length(),epsilon);      
    }
    
    @Test public void testCircletoConsoleCoordinates() {
        List<DoublePair> coordinates= new ArrayList<DoublePair>();
        coordinates.add(new DoublePair(13,13));  
        assertEquals((new CircleBumper(12,12)).toConsoleCoordinates(), coordinates);
    }
    
    
    @Test public void testCirclegadgetAction() {
        CircleBumper updated=(new CircleBumper(12,12));
        updated.gadgetAction();
        assertEquals(updated, (new CircleBumper(12,12)));
    }
    
    @Test public void testCirclegetSymbol() {
        assertEquals((new CircleBumper(12,12)).getSymbol(), "O");
    }
    
    
    @Test public void testCircleTriggerGadgetsFlipper() {
        LeftFlipper myflipper= new LeftFlipper(12,12);
        LeftFlipper oldflipper= new LeftFlipper(12,12);
        List<Gadget> mygadgets= new ArrayList<Gadget>();
        mygadgets.add(myflipper);
        CircleBumper mycircle= new CircleBumper(10,5, mygadgets);
        mycircle.triggerGadgets();
        myflipper.changePosition(.05);
        oldflipper.changePosition(.05);
        assertFalse(myflipper.equals(oldflipper));
    }
    
    @Test public void testCircleEquals(){
        CircleBumper bumper1= new CircleBumper(12,12);
        CircleBumper bumper2= new CircleBumper(12,12);
        assertEquals(bumper1, bumper2);
    }


    @Test public void testSquaregetTimeUntilCollisionNotinPath(){
        Ball myBall= new Ball(new Vect(1,1), 5, 5);    
        assertTrue(Double.isInfinite((new SquareBumper(2,2)).getTimeUntilCollision(myBall)));        
    }
    
    @Test public void testSquaregetTimeUntilCollisionCloseInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 5.75);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 5.75, .25), new Vect(0,1));
        double predictedTime=(new SquareBumper(6,6)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testSquaregetTimeUntilCollisionFarInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 1);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 1, .25), new Vect(0,1));
        double predictedTime=(new SquareBumper(6,6)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    } 
    
    @Test public void testSquaregetTimeUntilTreatsSidesAlltheSameWay(){
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        double predictedTime1=(new SquareBumper(12,12)).getTimeUntilCollision(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        double predictedTime2=(new SquareBumper(12,12)).getTimeUntilCollision(ball2);
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        double predictedTime3=(new SquareBumper(12,12)).getTimeUntilCollision(ball3);
        Ball ball4= new Ball(new Vect(-1,0), 16, 12.5);
        double predictedTime4=(new SquareBumper(12,12)).getTimeUntilCollision(ball4);
        double epsilon=.0001;
        assertEquals(predictedTime1,predictedTime2, epsilon);
        assertEquals(predictedTime1,predictedTime3, epsilon);
        assertEquals(predictedTime1,predictedTime4, epsilon);    
        
    }
    
    @Test public void testSquaregetReflectionVectorPerpendicularAngle() {
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Vect predictedVector=(new SquareBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(12,12,13,12), new Vect (0,1));
        assertEquals(predictedVector, actualVector);
        
    }
    
    @Test public void testSquaregetReflectionVectorCorner() {
        Ball ball1= new Ball(new Vect(1,1), 11.9, 11.9);
        Vect predictedVector=(new SquareBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectCircle(new Vect (12,12), new Vect(11.9, 11.9), new Vect (1,1));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testSquaregetReflectionVectorOtherAngle() {
        Ball ball1= new Ball(new Vect(4,3), 8.5, 9);
        Vect predictedVector=(new SquareBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(12,12,13,12), new Vect (4,3));
        assertEquals(predictedVector, actualVector);
        
    }
    
    @Test public void testSquaregetReflectionVectorTreatsSidesAlltheSameWay() {
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        Vect predictedVector1=(new SquareBumper(12,12)).getReflectionVector(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        Vect predictedVector2=(new SquareBumper(12,12)).getReflectionVector(ball2);
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        Vect predictedVector3=(new SquareBumper(12,12)).getReflectionVector(ball3);
        Ball ball4= new Ball(new Vect(-1,0), 16, 12.5);
        Vect predictedVector4=(new SquareBumper(12,12)).getReflectionVector(ball4);
        double epsilon=.0005;
        assertEquals(predictedVector1.length(),predictedVector2.length(),epsilon);
        assertEquals(predictedVector1.length(),predictedVector3.length(),epsilon);
        assertEquals(predictedVector1.length(),predictedVector4.length(),epsilon);      
        
    }
    
    @Test public void testSquaretoConsoleCoordinates() {
        List<DoublePair> coordinates= new ArrayList<DoublePair>();
        coordinates.add(new DoublePair(13,13));
        assertEquals((new SquareBumper(12,12)).toConsoleCoordinates(), coordinates);
    }
    
    @Test public void testSquaregadgetAction() {
        SquareBumper updated=(new SquareBumper(12,12));
        updated.gadgetAction();
        assertEquals(updated, (new SquareBumper(12,12)));
    }
    
    @Test public void testSquaregetSymbol() {
        assertEquals((new SquareBumper(12,12)).getSymbol(), "#");
    }
    
    
    @Test public void testSquareTriggerGadgetsFlipper() {
        LeftFlipper myflipper= new LeftFlipper(12,12);
        LeftFlipper oldflipper= new LeftFlipper(12,12);
        List<Gadget> mygadgets= new ArrayList<Gadget>();
        mygadgets.add(myflipper);
        SquareBumper mysquare= new SquareBumper(10,5, mygadgets);
        mysquare.triggerGadgets();
        myflipper.changePosition(.05);
        oldflipper.changePosition(.05);
        assertFalse(myflipper.equals(oldflipper));
    }
    
    @Test public void testSquareEquals(){
        SquareBumper bumper1= new SquareBumper(12,12);
        SquareBumper bumper2= new SquareBumper(12,12);      
        assertEquals(bumper1, bumper2);
    }
  
    
    
    
    @Test public void testTriangleOrientationDefaultis0(){
        assertEquals(new TriangleBumper(2,2).toString(), new TriangleBumper(2,2,Angle.ZERO).toString());
    }
    
    @Test public void testTrianglegetTimeUntilCollisionNotinPath(){
        Ball myBall= new Ball(new Vect(1,1), 5, 5);    
        assertTrue(Double.isInfinite((new TriangleBumper(2,2)).getTimeUntilCollision(myBall)));   
    }
    
    @Test public void testTrianglegetTimeUntilCollisionCloseInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 5.75);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 5.75, .25), new Vect(0,1));
        double predictedTime=(new TriangleBumper(6,6)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testTrianglegetTimeUntilCollisionFarInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 1);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 1, .25), new Vect(0,1));
        double predictedTime=(new TriangleBumper(6,6)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    
    @Test public void testTrianglegetTimeUntilTreatsSidesDifferently(){
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        double predictedTime1=(new TriangleBumper(12,12)).getTimeUntilCollision(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        double predictedTime2=(new TriangleBumper(12,12)).getTimeUntilCollision(ball2);
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        double predictedTime3=(new TriangleBumper(12,12)).getTimeUntilCollision(ball3);
        double epsilon=.0001;
        assertEquals(predictedTime2,predictedTime3, epsilon);
        assertFalse(predictedTime1==predictedTime3); 
    }
    
    @Test public void testTrianglegetTimeDifferentOrientations(){
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        double predictedTime1=(new TriangleBumper(12,12,Angle.ZERO)).getTimeUntilCollision(ball3);
        double predictedTime2=(new TriangleBumper(12,12,Angle.DEG_90)).getTimeUntilCollision(ball3);
        assertFalse(predictedTime1==predictedTime2); 
    }
    
    @Test public void testTrianglegetReflectionVectorPerpendicularAngle() {
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Vect predictedVector=(new TriangleBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(12,12,13,12), new Vect (0,1));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testTrianglegetReflectionVectorOtherAngle() {
        Ball ball1= new Ball(new Vect(4,3), 8.5, 9);
        Vect predictedVector=(new TriangleBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(12,12,13,12), new Vect (4,3));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testTrianglegetReflectionVectorCorner90Angle() {
        Ball ball1= new Ball(new Vect(1,1), 11.9, 11.9);
        Vect predictedVector=(new TriangleBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectCircle(new Vect (12,12), new Vect(11.9, 11.9), new Vect (1,1));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testTrianglegetReflectionVectorCorner45Angle() {
        Ball ball1= new Ball(new Vect(-1,1), 14, 11);
        Vect predictedVector=(new TriangleBumper(12,12)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectCircle(new Vect (13,12), new Vect(14, 11), new Vect (-1,1));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testTrianglegetReflectionVectorTreatsSidesDifferently() {
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        Vect vector1=(new TriangleBumper(12,12)).getReflectionVector(ball1);
        Ball ball2= new Ball(new Vect(0,1), 12.5, 9);
        Vect vector2=(new TriangleBumper(12,12)).getReflectionVector(ball2);
        Ball ball3= new Ball(new Vect(1,0), 9, 12.5);
        Vect vector3=(new TriangleBumper(12,12)).getReflectionVector(ball3);
        double epsilon=.005;
        assertEquals(vector2.length(),vector3.length(),epsilon);
        assertFalse(vector1.x()==vector3.x()); 
    }
    
    @Test public void testTriangletoConsoleCoordinates() {
        List<DoublePair> coordinates= new ArrayList<DoublePair>();
        coordinates.add(new DoublePair(13,13));  
        assertEquals((new TriangleBumper(12,12)).toConsoleCoordinates(), coordinates);
    }
    
    @Test public void testTrianglegadgetAction() {
        TriangleBumper updated=(new TriangleBumper(12,12));
        updated.gadgetAction();
        assertEquals(updated, (new TriangleBumper(12,12)));
    }
    
    @Test public void testTrianglegetSymbol0180() {
        assertEquals((new TriangleBumper(12,12,Angle.ZERO)).getSymbol(), "/");
        assertEquals((new TriangleBumper(12,12,Angle.DEG_180)).getSymbol(), "/");
    }
    
    @Test public void testTrianglegetSymbol90270() {
        assertEquals((new TriangleBumper(12,12,Angle.DEG_90)).getSymbol(), "\\");
        assertEquals((new TriangleBumper(12,12,Angle.DEG_270)).getSymbol(), "\\");
    }
    
    @Test public void testTriangleTriggerGadgetsFlipper() {
        LeftFlipper myflipper= new LeftFlipper(12,12);
        LeftFlipper oldflipper= new LeftFlipper(12,12);
        List<Gadget> mygadgets= new ArrayList<Gadget>();
        mygadgets.add(myflipper);
        TriangleBumper mytriangle= new TriangleBumper(10,5, mygadgets);
        mytriangle.triggerGadgets();
        myflipper.changePosition(.05);
        oldflipper.changePosition(.05);
        assertFalse(myflipper.equals(oldflipper));
    }
    
    @Test public void testTriangleEquals(){
        TriangleBumper bumper1= new TriangleBumper(12,12);
        TriangleBumper bumper2= new TriangleBumper(12,12);
        assertEquals(bumper1, bumper2);
    }
    
    @Test public void testTriangleEqualsDifferentOrientation(){
        TriangleBumper bumper1= new TriangleBumper(12,12,Angle.DEG_90);
        TriangleBumper bumper2= new TriangleBumper(12,12);
        assertNotEquals(bumper1, bumper2);
    }
    
    
    
    
    @Test public void testWallgetTimeUntilCollisionNotinPath(){
        Ball myBall= new Ball(new Vect(1,1), 5, 5);    
        assertTrue(Double.isInfinite((new Wall(2,2,3,3, WallDescription.BOTTOM)).getTimeUntilCollision(myBall)));   
    }
    
    @Test public void testWallgetTimeUntilCollisionCloseInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 5.75);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 5.75, .25), new Vect(0,1));
        double predictedTime=(new Wall(6,6,7,6, WallDescription.BOTTOM)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testWallgetTimeUntilCollisionFarInPath(){
        Ball myBall= new Ball(new Vect(0,1), 6.5, 1);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 1, .25), new Vect(0,1));
        double predictedTime=(new Wall(6,6,7,6, WallDescription.BOTTOM)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    } 
    
    
    @Test public void testWallgetReflectionVectorPerpendicularAngle() {
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Vect predictedVector=(new Wall(12,12,13,12, WallDescription.BOTTOM)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(12,12,13,12), new Vect (0,1));
        assertEquals(predictedVector, actualVector);
    }
    
    @Test public void testWallgetReflectionVectorOtherAngle() {
        Ball ball1= new Ball(new Vect(1,1), 5, 5);
        Vect predictedVector=(new Wall(10,12,14,12, WallDescription.BOTTOM)).getReflectionVector(ball1);
        Vect actualVector=Geometry.reflectWall(new LineSegment(10,12,14,12), new Vect (1,1));
        assertEquals(predictedVector, actualVector);
    }
    
    
    @Test public void testWallgadgetAction() {
        Wall updated=(new Wall(12,12,11,11, WallDescription.BOTTOM));
        updated.gadgetAction();
        assertEquals(updated, (new Wall(12,12,11,11, WallDescription.BOTTOM)));
    }
    
    @Test public void testWallgetSymbol() {
        assertEquals((new Wall(0, 12, 12, 12, WallDescription.BOTTOM)).getSymbol(), ".");
    }
    
    @Test public void testWallInvisibletoStringRepresentationShortBoardName(){
        Wall myWall=new Wall(0, 0, 20, 0, WallDescription.TOP);
        myWall.setConnectedBoardName("Board1");
        String wallRepresentation= myWall.getStringRepresentationInvsibleWall();
        String expectedRepresentation= ".......Board1........";
        assertEquals(wallRepresentation,expectedRepresentation);
    }
    
    @Test public void testWallInvisibletoStringRepresentationMorethan20LettersBoardName(){
        Wall myWall=new Wall(0, 0, 20, 0, WallDescription.TOP);
        myWall.setConnectedBoardName("BOARD1BOARDBOARDBOARDBOARDBOARDBOARDBOARD");
        String wallRepresentation= myWall.getStringRepresentationInvsibleWall();
        String expectedRepresentation= "BOARD1BOARDBOARDBOARD";
        assertEquals(wallRepresentation,expectedRepresentation);
    }
    
    @Test public void testWallInvsibleReflectionVectorPerpindicular(){
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Wall myWall=new Wall(12,12,13,12, WallDescription.BOTTOM);
        myWall.setIsInvisible(true);
        Vect predictedVector=myWall.getReflectionVector(ball1);
        assertEquals(predictedVector, new Vect(0,1));
    }
    
    @Test public void testWallInvisibleREflectionVectorOtherAngle(){
        Ball ball1= new Ball(new Vect(1,1), 5, 5);
        Wall myWall=new Wall(10,12,14,12, WallDescription.BOTTOM);
        myWall.setIsInvisible(true);
        Vect predictedVector=myWall.getReflectionVector(ball1);
        assertEquals(predictedVector, new Vect(1,1));
    }
    
   ////////////////////////
   // Visual tests
   /////////////////////////
    
    /* FORWARD SLASH TRIANGLE */
    /* BACK SLASH TRIANGLE */
    /* CIRCLE BUMPER */
    
    @Test
    public void testOneBumperInMiddle(){
        List<Gadget> gadgets= new ArrayList<Gadget>();
        gadgets.add(new CircleBumper(10,10));
        gadgets.add(new Ball(new Vect(0,0), 10, 1));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard =  "......................\n"
                              + ".                    .\n"
                              + ".          *         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".          O         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + "......................\n";

        // Assert that the initial board is correct
        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    @Test
    public void testMultipleBumpers() {
        List<Gadget> gadgets= new ArrayList<Gadget>();
        Ball b1 = new Ball(new Vect(1,1), 10, 1);
        CircleBumper c1 = new CircleBumper(10,10);
        CircleBumper c2 = new CircleBumper(5,5);
        CircleBumper c3 = new CircleBumper(14,13);
        CircleBumper c4 = new CircleBumper(7,17);
        gadgets.addAll(Arrays.asList(b1, c1, c2, c3, c4));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard = "......................\n"
                             + ".                    .\n"
                             + ".          *         .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".     O              .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".          O         .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".              O     .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + ".       O            .\n"
                             + ".                    .\n"
                             + ".                    .\n"
                             + "......................\n";
        
        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    @Test
    public void testBumpersInCorners() throws InterruptedException{
        List<Gadget> gadgets= new ArrayList<Gadget>();
        Ball b3 = new Ball(new Vect(5,4), 10, 1);

        CircleBumper c5 = new CircleBumper(0,0);
        CircleBumper c6 = new CircleBumper(19,19);
        CircleBumper c7 = new CircleBumper(0,19);
        CircleBumper c8 = new CircleBumper(19,0);
        
        gadgets.addAll(Arrays.asList(b3, c5, c6, c7, c8));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard =  "......................\n"
                               + ".O                  O.\n"
                               + ".          *         .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".O                  O.\n"
                               + "......................\n";
        

        assertTrue(expectedBoard.equals(board.toString()));

    }
    
    @Test
    public void testBumpersInARow() throws InterruptedException {
        List<Gadget> gadgets= new ArrayList<Gadget>();

        Ball b3 = new Ball(new Vect(5,4), 10, 1);

        CircleBumper c9 = new CircleBumper(8,13);
        CircleBumper c10 = new CircleBumper(9,13);
        CircleBumper c11 = new CircleBumper(10,13);
        CircleBumper c12 = new CircleBumper(15,13);
        CircleBumper c13 = new CircleBumper(16,13);
        gadgets.addAll(Arrays.asList(b3, c9, c10, c11, c12, c13));
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".        OOO    OO   .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";
        
   
        assertTrue(expectedBoard.equals(board.toString()));

    }
    
    /* SQUARE BUMPER */
 

    @Test
    public void testOneSqBumperInMiddle() {
        Ball b1 = new Ball(new Vect(0,0), 10, 1);
        SquareBumper s1 = new SquareBumper(10,10);
        List<Gadget> gadgets= new ArrayList<Gadget>();
        gadgets.add(s1);
        gadgets.add(b1);
        Board board = new Board();
        board.insertGadgets(gadgets);
        
        String expectedBoard =  "......................\n"
                              + ".                    .\n"
                              + ".          *         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".          #         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    
    @Test
    public void testMultipleSqBumpers() {
        List<Gadget> gadgets= new ArrayList<Gadget>();
        Ball b2 = new Ball(new Vect(2,1), 10, 1);
        SquareBumper s1 = new SquareBumper(10,10);
        SquareBumper s2 = new SquareBumper(5,5);
        SquareBumper s3 = new SquareBumper(14,13);
        SquareBumper s4 = new SquareBumper(7,17);
        gadgets.addAll(Arrays.asList(b2, s1, s2, s3, s4));
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".     #              .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".          #         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".              #     .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".       #            .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));

    }
    
    
    @Test
    public void testSqBumpersInCorners(){
        List<Gadget> gadgets= new ArrayList<Gadget>();
        Ball b2 = new Ball(new Vect(2,1), 10, 1);
        SquareBumper s5 = new SquareBumper(0,0);
        SquareBumper s6 = new SquareBumper(19,19);
        SquareBumper s7 = new SquareBumper(0,19);
        SquareBumper s8 = new SquareBumper(19,0);
        gadgets.addAll(Arrays.asList(b2, s5, s6, s7, s8));
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard =  "......................\n"
                               + ".#                  #.\n"
                               + ".          *         .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".                    .\n"
                               + ".#                  #.\n"
                               + "......................\n";
        
        assertTrue(expectedBoard.equals(board.toString()));

    }
      
    
    @Test
    public void testSqBumpersInARow() {
        List<Gadget> gadgets= new ArrayList<Gadget>();
        Ball b3 = new Ball(new Vect(5,4), 10, 1);
        SquareBumper s9 = new SquareBumper(8,13);
        SquareBumper s10 = new SquareBumper(9,13);
        SquareBumper s11 = new SquareBumper(10,13);
        SquareBumper s12 = new SquareBumper(14,13);
        SquareBumper s13 = new SquareBumper(15,13);
        
        gadgets.addAll(Arrays.asList(b3, s9, s10, s11, s12, s13));
        
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".        ###   ##    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));

    }

    
    @Test
    public void testForwardTriangleOneBumperInMiddle() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b1 = new Ball(new Vect(0,0), 10.5, 1);
        TriangleBumper ft1 = new TriangleBumper(10,10, Angle.DEG_180);
        gadgets.add(b1);
        gadgets.add(ft1);
        
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard =  "......................\n"
                              + ".                    .\n"
                              + ".           *        .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".          /         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + "......................\n";
        
        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    
    @Test
    public void testForwardTriangleMultipleBumpers() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        TriangleBumper ft1 = new TriangleBumper(10,10, Angle.DEG_180);
        TriangleBumper ft2 = new TriangleBumper(5,2, Angle.ZERO);
        TriangleBumper ft3 = new TriangleBumper(15,15, Angle.DEG_180);
        TriangleBumper ft4 = new TriangleBumper(4,12, Angle.ZERO);
        gadgets.addAll(Arrays.asList(b3, ft1, ft2, ft3, ft4));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".     /              .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".          /         .\n" +
                               ".                    .\n" +
                               ".    /               .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".               /    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    
    @Test
    public void testForwardTriangleBumpersInCorners() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b2 = new Ball(new Vect(0,0), 18.5, 1);
        TriangleBumper ft5 = new TriangleBumper(0,0, Angle.ZERO);
        TriangleBumper ft6 = new TriangleBumper(18,19, Angle.DEG_180);
        gadgets.addAll(Arrays.asList(b2, ft5, ft6));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard =  "......................\n"
                       + "./                   .\n"
                       + ".                   *.\n" 
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                  / .\n"
                       + "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
      
    
    @Test
    public void testForwardTriangleBumpersInARow() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        TriangleBumper ft7 = new TriangleBumper(5, 14, Angle.ZERO);
        TriangleBumper ft8 = new TriangleBumper(6, 14, Angle.ZERO);
        TriangleBumper ft9 = new TriangleBumper(7, 14, Angle.ZERO);
        TriangleBumper ft10 = new TriangleBumper(14,14, Angle.DEG_180);
        TriangleBumper ft11 = new TriangleBumper(15,14, Angle.DEG_180);
        TriangleBumper ft12 = new TriangleBumper(16,14, Angle.DEG_180);
        gadgets.addAll(Arrays.asList(b3, ft7, ft8, ft9, ft10, ft11, ft12));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".     ///      ///   .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    @Test
    public void testForwardTriangleDiagonalTriangles(){
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        TriangleBumper ft13 = new TriangleBumper(15,13, Angle.DEG_180);
        TriangleBumper ft14 = new TriangleBumper(14,14, Angle.DEG_180);
        TriangleBumper ft15 = new TriangleBumper(13,15, Angle.DEG_180);
        gadgets.addAll(Arrays.asList(b3, ft13, ft14, ft15));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".               /    .\n" +
                               ".              /     .\n" +
                               ".             /      .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
   

    
    @Test
    public void testBackTriangleOneBumperInMiddle() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b1 = new Ball(new Vect(0,0), 10.5, 1);
        TriangleBumper bt1 = new TriangleBumper(10,10, Angle.DEG_270);
        gadgets.add(b1);
        gadgets.add(bt1);
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard =  "......................\n"
                              + ".                    .\n"
                              + ".           *        .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".          \\         .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + ".                    .\n"
                              + "......................\n";
        
        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    
    @Test
    public void testBackTriangleMultipleBumpers(){
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        
        TriangleBumper bt1 = new TriangleBumper(10,10, Angle.DEG_270);
        
        TriangleBumper bt2 = new TriangleBumper(5,2, Angle.DEG_90);
        TriangleBumper bt3 = new TriangleBumper(15,15, Angle.DEG_270);
        TriangleBumper bt4 = new TriangleBumper(4,12, Angle.DEG_90);
        gadgets.addAll(Arrays.asList(b3, bt1, bt2, bt3, bt4));
        Board board = new Board();
        board.insertGadgets(gadgets);
        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".     \\              .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".          \\         .\n" +
                               ".                    .\n" +
                               ".    \\               .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".               \\    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";
 
        assertTrue(expectedBoard.equals(board.toString()));

    }
    
    
    @Test
    public void testBackTriangleBumpersInCorners(){
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b2 = new Ball( new Vect(0,0), 0.5, 1);
        TriangleBumper bt5 = new TriangleBumper(19, 0, Angle.DEG_90);
        TriangleBumper bt6 = new TriangleBumper(0, 19, Angle.DEG_270);
        
        gadgets.addAll(Arrays.asList(b2, bt5, bt6));
        Board board = new Board();
        board.insertGadgets(gadgets);

        String expectedBoard =  "......................\n"
                       + ".                   \\.\n"
                       + ". *                  .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".                    .\n"
                       + ".\\                   .\n"
                       + "......................\n";
        

        assertTrue(expectedBoard.equals(board.toString()));

    }
      
    
    @Test
    public void testBackTriangleBumpersInARow(){
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        TriangleBumper bt7 = new TriangleBumper(5, 14, Angle.DEG_90);
        TriangleBumper bt8 = new TriangleBumper(6, 14, Angle.DEG_90);
        TriangleBumper bt9 = new TriangleBumper(7, 14, Angle.DEG_90);
        TriangleBumper bt10 = new TriangleBumper(14,14, Angle.DEG_270);
        TriangleBumper bt11 = new TriangleBumper(15,14, Angle.DEG_270);
        TriangleBumper bt12 = new TriangleBumper(16,14, Angle.DEG_270);
        gadgets.addAll(Arrays.asList(b3, bt7, bt8, bt9, bt10, bt11, bt12));
        Board board = new Board();
        board.insertGadgets(gadgets);


        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".     \\\\\\      \\\\\\   .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
    
    @Test
    public void testBackTriangleDiagonalTriangles() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball b3 = new Ball( new Vect(5,4), 10, 1);
        TriangleBumper bt13 = new TriangleBumper(1,13, Angle.DEG_270);
        TriangleBumper bt14 = new TriangleBumper(2,14, Angle.DEG_270);
        TriangleBumper bt15 = new TriangleBumper(3,15, Angle.DEG_270);
        gadgets.addAll(Arrays.asList(b3, bt13, bt14, bt15));
        Board board = new Board();
        board.insertGadgets(gadgets);


        String expectedBoard = "......................\n" +
                               ".                    .\n" +
                               ".          *         .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ". \\                  .\n" +
                               ".  \\                 .\n" +
                               ".   \\                .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               ".                    .\n" +
                               "......................\n";

        assertTrue(expectedBoard.equals(board.toString()));
    }
}

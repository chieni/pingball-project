package phase1;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import phase1.Wall.WallDescription;
import physics.Vect;
import physics.Geometry;


public class BallTest {

    // Testing strategy
    //
    // hitInvisibleWall(wallLocation, connectedBoardName)
    //  -top 
    //  -bottom
    //  -left
    //  -right
    //
    // isBall
    //
    // getTimeUntilCollision (with other ball)
    //  the two balls will collide
    //      - time until collision > 0
    //      - time until collision = 0
    //  the two balls will not collide
    //
    // getReflectionVector (with other ball)
    //  other ball not moving
    //  other ball moving at angle
    //
    // triggerGadgets
    //  
    //
    // toConsoleCoordinates
    //  ball @ (1.5, 1.5), 
    //         (1.3, 1.2),
    //         (20.5, 20.7)
    //         
    // gadgetAction
    //  should do nothing
    //
    // getSymbol
    //  must return "*" for ball
    //
    // setVelocity and getVelocity
    // `set velocity, then get velocity, the two must equal
    //
    // changePosition(time)
    //  time = 0
    //  velocity = 0
    //  velocity.x > 0, velocity.y = 0
    //  velocty.x = 0, velocity.y < 0
    //
    // accelerateDueToGravity
    //
    // isWithinBoundingBox (see ToDoList for note on edge cases)
    //  far away
    //  clearly within
    //
    //
    // hashCode
    //  two equal balls
    // 
    // equals
    //  two equal balls
    //  two not equal balls
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR hitInvisibleWall
    ////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testHitInvisibleWallTop(){
        Ball ball= new Ball(new Vect(0,-1), 12, 1);
        ball.hitInvisibleWall(WallDescription.TOP, "Board2");
        String BallLocationandBoard= ball.getBallTrajectoryMessage();
        String expected= "--board Board2 --velocity 0,-1 --position 12,19";
        assertEquals(BallLocationandBoard, expected);
    }
    
    @Test
    public void testHitInvisibleWallBottom(){
        Ball ball= new Ball(new Vect(0,1), 12, 19);
        ball.hitInvisibleWall(WallDescription.BOTTOM, "Board2");
        String BallLocationandBoard= ball.getBallTrajectoryMessage();
        String expected= "--board Board2 --velocity 0,1 --position 12,1";
        assertEquals(BallLocationandBoard, expected);
    }
    
    @Test
    public void testHitInvisibleWallRight(){
        Ball ball= new Ball(new Vect(1,0), 19, 12);
        ball.hitInvisibleWall(WallDescription.RIGHT, "Board2");
        String BallLocationandBoard= ball.getBallTrajectoryMessage();
        String expected= "--board Board2 --velocity 1,0 --position 1,12";
        assertEquals(BallLocationandBoard, expected);
    }
    
    @Test
    public void testHitInvisibleWallLeft(){
        Ball ball= new Ball(new Vect(-1,0), 1, 12);
        ball.hitInvisibleWall(WallDescription.LEFT, "Board2");
        String BallLocationandBoard= ball.getBallTrajectoryMessage();
        String expected="--board Board2 --velocity -1,0 --position 19,12";
        assertEquals(BallLocationandBoard, expected);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR GetTimeUntilCollision
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Tests that two balls bound to collide given nothing else returns
    // valid time until collision
    @Test
    public void testGetTimeUntilCollisionBallsWillCollide() {
        Ball ball = new Ball(new Vect(0, 1), 5, 5);
        Ball otherBall = new Ball(new Vect(0, 0), 5, 10);
        double time = otherBall.getTimeUntilCollision(ball);
        assertTrue(time > 0);
        assertFalse(Double.isInfinite(time));
    }
    
    // Tests that two balls that are touching have zero time
    // until collision
    @Test
    public void testGetTimeUntilCollisionIsZero() {
        Ball ball = new Ball(new Vect(1, 0), 5, 5);
        double ballRadius = 0.25;
        Ball otherBall = new Ball(new Vect(0, 0), 5 + 2*ballRadius, 5);
        double time = ball.getTimeUntilCollision(otherBall);
        assertTrue(time < 2* Double.MIN_VALUE);
    }
    
    // Tests that two balls that won't collide have infinite time
    // till collision
    @Test
    public void testGetTimeUntilCollisionNoCollision() {
        Ball ball = new Ball(new Vect(0, 1), 5, 5);
        Ball otherBall = new Ball(new Vect(0, 0), 10, 10);
        double time = otherBall.getTimeUntilCollision(ball);
        assertTrue(Double.isInfinite(time));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR getReflectionVector
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Test ball reflecting off nonmoving other ball
    // The ball should bounce back the direction it came from
    @Test
    public void testGetReflectionVectorNegativeYZeroX() {
        Ball ball = new Ball(new Vect(0, 1), 5, 5);
        Ball otherBall = new Ball(new Vect(0, -1), 5, 5.55);
        Vect reflection = otherBall.getReflectionVector(ball);
        assertTrue(reflection.x() == 0.0);
        assertTrue(reflection.y() < 0); 
    }
    
    // Test ball reflecting off other moving ball
    @Test
    public void testGetReflectionVectorBothBallsMovingFortyFiveDegreeAngle() {
        Ball ball = new Ball(new Vect(1, 1), 5, 5);
        Ball otherBall = new Ball(new Vect(-1, 1), 5.5, 5);
        Vect reflection = otherBall.getReflectionVector(ball);
        assertEquals(-1, reflection.x(), 0.0001);
        assertEquals(1, reflection.y(), 0.0001);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR toConsoleCoordinates
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testToConsoleCoordinates() {
        double xBall = 5;
        double yBall = 7;
        Ball ball = new Ball(new Vect(0, 1), xBall, yBall);
        List<Geometry.DoublePair> coordinates = ball.toConsoleCoordinates();
        assertEquals(1, coordinates.size());
        Geometry.DoublePair ballLocation = coordinates.get(0);
        assertEquals(xBall + 1, ballLocation.d1, 0);
        assertEquals(yBall + 1, ballLocation.d2, 0);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR getSymbol
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testGetSymbol() {
        Ball ball = new Ball(new Vect(0, 1), 5, 5);
        String symbol = ball.getSymbol();
        assertEquals("*", symbol);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for setVelocity and  getVelocity
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testGetAndSetVelocity() {
        Ball ball = new Ball(new Vect(0,0), 5, 5);
        Vect newVelocity = new Vect(3.5, -2.5);
        ball.setVelocity(newVelocity);
        assertEquals(newVelocity, ball.getVelocity());
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for changePosition
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Tests that there is no change in position when velocity is zero
    @Test
    public void testChangePositionZeroVelocity() {
        double startX = 5;
        double startY = 5;
        Ball ball = new Ball(new Vect(0,0), startX, startY);
        ball.changePosition(5);
        Vect newPosition = ball.ball.getCenter();
        assertEquals(startX, newPosition.x(), 0);
        assertEquals(startY, newPosition.y(), 0);
    }
    
    // Tests that the position change is computed correctly
    @Test
    public void testChangePositionXAndYVelocities() {
        Ball ball = new Ball(new Vect(1, -1), 10, 10);
        ball.changePosition(0.5);
        Vect coordinate = ball.getShape().getCenter();
        assertEquals(10.5, coordinate.x(), 0.00001);
        assertEquals(9.5, coordinate.y(), 0.00001);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for accelerateDueToGravity
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Tests gravity in opposite direction of motion
    @Test
    public void testAccelerateDueToGravityDecreasingVelocity() {
        double startVelocityX = 0;
        double startVelocityY = -50;
        Ball ball = new Ball(new Vect(startVelocityX, startVelocityY), 10, 10);
        ball.accelerateDueToGravity(2, 25.0);
        
        // default gravity is 25L/sec^2, but this should be configurable
        double gravity = 25.0;
        double time = 2;
        double predictedVelocityY = startVelocityY + gravity * time;
               
        assertEquals(predictedVelocityY, ball.getVelocity().y(), 0.00001);
    }
    
    // Tests gravity in same direction of motion
    @Test
    public void testAccerelateDueToGravityIncreasingVelocity() {
        double startVelocityX = 0;
        double startVelocityY = 1;
        Ball ball = new Ball(new Vect(startVelocityX, startVelocityY), 10, 10);
        ball.accelerateDueToGravity(0.1, 25.0);
        
        double gravity = 25.0;
        double time = 0.1;
        double predictedVelocityY = startVelocityY + gravity * time;
               
        assertEquals(predictedVelocityY, ball.getVelocity().y(), 0.00001);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for isWithinBoundingBox
    ////////////////////////////////////////////////////////////////////////////////////

//    
//    @Test
//    public void testIsWithinBoundingBoxSameLocation() {
//        Ball firstBall = new Ball(new Vect(0, 0), 10, 10);
//        assertTrue(firstBall.isWithinBoundingBox(10, 10));
//    }
//    
//    @Test
//    public void testIsWithinBoundingBoxFarAway() {
//        Ball firstBall = new Ball(new Vect(0, 0), 10, 10);
//        assertTrue(firstBall.isWithinBoundingBox(15, 15));
//    }
     
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for hashCode
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testHashCodeBallsEqualByObservation() {
        Ball ball1 = new Ball(new Vect(5, 5), 5, 5);
        Ball ball2 = new Ball(new Vect(5, 5), 5, 5);
        assertEquals(ball1.hashCode(), ball2.hashCode());
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS for equals
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testEqualsByObservation() {
        Ball ball1 = new Ball(new Vect(5, 5), 5, 5);
        Ball ball2 = new Ball(new Vect(5, 5), 5, 5);
        assertTrue(ball1.equals(ball2));
    }
}




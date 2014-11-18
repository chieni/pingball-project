package phase1;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import physics.Geometry.DoublePair;

public class AbsorberTest {
    
    /*
     *  getTimeUntilCollision(Ball ball)
     *      -Path location: not in path, <.05 seconds in front, >.05 seconds in front
     *      -Side (check that 2 balls placed at equal distance from two sides return same time)
     *     
     * getReflectionVector(Ball ball)
     *      
     *  toConsoleCoordinates()
     *      
     *  gadgetAction()
     *      -one ball (check vector, location)
     *      -multiple balls together
     *      
     *  getSymbol()
     *      -call getSymbol(), return symbol
     *      
    */
    
    @Test public void testAbsorbergetTimeUntilCollisionNotinPath() {
        Ball myBall= new Ball(new Vect(1,1), 5, 5);    
        assertTrue(Double.isInfinite((new Absorber(2,2, 1,1)).getTimeUntilCollision(myBall)));        
    }
    
    @Test public void testAbsorbergetTimeUntilCollisionCloseInPath() {
        Ball myBall= new Ball(new Vect(0,1), 6.5, 5.75);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 5.75, .25), new Vect(0,1));
        double predictedTime=(new Absorber(6,6,1,1)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testAbsorbergetTimeUntilCollisionFarInPath() {
        Ball myBall= new Ball(new Vect(0,1), 6.5, 1);
        double actualTime= Geometry.timeUntilWallCollision(new LineSegment(6,6,7,6), new Circle(6.5, 1, .25), new Vect(0,1));
        double predictedTime=(new Absorber(6,6,1,1)).getTimeUntilCollision(myBall);
        double epsilon= .001;
        assertEquals(predictedTime, actualTime, epsilon);
    }
    
    @Test public void testAbsorbergetTimeUntilCollisionTwoSides() {
        Ball ball1= new Ball(new Vect(0,-1), 12.5, 16);
        double predictedTime1=(new Absorber(12,12,1,1)).getTimeUntilCollision(ball1);
        Ball ball2= new Ball(new Vect(1,0), 9, 12.5);
        double predictedTime2=(new Absorber(12,12,1,1)).getTimeUntilCollision(ball2);
        double epsilon=.0001;
        assertEquals(predictedTime1,predictedTime2, epsilon); 
    }
    
    @Test public void testAbsorbergetReflectionVector() {
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Vect predictedVector1=(new Absorber(12,12,1,1)).getReflectionVector(ball1);
        Ball ball2= new Ball(new Vect(3,4), 9.5, 8);
        Vect predictedVector2=(new Absorber(12,12,1,1)).getReflectionVector(ball2);
        Ball ball3= new Ball(new Vect(1,0), 1, 12.5);
        Vect predictedVector3=(new Absorber(12,12,1,1)).getReflectionVector(ball3);
        assertEquals(predictedVector1, predictedVector2);
        assertEquals(predictedVector1, predictedVector3);
        
    }
    
    @Test public void testAbsorbertoConsoleCoordinates() {
        List<DoublePair> coordinates1= new ArrayList<DoublePair>();
        coordinates1.add(new DoublePair(13,13));
        Absorber myabsorber= new Absorber(12,12,1,1);
        List<DoublePair> coordinates2= new ArrayList<DoublePair>();
        coordinates2.add(new DoublePair(4,5));
        coordinates2.add(new DoublePair(5,5));
        coordinates2.add(new DoublePair(6,5));
        Absorber myabsorber2=new Absorber(3,4,3,1);
        //System.out.println(myabsorber2.toConsoleCoordinates());
        assertEquals(myabsorber.toConsoleCoordinates(), coordinates1);
        assertEquals(myabsorber2.toConsoleCoordinates(),coordinates2);
    }
    
    @Test public void testAbsorberGadgetActionCheckBallVector() { //FIX
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Absorber myabsorber=new Absorber(12,12,1,1);
        ball1.setVelocity(myabsorber.getReflectionVector(ball1));
        myabsorber.gadgetAction();
        double epsilon=.0005;
        assertEquals(ball1.getVelocity().x(),new Vect(0,-50).x(),epsilon);
        assertEquals(ball1.getVelocity().y(),new Vect(0,-50).y(),epsilon);
        
    }

    @Test public void testAbsorberGadgetActionCheckBallLocation() { //FIX
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        Absorber myabsorber=new Absorber(12,12,1,1);
        ball1.setVelocity(myabsorber.getReflectionVector(ball1));
        assertEquals(ball1.getShape(), new Circle(12.75,12.75,.25));
        
    }
    
    @Test public void testAbsorberGadgetActionMultipleBalls() { //FIX
        Absorber myabsorber=new Absorber(12,12,1,1);
        Ball ball1= new Ball(new Vect(0,1), 12.5, 10);
        ball1.setVelocity(myabsorber.getReflectionVector(ball1));
        Ball ball2= new Ball(new Vect(0,1), 12.5, 10);
        ball2.setVelocity(myabsorber.getReflectionVector(ball1));
        myabsorber.gadgetAction();
        assertNotEquals(ball1.getShape(), ball2.getShape());
    }
    
    @Test public void testAbsorbergetSymbol() {
        assertEquals((new Absorber(1,18, 19, 2)).getSymbol(), "=");
    }
    
    
}

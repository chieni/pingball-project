package phase1;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import physics.Angle;
import physics.Vect;
import physics.Geometry;

// TODO: 
// Ensure that coordinate and orientation tests work
// Does timing work for the rotating reflection vector tests?
// Comment/explain the more complicated test cases
// Specify in specs what correct time until collision is
// 
// maybe also test in triggerGadgets when the flipper has > 1
// other gadgets that it triggers
//
//

public class FlipperTest {
    //
    // Testing strategy:
    //  
    // isBall
    //   false
    // 
    // getTimeUntilCollision
    //  left rotating flipper time vs. nonrotating time
    //  right rotating flipper time vs. nonrotating time
    //  not rotating flipper similar to line segment and ball
    //  ball comes directly towards the left edge of a horizontal flipper
    //
    // getReflectionVector
    //  rotating flipper
    //  not rotating flipper
    //  ball comes directly towards the left edge of a horizontal flipper
    //
    // triggerGadgets
    //  flipper is hit
    //
    // toConsoleCoordinates
    //  horizontal, vertical not rotating
    //  rotating: at 30 degrees, at 45 degrees at 70 degrees
    //
    // gadgetAction
    //  rotate
    //
    // getSymbol
    //  horizontal, vertical
    //
    // equals
    //
    // hashCode
    //
    // isWithinBoundingBox

    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR isBall
    ////////////////////////////////////////////////////////////////////////////////////
    @Test 
    public void testIsBallLeft() {
        LeftFlipper flipper = new LeftFlipper(2, 2);
        assertFalse(flipper.isBall());
    }
    
    @Test 
    public void testIsBallRight() {
        RightFlipper flipper = new RightFlipper(2, 2);
        assertFalse(flipper.isBall());
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR getTimeUntilCollision AND constructor with position only
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Test time range returned by getTimeUntilCollision 
    @Test
    public void testGetTimeUntilCollisionTimeRange() {
        RightFlipper flipper = new RightFlipper(10, 10);
        Ball ball = new Ball(new Vect(1, 0), 5, 11);
        double time = flipper.getTimeUntilCollision(ball);
        assertEquals(6.75, time, 0.000001);
    }
    
    
    // Test for timeUntilCollision when ball comes from left towards horizontal flipper
    @Test
    public void testGetTimeUntilCollisionBallHitsCorner() {
        RightFlipper flipper = new RightFlipper(10, 10);
        Ball ball = new Ball(new Vect(0, 1), 12, 5);
        double time = flipper.getTimeUntilCollision(ball);
        assertEquals(4.75, time, 0.000001);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR getReflectionVector
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Test get reflection vector with no rotation
    @Test
    public void testGetReflectionVectorLeftFlipperNoRotation() {
        LeftFlipper flipper = new LeftFlipper(10, 10);
        Ball ball = new Ball(new Vect(1, 0), 5, 11);
        Vect reflection = flipper.getReflectionVector(ball);
        assertEquals(-0.95, reflection.x(), 0.0001);
        assertEquals(0, reflection.y(), 0);
    }
    
    // Test get reflection vector with no rotation
    @Test
    public void testGetReflectionVectorRightFlipperNoRotation() {
        RightFlipper flipper = new RightFlipper(10, 10);
        Ball ball = new Ball(new Vect(1, 0), 5, 11);
        Vect reflection = flipper.getReflectionVector(ball);
        assertEquals(-0.95, reflection.x(), 0.0001);
        assertEquals(0, reflection.y(), 0);
    }
    
    // Test difference in reflection vector for rotating/nonrotating left flipper
    @Test
    public void testGetReflectionVectorLeftFlipper() {
        LeftFlipper flipper = new LeftFlipper(10, 10);
        Ball firstBall = new Ball(new Vect(0, 1), 5, 11);
        
        Vect nonRotatingReflectionVector = flipper.getReflectionVector(firstBall);
        
        flipper.gadgetAction();
        flipper.changePosition(0.1);
        
        Ball secondBall = new Ball(new Vect(0, 1), 5, 11);
        Vect rotatingReflectionVector = flipper.getReflectionVector(secondBall);
        assertTrue(nonRotatingReflectionVector.length() > rotatingReflectionVector.length());
    }
    
    
    // Test difference in reflection vector for rotating/nonrotating right flipper
    @Test
    public void testGetReflectionVectorRightFlipper() {
        RightFlipper flipper = new RightFlipper(10, 10);
        Ball firstBall = new Ball(new Vect(1, 0), 5, 11);
        Vect nonRotatingReflectionVector = flipper.getReflectionVector(firstBall);
        
        flipper.gadgetAction();
        flipper.changePosition(0.1);
        Ball secondBall = new Ball(new Vect(1, 0), 5, 11);
        Vect rotatingReflectionVector = flipper.getReflectionVector(secondBall);
        
        assertTrue(nonRotatingReflectionVector.length() < rotatingReflectionVector.length());
    }
    
    // Test reflection vector when ball comes from left/right towards horizontal flipper
    @Test
    public void testGetReflectionVectorBallHitsCornerLeftFlipper() {
        LeftFlipper flipper = new LeftFlipper(10, 10);
        Ball ball = new Ball(new Vect(0, 1), 10, 5);
        Vect reflection = flipper.getReflectionVector(ball);
        assertEquals(0, reflection.x(), 0);
        assertEquals(-0.95, reflection.y(), 0.0001);
    }
    
    // Test reflection vector when ball comes from left/right towards horizontal flipper
    @Test
    public void testGetReflectionVectorBallHitsCornerRightFlipper() {
        RightFlipper flipper = new RightFlipper(10, 10);
        Ball ball = new Ball(new Vect(0, 1), 12, 5);
        Vect reflection = flipper.getReflectionVector(ball);
        assertEquals(0, reflection.x(), 0);
        assertEquals(-0.95, reflection.y(), 0.0001);
    }
    
    
    // //////////////////////////////////////////////////////////////////////////////////
    // TESTS FOR toConsoleCoordinates and Constructors with orientation
    // orientation is 0, 90, 180, 270 clockwise around center of bounding box
    // //////////////////////////////////////////////////////////////////////////////////

    // 0 degrees. Same for right and left.
    @Test
    public void testToConsoleCoordinatesNotRotatingLeftZeroDegrees() {
        LeftFlipper flipper = new LeftFlipper(5, 5);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 6)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 7)));
    }

    // Left 90 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingLeftNinetyDegrees() {
        LeftFlipper flipper = new LeftFlipper(5, 5, Angle.DEG_90);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 6)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7, 6)));
    }

    // Right 90 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingRightNinetyDegrees() {
        RightFlipper flipper = new RightFlipper(5, 5, Angle.DEG_90);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6,7)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7,7)));
    }

    // Left 180 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingLeftHundredEightyDegrees() {
        LeftFlipper flipper = new LeftFlipper(5, 5, Angle.DEG_180);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7, 6)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7, 7)));
    }

    // Right 180 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingRightHundredEightyDegrees() {
        RightFlipper flipper = new RightFlipper(5, 5, Angle.DEG_180);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 6)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 7)));
    }

    // Left 270 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingLeftTwoHundredSeventyDegrees() {
        LeftFlipper flipper = new LeftFlipper(5, 5, Angle.DEG_270);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 7)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7, 7)));
    }

    // Right 270 degrees
    @Test
    public void testToConsoleCoordinatesNotRotatingRightTwoHundredSeventyDegrees() {
        RightFlipper flipper = new RightFlipper(5, 5, Angle.DEG_270);
        List<Geometry.DoublePair> flipperCoordinates = flipper
                .toConsoleCoordinates();
        assertEquals(2, flipperCoordinates.size());
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(6, 6)));
        assertTrue(flipperCoordinates.contains(new Geometry.DoublePair(7, 6)));
    }
    

  
    ////////////////////////////////////////////////////////////////////////////////////
    // TEST FOR getSymbol
    ////////////////////////////////////////////////////////////////////////////////////
    
    // Test for vertical gadget
    @Test
    public void testGetSymbolVerticalFlipper() {
        LeftFlipper flipper = new LeftFlipper(5, 5);
        String symbol = flipper.getSymbol();
        assertEquals("|", symbol);
    }

    // Test for horizontal gadget
    @Test
    public void testGetSymbolHorizontalFlipper() {
        LeftFlipper flipper = new LeftFlipper(5, 5, Angle.DEG_90);
        String symbol = flipper.getSymbol();
        assertEquals("-", symbol);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TEST FOR equals
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testEqualsLeftFlipper() {
        LeftFlipper flipper1 = new LeftFlipper(5, 5, Angle.DEG_90);
        LeftFlipper flipper2 = new LeftFlipper(5, 5, Angle.DEG_90);
        assertTrue(flipper1.equals(flipper2));
    }
    
    @Test
    public void testEqualsRightFlipper() {
        RightFlipper flipper1 = new RightFlipper(5, 5, Angle.DEG_90);
        RightFlipper flipper2 = new RightFlipper(5, 5, Angle.DEG_90);
        assertTrue(flipper1.equals(flipper2));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // TEST FOR hashCode
    ////////////////////////////////////////////////////////////////////////////////////
    
    @Test
    public void testHashCodeLeftFlipper() {
        LeftFlipper flipper1 = new LeftFlipper(5, 5, Angle.DEG_90);
        LeftFlipper flipper2 = new LeftFlipper(5, 5, Angle.DEG_90);
        assertEquals(flipper1.hashCode(), flipper2.hashCode());
    }
    
    @Test
    public void testHashCodeRightFlipper() {
        RightFlipper flipper1 = new RightFlipper(5, 5, Angle.DEG_90);
        RightFlipper flipper2 = new RightFlipper(5, 5, Angle.DEG_90);
        assertEquals(flipper1.hashCode(), flipper2.hashCode());
    }

}

package phase1;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import phase1.Wall.WallDescription;
import physics.Angle;
import physics.Vect;

/**
 * This class contains unit tests for the Board class. 
 *
 * insertGadget
 *      - 0 items in gadgets list
 *      - 1 item in gadgets list
 *      - multiple items in gadgets list
 *          - balls, various bumpers, flippers, absorbers 
 * 
 * updateBallVelocities
 *      Number of balls:
 *      - 0, 1, >= 2 balls
 *      
 *      Reflections:
 *      - Ball about to collide
 *      - Two balls colliding with each other
 *      - Ball not colliding
 *      
 *      
 * getGadgetWithMinimumTimeToCollision
 *      - 0, 1, >= 2 other gadgets in addition to walls
 *      - collides with wall/ball/other first
 *      
 * applyFriction
 *      - input: velocity < 0, = 0, > 0
 * 
 * updateGadgetPositions
 *      - 0, 1, >= 2 gadgets
 *
 * toPrint
 *      - number of gadgets
 *          - none
 *          - multiple
 *      - types
 *          - balls, bumpers, absorbers, flippers
 *      - orientations
 *          - for triangle bumpers
 *          - for flippers
 *      - invisible walls
 *          -top
 *          -bottom
 *          -left
 *          -right
 *          -all
 *  
 */
public class BoardTest {
    // ======================== insertGadget ========================
    @Test public void testInsertGadgetZeroGadgets() {
        
        Board theBoard = new Board();
        Set<Gadget> boardGadgets = theBoard.testHelperGetGadgets();
        Set<Gadget> answer = new HashSet<Gadget>();
        answer.addAll(boardGadgets);
        
        // now insert empty list of gadgets
        List<Gadget> gadgets = new ArrayList<Gadget>();
        theBoard.insertGadgets(gadgets);
        
        assertEquals(theBoard.testHelperGetGadgets(), answer);
    }

    @Test public void testInsertGadgetOneGadget() {
        Board theBoard = new Board();
        Set<Gadget> boardGadgets = theBoard.testHelperGetGadgets();
        Set<Gadget> answer = new HashSet<Gadget>();
        answer.addAll(boardGadgets);
        
        // now insert gadget
        Gadget squareBumper = new SquareBumper(3, 4);
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(squareBumper);
        
        theBoard.insertGadgets(gadgets);
        answer.add(squareBumper);
        assertEquals(theBoard.testHelperGetGadgets(), answer);
    }
    

    @Test public void testInsertGadgetMultipleBallsBumperFlipperAbsorber() {
        Board theBoard = new Board();
        Set<Gadget> boardGadgets = theBoard.testHelperGetGadgets();
        Set<Gadget> answer = new HashSet<Gadget>();
        answer.addAll(boardGadgets);
        
        Set<Gadget> ballsAnswer = new HashSet<Gadget>();
        
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new SquareBumper(3, 4));
        gadgets.add(new Ball(new Vect(-1.9, 4.3), 5, 6));
        gadgets.add(new Ball(new Vect(2.1, 3.2), 1,2));
        gadgets.add(new CircleBumper(13, 3));
        gadgets.add(new TriangleBumper(7, 17));
        gadgets.add(new SquareBumper(7, 17));
        gadgets.add(new RightFlipper(13, 14));
        gadgets.add(new Absorber(0, 18, 20, 1));
        gadgets.add(new CircleBumper(4, 18));
        
        theBoard.insertGadgets(gadgets);
        
        answer.add(new SquareBumper(3, 4));
        answer.add(new CircleBumper(13, 3));
        answer.add(new TriangleBumper(7, 17));
        answer.add(new SquareBumper(7, 17));
        answer.add(new RightFlipper(13, 14));
        answer.add(new Absorber(0, 18, 20, 1));
        answer.add(new CircleBumper(4, 18));
        ballsAnswer.add(new Ball(new Vect(2.1, 3.2), 1,2));
        ballsAnswer.add(new Ball(new Vect(-1.9, 4.3), 5, 6));
        
        assertEquals(theBoard.testHelperGetGadgets(), answer);
        assert(theBoard.getBalls().containsAll(ballsAnswer));


    }
    
    
    // ============ getGadgetWithMinimumTimeToCollision ===============
    
    // tests 0 other gadgets in addition to wall + collides first with wall
    @Test public void testGetGadgetWithMinimumTimeToCollisionOnlyWalls() {
        Board board = new Board();
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball ball = new Ball(new Vect(0, 1), 5, 5);
        gadgets.add(ball);
        board.insertGadgets(gadgets);
        
        Gadget minimumTimeGadget = board.getGadgetWithMinimumTimeToCollision(ball, 
                board.testHelperGetGadgets());
        Wall bottomWall = new Wall(0, 21, 20, 21, WallDescription.BOTTOM);
        assertEquals(bottomWall, minimumTimeGadget);
    }
    
    // tests 1 other gadget + collides first with ball
    @Test public void testGetGadgetWithMinimumTimeToCollisionOneOtherBall() {
        Board board = new Board();
        List<Gadget> gadgets = new ArrayList<Gadget>();
        Ball ball = new Ball(new Vect(0, 5), 10, 10);
        Ball otherBall = new Ball(new Vect(0, -5), 10, 15);
        
        gadgets.add(ball);
        gadgets.add(otherBall);
        board.insertGadgets(gadgets);
        
        Gadget minimumTimeGadget = board.getGadgetWithMinimumTimeToCollision(ball, 
                board.testHelperGetGadgets());
        assertEquals(otherBall, minimumTimeGadget);
    }
    

    
    // ======================== applyFriction ========================
    
    // should match formula in problem set
    @Test public void testApplyFriction() {
        double MU = 0.025;
        double MU2 = 0.025;
        double deltaTime = 0.005;
        Board board = new Board();
        
        Vect testVelocity = new Vect(3, 5);
        Vect actualVelocity = testVelocity.times(
                1 - MU * deltaTime - MU2 * testVelocity.length() * deltaTime);
        
        Vect predictedVelocity = board.applyFriction(testVelocity, deltaTime);
        assertEquals(actualVelocity, predictedVelocity);
    }
    
    

    
    // ======================== toPrint ========================
    @Test public void testToPrintBoardHasNoGadgets() {
        Board theBoard = new Board();      
        String answer = "......................\n" + 
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
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "......................\n";
        
        assertEquals(theBoard.toPrint(), answer);
                        
    }
    
    @Test public void testToPrintBoardHasNoGadgetsInvisibleWallTop() {
        Board myBoard = new Board();
        myBoard.findAndUpdateInvisibleWall("top", "Board1");
        String answer = "........Board1........\n" + 
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
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "......................\n";
        
        assertEquals(myBoard.toPrint(), answer);
                        
    }
    
    @Test public void testToPrintBoardHasNoGadgetsInvisibleWallLeft() {
        Board myBoard = new Board();
        myBoard.findAndUpdateInvisibleWall("left", "BOARD1");
        String answer = "......................\n" + 
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "B                    .\n" +
                        "O                    .\n" +
                        "A                    .\n" +
                        "R                    .\n" +
                        "D                    .\n" +
                        "1                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "......................\n";
        
        assertEquals(myBoard.toPrint(), answer);
                        
    }
    
    @Test public void testToPrintBoardHasNoGadgetsInvisibleWallMadeSolid() {
        Board myBoard = new Board();
        myBoard.findAndUpdateInvisibleWall("left", "BOARD1");
        myBoard.findAndUpdateSolidWall("BOARD1");
        String answer = "......................\n" + 
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
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "......................\n";
        
        assertEquals(myBoard.toPrint(), answer);
                        
    }
    
    @Test public void testToPrintBoardHasNoGadgetsInvisibleWallAll() {
        Board myBoard = new Board();
        myBoard.findAndUpdateInvisibleWall("left", "BOARD1");
        myBoard.findAndUpdateInvisibleWall("right", "BOARD2");
        myBoard.findAndUpdateInvisibleWall("top", "Board1");
        myBoard.findAndUpdateInvisibleWall("bottom", "Board2");
        String answer = "........Board1........\n" + 
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        "B                    .\n" +
                        "O                    B\n" +
                        "A                    O\n" +
                        "R                    A\n" +
                        "D                    R\n" +
                        "1                    D\n" +
                        ".                    2\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".......Board2.........\n";
        
        assertEquals(myBoard.toPrint(), answer);
                        
    }
    
    @Test public void testToPrintBoardHasMultipleGadgetsBallBumperAbsorberFlipper() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new SquareBumper(3, 4));
        gadgets.add(new Ball(new Vect(-1.9, 4.3), 5, 6));
        gadgets.add(new Ball(new Vect(2.1, 3.2), 1,2));
        gadgets.add(new CircleBumper(13, 3));
        gadgets.add(new TriangleBumper(7, 17));
        //gadgets.add(new SquareBumper(7, 17));
        gadgets.add(new RightFlipper(13, 14));
        gadgets.add(new Absorber(0, 19, 20, 1));
        
        Board theBoard = new Board();
        theBoard.insertGadgets(gadgets);

        String answer = "......................\n" + 
                        ".                    .\n" +
                        ".                    .\n" +
                        ". *                  .\n" +
                        ".             O      .\n" +
                        ".   #                .\n" +
                        ".                    .\n" +
                        ".     *              .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".              |     .\n" +
                        ".              |     .\n" +
                        ".                    .\n" +
                        ".       /            .\n" +
                        ".                    .\n" +
                        ".====================.\n" +
                        "......................\n";
        assertEquals(theBoard.toPrint(), answer);
    }
    
    @Test public void testToPrintBoardHasMultipleFlipperTriangleBumperAlternateOrientations() {
        List<Gadget> gadgets = new ArrayList<Gadget>();
        gadgets.add(new SquareBumper(3, 4));
        gadgets.add(new Ball(new Vect(-1.9, 4.3), 5, 6));
        gadgets.add(new Ball(new Vect(2.1, 3.2), 1,2));
        gadgets.add(new CircleBumper(13, 3));
        gadgets.add(new TriangleBumper(7, 17, Angle.DEG_90));
        //gadgets.add(new SquareBumper(7, 17));
        gadgets.add(new LeftFlipper(13, 14, Angle.DEG_90)); //horizontal? 
        gadgets.add(new Absorber(0, 19, 20, 1));
        
        Board theBoard = new Board();
        theBoard.insertGadgets(gadgets);

        String answer = "......................\n" + 
                        ".                    .\n" +
                        ".                    .\n" +
                        ". *                  .\n" +
                        ".             O      .\n" +
                        ".   #                .\n" +
                        ".                    .\n" +
                        ".     *              .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".             --     .\n" +
                        ".                    .\n" +
                        ".                    .\n" +
                        ".       \\            .\n" +
                        ".                    .\n" +
                        ".====================.\n" +
                        "......................\n";
        
        assertEquals(theBoard.toPrint(), answer);
    }
    
    
    
}





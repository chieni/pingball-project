package grammar;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.junit.*;

import phase1.*;
import physics.Angle;
import physics.Vect;
import server.Pingball;
public class GrammarTest {
/**
 * Testing strategy:
 * - unit tests on BoardCreator (testing each method)
 *  + All possible constructions tested (i.e. no orientation provided vs. orientation provided)
 * - testing the output board from BoardFileProcessor for all example boards to confirm accuracy
 *  + Making sure all expected gadgets are present
 *  + Making sure the gadgets have the expected triggers
 * @throws IOException 
 * 
 */
    
/*-------------------------------------------------
 * BoardCreator Unit Tests
 *------------------------------------------------*/
 
    @Test
    public void getNameTest(){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("name", "thisName");
        properties.put("NAME", "notName");
        properties.put("nAme", "notName");
        properties.put("rand", "notName");
        assertEquals(BoardCreator.getName(properties), "thisName");
    }
    
    @Test
    public void createSquareBumperTest(){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "11");
        properties.put("xx", "20");
        properties.put("yy", "20");
        assertEquals(BoardCreator.createSquareBumper(properties), new SquareBumper(10, 11));
    }
    
    @Test
    public void createCircleBumperTest(){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "11");
        properties.put("xx", "20");
        properties.put("yy", "20");
        assertEquals(BoardCreator.createCircleBumper(properties), new CircleBumper(10, 11));
    }
    
    @Test
    public void createTriangleBumperWithOrientationTest(){
        // With orientation
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "11");
        properties.put("xx", "20");
        properties.put("yy", "20");
        properties.put("orientation", "90");
        assertEquals(BoardCreator.createTriangleBumper(properties),new TriangleBumper(10, 11, Angle.DEG_90));
    }
    
    @Test
    public void createTriangleBumperWithoutOrientationTest(){
        // Without orientation
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "11");
        properties.put("xx", "20");
        properties.put("yy", "20");
        assertEquals(BoardCreator.createTriangleBumper(properties),new TriangleBumper(10, 11));
    }
    
    @Test
    public void createBallTest(){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "2.25");
        properties.put("y", "7.5");
        Vect velocity = new Vect(0.1, 0.0);
        properties.put("xVelocity", "0.1");
        properties.put("yVelocity", "0.0");
        Ball ball = new Ball(velocity, 2.25, 7.5);
        Ball createdBall = BoardCreator.createBall(properties);
        assertEquals(createdBall.getCenter(), ball.getCenter());
    }
    
    @Test
    public void createLeftFlipperTest(){
        // With orientation
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "10");
        properties.put("orientation", "90");
        assertEquals(BoardCreator.createLeftFlipper(properties), new LeftFlipper(10, 10, Angle.DEG_90));
        // Without orientation
        HashMap<String, String> properties2 = new HashMap<String, String>();
        properties2.put("x", "10");
        properties2.put("y", "10");
        assertEquals(BoardCreator.createLeftFlipper(properties2), new LeftFlipper(10, 10));
    }
    
    @Test
    public void createRightFlipperTest(){
        // With orientation
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "10");
        properties.put("y", "10");
        properties.put("orientation", "90");
        assertEquals(BoardCreator.createRightFlipper(properties), new RightFlipper(10, 10, Angle.DEG_90));
        // Without orientation
        HashMap<String, String> properties2 = new HashMap<String, String>();
        properties2.put("x", "10");
        properties2.put("y", "10");
        assertEquals(BoardCreator.createRightFlipper(properties2), new RightFlipper(10, 10));
    }
    
    @Test
    public void createAbsorberTest(){
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("x", "0");
        properties.put("y", "20");
        properties.put("width", "20");
        properties.put("height", "3");
        assertEquals(BoardCreator.createAbsorber(properties), new Absorber(0,20,20,3));
    }

/*-------------------------------------------------
 * Example Board Accuracy Tests
 *------------------------------------------------*/

    /**
     * It is additionally possible to visually test each of the provided boards by running them as Single-Machine Play
     */
    private static Optional<File> getFileForTest(String flag) throws IOException{
        Optional<File> file = Optional.empty();   
        file = Optional.of(new File("resources/" + flag));
        return file;
    }
    @Test
    public void absorberTest() throws IOException{
        String flag = "absorber.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
    
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();

        // Visually test that the board looks accurate.
        System.out.println(board.toString());
        
        String expectedFires = "{CircleC=[Abs], CircleB=[Abs], CircleA=[Abs], CircleE=[Abs], CircleD=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "Absorber");
    }
    
    @Test
    public void defaultTest() throws IOException{
        String flag = "default.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "Default");   
        System.out.println(board.toString());
    }
    
    @Test
    public void flippersTest() throws IOException{
        String flag = "flippers.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));  
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{CircleC=[FlipA], Abs=[FlipE, FlipF, Abs], CircleF=[FlipD], CircleE=[FlipC]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "Flippers");
        System.out.println(board.toString());
    }
    
    @Test
    public void multiplayerLeftTest() throws IOException{
        String flag = "multiplayer_left.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "MultiplayerLeft");
        System.out.println(board.toString());
    }
    
    @Test
    public void multiplayerRightTest() throws IOException{
        String flag = "multiplayer_right.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "MultiplayerRight");
        System.out.println(board.toString());
    }
    
    
    @Test
    public void sampleBoard1Test() throws IOException{
        String flag = "sampleBoard1.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "sampleBoard1");
        System.out.println(board.toString());
    }
    
    @Test
    public void sampleBoard2Test() throws IOException{
        String flag = "sampleBoard2-1.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "sampleBoard2_1");
        System.out.println(board.toString());
    }
    
    @Test
    public void sampelBoard22Test() throws IOException{
        String flag = "sampleBoard2-2.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "sampleBoard2_2");
        System.out.println(board.toString());
    }
    
    @Test
    public void sampleBoard3Test() throws IOException{
        String flag = "sampleBoard3.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{SquareD=[FlipL], SquareC=[FlipL], SquareB=[FlipL], Abs=[Abs], Square=[FlipL], SquareH=[FlipL], SquareG=[FlipL], SquareF=[FlipL], SquareE=[FlipL]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "ExampleB");
        System.out.println(board.toString());
    }
    
    @Test
    public void sampleBoard4Test() throws IOException{
        String flag = "sampleBoard4.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "ExampleA");
        System.out.println(board.toString());
    }
    
    @Test
    public void simpleBoardTest() throws IOException{
        String flag = "simple_board.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{Abs=[Abs]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "SimpleBoard");
        System.out.println(board.toString());
    }
    
    @Test
    public void triggersTest() throws IOException{
        String flag = "triggers.pb";
        BoardFileProcessor processor = new BoardFileProcessor(GrammarTest.getFileForTest(flag));
        Board board = processor.createBoard();
        String boardName = processor.getBoardName();
        String expectedFires = "{SquareD=[FlipL], SquareC=[FlipL], SquareB=[FlipL], Abs=[Abs], Square=[FlipL], SquareH=[FlipL], SquareG=[FlipL], SquareF=[FlipL], SquareE=[FlipL]}";
        assertEquals(processor.getFires().toString(), expectedFires);
        assertEquals(boardName, "Triggers");
        System.out.println(board.toString());
    }

}
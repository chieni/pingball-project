package grammar;
import phase1.*;
import physics.*;

import java.io.File;
import java.util.*;
/**
 * This class works to create a board from data in the form of the HashMaps and Lists that it receives
 * from BoardGrammarLaoder, the class that processes information garnered from our ANTLR grammar. 
 * 
 * It contains helper methods that create various gadgets, and one method that creates the Board itself,
 * which is more comprehensive.
 * @author IsabelChien
 *
 */
public class BoardCreator {
    public final HashMap<String,String> board;
    public final List<HashMap<String, String>> balls;
    // Maps gadgetType -> ArrayList of such gadgets -> HashMap of properties of those gadgets
    public final HashMap<String, ArrayList<HashMap<String, String>>> gadgetsMap;
    public final HashMap<String, ArrayList<String>> fires;

    public static final HashMap<Integer, Angle> angles;
    public static final ArrayList<String> gadgetTypes;
    static
    {
        angles = new HashMap<Integer, Angle>();
        angles.put(0, Angle.ZERO);
        angles.put(90, Angle.DEG_90);
        angles.put(180, Angle.DEG_180);
        angles.put(270, Angle.DEG_270);  
        
        gadgetTypes = new ArrayList<String>();
        gadgetTypes.add("squareBumper");
        gadgetTypes.add("circleBumper");
        gadgetTypes.add("triangleBumper");
        gadgetTypes.add("leftFlipper");
        gadgetTypes.add("rightFlipper");
        gadgetTypes.add("absorber");
    }
    
    public BoardCreator(HashMap<String,String> board, List<HashMap<String, String>> balls, 
            HashMap<String, ArrayList<HashMap<String, String>>> gadgetsMap, HashMap<String, ArrayList<String>>  fires){
        this.board = board;
        this.balls = balls;
        this.gadgetsMap = gadgetsMap;
        this.fires = fires;
        
        checkRep();
    }
    
    /**
     * Checks the representation of BoardCreator, namely that the board HashMap is not empty. All of the other parameters may be null,
     * but there must be information about the board, including its name.
     */
    private void checkRep(){
        boolean goodRep = (this.board != null) && (this.getBoardName().length() > 0);
        if (!goodRep){
            throw new RuntimeException("BoardCreator not initialized properly, there must be a non-empty board HashMap passed in.");
        }
    }
    
    /**
     * Extracts the name of the board.
     * @return String name of the board
     */
    public String getBoardName(){
        return BoardCreator.getName(this.board);
    }
    
    /**
     * Getter for the gadgetsMap, which maps all of the gadgets to their types and properties.
     * @return
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> getGadgetsMap(){
        return this.gadgetsMap;
    }
    
    /**
     * Getter for the firesMap, which maps a gadget by name to an ArrayList containing
     * the gadgets, by name, of the gadgets it triggers.
     * @return
     */
    public HashMap<String, ArrayList<String>> getFiresMap(){
        return this.fires;
    }
    
    /**
     * Creates the Board object with all of the gadgets filled in and all of the appropriate
     * triggers added.
     * @return Board board 
     */
    public Board createBoard(){
        // Create board with defined properties
        Optional<Double> gravity = getBoardProperty(board, "gravity");
        Optional<Double> mu = getBoardProperty(board, "friction1");
        Optional<Double> mu2 = getBoardProperty(board, "friction2");
        
        Board board = new Board(gravity, mu, mu2);
        List<Gadget> gadgets = new ArrayList<Gadget>();
        HashMap<String, Gadget> gadgetsWithoutTriggers = new HashMap<String, Gadget> ();
        // Create all balls and insert into gadget list
        for (HashMap<String, String> ballProperties: balls){
            gadgets.add(createBall(ballProperties));
        }
        // Create all gadgets and inserts into HashMap with names pointing to the Gadget object, later used 
        // to set triggers
        for (String gadgetType: gadgetTypes){
            // If the HashMap of potential gadgets contains the key of a gadget that exists
            if (gadgetsMap.containsKey(gadgetType)){
                // Identify the gadget type and then create the appropriate Gadget objects
                switch(gadgetType){
                    case "squareBumper":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createSquareBumper(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                    case "triangleBumper":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createTriangleBumper(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                    case "circleBumper":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createCircleBumper(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                    case "leftFlipper":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createLeftFlipper(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                    case "rightFlipper":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createRightFlipper(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                    case "absorber":
                        for (HashMap<String, String> gadgetProperties: gadgetsMap.get(gadgetType)){
                            String name = BoardCreator.getName(gadgetProperties);
                            Gadget gadget = BoardCreator.createAbsorber(gadgetProperties);
                            gadgetsWithoutTriggers.put(name, gadget);
                        }
                        break;
                }
            }
        }
        
        // Set triggers
        for (String gadgetName: fires.keySet()){
            Gadget trigger = gadgetsWithoutTriggers.get(gadgetName);
            for (String trigeeName: fires.get(gadgetName)){
                Gadget trigee = gadgetsWithoutTriggers.get(trigeeName);
                if (trigger instanceof Absorber){
                    if (gadgetName.equals(trigeeName)){
                        trigger.makeSelfTriggering();
                    }
                    else {
                        trigger.addTrigger(trigee);
                    }
                }
                else {
                    trigger.addTrigger(trigee);
                }
                
            }
        }
        
        // Add all gadgets to board
        for (String gadgetName: gadgetsWithoutTriggers.keySet()){
            gadgets.add(gadgetsWithoutTriggers.get(gadgetName));
        }
        board.insertGadgets(gadgets);
        // Return the board
        return board;
    }
    
    /**
     * Extracts the "name" property from a HashMap of properties
     * @param properties, HashMap that maps property id to property value
     * @return the value of the property "name"
     */
    public static String getName(HashMap<String, String> properties){
        if (properties.containsKey("name")){
            return properties.get("name");
        }
        else {
            throw new RuntimeException("Object does not have expected property of name");
        }
    }
    
    public static Optional<Double> getBoardProperty(HashMap<String, String> properties, String property){
        if (properties.containsKey(property)){
            return Optional.of(Double.parseDouble(properties.get(property)));
        }
        else {
            return Optional.empty();
        }
    }
    /**
     * Creates a SquareBumper from a given HashMap of properties
     * @param HashMap properties
     * @return SquareBumper 
     */
    public static SquareBumper createSquareBumper(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y")){
            return new SquareBumper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")));
        }
        else {
            throw new RuntimeException("File not formatted correctly on SquareBumper creation.");
        }
        
    }
    /**
     * Creates a CircleBumper from a given HashMap of properties
     * @param HashMap properties
     * @return CircleBumper 
     */
    public static CircleBumper createCircleBumper(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y")){
            return new CircleBumper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")));
        }
        else {
            throw new RuntimeException("File not formatted correctly on CircleBumper creation.");
        }
    }
    
    /**
     * Creates a TriangleBumper from a given HashMap of properties
     * @param HashMap properties
     * @return TriangleBumper 
     */
    public static TriangleBumper createTriangleBumper(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y")){
            if (properties.containsKey("orientation")){
                if (BoardCreator.angles.containsKey(Integer.parseInt(properties.get("orientation")))){
                    return new TriangleBumper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")), BoardCreator.angles.get(Integer.parseInt(properties.get("orientation"))));
                }
                else {
                    throw new RuntimeException("File not formatted correctly on TriangleBumper creation.");
                }
            }
            else {
                return new TriangleBumper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")));
            }
        }
        else {
            throw new RuntimeException("File not formatted correctly on TriangleBumper creation.");
        }
    }
    
    /**
     * Creates a Ball from a given HashMap of properties
     * @param HashMap properties
     * @return Ball ball
     */
    public static Ball createBall(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y") && properties.containsKey("xVelocity") && properties.containsKey("yVelocity")){
            float xVelocity = Float.parseFloat(properties.get("xVelocity"));
            float yVelocity = Float.parseFloat(properties.get("yVelocity"));
            float x = Float.parseFloat(properties.get("x"));
            float y = Float.parseFloat(properties.get("y"));
            return new Ball(new Vect(xVelocity, yVelocity), x, y);
        }
        else {
            throw new RuntimeException("File not formatted correctly on Ball creation.");
        }
    }
    
    /**
     * Creates a LeftFlipper from a given HashMap of properties
     * @param properties
     * @return LeftFlipper 
     */
    public static LeftFlipper createLeftFlipper(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y")){
            if (properties.containsKey("orientation")){
                if (BoardCreator.angles.containsKey(Integer.parseInt(properties.get("orientation")))){
                    return new LeftFlipper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")), BoardCreator.angles.get(Integer.parseInt(properties.get("orientation"))));
                }
                else {
                    throw new RuntimeException("File not formatted correctly on LeftFlipper creation.");
                }
            }
            else {
                return new LeftFlipper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")));
            }
        }
        else {
            throw new RuntimeException("File not formatted correctly on LeftFlipper creation.");
        }
    }
    
    /**
     * Creates a RightFlipper from a given set of properties
     * @param properties
     * @return RightFlipper
     */
    public static RightFlipper createRightFlipper(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y")){
            if (properties.containsKey("orientation")){
                if (BoardCreator.angles.containsKey(Integer.parseInt(properties.get("orientation")))){
                    return new RightFlipper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")), BoardCreator.angles.get(Integer.parseInt(properties.get("orientation"))));
                }
                else {
                    throw new RuntimeException("File not formatted correctly on LeftFlipper creation.");
                }
            }
            else {
                return new RightFlipper(Integer.parseInt(properties.get("x")), Integer.parseInt(properties.get("y")));
            }
        }
        else {
            throw new RuntimeException("File not formatted correctly on RightFlipper creation.");
        }
    }
    
    /**
     * Creates an absorber from a given set of properties
     * @param properties
     * @return Absorber
     */
    public static Absorber createAbsorber(HashMap<String, String> properties){
        if (properties.containsKey("x") && properties.containsKey("y") && properties.containsKey("width") && properties.containsKey("height")){
           int x = Integer.parseInt(properties.get("x"));
           int y = Integer.parseInt(properties.get("y"));
           int width = Integer.parseInt(properties.get("width"));
           int height = Integer.parseInt(properties.get("height"));
           return new Absorber(x, y, width, height);
        }
        else {
            throw new RuntimeException("File not formatted correctly on Absorber creation.");
        }
    }
}

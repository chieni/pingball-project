package grammar;
import java.util.*;

import org.antlr.v4.misc.OrderedHashMap;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.*;

/**
 * This class extends BoardGrammarBaseListener, which is a class provided
 * by ANTLR that processes information received while traversing the ANTLR
 * created grammar tree. When traversing certain nodes, information is added to the
 * private instance variables that are later used in BoardCreator to create
 * a Board object.
 * @author IsabelChien
 *
 */
public class BoardGrammarLoader extends BoardGrammarBaseListener{
    private HashMap<String,String> board = new HashMap<String, String>();
    private List<HashMap<String, String>> balls = new ArrayList<HashMap<String, String>>();
    private HashMap<String, ArrayList<HashMap<String, String>>> gadgets = new HashMap<String, ArrayList<HashMap<String, String>>>();
    private HashMap<String, ArrayList<String>> fires = new HashMap<String, ArrayList<String>>();

    /**
     * Getter method for this.board
     * @return HashMap<String, String> board
     */
    public HashMap<String,String> getBoard(){
        return this.board;
    }
    
    /**
     * Getter method for this.balls
     * @return List<HashMap<String, String>> balls
     */
    public List<HashMap<String, String>> getBalls(){
        return this.balls;
    }
    
    /**
     * Getter method for this.gadgets
     * @return HashMap<String, ArrayList<HashMap<String, String>>> gadgets
     */
    public HashMap<String, ArrayList<HashMap<String, String>>> getGadgets(){
        return this.gadgets;
    }
    
    /**
     * Getter method for this.fires
     * @return HashMap<String, ArrayList<String>> fires
     */
    public HashMap<String, ArrayList<String>> getFires(){
        return this.fires;
    }
    
    @Override 
    /**
     * Upon exiting the board node, retrieves all of the properties (id and value) and puts
     * it into the board HashMap.
     */
    public void exitBoard(@NotNull BoardGrammarParser.BoardContext ctx) { 
        // Iterating through all of the properties
        for (int i = 1; i<ctx.getChildCount(); i++){
            board.put(ctx.getChild(i).getChild(0).getText(),ctx.getChild(i).getChild(2).getText());
        }
    }
    @Override 
    /**
     * Upon exiting each ball node, puts the properties in a HashMap and adds that
     * HashMap to the list of balls
     */
    public void exitBall(@NotNull BoardGrammarParser.BallContext ctx) {
        HashMap<String, String> ball = new HashMap<String, String>();
        for (int i = 1; i<ctx.getChildCount(); i++){
            ball.put(ctx.getChild(i).getChild(0).getText(),ctx.getChild(i).getChild(2).getText());
        }
        balls.add( ball);
        
    }
    @Override 
    /**
     * Upon exiting each gadget node, puts the properties into a Hash and adds that to a list of hashes for the
     * appropriate gadget type, then adds that to a hash that hashes from the gadget type to that list.
     * (Sorry it's a bit of a confusing data structure)
     * HashMap(gadgetType -> List(HashMap(property -> value)))
     */
    public void exitGadget(@NotNull BoardGrammarParser.GadgetContext ctx) { 
        HashMap<String, String> gadget = new HashMap<String, String>();
        String id = ctx.getChild(0).getText();
        for (int i = 1; i<ctx.getChildCount(); i++){
            gadget.put(ctx.getChild(i).getChild(0).getText(),ctx.getChild(i).getChild(2).getText());
        }
        if (gadgets.containsKey(id)){
            ArrayList<HashMap<String, String>> existingList = gadgets.get(id);
            existingList.add(gadget);
            gadgets.put(id, existingList);
        }
        else {
            ArrayList<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
            newList.add(gadget);
            gadgets.put(id, newList);
        }
    }
    
    /**
     * Upon exiting a fire node, hashes the name of the gadget that is the trigger
     * to a list of gadgets that are triggered by it (appends to a list if it already exists,
     * creates new key and list if key isn't already present).
     */
    @Override 
    public void exitFire(@NotNull BoardGrammarParser.FireContext ctx) { 
        String trigger = "";
        String action = "";
        for (int i = 1; i<ctx.getChildCount(); i++){
            if (ctx.getChild(i).getChild(0).getText().equals("trigger")) {
                trigger = ctx.getChild(i).getChild(2).getText();
            }
            else if (ctx.getChild(i).getChild(0).getText().equals("action")){
                action = ctx.getChild(i).getChild(2).getText();
            }
            
        }
        
        if (fires.containsKey(trigger)){
            ArrayList<String> trigees = fires.get(trigger);
            trigees.add(action);
            fires.put(trigger, trigees);
        }
        else{
            ArrayList<String> trigees = new ArrayList<String>();
            trigees.add(action);
            fires.put(trigger, trigees);
        }
        
    }
}

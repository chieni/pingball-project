package grammar;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import phase1.Board;

/**
 * This class does the overarching processing from the grammar level to the BoardCreator level.
 * 
 * @author IsabelChien
 *
 */
public class BoardFileProcessor {
    
    private BoardCreator boardCreator;
    
    /**
     * Takes in the name of the file that we want to turn into a Board, reads the file,
     * processes it via ANTLR generated lexer/parser methods that were generated from the grammar BoardGrammar.
     * Then it walks the tree created by the parser, then runs the loader on it to process the data
     * in a way that BoardCreator can finally use to create the actual Board object.
     * @param fileName
     * @throws IOException
     */
    public BoardFileProcessor(Optional<File> fileName) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName.get()));
        String file = "";
        String line;

        while((line = bufferedReader.readLine())!=null){
            file += line + "\n";
        }
        
        bufferedReader.close();
        
        ANTLRInputStream input = new ANTLRInputStream(file);
        BoardGrammarLexer lexer = new BoardGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BoardGrammarParser parser = new BoardGrammarParser(tokens);
        ParseTree tree = parser.file();
        
        ParseTreeWalker walker = new ParseTreeWalker();
        BoardGrammarLoader loader = new BoardGrammarLoader();
        walker.walk(loader, tree);
        this.boardCreator = new BoardCreator(loader.getBoard(), loader.getBalls(), loader.getGadgets(), loader.getFires());
        
        checkRep();
    }
    
    /**
     * Checks the representation of BoardFileProcessor, namely that boardCreator created in the constructor
     * is not null, because it is necessary for all other actions.
     */
    private void checkRep(){
        boolean goodRep = (this.boardCreator != null) && (this.getBoardName().length() > 0);
        if (!goodRep){
            throw new RuntimeException("BoardFileProcessor not initialized properly.");
        }
    }

    /**
     * Returns the board created by boardCreator.
     * @param fileName
     * @return
     * @throws IOException
     */
    public Board createBoard() {

        Board board = this.boardCreator.createBoard();
        return board;
    }
    
    /**
     * Returns the name of the board via boardCreator.
     * @return String boardName
     */
    public String getBoardName(){
        String boardName = this.boardCreator.getBoardName();
        return boardName;
    }
    
    /**
     * Returns the HashMap that contains the name of gadgets hashing to an
     * ArrayList of gadgets (by name) that it triggers.
     */
    public HashMap<String, ArrayList<String>> getFires(){
        return this.boardCreator.getFiresMap();
    }
}

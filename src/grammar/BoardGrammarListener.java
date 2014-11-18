// Generated from BoardGrammar.g4 by ANTLR 4.4
package grammar;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BoardGrammarParser}.
 */
public interface BoardGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#ball}.
	 * @param ctx the parse tree
	 */
	void enterBall(@NotNull BoardGrammarParser.BallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#ball}.
	 * @param ctx the parse tree
	 */
	void exitBall(@NotNull BoardGrammarParser.BallContext ctx);
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#file}.
	 * @param ctx the parse tree
	 */
	void enterFile(@NotNull BoardGrammarParser.FileContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#file}.
	 * @param ctx the parse tree
	 */
	void exitFile(@NotNull BoardGrammarParser.FileContext ctx);
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#gadget}.
	 * @param ctx the parse tree
	 */
	void enterGadget(@NotNull BoardGrammarParser.GadgetContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#gadget}.
	 * @param ctx the parse tree
	 */
	void exitGadget(@NotNull BoardGrammarParser.GadgetContext ctx);
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#property}.
	 * @param ctx the parse tree
	 */
	void enterProperty(@NotNull BoardGrammarParser.PropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#property}.
	 * @param ctx the parse tree
	 */
	void exitProperty(@NotNull BoardGrammarParser.PropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#fire}.
	 * @param ctx the parse tree
	 */
	void enterFire(@NotNull BoardGrammarParser.FireContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#fire}.
	 * @param ctx the parse tree
	 */
	void exitFire(@NotNull BoardGrammarParser.FireContext ctx);
	/**
	 * Enter a parse tree produced by {@link BoardGrammarParser#board}.
	 * @param ctx the parse tree
	 */
	void enterBoard(@NotNull BoardGrammarParser.BoardContext ctx);
	/**
	 * Exit a parse tree produced by {@link BoardGrammarParser#board}.
	 * @param ctx the parse tree
	 */
	void exitBoard(@NotNull BoardGrammarParser.BoardContext ctx);
}
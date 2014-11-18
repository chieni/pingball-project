// Generated from BoardGrammar.g4 by ANTLR 4.4
package grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BoardGrammarLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EQUALS=1, BOARD=2, BALL=3, GADGET=4, FIRE=5, ID=6, VALUE=7, FLOAT=8, INT=9, 
		NAME=10, WS=11, COMMENT=12, NEGATIVE=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'"
	};
	public static final String[] ruleNames = {
		"EQUALS", "BOARD", "BALL", "GADGET", "FIRE", "ID", "VALUE", "FLOAT", "DIGIT", 
		"INT", "NAME", "WS", "COMMENT", "NEGATIVE"
	};


	public BoardGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BoardGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17\u0101\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\5\5r\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u00cc\n\7\3\b\3\b\3\b\5\b\u00d1"+
		"\n\b\3\t\5\t\u00d4\n\t\3\t\6\t\u00d7\n\t\r\t\16\t\u00d8\3\t\5\t\u00dc"+
		"\n\t\3\t\7\t\u00df\n\t\f\t\16\t\u00e2\13\t\3\n\3\n\3\13\6\13\u00e7\n\13"+
		"\r\13\16\13\u00e8\3\f\3\f\6\f\u00ed\n\f\r\f\16\f\u00ee\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\7\16\u00f7\n\16\f\16\16\16\u00fa\13\16\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\u00f8\2\20\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\2\25\13"+
		"\27\f\31\r\33\16\35\17\3\2\7\3\2\62;\5\2C\\aac|\6\2\62;C\\aac|\5\2\13"+
		"\f\17\17\"\"\4\2\f\f\17\17\u0118\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\25\3\2"+
		"\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2"+
		"\5!\3\2\2\2\7\'\3\2\2\2\tq\3\2\2\2\13s\3\2\2\2\r\u00cb\3\2\2\2\17\u00d0"+
		"\3\2\2\2\21\u00d3\3\2\2\2\23\u00e3\3\2\2\2\25\u00e6\3\2\2\2\27\u00ea\3"+
		"\2\2\2\31\u00f0\3\2\2\2\33\u00f4\3\2\2\2\35\u00ff\3\2\2\2\37 \7?\2\2 "+
		"\4\3\2\2\2!\"\7d\2\2\"#\7q\2\2#$\7c\2\2$%\7t\2\2%&\7f\2\2&\6\3\2\2\2\'"+
		"(\7d\2\2()\7c\2\2)*\7n\2\2*+\7n\2\2+\b\3\2\2\2,-\7u\2\2-.\7s\2\2./\7w"+
		"\2\2/\60\7c\2\2\60\61\7t\2\2\61\62\7g\2\2\62\63\7D\2\2\63\64\7w\2\2\64"+
		"\65\7o\2\2\65\66\7r\2\2\66\67\7g\2\2\67r\7t\2\289\7e\2\29:\7k\2\2:;\7"+
		"t\2\2;<\7e\2\2<=\7n\2\2=>\7g\2\2>?\7D\2\2?@\7w\2\2@A\7o\2\2AB\7r\2\2B"+
		"C\7g\2\2Cr\7t\2\2DE\7v\2\2EF\7t\2\2FG\7k\2\2GH\7c\2\2HI\7p\2\2IJ\7i\2"+
		"\2JK\7n\2\2KL\7g\2\2LM\7D\2\2MN\7w\2\2NO\7o\2\2OP\7r\2\2PQ\7g\2\2Qr\7"+
		"t\2\2RS\7n\2\2ST\7g\2\2TU\7h\2\2UV\7v\2\2VW\7H\2\2WX\7n\2\2XY\7k\2\2Y"+
		"Z\7r\2\2Z[\7r\2\2[\\\7g\2\2\\r\7t\2\2]^\7t\2\2^_\7k\2\2_`\7i\2\2`a\7j"+
		"\2\2ab\7v\2\2bc\7H\2\2cd\7n\2\2de\7k\2\2ef\7r\2\2fg\7r\2\2gh\7g\2\2hr"+
		"\7t\2\2ij\7c\2\2jk\7d\2\2kl\7u\2\2lm\7q\2\2mn\7t\2\2no\7d\2\2op\7g\2\2"+
		"pr\7t\2\2q,\3\2\2\2q8\3\2\2\2qD\3\2\2\2qR\3\2\2\2q]\3\2\2\2qi\3\2\2\2"+
		"r\n\3\2\2\2st\7h\2\2tu\7k\2\2uv\7t\2\2vw\7g\2\2w\f\3\2\2\2xy\7p\2\2yz"+
		"\7c\2\2z{\7o\2\2{\u00cc\7g\2\2|\u00cc\4z{\2}~\7z\2\2~\177\7X\2\2\177\u0080"+
		"\7g\2\2\u0080\u0081\7n\2\2\u0081\u0082\7q\2\2\u0082\u0083\7e\2\2\u0083"+
		"\u0084\7k\2\2\u0084\u0085\7v\2\2\u0085\u00cc\7{\2\2\u0086\u0087\7{\2\2"+
		"\u0087\u0088\7X\2\2\u0088\u0089\7g\2\2\u0089\u008a\7n\2\2\u008a\u008b"+
		"\7q\2\2\u008b\u008c\7e\2\2\u008c\u008d\7k\2\2\u008d\u008e\7v\2\2\u008e"+
		"\u00cc\7{\2\2\u008f\u0090\7q\2\2\u0090\u0091\7t\2\2\u0091\u0092\7k\2\2"+
		"\u0092\u0093\7g\2\2\u0093\u0094\7p\2\2\u0094\u0095\7v\2\2\u0095\u0096"+
		"\7c\2\2\u0096\u0097\7v\2\2\u0097\u0098\7k\2\2\u0098\u0099\7q\2\2\u0099"+
		"\u00cc\7p\2\2\u009a\u009b\7y\2\2\u009b\u009c\7k\2\2\u009c\u009d\7f\2\2"+
		"\u009d\u009e\7v\2\2\u009e\u00cc\7j\2\2\u009f\u00a0\7j\2\2\u00a0\u00a1"+
		"\7g\2\2\u00a1\u00a2\7k\2\2\u00a2\u00a3\7i\2\2\u00a3\u00a4\7j\2\2\u00a4"+
		"\u00cc\7v\2\2\u00a5\u00a6\7v\2\2\u00a6\u00a7\7t\2\2\u00a7\u00a8\7k\2\2"+
		"\u00a8\u00a9\7i\2\2\u00a9\u00aa\7i\2\2\u00aa\u00ab\7g\2\2\u00ab\u00cc"+
		"\7t\2\2\u00ac\u00ad\7c\2\2\u00ad\u00ae\7e\2\2\u00ae\u00af\7v\2\2\u00af"+
		"\u00b0\7k\2\2\u00b0\u00b1\7q\2\2\u00b1\u00cc\7p\2\2\u00b2\u00b3\7i\2\2"+
		"\u00b3\u00b4\7t\2\2\u00b4\u00b5\7c\2\2\u00b5\u00b6\7x\2\2\u00b6\u00b7"+
		"\7k\2\2\u00b7\u00b8\7v\2\2\u00b8\u00cc\7{\2\2\u00b9\u00ba\7h\2\2\u00ba"+
		"\u00bb\7t\2\2\u00bb\u00bc\7k\2\2\u00bc\u00bd\7e\2\2\u00bd\u00be\7v\2\2"+
		"\u00be\u00bf\7k\2\2\u00bf\u00c0\7q\2\2\u00c0\u00c1\7p\2\2\u00c1\u00cc"+
		"\7\63\2\2\u00c2\u00c3\7h\2\2\u00c3\u00c4\7t\2\2\u00c4\u00c5\7k\2\2\u00c5"+
		"\u00c6\7e\2\2\u00c6\u00c7\7v\2\2\u00c7\u00c8\7k\2\2\u00c8\u00c9\7q\2\2"+
		"\u00c9\u00ca\7p\2\2\u00ca\u00cc\7\64\2\2\u00cbx\3\2\2\2\u00cb|\3\2\2\2"+
		"\u00cb}\3\2\2\2\u00cb\u0086\3\2\2\2\u00cb\u008f\3\2\2\2\u00cb\u009a\3"+
		"\2\2\2\u00cb\u009f\3\2\2\2\u00cb\u00a5\3\2\2\2\u00cb\u00ac\3\2\2\2\u00cb"+
		"\u00b2\3\2\2\2\u00cb\u00b9\3\2\2\2\u00cb\u00c2\3\2\2\2\u00cc\16\3\2\2"+
		"\2\u00cd\u00d1\5\25\13\2\u00ce\u00d1\5\21\t\2\u00cf\u00d1\5\27\f\2\u00d0"+
		"\u00cd\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00cf\3\2\2\2\u00d1\20\3\2\2"+
		"\2\u00d2\u00d4\7/\2\2\u00d3\u00d2\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6"+
		"\3\2\2\2\u00d5\u00d7\5\23\n\2\u00d6\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2"+
		"\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00db\3\2\2\2\u00da\u00dc"+
		"\7\60\2\2\u00db\u00da\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00e0\3\2\2\2"+
		"\u00dd\u00df\5\23\n\2\u00de\u00dd\3\2\2\2\u00df\u00e2\3\2\2\2\u00e0\u00de"+
		"\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\22\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e3"+
		"\u00e4\t\2\2\2\u00e4\24\3\2\2\2\u00e5\u00e7\t\2\2\2\u00e6\u00e5\3\2\2"+
		"\2\u00e7\u00e8\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9\26"+
		"\3\2\2\2\u00ea\u00ec\t\3\2\2\u00eb\u00ed\t\4\2\2\u00ec\u00eb\3\2\2\2\u00ed"+
		"\u00ee\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\30\3\2\2"+
		"\2\u00f0\u00f1\t\5\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\b\r\2\2\u00f3\32"+
		"\3\2\2\2\u00f4\u00f8\7%\2\2\u00f5\u00f7\13\2\2\2\u00f6\u00f5\3\2\2\2\u00f7"+
		"\u00fa\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\u00fb\3\2"+
		"\2\2\u00fa\u00f8\3\2\2\2\u00fb\u00fc\t\6\2\2\u00fc\u00fd\3\2\2\2\u00fd"+
		"\u00fe\b\16\2\2\u00fe\34\3\2\2\2\u00ff\u0100\7/\2\2\u0100\36\3\2\2\2\r"+
		"\2q\u00cb\u00d0\u00d3\u00d8\u00db\u00e0\u00e8\u00ee\u00f8\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
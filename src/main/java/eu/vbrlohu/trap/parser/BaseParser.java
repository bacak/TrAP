package eu.vbrlohu.trap.parser;

import antlr.LLkParser;
import antlr.ParserSharedInputState;
import antlr.TokenBuffer;
import antlr.TokenStream;

public abstract class BaseParser extends LLkParser {
    
    protected BaseParser(int k) {
        super(k);
    }

    protected BaseParser(ParserSharedInputState parserSharedInputState, int k) {
        super(parserSharedInputState, k);
    }

    protected BaseParser(TokenBuffer tokenBuffer, int k) {
        super(tokenBuffer, k);
    }

    protected BaseParser(TokenStream lexer, int k) {
        super(lexer, k);
    }

}

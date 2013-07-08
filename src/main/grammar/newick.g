
header {
  package eu.vbrlohu.trap.parser;
}

/*

class NewickLexer extends Lexer; 

SPECIES : (('a'..'z'|'A'..'Z'|'0'..'9'|'_')+':')=>(('a'..'z'|'A'..'Z'|'0'..'9'|'_')+) 
          ;

LENGTH : double;

COMMA : ',' ;

SEMI : ';' ;

COLON : ':' ;

LPAREN : '(' ;

RPAREN : ')' ;

WS     :
    (' ' 
    | '\t' 
    | '\r' '\n' { newline(); } 
    | '\n'      { newline(); }
    ) 
    { $setType(Token.SKIP); } 
  ;




class NewickParser extends Parser("BaseParser");
options {
  defaultErrorHandler=false;
}

entry : treeList : treeList tree SEMI;


treeList : treeList tree SEMI;


treeList : tree SEMI;

tree : "(" node "," node ")"

node : node "," node

node : nodeName ":" length

nodeName : SPECIES

length : LENGTH


*/
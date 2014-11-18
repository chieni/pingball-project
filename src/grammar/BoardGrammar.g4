grammar BoardGrammar;

/* ---------------------------------------------
* PARSER RULES
* ----------------------------------------------*/
file : board ball* gadget* fire*;

board: BOARD property+;
ball: BALL property+;
gadget: GADGET property+;
fire: FIRE property+;
property: ID EQUALS VALUE;	

/*---------------------------------------------------
* LEXER RULES
* --------------------------------------------------*/
EQUALS: ('=');
BOARD: ('board');
BALL: ('ball');
GADGET	: ('squareBumper'|'circleBumper'|'triangleBumper'
          |'leftFlipper'|'rightFlipper'|'absorber');
FIRE: ('fire');

ID	:	 ('name'| 'x'| 'y'| 'xVelocity'| 'yVelocity'| 'orientation'
         | 'width'| 'height'| 'trigger'|'action'
         |'gravity'|'friction1'|'friction2');

VALUE	:	 (INT|FLOAT|NAME);
FLOAT	:	('-')?(DIGIT)+('.')?(DIGIT)*
        ;
        
fragment DIGIT: [0-9];

INT  :    [0-9]+
    ;
NAME	:	[A-Za-z_][A-Za-z_0-9]+;
WS  
    :   (' ' | '\t' | '\r' | '\n') -> skip
    ;
COMMENT	:	'#' .*? ('\n'|'\r') -> skip
;

NEGATIVE:
            '-';

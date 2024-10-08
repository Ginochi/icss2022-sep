grammar ICSS;

//--- LEXER: ---

// IF support:
IF: 'if';
ELSE: 'else';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Declaration types:
WIDTH_DEC: 'width';
HEIGHT_DEC: 'height';
COLOR_DEC: 'color';
BACKGROUNDCOLOR_DEC: 'background-color';

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';



//--- PARSER: ---
stylesheet: variable* stylerule* EOF;
stylerule: tagSelector OPEN_BRACE ifExpression* variable* declaration* CLOSE_BRACE;

tagSelector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;

ifExpression: IF BOX_BRACKET_OPEN (bool | variableName) BOX_BRACKET_CLOSE OPEN_BRACE ifBody CLOSE_BRACE (ELSE OPEN_BRACE ifBody CLOSE_BRACE)?;
ifBody: ifExpression* declaration*;

variable: variableName ASSIGNMENT_OPERATOR (literal | variableName) SEMICOLON;
variableName: CAPITAL_IDENT;

declaration: (sizeDeclaration | colorDeclaration) SEMICOLON;
sizeDeclaration: (WIDTH_DEC | HEIGHT_DEC) COLON (size | variableName);
colorDeclaration: (COLOR_DEC | BACKGROUNDCOLOR_DEC) COLON (COLOR | variableName);

literal: size | COLOR | bool;
size: PIXELSIZE | PERCENTAGE;
bool: TRUE | FALSE;
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

//Property types:
WIDTH_PROP: 'width';
HEIGHT_PROP: 'height';
COLOR_PROP: 'color';
BGCOLOR_PROP: 'background-color';

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
stylerule: tagSelector OPEN_BRACE (ifClause | variable | declaration)* CLOSE_BRACE;

tagSelector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;

ifClause: IF expression ifBody elseClause?;
elseClause: ELSE ifBody;
expression: BOX_BRACKET_OPEN (bool | variableReference) BOX_BRACKET_CLOSE;
ifBody: OPEN_BRACE (ifClause | declaration)* CLOSE_BRACE;

variable: variableReference ASSIGNMENT_OPERATOR variableAssignment SEMICOLON;
variableReference: CAPITAL_IDENT;
variableAssignment: literal | variableReference;

declaration: propertyName COLON propertyValue SEMICOLON;
propertyName: WIDTH_PROP | HEIGHT_PROP | COLOR_PROP | BGCOLOR_PROP;
propertyValue: literal | variableReference;

literal: size | color | bool;
size: PIXELSIZE | PERCENTAGE;
color: COLOR;
bool: TRUE | FALSE;
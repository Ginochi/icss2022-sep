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
stylesheet: (stylerule | variableAssignment)* EOF;
stylerule: selector OPEN_BRACE (declaration | variableAssignment | ifClause)* CLOSE_BRACE;

selector: tagSelector | idSelector | classSelector;
tagSelector: LOWER_IDENT;
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;

declaration: propertyName COLON propertyValue SEMICOLON;
propertyName: WIDTH_PROP | HEIGHT_PROP | COLOR_PROP | BGCOLOR_PROP;
propertyValue: value | variableReference;

variableAssignment: variableReference ASSIGNMENT_OPERATOR (value | variableReference)* SEMICOLON;
variableReference: CAPITAL_IDENT;

ifClause: IF boolExpression ifBody elseClause?;
elseClause: ELSE ifBody;
boolExpression: BOX_BRACKET_OPEN (boolLiteral | variableReference) BOX_BRACKET_CLOSE;
ifBody: OPEN_BRACE (declaration | ifClause)* CLOSE_BRACE;

value: size | colorLiteral | boolLiteral;
size: sizeCalc | pixelLiteral | percentageLiteral;
pixelLiteral: PIXELSIZE;
percentageLiteral: PERCENTAGE;
colorLiteral: COLOR;
boolLiteral: TRUE | FALSE;

sizeCalc: sizeCalc (MUL) sizeCalc |
            sizeCalc (PLUS | MIN) sizeCalc |
            pixelLiteral | percentageLiteral | SCALAR;
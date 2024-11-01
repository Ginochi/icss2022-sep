package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;


public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        checkStylesheet(ast.root);
    }

    private void checkStylesheet(Stylesheet node) {
        HashMap<String, ExpressionType> stylesheetVariables = new HashMap<>();
        variableTypes.addFirst(stylesheetVariables);
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            }
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
        }
    }

    private void checkStylerule(Stylerule node) {
        HashMap<String, ExpressionType> styleruleVariables = new HashMap<>();
        variableTypes.addFirst(styleruleVariables);
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            }
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
            if (child instanceof IfClause) {
                checkIfClause((IfClause) child);
            }
        }
        variableTypes.removeFirst();
    }

    private void checkDeclaration(Declaration node) {
        if (node.property.name.equals("width") || node.property.name.equals("height")) {
            if (!(node.expression instanceof PixelLiteral || node.expression instanceof PercentageLiteral || node.expression instanceof VariableReference || node.expression instanceof Operation)) {
                node.setError("Property " + node.property.name + " has invalid type");
            }
            if (node.expression instanceof VariableReference) {
                boolean variableExists = false;
                boolean variableValid = false;
                for (int i = variableTypes.getSize(); i > 0; i--) {
                    if (variableTypes.get(i).containsKey(((VariableReference) node.expression).name)) {
                        variableExists = true;
                        if ((ExpressionType.PIXEL.equals(variableTypes.get(i).get(((VariableReference) node.expression).name)) || ExpressionType.PERCENTAGE.equals(variableTypes.get(i).get(((VariableReference) node.expression).name)))) {
                            variableValid = true;
                        }
                    }
                }
                if (!variableValid) {
                    node.setError("Variable " + ((VariableReference) node.expression).name + " has invalid type");
                }
                if (!variableExists) {
                    node.setError("Variable " + ((VariableReference) node.expression).name + " does not exist in used scope");
                }
            }
            if (node.expression instanceof Operation) {
                ExpressionType type = checkOperation((Operation) node.expression);
                if (!(ExpressionType.PIXEL.equals(type) || ExpressionType.PERCENTAGE.equals(type))) {
                    node.setError("Operation " + node.property.name + " has invalid type");
                }
            }
        }
        if (node.property.name.equals("color") || node.property.name.equals("background-color")) {
            if (!(node.expression instanceof ColorLiteral || node.expression instanceof VariableReference)) {
                node.setError("Property " + node.property.name + " has invalid type");
            }
            if (node.expression instanceof VariableReference) {
                boolean variableExists = false;
                boolean variableValid = false;
                for (int i = variableTypes.getSize(); i > 0; i--) {
                    if (variableTypes.get(i).containsKey(((VariableReference) node.expression).name)) {
                        variableExists = true;
                        if (ExpressionType.COLOR.equals(variableTypes.get(i).get(((VariableReference) node.expression).name))) {
                            variableValid = true;
                        }
                    }
                }
                if (!variableValid) {
                    node.setError("Variable " + ((VariableReference) node.expression).name + " has invalid type");
                }
                if (!variableExists) {
                    node.setError("Variable " + ((VariableReference) node.expression).name + " does not exist in used scope");
                }
            }
        }
    }

    private void checkVariableAssignment(VariableAssignment node) {
        String name = node.name.name;
        ExpressionType type = ExpressionType.UNDEFINED;
        if (node.expression instanceof PixelLiteral) {
            type = ExpressionType.PIXEL;
        } else if (node.expression instanceof PercentageLiteral) {
            type = ExpressionType.PERCENTAGE;
        } else if (node.expression instanceof ColorLiteral) {
            type = ExpressionType.COLOR;
        } else if (node.expression instanceof ScalarLiteral) {
            type = ExpressionType.SCALAR;
        } else if (node.expression instanceof BoolLiteral) {
            type = ExpressionType.BOOL;
        } else if (node.expression instanceof VariableReference) {
            boolean variableExists = false;
            for (int i = variableTypes.getSize(); i > 0; i--) {
                if (variableTypes.get(i).containsKey(node.name.name)) {
                    variableExists = true;
                    type = variableTypes.get(i).get(name);
                }
            }
            if (!variableExists) {
                node.setError("Variable " + node.expression.toString() + " does not exist in used scope");
            }
        } else if (node.expression instanceof Operation) {
            type = checkOperation((Operation) node.expression);
        }
        variableTypes.getFirst().put(name, type);
    }

    private void checkIfClause(IfClause node) {
        HashMap<String, ExpressionType> ifClauseVariables = new HashMap<>();
        variableTypes.addFirst(ifClauseVariables);
        if (!(node.conditionalExpression instanceof BoolLiteral || node.conditionalExpression instanceof VariableReference)) {
            node.setError("Boolean expression has invalid type");
        }
        if (node.conditionalExpression instanceof VariableReference) {
            boolean variableExists = false;
            boolean variableValid = false;
            for (int i = variableTypes.getSize(); i > 0; i--) {
                if (variableTypes.get(i).containsKey(((VariableReference) node.conditionalExpression).name)) {
                    variableExists = true;
                    if ((ExpressionType.BOOL.equals(variableTypes.get(i).get(((VariableReference) node.conditionalExpression).name)))) {
                        variableValid = true;
                    }
                }
            }
            if (!variableValid) {
                node.setError("Variable " + ((VariableReference) node.conditionalExpression).name + " has invalid type");
            }
            if (!variableExists) {
                node.setError("Variable " + ((VariableReference) node.conditionalExpression).name + " does not exist in used scope");
            }
        }
        for (ASTNode child : node.body) {
            if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            }
            if (child instanceof VariableAssignment) {
                checkVariableAssignment((VariableAssignment) child);
            }
            if (child instanceof IfClause) {
                checkIfClause((IfClause) child);
            }
        }
        if (node.elseClause != null) {
            for (ASTNode child : node.elseClause.body) {
                if (child instanceof Declaration) {
                    checkDeclaration((Declaration) child);
                }
                if (child instanceof VariableAssignment) {
                    checkVariableAssignment((VariableAssignment) child);
                }
                if (child instanceof IfClause) {
                    checkIfClause((IfClause) child);
                }
            }
        }
        variableTypes.removeFirst();
    }

    private ExpressionType checkOperation(Operation node) {
        return null;
    }

}

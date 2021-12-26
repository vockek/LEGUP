package edu.rpi.legup.puzzle.shorttruthtable.rules.contradiction;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;

import java.util.Arrays;


public abstract class ContradictionRule_GenericStatement extends ContradictionRule{

    private final char operationSymbol;

    private final ShortTruthTableCellType[][] contradictionPatterns;

    final static ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
    final static ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
    final static ShortTruthTableCellType n = null;

    private final String NOT_RIGHT_OPERATOR_ERROR_MESSAGE = "This cell does not contain the correct operation";
    private final String NOT_TRUE_FALSE_ERROR_MESSAGE = "Can only check for a contradiction on a cell that is assigned a value of True or False";
    private final String NO_CONTRADICTION_MESSAGE = "This cell does not match any contradiction patterns for this rule";

    public ContradictionRule_GenericStatement(String ruleName, String description, String imageName,
                                              char operationSymbol, ShortTruthTableCellType[][] contradictionPatterns){
        super(ruleName, description, imageName);
        this.operationSymbol = operationSymbol;
        this.contradictionPatterns = contradictionPatterns;
    }

    @Override
    public String checkContradictionAt(Board puzzleBoard, PuzzleElement puzzleElement) {

        //cast the board to a shortTruthTableBoard
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzleBoard;

        //get the cell that contradicts another cell in the board
        ShortTruthTableCell cell = board.getCellFromElement(puzzleElement);
        ShortTruthTableStatement statement = cell.getStatementReference();
        ShortTruthTableStatement parentStatement = statement.getParentStatement();
        System.out.println("Statement: " + statement);


        //must be the correct statement
        System.out.println("Symbol: " + cell.getSymbol());

        // ISSUE: IT SEEMS TO BE THAT puzzleElement IS EXPECTED TO BE THE OPERATOR
        if(cell.getSymbol() != this.operationSymbol)
            return this.NOT_RIGHT_OPERATOR_ERROR_MESSAGE;

        //check that the initial statement is assigned
        ShortTruthTableCellType cellType = cell.getType();
        System.out.println("contra rule generic cell: "+cell);
        if(!cellType.isTrueOrFalse())
            return this.NOT_TRUE_FALSE_ERROR_MESSAGE;

        //get the pattern for this sub-statement
        ShortTruthTableCellType[] testPattern = statement.getCellTypePattern();

        //if the board pattern matches any contradiction pattern, it is a valid contradiction
        System.out.println("Name: " + this.ruleName);
        System.out.println("Testing pattern: "+Arrays.toString(testPattern));
        for(ShortTruthTableCellType[] pattern : contradictionPatterns){
            System.out.println("Comparing to: "+Arrays.toString(pattern));
            boolean matches = true;
            for(int i = 0; i<3; i++){
                //null means that part does not affect the statement
                if(pattern[i] == null) continue;
                //if it is not null, it must match the test pattern
                if(pattern[i] != testPattern[i]){
                    matches = false;
                    break;
                }
            }
            //if testPattern matches one of the valid contradiction patterns, the contradiction is correct
            if(matches){
                System.out.println("This is a valid contradiction: matches pat: "+Arrays.toString(pattern));
                return null;
            }
        }

        System.out.println("No patterns match. There is not a contradiction");
        return this.NO_CONTRADICTION_MESSAGE;
    }

    public String getNotRightOperatorErrorMessage()
    {
        return this.NOT_RIGHT_OPERATOR_ERROR_MESSAGE;
    }

    public String getNotTrueFalseErrorMessage()
    {
        return this.NOT_TRUE_FALSE_ERROR_MESSAGE;
    }

    public String getNoContradictionMessage()
    {
        return this.NO_CONTRADICTION_MESSAGE;
    }
}
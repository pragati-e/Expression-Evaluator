package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class DivideOperator extends Operator  {

    @Override
    public int priority() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        int res = operandOne.getValue()/operandTwo.getValue();
        return new Operand(res) ;
    }
}
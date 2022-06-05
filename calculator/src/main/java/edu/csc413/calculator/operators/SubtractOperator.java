package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class SubtractOperator extends Operator  {

    @Override
    public int priority() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        // TODO Auto-generated method stub
        int res=operandOne.getValue()-operandTwo.getValue();
        return new Operand(res) ;
    }
}
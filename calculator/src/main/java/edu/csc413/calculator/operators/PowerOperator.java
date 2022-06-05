package edu.csc413.calculator.operators;

import edu.csc413.calculator.evaluator.Operand;

public class PowerOperator extends Operator  {

    @Override
    public int priority() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public Operand execute(Operand operandOne, Operand operandTwo) {
        System.out.println("In Power Operand execute(");
        double num1=operandOne.getValue();
        double num2=operandTwo.getValue();
        System.out.println(num1+ " "+num2);
        int res=(int) Math.pow(num1,num2);
        System.out.println("Result is: "+res);
        return new Operand(res);
    }
}

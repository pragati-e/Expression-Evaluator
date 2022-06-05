package edu.csc413.calculator.evaluator;

/**
 * Operand class used to represent an operand
 * in a valid mathematical expression.
 */
public class Operand {
    /**
     * construct operand from string token.
     */
    private int value;
    public Operand(String token) {
        try {
            this.value = Integer.parseInt(token);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * construct operand from integer
     */
    public Operand(int value) {
        this.value=value;
    }
    /**
     * return value of operand
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Check to see if given token is a valid
     * operand.
     */
    public static boolean check(String token) {
        try {
            Integer.parseInt(token);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }
}

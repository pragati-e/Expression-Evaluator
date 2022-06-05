package edu.csc413.calculator.evaluator;



import edu.csc413.calculator.exceptions.InvalidTokenException;
import edu.csc413.calculator.operators.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {

  private Stack<Operand> operandStack;
  private Stack<Operator> operatorStack;
  private StringTokenizer expressionTokenizer;
  private final String delimiters = "()+/*-^";

  public Evaluator() {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
  }

  public int evaluateExpression(String expressionOne) throws InvalidTokenException {
    expressionOne = this.trimExpression(expressionOne);

    //To check if the expression doesn't contain uneven number of brackets,
    //consecutive operators and checking if the given expression contains non numeric digits
    if(checkChar(expressionOne)==false) {
      throw new InvalidTokenException("exception");

    } else if(operatorCheck(expressionOne)==false){
      throw new EmptyStackException();
    } else if(compareBracket(expressionOne)==false){
      throw new NullPointerException();
    }


    String expression = expressionOne;
    char[] ch = expression.toCharArray();
    int operatorcounter = operatorCount(ch); //To know the number of operators in the given expression

    while (operatorcounter > 0) {
      if(bracketCount(ch)>=1)
        expression = bracketEvaluator(ch,expression); //Checking if the expression has brackets

      //Checking for power operator
      if (expression.contains("^")) {
        for (int i = ch.length - 1; i >= 0; i--) {
          if (ch[i] == '^') {
            //Evaluates the given expression
            expression = evalSubExpr(ch, i, expression);
            // Convert the remaining expression to the char arrat
            ch = expression.toCharArray();
          }
        }
      } else if (expression.contains("/")) {
        ch=expression.toCharArray();
        for (int i = 0; i < ch.length; i++) {
          if (ch[i] == '/') {
            expression = evalSubExpr(ch, i, expression);
            if(this.operandCount(expression.toCharArray())==1 && this.operatorCount(expression.toCharArray())==1) {
              operatorcounter=0;
              ch = null;
              break;

            } else {
              ch = expression.toCharArray();
            }

          }
        }
      } else if (expression.contains("*")) {
        ch=expression.toCharArray();
        for (int i = 0; i < ch.length; i++) {
          if (ch[i] == '*') {
            expression = evalSubExpr(ch, i, expression);
            ch = expression.toCharArray();
          }
        }
      }

      else if(expression.contains("-")){
        ch = expression.toCharArray();
        if(expression.startsWith("-") && this.operatorCount(expression.toCharArray())>1) {
          String negativestart = "-";
          for (int i = 0; i < ch.length; i++) {
            if(!Operator.check(""+ch[i]))
              negativestart = negativestart+ch[i];
            else
              break;
          }
          expression = expression.replace(negativestart, "");
          expression = expression+negativestart;

        }


        for (int i = 0; i < ch.length; i++) {
          if (ch[i] == '-') {
            expression = evalSubExpr(ch, i, expression);

            if(this.operandCount(expression.toCharArray())==1 && this.operatorCount(expression.toCharArray())==1) {
              operatorcounter=0;
              ch = null;
              break;

            } else {
              ch = expression.toCharArray();
            }
          }
        }
      }
      else {
        for (int i = 0; i < ch.length; i++) {
          if (ch[i] == '+') {
            ch=expression.toCharArray();
            expression = evalSubExpr(ch, i, expression);
            if (expression != null)
              ch = expression.toCharArray();

          }
        }
      }
      operatorcounter--;
    }
    int finalresult = Integer.parseInt(expression);
    if(expression.startsWith("-")) {
    }
    //System.out.println("Return value of evaluateExpression(): "+ finalresult );
    return finalresult;
  }
  //Solves the given expression
  public String evalSubExpr(char ch[], int i, String expression) {
    int operatorListSize = this.operatorList(ch).size();
    int j = i;
    String remember = "";
    boolean flag = false;
    ArrayList<String> list = this.operatorList(ch);
    if(operatorListSize==2 && (list.get(0).toString().equals("-") && list.get(1).toString().equals("-"))) {
      remember="-";
      expression = expression.replace("-", "+");
      char [] exprarray = expression.toCharArray();
      String expresstionstr = "";
      for(int a =1;a<exprarray.length;a++ ) {
        if(exprarray[a]=='+')
          i=a-1;
        expresstionstr = expresstionstr+exprarray[a];
      }
      flag = true;
      expression = expresstionstr;

      ch=expression.toCharArray();
    }

    String first = "";
    int l = i;
    l=i-1;
    while (l >= 0) {


      char c = ch[l];
      if (Character.isDigit(c))
        first = first + ch[l];
      else
        break;

      if (l == 0)
        break;
      l--;
    }
    first = reverseString(first);
    String second = "";
    j=j+1;
    while (j < expression.length()) {

      if (Character.isDigit(ch[j]))
        second = second + ch[j];
      else
        break;
      j++;
    }

    String genexpression = "" + first + ch[i] + second;

    //System.out.println("----------->>>>>:" + genexpression);
    int powresult = 0;
    try {
      powresult = computeSingleExpr(genexpression); // Prof
      if (genexpression.equals(expression))
        expression = "";
    } catch (InvalidTokenException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    if (expression.equals("")&& flag==false)
      expression =  "" + powresult;
    else if (expression.equals("")&& flag==true)
      expression = remember+powresult;
    else
      expression = expression.replace(genexpression, ""+powresult);
    if(expression.contains("+-") ||  expression.contains("-+"))
      expression = expression.replace("+-", "-");
    if(expression.contains("--"))
      expression = expression.replace("--", "+");
    //System.out.println("Return value of evalSubExpr():" + expression);
    return expression;

  }

  // this method gives the total number of operators in a given expression
  public int operatorCount(char[] ch) {
    //System.out.println("Inside operatorCount(): " );
    int operatorcounter = 0;
    for (int i = ch.length - 1; i >= 0; i--) {
      if (ch[i] == '^' || ch[i] == '/' || ch[i] == '*' || ch[i] == '-' || ch[i] == '+') {
        operatorcounter++;
      }
    }
    return operatorcounter;
  }
  //Returns the list of operators present in the exprrssion as an ArrayList
  public ArrayList<String> operatorList(char[] ch) {
    ArrayList<String> operatorcounter = new ArrayList<String>() ;
    for (int i = 0; i <ch.length; i++) {
      if (ch[i] == '^' || ch[i] == '/' || ch[i] == '*' || ch[i] == '-' || ch[i] == '+') {
        operatorcounter.add(""+ch[i]);
      }
    }
    return operatorcounter;
  }
  //Counts the number of operands present in the exprrssion
  public int operandCount(char[] ch) {
    int operandcounter = 0;
    for (int i = 0; i <ch.length; i++) {
      if (ch[i] == '^' || ch[i] == '/' || ch[i] == '*' || ch[i] == '-' || ch[i] == '+') {

      } else
        operandcounter++;
    }
    return operandcounter;
  }
  //Counts the number of brackets present in the exprrssion
  public int bracketCount(char[] ch) {
    int operatorcounter = 0;
    for (int i = ch.length - 1; i >= 0; i--) {
      if (ch[i] == '(') {
        operatorcounter++;
      }
    }
    return operatorcounter;
  }

  // reverse a given string
  public String reverseString(String s) {
    String result = "";
    for (int i = s.length() - 1; i >= 0; i--) {
      result = result + s.charAt(i);
    }
    //System.out.println("Return value of reverseString(): "+ result );
    return result;
  }


  //This method returns the inner most bracket's expression
  public String bracketEvaluator(char[] ch, String expression)throws InvalidTokenException {
    while (bracketCount(ch)>=1) {
      for (int i = ch.length - 1; i >= 0; i--) {
        if (ch[i] == '(') {
          String bracketExpression = "";
          for (int j = i; j < ch.length; j++) {
            if (ch[j] == ')') {
              bracketExpression = expression.substring(i+1, j);
              String evalexpression="";
              if(operatorCount(bracketExpression.toCharArray())>1)
                evalexpression = ""+evaluateExpression(bracketExpression) ;
              else
                evalexpression = ""+this.computeSingleExpr(bracketExpression);
              expression = expression.replace("("+bracketExpression+")", ""+evalexpression);
              ch = expression.toCharArray();
            }
          }
        }
      }
    }

    return expression;
  }

  public  String trimExpression(String expression) {
    String res = "";
    for(int i=0;i<expression.length();i++) {
      if(expression.charAt(i)!=' ') {
        res = res+expression.charAt(i);
      }
    }
    //System.out.println(res);
    return res;
  }

  //Exception methods:
  //Compares if the expression has even number of brackets
  public static boolean compareBracket(String expression)  {
    char []ch = expression.toCharArray();
    int j = 0;
    int frontbracket=0,backbracket=0;
    for(int i=0;i<ch.length;i++) {
      if(ch[i]=='(') {
        frontbracket++;
        for(j=j+1;j<ch.length;j++) {
          if (ch[j]==')') {
            backbracket++;
            break;
          }

        }
      }
    }
    if(frontbracket == backbracket)
      return true;
    else return false;
  }

  public static boolean operatorCheck(String expression)   {
    boolean res = true;
    for(int i=0;i<expression.length();i++) {

      if(i < expression.length() ) {
        if ( Operator.check(""+expression.charAt(i)) && Operator.check(""+expression.charAt(i+1)) ) {
          res = false;
          break;
        } else {
          res = true;
        } if(res==false)
          break;
      }
    }
    return res;

  }

  public static boolean checkChar(String expression)   {
    boolean res = false;
    for(int i=0;i<expression.length();i++) {
      int c = (int)expression.charAt(i);
      if((c>=40 && c<=57) || c==94) {
        res= true;
      }else {
        res= false;
        break;
      }
    }
    return res;
  }



  // Evaluate when the expression has 1 operator
  public int computeSingleExpr(String expression) throws InvalidTokenException {
    //System.out.println("Inside computeSingleExpr(): "+ expression );
    int finalresult = 0;
    Operator newOperator = null;
    // The 3rd argument is true to indicate that the delimiters should be used
    // as tokens, too. But, we'll need to remember to filter out spaces.
    this.expressionTokenizer = new StringTokenizer(expression, this.delimiters, true);
    // System.out.println(expressionTokenizer.toString());

    // initialize operator stack - necessary with operator priority schema
    // the priority of any operator in the operator stack other than
    // the usual mathematical operators - "+-*/" - should be less than the priority
    // of the usual operators

    int i = 0, operatorcount = 0, j = 0, operandcount = 0, k = 0;
    while (this.expressionTokenizer.hasMoreTokens()) {
      String expressionToken = this.expressionTokenizer.nextToken();
      // filter out spaces
      if (!(expressionToken).equals(" ")) {
        // check if token is an operand
        if (Operand.check(expressionToken)) {
          operandStack.push(new Operand(expressionToken));
          operandcount++;
        } else {
          if (!Operator.check(expressionToken)) {
            throw new InvalidTokenException(expressionToken);
          } else {
            operatorStack.push(Operator.getOperator(expressionToken));
            operatorcount++;
            newOperator = Operator.getOperator(expressionToken);
          }
        }
        // TODO Operator is abstract - these two lines will need to be fixed:
        // The Operator class should contain an instance of a HashMap,
        // and values will be instances of the Operators. See Operator class
        if (i == 2) {
          //System.out.println(operatorStack.peek().priority() + " " + operatorStack.peek());
          while (!operatorStack.isEmpty() && operatorStack.peek().priority() >= newOperator.priority()
                  && j < operatorcount && k < operandcount) {
            // operatorStack.peek().priority() + "==priority check=== " +
            // newOperator.priority());
            // note that when we eval the expression 1 - 2 we will
            // push the 1 then the 2 and then do the subtraction operation
            // This means that the first number to be popped is the
            // second operand, not the first operand - see the following code
            Operator operatorFromStack = operatorStack.pop();
            Operand operandTwo = operandStack.pop();
            Operand operandOne = operandStack.pop();

            Operand result = operatorFromStack.execute(operandOne, operandTwo);
            finalresult = result.getValue();
            operandStack.push(result);
            j++;
          }
          operatorStack.push(newOperator);
        }

        i++;
      }

      // Control gets here when we've picked up all of the tokens; you must add
      // code to complete the evaluation - consider how the code given here
      // will evaluate the expression 1+2*3
      // When we have no more tokens to scan, the operand stack will contain 1 2
      // and the operator stack will have + * with 2 and * on the top;
      // In order to complete the evaluation we must empty the stacks,
      // that is, we should keep evaluating the operator stack until it is empty;
      // Suggestion: create a method that processes the operator stack until empty.

    }
    return finalresult;

  }

}

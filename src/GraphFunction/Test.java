/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaproject1;

import java.util.Stack;
import javax.swing.JOptionPane;

/**
 *
 * @author NHH
 */
public class Test {
    
    private double string2number(String string){
        double result = 0;
        boolean checkDouble = false;
        int temp = 0;
        for (int i = 0; i < string.length(); i++) { 
            if(string.charAt(i) == '.'){
                checkDouble = true;
                temp = i;
            }else if(checkDouble == false){
                result = result * 10 + Integer.valueOf(string, i);
            }else{
                result += Math.pow(Integer.valueOf(string, i), temp - i);
            }
        }
        return result;
    }
    
    private static boolean isOperand(char c){
        if(c >= '0' && c <= '9'){
            return true;
        }else{
            return false;
        }
    }
    
    private static boolean isOperator(char c){
        if((c == '+') || (c == '*') || (c == '-') ||(c == '/') || (c == '^')
                || (c == '(') || (c == ')') ){
            return true;
        }else{
            return false;
        }
    }
    
    private static int precedence(String c){
        switch(c){
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default :
                return 0;
        }
    }
    
    private static String infix2postfix(String string){
        String result = "";
        Stack<String> stack = new Stack<>();
        for(int i = 0; i < string.length();i++){
            char c = string.charAt(i);
            if(c == ' '){
                continue;
            }
            if(c == 'x' || isOperand(c) || c == '.'){
                result += c;
            }else if(c == 'p'){
                result += "pi";
                i++;
            }else if(isOperator(c)){
                if(c == '('){
                    stack.push(String.valueOf(c));
                }else if (c == ')'){
                    while(!stack.isEmpty() && !"(".equals(stack.peek()) && stack.peek() != "sin" && stack.peek() != "cos"
                                && stack.peek() != "tan" && stack.peek() != "cot"){
                        result += stack.pop();
                    }
                    if(stack.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Function is invalid");
                        break;
                    }else{
                        if(stack.peek().equals("(")){
                            stack.pop();
                        }else{
                            result += stack.pop();
                        }
                    }
                }else{
                    if(stack.isEmpty()){
                        stack.push(String.valueOf(c));
                    }else{
                        if(precedence(String.valueOf(c)) > precedence(stack.peek())){
                            stack.push(String.valueOf(c));
                        }else{
                            while(!stack.isEmpty() && precedence(String.valueOf(c)) <= precedence(stack.peek())){                             
                                if("(".equals(stack.peek())   || stack.peek() == "sin" || stack.peek() == "cos"
                                        || stack.peek() == "tan" || stack.peek() == "cot"){
                                    JOptionPane.showMessageDialog(null, "Function is invalid");
                                    break;
                                }
                                result += stack.pop();
                            }
                            stack.push(String.valueOf(c));
                        }
                    }     
                }
            }else{    
                if(c == 's'){
                    stack.push("sin");
                    i += 3;
                }else if(c == 't'){
                    stack.push("tan");
                    i += 3;
                }else{
                    if(string.charAt(i+2) == 's'){
                        stack.push("cos");
                        i += 3;
                    }else{
                        stack.push("cot");
                        i += 3;
                    }
                }
            }
        }
        while(!stack.isEmpty()){
            result += stack.pop();
        }
        return result;
    }
        
    private static double caculate(String string, double n){
        double result = 0;
        boolean check = false;
        int index = 0;
        String function = infix2postfix(string);
        Stack<Double> stack = new Stack();
        for(int i = 0; i < function.length(); i++){
            char c = function.charAt(i);
            if(c == 'x' ){
                stack.push(n);
                check = false;
            }else if(c == 'p'){
                stack.push(Math.PI);
                i++;
            }else if(isOperand(c) || c == '.'){
                if( i > 0){
                    if(c == '.'){
                        check = true;
                        index = 1;
                    }
                    if( check == false){
                        if(isOperand(function.charAt(i - 1))){
                            double temp = stack.pop();
                            temp = temp * 10 + Integer.parseInt(String.valueOf(c));
                            stack.push(temp);
                        }else{
                            stack.push(Double.parseDouble(String.valueOf(c)));
                        }
                    }
                    if(check == true && c != '.'){
                        double temp = stack.pop();
                        temp = temp + Integer.parseInt(String.valueOf(c)) * Math.pow(10, -index);
                        stack.push(temp);
                        index++;
                    }
                }else{
                    stack.push(Double.parseDouble(String.valueOf(c)));
                }
            }else if(isOperator(c) || c == 's' || c == 't' || c == 'c'){
                check = false;
                index = 0;
                double a,b;
                switch(c){
                    case '+':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a + b);
                        break;
                    case '-':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a - b);
                        break;
                    case '*':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a * b);
                        break;
                    case '/':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a / b);
                        break;
                    case '^':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(Math.pow(a,b));
                        break;
                    case 's':
                        a = stack.pop();
                        stack.push(Math.sin(a));
                        i += 2;
                        break;
                    case 't':
                        a = stack.pop();
                        stack.push(Math.tan(a));
                        i += 2;
                        break;
                    case 'c':
                        if(function.charAt(i+2) == 's'){
                            a = stack.pop();
                            stack.push(Math.cos(a));
                        }else{
                            a = stack.pop();
                            stack.push(Math.cosh(a));
                        }
                        i += 2;
                        break;
                }
            }
        }
        return stack.peek();
    }
        
        
    public static void main(String[] args) {
        String string = "3*x";
        System.out.println(10/0);
        System.out.println(infix2postfix(string));
        System.out.println(caculate(string, -10));
        

    }
}

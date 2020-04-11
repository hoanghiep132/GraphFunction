/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaproject1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JLabel;
import javax.swing.JOptionPane;



public class Graph extends javax.swing.JFrame {

    private final ArrayList<Coordinate> list;
    private final Graphics2D graphics;
    private int pixel = 10;
    
    public Graph() {
        initComponents();
        graphics = (Graphics2D) graphPanel.getGraphics();
        list = new ArrayList();
    }
    
    private void drawSpan(){
        graphics.setColor(Color.black);
        graphics.setStroke(new BasicStroke(2.0f));
        for(int i = 0; i <= 1000; i += 1){
            graphics.drawLine(i, 300, i + 1, 300);
            if(i % pixel == 0 && i != 510){
                graphics.drawLine(i, 297, i, 303);
            }
        }
        for(int i = 0; i <= 600; i += 1){
            graphics.drawLine(510, i, 510, i + 1);
            if(i % pixel == 0 && i != 300){
                graphics.drawLine(507, i, 513, i);
            }
        }
        JLabel originLabel = new JLabel("0");
        originLabel.setBounds(510, 310, 20, 20);
        originLabel.setFont(new java.awt.Font("Tahoma", 1, 16));
        graphPanel.add(originLabel);
    }   

    private boolean isOperand(char c){
        if(c >= '0' && c <= '9'){
            return true;
        }else{
            return false;
        }
    }
    
    private boolean isOperator(char c){
        if((c == '+') || (c == '*') || (c == '-') ||(c == '/') || (c == '^')
                || (c == '(') || (c == ')') ){
            return true;
        }else{
            return false;
        }
    }
    
    private int getLevel(String c){
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
    

    private String infix2postfix(String string){
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
                result += "p";
                i++;
            }else if(isOperator(c)){
                if(c == '('){
                    stack.push(String.valueOf(c));
                }else if (c == ')'){
                    while(!stack.isEmpty() && !"(".equals(stack.peek()) && !"sin".equals(stack.peek()) && !"cos".equals(stack.peek())
                                && !"tan".equals(stack.peek()) && !"cot".equals(stack.peek())){
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
                        if(getLevel(String.valueOf(c)) > getLevel(stack.peek())){
                            stack.push(String.valueOf(c));
                        }else{
                            while(!stack.isEmpty() && getLevel(String.valueOf(c)) <= getLevel(stack.peek())){                             
                                if("(".equals(stack.peek())   || "sin".equals(stack.peek()) || "cos".equals(stack.peek())
                                        || "tan".equals(stack.peek()) || "cot".equals(stack.peek())){
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
                switch (c) {
                    case 's':
                        stack.push("sin");
                        i += 3;
                        break;
                    case 't':
                        stack.push("tan");
                        i += 3;
                        break;
                    case 'c':
                        if(string.charAt(i+2) == 's'){
                            stack.push("cos");
                            i += 3;
                        }else{
                            stack.push("cot");
                            i += 3;
                        }  
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Function is invalid!");
                        return " ";
                }
            }
        }
        while(!stack.isEmpty()){
            result += stack.pop();
        }
        return result;
    }
    
    private double caculate(String string){
        boolean check = false;
        int index = 0;
        Stack<Double> stack = new Stack();
        string = infix2postfix(string);
        for(int i = 0; i < string.length(); i++){
            char c = string.charAt(i);
            if(c == 'p'){
                stack.push(Math.PI);
            }else if(isOperand(c) || c == '.'){
                if( i > 0){
                    if(c == '.'){
                        check = true;
                        index = 1;
                    }
                    if( check == false){
                        if(isOperand(string.charAt(i - 1))){
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
                double a = 0, b = 0;
                switch(c){
                    case '+':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a + b);
                        break;
                    case '-':
                        b = stack.pop();
                        if(stack.isEmpty()){
                            stack.push(-b);
                        }else{
                            a = stack.pop();
                            stack.push(a - b);
                        }
                        break;
                    case '*':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a * b);
                        break;
                    case '/':
                        b = stack.pop();
                        a = stack.pop();
                        try{
                            stack.push(a / b);
                        }catch(ArithmeticException ex){
                            
                        }
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
                        try{
                            stack.push(Math.tan(a));
                        }catch(ArithmeticException ex){
                            
                        }
                        i += 2;
                        break;
                    case 'c':
                        if(string.charAt(i+2) == 's'){
                            a = stack.pop();
                            stack.push(Math.cos(a));
                        }else{
                            a = stack.pop();
                            try{
                            stack.push(Math.cosh(a));
                            }catch(ArithmeticException ex){
                                
                            }
                        }
                        i += 2;
                        break;
                }
            }
        }
        return stack.peek();
    }


    private double caculate(double n){
        boolean check = false;
        int index = 0;
        
        String function = infix2postfix(functionField.getText());
        Stack<Double> stack = new Stack();
        for(int i = 0; i < function.length(); i++){
            char c = function.charAt(i);
            if(c == 'x'){
                stack.push(n);
                check = false;
            }else if(c == 'p'){
                stack.push(Math.PI);
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
                double a = 0, b = 0;
                switch(c){
                    case '+':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a + b);
                        break;
                    case '-':
                        b = stack.pop();
                        if(stack.isEmpty()){
                            stack.push(-b);
                        }else{
                            a = stack.pop();
                            stack.push(a - b);
                        }
                        break;
                    case '*':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push(a * b);
                        break;
                    case '/':
                        b = stack.pop();
                        a = stack.pop();
                        try{
                            stack.push(a/b);
                        }catch(Exception ex){

                        }
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
                        try{
                            stack.push(Math.tan(a));
                        }catch(Exception ex){
                            
                        }
                        i += 2;
                        break;
                    case 'c':
                        if(function.charAt(i+2) == 's'){
                            a = stack.pop();
                            stack.push(Math.cos(a));
                        }else{
                            a = stack.pop();
                            if(a % Math.PI == 0){
                                return 0;
                            }else{
                                try{
                                    stack.push(1/Math.tan(a));
                                }catch(Exception ex){
                                    
                                }
                            }
                        }
                        i += 2;
                        break;
                }
            }
        }
        return stack.peek();
    }
    
    private String derivative(){
        String string = functionField.getText();
        String result = "";
        for(int i = 0; i < string.length(); i++){
            char c = string.charAt(i);
            
        }
        return result;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        functionField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        endFiled = new javax.swing.JTextField();
        beginField = new javax.swing.JTextField();
        excuteButton = new javax.swing.JButton();
        pixelComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        graphPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        functionField.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        functionField.setForeground(new java.awt.Color(204, 204, 204));
        functionField.setText("x^2");
        functionField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                functionFieldMouseClicked(evt);
            }
        });
        functionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                functionFieldActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText(" y = f(x) =");

        jLabel2.setText("Begin");

        jLabel3.setText("End");

        endFiled.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        endFiled.setForeground(new java.awt.Color(204, 204, 204));
        endFiled.setText("20");
        endFiled.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                endFiledMouseClicked(evt);
            }
        });
        endFiled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endFiledActionPerformed(evt);
            }
        });

        beginField.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        beginField.setForeground(new java.awt.Color(204, 204, 204));
        beginField.setText("-20");
        beginField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                beginFieldMouseClicked(evt);
            }
        });
        beginField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beginFieldActionPerformed(evt);
            }
        });

        excuteButton.setText("Excute");
        excuteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excuteButtonActionPerformed(evt);
            }
        });

        pixelComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "30", "20", "15", "10" }));

        jButton1.setText("Reset");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(functionField, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(beginField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endFiled, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 76, 76)
                .addComponent(excuteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(pixelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(beginField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endFiled, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(functionField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pixelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(excuteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        graphPanel.setBackground(new java.awt.Color(255, 255, 255));
        graphPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                graphPanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(graphPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void functionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_functionFieldActionPerformed
        functionField.setText("");
        functionField.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        functionField.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_functionFieldActionPerformed

    private void beginFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beginFieldActionPerformed
        beginField.setText("");
        beginField.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        beginField.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_beginFieldActionPerformed

    private void endFiledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endFiledActionPerformed
        endFiled.setText("");
        endFiled.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        endFiled.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_endFiledActionPerformed

    private void excuteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excuteButtonActionPerformed
        pixel = Integer.parseInt((String) pixelComboBox.getSelectedItem());
        drawSpan();
        double x1,x2,y1,y2;
        double begin = caculate(beginField.getText());
        double end = caculate(endFiled.getText());
        double increase = (end - begin) / 1000;
        int zero ;
        if(increase != 0){
            graphics.setColor(Color.red);
            graphics.setStroke(new BasicStroke(2.0f));
            for(double i = begin; (increase>0)?i < end:i>end; i += increase){
                x1 = i;
                y1 = caculate(x1);
//                x2 = i + increase;
//                y2 = caculate(x2);
                if(y1 > Double.MAX_VALUE ){
                    System.out.println(x1);
                    continue;
                }
                x1 = 510 + x1 * pixel;
                y1 = 300 - y1 * pixel;
//                x2 = 510 + x2 * pixel;
//                y2 = 300 - y2 * pixel;
//                graphics.drawLine((int)x1, (int)y1, (int)x2,(int)y2);
                list.add(new Coordinate(x1, y1));
            }
        }
        for(int i = 0; i < list.size() - 1;i++){
            graphics.drawLine((int)list.get(i).getX(), (int)list.get(i).getY(), (int)list.get(i+1).getX(),(int)list.get(i+1).getY());
        }
    }//GEN-LAST:event_excuteButtonActionPerformed

    private void functionFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_functionFieldMouseClicked
        functionField.setText("");
        functionField.setFont(new java.awt.Font("Tahoma", 0, 14)); 
        functionField.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_functionFieldMouseClicked

    private void beginFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_beginFieldMouseClicked
        beginField.setText("");
        beginField.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        beginField.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_beginFieldMouseClicked

    private void endFiledMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_endFiledMouseClicked
        endFiled.setText("");
        endFiled.setFont(new java.awt.Font("Tahoma", 0, 13)); 
        endFiled.setForeground(new java.awt.Color(0,0,0));
    }//GEN-LAST:event_endFiledMouseClicked

    private void graphPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_graphPanelMouseClicked

    }//GEN-LAST:event_graphPanelMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        graphics.setColor(Color.white);
        for(int i = 0; i < list.size() - 1;i++){
            double x1 = list.get(i).getX();
            double y1 = list.get(i).getY();
            double x2 = list.get(i+1).getX();
            double y2 = list.get(i+1).getY();
            if(x1 != 510 && x2 != 510 && y1 != 300 && y2 != 300){
                graphics.drawLine((int)x1, (int)y1, (int)x2,(int)y2);
            }    
        }
        list.removeAll(list);
        drawSpan();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Graph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Graph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Graph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Graph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Graph().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField beginField;
    private javax.swing.JTextField endFiled;
    private javax.swing.JButton excuteButton;
    private javax.swing.JTextField functionField;
    private javax.swing.JPanel graphPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> pixelComboBox;
    // End of variables declaration//GEN-END:variables
}

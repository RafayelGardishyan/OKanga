package com.rafayel.okanga;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Values {
    private static ArrayList<String> Tokens;
    private static Hashtable<String, String[]> Variables;
    private static Hashtable<String, Function> Functions;
    private static Hashtable<String, Hashtable<String, String[]>> FunctionArguments;
    private static Stack<String> currentFunction;

    Values(){
        Tokens = new ArrayList<>();
        Variables = new Hashtable<>();
        Functions = new Hashtable<>();
        currentFunction = new Stack<>();
        FunctionArguments = new Hashtable<>();
        Tokens.add("if");
        Tokens.add("else");
        Tokens.add("elif");
        Tokens.add("endif");
        Tokens.add("def:");
        Tokens.add("endef");
        Tokens.add("call:");
        Tokens.add("screen:");
        Tokens.add("var:");
        Tokens.add("$");
        Tokens.add("input:");
        Tokens.add("//");
        Tokens.add("convert:");
        Tokens.add("import:");
    }

    public ArrayList<String> getTokens(){
        return Tokens;
    }

    public void addFunction(String name, Function function){
        Functions.put(name, function);
    }

    public Function getFunction(String name){
        return Functions.get(name);
    }

    public void addFunctionArguments(String name, Hashtable<String, String[]> arguments){
        FunctionArguments.put(name, arguments);
    }

    public Hashtable<String, String[]> getFunctionArguments(String name){
        return FunctionArguments.get(name);
    }

    public void addVariable(String name, String[] value){
        Variables.put(name, value);
    }

    public String[] getVariable(String name){
        return Variables.get(name);
    }

    public void setCurrentFunction(String name){
        currentFunction.push(name);
    }

    public String getCurrentFunction(){
        String func = currentFunction.pop();
        setCurrentFunction(func);
        return func;
    }

    public void deleteLastFunction(){
        currentFunction.pop();
    }
}

package com.rafayel.okanga;

import java.util.ArrayList;
import java.util.Hashtable;

public class Values {
    private static ArrayList<String> Tokens;
    private static Hashtable<String, String[]> Variables;
    Values(){
        Tokens = new ArrayList<>();
        Variables = new Hashtable<>();
        Tokens.add("if");
        Tokens.add("else");
        Tokens.add("elif");
        Tokens.add("end");
        Tokens.add("screen:");
        Tokens.add("var:");
        Tokens.add("$");
        Tokens.add("input:");
        Tokens.add("//");
        Tokens.add("convert:");
    }

    public static ArrayList<String> getTokens(){
        return Tokens;
    }

    public static void addVariable(String name, String[] value){
        Variables.put(name, value);
    }

    public static String[] getVariable(String name){
        return Variables.get(name);
    }
}

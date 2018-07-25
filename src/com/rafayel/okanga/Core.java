package com.rafayel.okanga;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Core {
    private static String[] parseVar(Values values, String var, Integer i){
        Pattern r = Pattern.compile("(\\d+)");
        String variable = "";
        Boolean stringEnabled = false;
        Boolean bool = false;
        String type = "";
        for (Integer j = i; j < var.length(); j++) {
            if (Character.toString(var.charAt(j)).equals("$") && !stringEnabled) return getVar(values, var, j + 1);
            if (Character.toString(var.charAt(j)).equals("!") && !stringEnabled) {
                String name = "";
                for (Integer k = j + 1; k < var.length(); k++){
                    name += var.charAt(k);
                }
                return values.getFunctionArguments(values.getCurrentFunction()).get(name);
            }

            if (var.charAt(j) == "\"".charAt(0)){
                if (stringEnabled) {
                    break;
                }else stringEnabled = !stringEnabled;
                continue;
            }
            if (r.matcher(Character.toString(var.charAt(j))).find()) {
                if (type.equals("")) type = "integer";
            } else if (stringEnabled) {
                type = "string";
            }
            variable += var.charAt(j);
        }


        if (!type.equals("string") && (variable.contains("true") || variable.contains("false"))){
            type = "boolean";
        }


        if (variable.length() == 0 || variable.equals("null")) {
            type = "null";
            variable = "null";
        }

        if (variable.equals("line") && type.equals("")){
            type = "newline";
            variable = "";
        }

        if (type.equals("")) Errors.RaiseError("Not a type at: \"" + var + "\"");

        return new String[]{type, variable};
    }

    public static Values screen(Values values, String string, Integer i){
        System.out.println(parseVar(values, string, i)[1]);
        return values;
    }

    private static String[] getVar(Values values, String string, Integer i){
        String name = "";
        for (Integer j = i; j<string.length(); j++){
            name += Character.toString(string.charAt(j));
        }
        return values.getVariable(name);
    }

    public static Values setVar(Values values, String string, Integer i) {
        Values new_values = values;
        String varname = "";
        Integer end_i = 0;
        for (Integer j=i; j<string.length(); j++){
            String s = Character.toString(string.charAt(j));
            if (s.equals(" ")){end_i = j + 3; break;}
            varname += s;
        }
        new_values.addVariable(varname, parseVar(new_values, string, end_i));
        return new_values;
    }

    public static Values setArgVar(Values values, String string, Integer i) {
        Values new_values = values;
        String varname = "";
        Integer end_i = 0;
        for (Integer j=i; j<string.length(); j++){
            String s = Character.toString(string.charAt(j));
            if (s.equals(" ")){end_i = j + 3; break;}
            varname += s;
        }
        new_values.getFunctionArguments(new_values.getCurrentFunction()).put(varname, parseVar(new_values, string, end_i));
        return new_values;
    }

    public static Values getInput(Values values, String string, Integer i) {
        Values new_values = values;
        String varname = "";
        String text = "";
        Integer end_i = 0;
        for (Integer j=i; j<string.length(); j++){
            String s = Character.toString(string.charAt(j));
            if (s.equals(",")){end_i = j + 2; break;}
            varname += s;
        }
        for (Integer j=end_i; j<string.length(); j++){
            String s = Character.toString(string.charAt(j));
            text += s;
        }
        Scanner reader = new Scanner(System.in);
        System.out.print(">> " + parseVar(values, text, 0)[1]);
        new_values.addVariable(varname, parseVar(new_values, "\"" + reader.next() + "\"", 0));
//        reader.close();
        return new_values;
    }

    public static Boolean getIfValue(Values values, String string, Integer i) {
        Boolean state = true;
        Integer action = 0;
        String operator = "";
        String firstOperand = "";
        String secondOperand = "";
        for (Integer j = i; j<string.length(); j++){
            String cc = Character.toString(string.charAt(j));
            if (cc.equals(")") || cc.equals("(")) continue;
            if (cc.equals(" ")) {action++; continue;}
            if (action == 0) firstOperand += cc;
            if (action == 1) operator += cc;
            if (action == 2) secondOperand += cc;
        }
        String[] parsedFirstOperand = parseVar(values, firstOperand, 0);
        String[] parsedSecondOperand = parseVar(values, secondOperand, 0);
//        System.out.println(operator);
        if (parsedFirstOperand == null || parsedSecondOperand == null) state = false;
        else {
            if (parsedFirstOperand[0].equals(parsedSecondOperand[0])) {
                switch (operator) {
                    case "==":
                        state = (parsedFirstOperand[0].equals(parsedSecondOperand[0])) && (parsedFirstOperand[1].equals(parsedSecondOperand[1]));
                        break;
                    case "!=":
                        state = !((parsedFirstOperand[0].equals(parsedSecondOperand[0])) && (parsedFirstOperand[1].equals(parsedSecondOperand[1])));
                        break;
                    case "<":

                            if (parsedFirstOperand[0].equals("integer") && parsedSecondOperand[0].equals("integer")) {
                                state = Integer.parseInt(parsedFirstOperand[1]) < Integer.parseInt(parsedSecondOperand[1]);
                            } else if (parsedFirstOperand[0].equals("string") && parsedSecondOperand[0].equals("string")) {
                                state = parsedFirstOperand[1].length() < parsedSecondOperand[1].length();
                            }
                        break;
                }
            } else{
                if (parsedFirstOperand[0].equals("null") || parsedSecondOperand[0].equals("null")) return false;
                Errors.RaiseError("Incompatible types " + parsedFirstOperand[0] + " and " + parsedSecondOperand[0] + " at \"" + string + "\"");
            }
        }
        return state;
    }

    public static Values convert(Values values, String string, Integer i) {
        Values new_values = values;
        String varname = "";
        String toType = "";
        Integer action = 0;
        for (Integer j = i; j<string.length(); j++){
            String cc = Character.toString(string.charAt(j));
            if (cc.equals(" ") || cc.equals("-")) {action++; continue;}
            if (action == 0) varname += cc;
            if (action == 2) toType += cc;
        }
        
        if (toType.equals("null")) new_values.addVariable(varname, new String[] {"null", "null"});

        String[] varval = new_values.getVariable(varname);

        if (toType.equals("boolean")) {
            String bool;
            if (varval[1].equals("0")) bool = "false";
            else bool = "true";
            new_values.addVariable(varname, new String[] {"boolean", bool});
        }

        if (toType.equals("string")){
            Pattern r = Pattern.compile("^(.*)$");
            if (r.matcher(varval[1]).find()){
                new_values.addVariable(varname, new String[] {"string", varval[1]});
            }
        }

        if (toType.equals("integer")){
            Pattern r = Pattern.compile("^([0-9]*)$");
            if (r.matcher(varval[1]).find()){
                new_values.addVariable(varname, new String[] {"integer", varval[1]});
            }
        }

        return new_values;
    }

    public static String getDefName(Values values, String string, Integer i) {
        String name = "";
        for (Integer j = i; j<string.length(); j++){
            String cc = Character.toString(string.charAt(j));
            if (cc.equals("(")) {break;}
            name += cc;
        }
        return name;
    }

    public static Values runFunction(Values values, String string, Integer i) {
        String name = "";
        Integer action = 0;
        ArrayList<String[]> args = new ArrayList<>();
        String temp = "";
        for (Integer j = i; j<string.length(); j++){
            String cc = Character.toString(string.charAt(j));
            if (cc.equals("(")) {++action; continue;}

            if (action == 0) name += cc;
            if (action == 1) {
                if (cc.equals(",") || cc.equals(")")){
                    args.add(parseVar(values, temp, 0));
                    temp = "";
                } else {
                    temp += cc;
                }
            }
        }
        values.setCurrentFunction(name);
        return values.getFunction(name).run(values, args);
    }

    public static String[] getFilename(Values values, String string, Integer i) {
        String[] name;
        String rawname = "";
        if (String.valueOf(string.charAt(i)).equals("$")) return parseVar(values, string, i);
        for (Integer j = i; j<string.length(); j++){
            rawname += string.charAt(j);
        }
        name = new String[]{"string", rawname};
        return name;
    }

    public static String[] getDefArguments(Values values, String string, Integer i) {
        Integer action = 0;
        String[] endargs;
        ArrayList<String> args = new ArrayList<>();
        Integer argindex = 0;
        String temp = "";
        for (Integer j = i; j<string.length(); j++){
            String cc = Character.toString(string.charAt(j));
            if (cc.equals("(")) {++action; continue;}
            if (action == 1) {
                if (cc.equals(",") || cc.equals(")")){
                    args.add(temp);
                    temp = "";
                    argindex++;
                } else {
                    temp += cc;
                }
            }
        }
        endargs = new String[args.size()];
        for (Integer k = 0; k < args.size(); k++){
            endargs[k] = args.get(k);
        }

        return endargs;
    }
}

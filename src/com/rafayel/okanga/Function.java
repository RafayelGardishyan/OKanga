package com.rafayel.okanga;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Function{
    private String name;
    private String code;
    private String[] arguments;
    private List<String> tokenizedCode;

    public Function(FunctionProperties fp) {
        this.name = fp.name;
        this.arguments = fp.arguments;
        this.code = fp.code;
    }

    private void tokenizeCode(){
        this.tokenizedCode = Tokenizer.readString(this.code);
    }

    public Values run(Values values, ArrayList<String[]> inputArguments){
        Hashtable<String, String[]> args = new Hashtable<>();

        if (inputArguments.size() !=  arguments.length) Errors.RaiseError(String.format("Function %s requires %d arguments but %d were given", this.name, arguments.length, inputArguments.size()));

        for (int i = 0; i < inputArguments.size() && i < arguments.length; i++){
            args.put(arguments[i], inputArguments.get(i));
        }
        values.addFunctionArguments(this.name, args);
        tokenizeCode();
        Values new_values = Parser.parseTokens(this.tokenizedCode, values);
        new_values.deleteLastFunction();
        return new_values;
    }

}

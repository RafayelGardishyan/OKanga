package com.rafayel.okanga;

import java.util.List;

public class Function{
    private String name;
    private String code;
    private List<String> tokenizedCode;

    public Function(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private void tokenizeCode(){
        this.tokenizedCode = Reader.readString(this.code);
    }

    public Values run(Values values){
        tokenizeCode();
        return Tokenizer.parseTokens(this.tokenizedCode, values);
    }

}

package com.rafayel.okanga;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> file = Tokenizer.readFile("C:\\Users\\rgard\\OneDrive\\Documents\\oKanga\\test.okg");
        Parser.parseTokens(file, new Values());
    }
}

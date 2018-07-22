package com.rafayel.okanga;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> file = Reader.readFile("C:\\Users\\rgard\\OneDrive\\Documents\\oKanga\\test.okg");
        Tokenizer.parseTokens(file, new Values());
    }
}

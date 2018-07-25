package com.rafayel.okanga;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public static Values parseTokens(List<String> fileOpenerOutput, Values values){
        ArrayList<String> Tokens = values.getTokens();
        Block currentBlock = new Block(null);
        Boolean recordingFunction = false;
        String functionBody = "";
        String defName = "";
        String[] functionArguments = new String[] {null};
        for (String string: fileOpenerOutput) {
            String temp = "";
            for (int i = 0; i < string.length(); i++) {
                if (temp.equals("\t") || temp.equals(" ")) {
                    temp = "";
                    break;
                }

                temp += string.charAt(i);
                if (recordingFunction) functionBody += string.charAt(i);

                if (Tokens.contains(temp)) {
                    if (temp.equals("endef")) {
                        recordingFunction = false;
                        values.addFunction(defName, new Function(new FunctionProperties(defName, functionArguments, functionBody)));
                        break;
                    }

                    if (!recordingFunction) {
                        if (temp.equals("import:")){
                            String filename = Core.getFilename(values, string, i + 2)[1];
                            List<String> file = Reader.readFile(filename);
                            values = Tokenizer.parseTokens(file, values);
                        }

                        if (temp.equals("def:")) {
                            functionBody = "";
                            defName = Core.getDefName(values, string, i + 2);
                            functionArguments = Core.getDefArguments(values, string, i + 2);
                            recordingFunction = true;
                        }
                        if (temp.equals("call:")) {values = Core.runFunction(values, string, i + 2);}

                        if (temp.equals("//")) break;

                        if (temp.equals("endif")) {
                            currentBlock = currentBlock.getParrentBlock();
                            break;
                        }
                        if (temp.equals("else")) {
                            if (!currentBlock.getSkipElse()) {
                                currentBlock.setIfState(true);
                                currentBlock.setInElse(true);
                            } else currentBlock.setIfState(false);
                            break;
                        }
                        if (temp.equals("elif")) {
                            currentBlock.setIfState(Core.getIfValue(values, string, i + 2));
                            checkif(currentBlock);
                        }
                        if (currentBlock.getIfState() || (!currentBlock.getSkipElse() && currentBlock.getInElse())) {
                            if (temp.equals("if")) {
                                currentBlock = new Block(currentBlock);
                                currentBlock.setIfState(Core.getIfValue(values, string, i + 2));
                                checkif(currentBlock);
                            }
                            if (temp.equals("screen:")) values = Core.screen(values, string, i + 2);
                            if (temp.equals("var:")) values = Core.setVar(values, string, i + 2);
                            if (temp.equals("$")) values = Core.setVar(values, string, i + 1);
                            if (temp.equals("!")) values = Core.setArgVar(values, string, i + 1);
                            if (temp.equals("input:")) values = Core.getInput(values, string, i + 2);
                            if (temp.equals("convert:")) values = Core.convert(values, string, i + 2);
                            break;
                        }
                    }
                }
            }
            functionBody += ";";
        }
        return values;
    }

    private static void checkif(Block currentBlock) {
        if (currentBlock.getIfState()) {currentBlock.setSkipElse(true); currentBlock.setIfState(true);}
        else {currentBlock.setIfState(false); currentBlock.setSkipElse(false);}
    }
}

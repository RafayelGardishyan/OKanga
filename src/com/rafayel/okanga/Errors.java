package com.rafayel.okanga;

public class Errors {
    public static void RaiseError(String message){
        System.out.println("\n\tERROR");
        System.out.println("\t" + message);
        System.exit(1);
    }

}

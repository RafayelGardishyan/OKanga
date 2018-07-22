package com.rafayel.okanga;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.FileReader;

public class Reader {
    public static List<String> readFile(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.addAll(Arrays.asList(line.split(";")));
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readString(String string){
        List<String> records = new ArrayList<String>();
        records.addAll(Arrays.asList(string.split(";")));
        return records;
    }
}

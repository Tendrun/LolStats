package com.backendwebsite.DatabaseBuilder.Helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DatabaseHelper {
    public static String reader(BufferedReader reader){
        return reader.lines().reduce("", (a, b) -> a + b);
    }
}

package com.backendwebsite.DatabaseBuilder.Helper;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DatabaseHelper {
    public static String reader(BufferedReader reader){
        return reader.lines().reduce("", (a, b) -> a + b);
    }
}

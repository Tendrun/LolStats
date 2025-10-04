package com.backendwebsite.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class KeysLoader {
    public static String loadSecretValue(String key) {
        String fileLocation = "secret/secret.txt";
        try (InputStream inputStream = KeysLoader.class.getClassLoader().getResourceAsStream(fileLocation);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key + "=")) {
                    return line.split("=", 2)[1].replace("'", "").trim();
                }
            }
            throw new RuntimeException(key + " not found in " + fileLocation);
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Error reading secret from file: " + fileLocation, e);
        }
    }
}

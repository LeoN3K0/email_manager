package com.leon3k0;
import java.util.Random;

public class PasswordGenerator {
    public String generateRandomPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%^&*()_-+=<>?";

        String allowedCharacters = uppercaseLetters + lowercaseLetters + numbers + specialCharacters;

        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(allowedCharacters.length());
            password.append(allowedCharacters.charAt(index));
        }

        return password.toString();
    }
    
}

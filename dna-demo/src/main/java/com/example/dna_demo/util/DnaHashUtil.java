package com.example.dna_demo.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DnaHashUtil {

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Generates a SHA-256 hash from a DNA sequence array
     * @param dna Array of DNA strings
     * @return Hexadecimal string representation of the hash
     */
    public static String generateHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

            // Concatenate all DNA strings with a delimiter
            String concatenated = String.join("|", dna);

            byte[] hashBytes = digest.digest(concatenated.getBytes(StandardCharsets.UTF_8));

            // Convert bytes to hexadecimal string
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Converts a DNA array to a single string for storage
     * @param dna Array of DNA strings
     * @return Concatenated DNA string with delimiter
     */
    public static String dnaToString(String[] dna) {
        return String.join("|", dna);
    }

    /**
     * Converts stored DNA string back to array
     * @param dnaString Concatenated DNA string
     * @return Array of DNA strings
     */
    public static String[] stringToDna(String dnaString) {
        return dnaString.split("\\|");
    }

    /**
     * Converts byte array to hexadecimal string
     * @param bytes Byte array
     * @return Hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

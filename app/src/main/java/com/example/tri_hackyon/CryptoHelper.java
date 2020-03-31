package com.example.tri_hackyon;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class CryptoHelper {

    public String digestString(String plaintext){
        byte[] rawPlain;
        MessageDigest md;

        try {
            rawPlain = plaintext.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "error";
        }
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        }

        rawPlain = md.digest(rawPlain);

        try {
            String hash = new String(rawPlain, "UTF8");
            return hash;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "error";
        }
    }




}

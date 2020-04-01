package com.example.tri_hackyon;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

class CryptoHelper {
    private byte[] keyValue;
    public String encrypt(String plaintext,String password) throws Exception {
        keyValue = digestByte(password);
        byte[] rawKey = getRawKey();
        SecretKey key = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return toHex(encrypted);
    }

    public String decrypt(String ciphertext,String password) throws Exception {
        keyValue = digestByte(password);
        byte[] enc = toByte(ciphertext);
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(enc);
        String plaintext = new String(decrypted,"UTF-8");
        return plaintext;
    }

    private byte[] getRawKey() throws Exception {
        SecretKey key = new SecretKeySpec(keyValue, "AES");
        byte[] raw = key.getEncoded();
        return raw;
    }



    public byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final String HEX = "0123456789ABCDEF";

    private void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    private byte[] digestByte(String plaintext){
        byte[] rawPlain;
        MessageDigest md;
        try {
            rawPlain = plaintext.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        rawPlain = md.digest(rawPlain);
        return rawPlain;
    }
    public String digestString(String plaintext){
        byte[] rawPlain;
        MessageDigest md;
        try {
            rawPlain = plaintext.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        rawPlain = md.digest(rawPlain);
        String hash = null;
        try {
            hash = new String(rawPlain,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hash;
    }
}

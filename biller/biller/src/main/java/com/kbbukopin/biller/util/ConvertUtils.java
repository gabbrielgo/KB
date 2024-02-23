package com.kbbukopin.biller.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

public class ConvertUtils {
    
    private static byte[] arranet_session_key;
    
    public static byte[] ConvertToBit48  (String value){
        try{
            String bit48 = value.substring(63,115);
            String str_arranet_session_key = bit48.substring (4,52);
            arranet_session_key = str_arranet_session_key.getBytes();
            
            System.out.println("Bit48 : " + bit48);
            System.out.println("str_arranet_session_key data: " + str_arranet_session_key);
            System.out.println("arranet_session_key data: " + arranet_session_key);
            
            return arranet_session_key;
        } catch (Exception e){
           e.printStackTrace();
           return null;
        }
    }
        
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    
    public static int HexToDecimal(String hexStr) {
	    return Integer.parseInt(hexStr, 16);
    }
    
    public static String stringToHex0x(String s) {
      String result = "";

      for (int i = 0; i < s.length(); i++) {
        result += String.format("0x%02X ", (int) s.charAt(i));
      }

      return result;
    }
    
    public static String stringToHex(String s) {
      String result = "";

      for (int i = 0; i < s.length(); i++) {
        result += String.format("%02X ", (int) s.charAt(i));
      }

      return result;
    } 
    
    public static String hexToAscii(String hexStr) {
       StringBuilder output = new StringBuilder("");
	    
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }
    
    public static String asciiToHex(String ascii) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        return hex.toString();
    }
    
    public static String padData(String data) {
        // Add NoPadding manually, as it is not supported by Cipher padding options
        int paddingSize = 8 - (data.length() % 8);
        for (int i = 0; i < paddingSize; i++) {
            data += '\0';
        }
        return data;
    }

    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String removePadding(String data) {
        // Remove padding manually
        return data.replaceAll("\0", "");
    }
    
    public static void saveVariableToFile(Object variable) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arranetKey.txt"))) {
            writer.write(variable.toString());
            System.out.println("Variable saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readVariableFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String value = reader.readLine();
            return value != null ? value : ""; // Return an empty string if file is empty
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    

  }

package com.kbbukopin.biller.util;

import static com.kbbukopin.biller.Arranet.ArranetRequestKey.arranet_trace_number;
import static com.kbbukopin.biller.util.ConvertUtils.hexStringToByteArray;
import static com.kbbukopin.biller.util.ConvertUtils.byteArrayToHexString;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;


public class EncryptionUtils {
    
    public static String decryptData(String arranetSessionKey, String arranet_private_key) throws Exception {
       SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(arranet_private_key), "DESede");

       Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
       cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

       byte[] decryptedSessionKeyBytes = cipher.doFinal(hexStringToByteArray(arranetSessionKey));
       String decryptSessionKey = byteArrayToHexString(decryptedSessionKeyBytes);

       System.out.println("decrypt_session_key:" + decryptSessionKey);
       return decryptSessionKey;
   }

//     private static String encrypt(String data, String key) throws Exception {
//        Cipher encryptCipher = Cipher.getInstance("DESede/ECB/NoPadding");
//        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "DESede");
//        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptedBytes = encryptCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        return DatatypeConverter.printHexBinary(encryptedBytes);
//    }
     
     public static String encryptData(String decryptSessionKey, String trace_number) throws Exception {
        String traced_number = trace_number + "FFFF";
        byte[] traceNumberBytes = hexStringToByteArray(traced_number);
        byte[] decryptSessionKeyBytes = hexStringToByteArray(decryptSessionKey);

        Cipher encryptCipher = Cipher.getInstance("DESede/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(decryptSessionKeyBytes, "DESede"));

        byte[] arranetKeyBytes = encryptCipher.doFinal(traceNumberBytes);
        String arranetKey = ConvertUtils.byteArrayToHexString(arranetKeyBytes);
        
        System.out.println("arranet_trace_number:" + traced_number);
        System.out.println("arranet_key:" + arranetKey);
        return arranetKey;
     } 
}

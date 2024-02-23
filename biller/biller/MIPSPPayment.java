/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kbbukopin.biller;

/**
 *
 * @author sunardo
 */
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import com.nimbusds.jwt.EncryptedJWT;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MIPSPPayment {

    public static void main(String[] args) throws Exception {
        String pemPrivateKey = """
                               -----BEGIN PRIVATE KEY-----
                               MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCH68y8X3Uzaotd
                               ufhhF+smvjjT4ItvRXu7hZRewO6GtSloYopvxsjaLgF5K3MP18z+SLQZ2TkFI65s
                               MpqHdynrIROHhOCh+YayGE3D4aDueV/lUgglbhc28+0kRBVbPBX9EB8jomBHIMFW
                               RMmR6fU1zzUXfcGDFuOBdb5kKN2zInEjFh8Xm+3rWcXdt8i4wRg8e8Fexrzge8pv
                               wyMKEDvODdo+gnQjLel4Hb2biMhU6SZ2DJXP7Y4MV7lB8VI3JKa8DTyRFSuV+QEE
                               4ol/ZNvrDuTlgavfbf/xx2JTvnm1Rr62frrPpiDigC351gyYAjcL1Glfj5sNPa9W
                               DxG1hzKNAgMBAAECggEASAOcFxYwf4xsyLz2p2LH2WAhtcibt9nFo+YzZTytZDvp
                               8396P5y/4DFN4Igvz24UE4oruiJIPrvX90WccDenwwdkVIIHjmopfoS39xe9b3xp
                               XZPn8wTwUdJNi9ByzEL6l3kKNpQalLT7Gp0ZezDNFbRyOb8MX0sqQPZEaMbqJ7VK
                               02syq/Qlgnd9u/cXPstD5a7Xk6PBKphOWm64XTe9WpWYBbdn376csDsg1Yf1GO47
                               MdJJWeZ2cHvHoMW4x5rIrL+jBkjmL5zKfShfK4IQNOB1D3lgD6WrE0DPvHo0+4IK
                               lPfc7ce/XcLwJei6ERhcD6zSZsUObWZ301cHbuxdwQKBgQDGqvFGxdxT3+vWe5tr
                               0dcdzVPnBd4haWV6+oPCPC71fsUTh/uCfjKNaFbh5h/zq5l3WA9bMF3uk9V6nkRg
                               GIEziPBjuq9xW4mrBoFaGDwLYn513eut/uzBTIKfzAeunZM7ZkkMaJSvc/FHpPZN
                               aalSrX4BNTvvy98yMb1NXIqNnQKBgQCvJUwl2U2UnSZJTczr1Cle1crDnSl7BAET
                               3Asub1FbSjKSn3/NiD1g3MRSUnSda0aGvKFGtokYfE3W1B5a3h+vKEmQUD7XOJeH
                               4JZ8pZ5hdxQrHQl8yhJp979BnDAx80e9NeaeLZYJ8iDEmlUgb03roVsLMSpOmZ7M
                               dUWiH8qdsQKBgGoavkYoQqZhP5oL5lRNfHCkSx1l2t9JZTOrPFeuwP3IuQTXaZKm
                               R+WSRmTb/VCs44gD9h+j02cqeafFg4s3Szn09z6sZCtM0lgPX2J48wi5kATvg4Io
                               VtZeQJ2L4MZ3zjy5QmZoLJEQgVrcVde+iPppQpTuk5Zc8lXijtIUjMNZAoGAYwoB
                               NEnb39S5aKww244eUr0sKk89vI04GsXMINbbt3aOgHDm8Q9APfy4myB7RuT5fYk+
                               3WCsx4bK8VcDTzjZy89JQezOjB85OKGcFyIEHHMcmYCS89jEbt2kbfJnImfCQlEv
                               dnSqqjcbpnDrACtQcZTNidekZ8vgixcpaBL4HRECgYEAkn6NVNiOF0wjupJNIj6M
                               HIerUwpjq+bbCwVBDS8RiPbONLPfcrhLa43FiO2H4d7n9dccci6p0j5oHGqtNbHV
                               jWf9ZMwrJAKrjmjjD4BIJ8aCNKnAD1H9mNdmMlMTauWGPauBHwBwNWrIXXv+gZ+A
                               mVvKUeC/v++owmZr6hiR5pg=
                               -----END PRIVATE KEY-----""";
    

        String pemPublicKey = """
                              -----BEGIN PUBLIC KEY-----
                              MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh+vMvF91M2qLXbn4YRfr
                              Jr440+CLb0V7u4WUXsDuhrUpaGKKb8bI2i4BeStzD9fM/ki0Gdk5BSOubDKah3cp
                              6yETh4TgofmGshhNw+Gg7nlf5VIIJW4XNvPtJEQVWzwV/RAfI6JgRyDBVkTJken1
                              Nc81F33BgxbjgXW+ZCjdsyJxIxYfF5vt61nF3bfIuMEYPHvBXsa84HvKb8MjChA7
                              zg3aPoJ0Iy3peB29m4jIVOkmdgyVz+2ODFe5QfFSNySmvA08kRUrlfkBBOKJf2Tb
                              6w7k5YGr323/8cdiU755tUa+tn66z6Yg4oAt+dYMmAI3C9RpX4+bDT2vVg8RtYcy
                              jQIDAQAB
                              -----END PUBLIC KEY-----""";

        // Replace with the JWE HTTP request URL
        String httpRequestURL = "https://dev.infolink.co.id:30441";

        // Replace with your payload
        String payload = "{\n" +
                "    \"agentId\": \"AGENT\",\n" +
                "    \"amount\": \"140000\",\n" +
                "    \"billData\": {\n" +
                "        \"private\": \"062081818181818 1071440736 11 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000166500000000000000000000000000Nama Pelanggan 11052023 \"\n" +
                "    },\n" +
                "    \"billId\": \"0818181818181\",\n" +
                "    \"caId\": \"CA\",\n" +
                "    \"merchantType\": \"6017\",\n" +
                "    \"productCode\": \"013001\",\n" +
                "    \"requestId\": \"1212\",\n" +
                "    \"requestTime\": \"2023-05-04T09:02:22+07:00\",\n" +
                "    \"terminalId\": \"TERMID\"\n" +
                "}}";

        // JWE header
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512)
                .contentType("application/json")
                .keyID("c54c0f94-483e-3be3-8ef8-4cf6a1d17745")
                .build();

        // Convert private key to Java PrivateKey
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(pemPrivateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "")));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", BouncyCastleProviderSingleton.getInstance());
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // Convert public key to Java PublicKey
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pemPublicKey
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "")));
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Create JWE object
        JWEEncrypter encrypter = new RSAEncrypter((RSAPublicKey) publicKey);
        Payload jwePayload = new Payload(payload);
        JWEObject jweObject = new JWEObject(jweHeader, jwePayload);
        jweObject.encrypt(encrypter);

        // Get the JWE string
        String jweString = jweObject.serialize();

        // Make HTTP request
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(httpRequestURL))
                .header("Content-Type", "application/jwe")
                .header("Accept", "application/jwe")
                .POST(HttpRequest.BodyPublishers.ofString(jweString))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        // Process JWE response
        String jweResponse = httpResponse.body();
        System.out.println("JWE Response: " + jweResponse);

        // Decrypt the JWE response
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(jweResponse);
        RSADecrypter decrypter = new RSADecrypter(privateKey);
        encryptedJWT.decrypt(decrypter);

        // Get the decrypted payload
        String decryptedPayload = encryptedJWT.getPayload().toString();
        System.out.println("Decrypted Payload: " + decryptedPayload);
    }
}


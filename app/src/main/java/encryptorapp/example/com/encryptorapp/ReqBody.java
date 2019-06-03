package encryptorapp.example.com.encryptorapp;

import android.util.*;
import org.json.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;

public class ReqBody {

    String appVersion = "1.0.0" ;
    String IV = "YGFnUSIRVVA2aWU1ZClkEg==";
    JSONObject body ;
    static byte[] desRawKey = "My DES Key".getBytes();
    static byte[] aesRawKey = "My AES Key".getBytes();
    static byte[] desKey;
    static byte[] aesKey;

    ReqBody(String username, String password) throws JSONException {
        body = new JSONObject();
        body.put("username",username);
        body.put("password", password);
        body.put("timestamp",System.currentTimeMillis());
        body.put("appVersion", appVersion);
    }

    public static void initKeys () throws NoSuchAlgorithmException {
        MessageDigest desDigest = MessageDigest.getInstance("SHA-256");
        desDigest.update(desRawKey,0,desRawKey.length);
        desKey = desDigest.digest();

        MessageDigest aesDigest = MessageDigest.getInstance("SHA-256");
        aesDigest.update(aesRawKey,0,aesRawKey.length);
        aesKey = aesDigest.digest();
    }

    public String DESEncryption() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        DESKeySpec desKeySpec = new DESKeySpec(desKey);
        SecretKeyFactory desKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretDESKey = desKeyFactory.generateSecret(desKeySpec);
        Cipher desCipher = Cipher.getInstance("DES");
        desCipher.init(Cipher.ENCRYPT_MODE, secretDESKey);
        return Base64.encodeToString(desCipher.doFinal(body.toString().getBytes()),0) ;
    }


    public String AESEncryption() throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec aesKeySpec ;
        aesKeySpec = new SecretKeySpec(aesKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.decode(IV,0));
        Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec,ivSpec);
        byte[] res = aesCipher.doFinal(body.toString().getBytes());
        return Base64.encodeToString(res, 0) ;
    }
}

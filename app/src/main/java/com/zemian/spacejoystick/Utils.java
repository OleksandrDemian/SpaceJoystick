package com.zemian.spacejoystick;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Oleksandr on 21/03/2017.
 */

public class Utils {

    public static int toNum(String text){
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception){
            return -1;
        }
    }

    public static String encrypt(String input) {
        String strData="";
        String key = "keyandolokeyandolo";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted=cipher.doFinal(input.getBytes());
            strData=new String(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static String decrypt(String input) {
        String strData="";
        String key = "keyandolokeyandolo";

        try {
            SecretKeySpec skeyspec=new SecretKeySpec(key.getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted=cipher.doFinal(input.getBytes());
            strData=new String(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

}

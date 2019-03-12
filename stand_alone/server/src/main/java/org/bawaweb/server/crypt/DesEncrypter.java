package org.bawaweb.server.crypt;

/**
 * Created by medhoran on 12/20/13.
 * This class will be used for decrypting the card information from the UAT and QA database schema
 * smartagent_UAT2 schema
 */
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

public class DesEncrypter {

    private static final String UNICODE_FORMAT  = "UTF8";
    private static final String ALGORITHM  = "DESede";
    private static final String TRANSFORMATION = "DESede/ECB/NoPadding";

    // 192 bit Key bytes
    private byte[] keyBytes = null;

    // Cipher object for encryption
    private Cipher encipher;

    // Cipher object for decryption
    private Cipher decipher;

    /**
     * Constructor
     *
     * @param base64Key Key in base64 format
     */
    public DesEncrypter(String base64Key) throws Exception {
        keyBytes = Base64.decodeBase64(base64Key);

        // Make the key
        byte[] desedeKey = (new DESedeKeySpec(keyBytes)).getKey();
        SecretKey secretKey = new SecretKeySpec(desedeKey, ALGORITHM);

        // Create the ciphers
        encipher = Cipher.getInstance(TRANSFORMATION);
        decipher = Cipher.getInstance(TRANSFORMATION);
        encipher.init(Cipher.ENCRYPT_MODE, secretKey);
        decipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    /**
     * Encrypt a string
     *
     * @param str plain text to encrypt
     * @return Encrypted data in Base64 format
     */
    public String encryptString(String str) throws Exception {
        if (str == null) {
            return null;
        }

        byte[] utf8 = str.getBytes(UNICODE_FORMAT);
        byte[] enc = encipher.doFinal(utf8);

        return Base64.encodeBase64StringUnChunked(enc);
    }

    /**
     * Decrypt a string
     *
     * @param base64StringToDecrypt Encrypted string in Base64 format
     * @return plain text
     */
    public String decryptString(String base64StringToDecrypt) throws Exception {
        if (base64StringToDecrypt == null){
            return null;
        }

        byte[] dec = Base64.decodeBase64(base64StringToDecrypt.getBytes(UNICODE_FORMAT));
        byte[] utf8 = decipher.doFinal(dec);
        return new String(utf8, UNICODE_FORMAT).trim();
    }


    public static void main(String[] args) throws Exception {
        DesEncrypter des = new DesEncrypter("du345678234567mmy");
        final String encNum = "MmL7uUi/XY7G7acWTqdZZtW7OyQvdfwI";
        System.out.println(des.decryptString(encNum));
    }

}

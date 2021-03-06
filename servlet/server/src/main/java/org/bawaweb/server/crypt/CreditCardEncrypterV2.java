///**
// * 
// */
//package org.bawaweb.server.crypt;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.Provider;
//import java.security.spec.InvalidKeySpecException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Set;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.PBEKeySpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.bouncycastle.crypto.BufferedBlockCipher;
//import org.bouncycastle.crypto.CipherParameters;
//import org.bouncycastle.crypto.DataLengthException;
//import org.bouncycastle.crypto.InvalidCipherTextException;
//import org.bouncycastle.crypto.engines.AESEngine;
//import org.bouncycastle.crypto.engines.RijndaelEngine;
//import org.bouncycastle.crypto.modes.CBCBlockCipher;
//import org.bouncycastle.crypto.paddings.BlockCipherPadding;
//import org.bouncycastle.crypto.paddings.PKCS7Padding;
//import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
//import org.bouncycastle.crypto.params.KeyParameter;
//import org.bouncycastle.crypto.params.ParametersWithIV;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.jasypt.encryption.pbe.StandardPBEBigIntegerEncryptor;
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
//import org.jasypt.salt.FixedStringSaltGenerator;
//
///**
// * @author medhoran
// * Will handle the credit card encryption using jasypt StandardPBEStringEncryptor and boundcycastle 
// *  http://stackoverflow.com/questions/5641326/256bit-aes-cbc-pkcs5padding-with-bouncy-castle
// *  http://stackoverflow.com/questions/12573186/aes-256-instead-of-128-with-bouncycastle?lq=1
// *  http://moi.vonos.net/java/symmetric-encryption-bc/
// *  
// *
// */
//public final class CreditCardEncrypter {
//	//                   StandardPBEStringEncryptor 
//	private final static StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
//	
//	private static Cipher cipher = null;
//	
//	private static final String PASSWORD_STR = "abcdefghijklmnopqrstuvxyz";				//@TODO - whirlpool, passwords
//	private static final String ALGORITHM_STR = "PBEWithSHA256And256BitAES-CBC-BC"; //"PBEWithSHA1AndRC2";//"Blowfish";// "PBEWithMD5AndDES";//"WHIRLPOOL";//"PBEWithMD5AndDES";//"PBEWithSHAAndTwofish-CBC";//"PBEWITHSHA256AND128BITAES-CBC-BC";		//"PBEWithMD5AndTripleDES";//@TODO - whirlpool, passwords
//	private static final String SECRET_KEY_STR = "PBEWithSHA256And256BitAES-CBC-BC";
//	private static SecretKeySpec secretKey;
//	private static final String SALT_STR = "564732";
//	
//	private static byte[] key;
//	
//	private CreditCardEncrypter(){}
//	
//	static {
//		
//		java.security.Security.addProvider(new BouncyCastleProvider());
//
//		try {
//			
//			stringEncryptor.setProviderName("BC");
//			
//			// AES algorithm with CBC cipher and PKCS5 padding
//			
//			cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
//			
//			// Construct AES key from salt and 50 iterations 
//		    PBEKeySpec pbeEKeySpec = new PBEKeySpec(PASSWORD_STR.toCharArray(), SALT_STR.getBytes(), 50, 256);
//		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_STR);
//		    secretKey = new SecretKeySpec(keyFactory.generateSecret(pbeEKeySpec).getEncoded(), "AES");
//		    
//		    byte[] key = secretKey.getEncoded();
//		    
//		    
//		    
//		    
//		    
//		    /*java.security.Provider p[] = java.security.Security.getProviders();
//			for(int i = 0; i < p.length; i++)
//				System.out.println(i + "   " + p[i].getName());*/
//			
//			stringEncryptor.setPassword(PASSWORD_STR);
//			stringEncryptor.setAlgorithm(ALGORITHM_STR);
//			
//			FixedStringSaltGenerator generator = new FixedStringSaltGenerator();
//			generator.setSalt("SALT_STR");
//			stringEncryptor.setSaltGenerator(generator);
//			
//			stringEncryptor.initialize();
//
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			e.printStackTrace();
//		} catch (NoSuchProviderException e) {
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//		}
//	}
//	
//
//	
//	
///*	public static String encryptCardNumber(String cardNumber) {
//		System.out.println("Number to encrypt [card number] is " + cardNumber);
//		StandardPBEBigIntegerEncryptor numberEncryptor = new StandardPBEBigIntegerEncryptor();
//		numberEncryptor.setPassword("abcdefg");
//		numberEncryptor.setAlgorithm("PBEWithMD5AndTripleDES");
//		BigInteger encryptedNumber = null;
//		do {
//			encryptedNumber = numberEncryptor.encrypt(new BigInteger(cardNumber));
//
//			System.out.println("for number " + cardNumber + " encrypted number val is " + encryptedNumber.longValue() + " and it is " + encryptedNumber);
//		} while (encryptedNumber.longValue() < 0);
//		
////		System.out.println("for number " + cardNumber + " encrypted number is " + encryptedNumber);
//
//		return encryptedNumber.toString();
//	}	
//	
//	
//	public static String decryptCardNumber(String encryptedCardNumber) {
//		System.out.println("Number to decrypt[encryptedCardNumber] is  " + encryptedCardNumber);
//		StandardPBEBigIntegerEncryptor numberEncryptor = new StandardPBEBigIntegerEncryptor();		
//		numberEncryptor.setPassword("abcdefg");
//		numberEncryptor.setAlgorithm("PBEWithMD5AndTripleDES");
//		BigInteger decryptedNumber = numberEncryptor.decrypt(new BigInteger(encryptedCardNumber));
//			
//		System.out.println("for encryptednumber " + encryptedCardNumber + " decrypted number is " + decryptedNumber);
//		return decryptedNumber.toString();
//	}
//	
//*/	
//	public static String encryptCardNumber(String cardNumber) throws EncryptionOperationNotPossibleException {
//		System.out.println("in encryptCardNumber cardNumber is " + cardNumber);
//		
//System.out.println("in cardNumber CardNumber is " + cardNumber);
//		
//		//// will change getBytes to do hex encoding
//		// IV seed for first block taken from first 32 bytes
////	    byte[] ivData = cardNumber.substring(0, 32).getBytes();
//	    // AES encrypted data
////	    byte[] encData = cardNumber.substring(32).getBytes();
//	    
//	 // setup cipher parameters with key and IV
//	    KeyParameter keyParam = new KeyParameter(key);
////	    CipherParameters params = new ParametersWithIV(keyParam, ivData);
//	    
//	 // setup AES cipher in CBC mode with PKCS7 padding
//	    BlockCipherPadding padding = new PKCS7Padding();
//	    BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
//	            new CBCBlockCipher(new RijndaelEngine(256)), padding);
//	    cipher.reset();
////	    cipher.init(false, params);
//
//	    // do the encrypting
//	    
//	    return null;
//
////		
////		String encryptedString = stringEncryptor.encrypt(cardNumber);
////		System.out.println("for number " + cardNumber + " encrypted number is " + encryptedString);
////		return encryptedString;
//	}
//	
////	public static String enc(String cardNumber) throws EncryptionOperationNotPossibleException {
////		 byte[] ivData = card.substring(0, 32).getb;
////		    byte[] encData = toByte(encString.substring(32));
////
////		    // get raw key from password and salt
////		    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),
////		            toByte(salt), 50, 256);
////		    SecretKeyFactory keyFactory = SecretKeyFactory
////		            .getInstance("PBEWithSHA256And256BitAES-CBC-BC");
////		    SecretKeySpec secretKey = new SecretKeySpec(keyFactory.generateSecret(
////		            pbeKeySpec).getEncoded(), "AES");
////		    byte[] key = secretKey.getEncoded();
////
////		    // setup cipher parameters with key and IV
////		    KeyParameter keyParam = new KeyParameter(key);
////		    CipherParameters params = new ParametersWithIV(keyParam, ivData);
////
////		    // setup AES cipher in CBC mode with PKCS7 padding
////		    BlockCipherPadding padding = new PKCS7Padding();
////		    BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
////		            new CBCBlockCipher(new AESEngine()), padding);
////		    cipher.reset();
////		    cipher.init(false, params);
////
////		    // create a temporary buffer to decode into (it'll include padding)
////		    byte[] buf = new byte[cipher.getOutputSize(encData.length)];
////		    int len = cipher.processBytes(encData, 0, encData.length, buf, 0);
////		    len += cipher.doFinal(buf, len);
////
////		    // remove padding
////		    byte[] out = new byte[len];
////		    System.arraycopy(buf, 0, out, 0, len);
////
////		    // return string representation of decoded bytes
////		    return new String(out, "UTF-8");
////
////	}
//	
//	public static String decryptCardNumber(String encryptedCardNumber) throws EncryptionOperationNotPossibleException {
//		System.out.println("in decryptCardNumber encryptedCardNumber is " + encryptedCardNumber);
//		
//		//// will change getBytes to do hex encoding
//		// IV seed for first block taken from first 32 bytes
//	    byte[] ivData = encryptedCardNumber.substring(0, 32).getBytes();
//	    // AES encrypted data
//	    byte[] encData = encryptedCardNumber.substring(32).getBytes();
//	    
//	 // setup cipher parameters with key and IV
//	    KeyParameter keyParam = new KeyParameter(key);
//	    CipherParameters params = new ParametersWithIV(keyParam, ivData);
//	    
//	 // setup AES cipher in CBC mode with PKCS7 padding
//	    BlockCipherPadding padding = new PKCS7Padding();
//	    BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
//	            new CBCBlockCipher(new RijndaelEngine(256)), padding);
//	    cipher.reset();
//	    cipher.init(false, params);
//
//	    // create a temporary buffer to decode into (it'll include padding)
//	    byte[] buf = new byte[cipher.getOutputSize(encData.length)];
//	    int len = cipher.processBytes(encData, 0, encData.length, buf, 0);
//	    try {
//			len += cipher.doFinal(buf, len);
//
//			// remove padding
//			byte[] out = new byte[len];
//			System.arraycopy(buf, 0, out, 0, len);
//
//			// return string representation of decoded bytes
//			return new String(out, "UTF-8");
//		} catch (DataLengthException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (InvalidCipherTextException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	    
//	    return null;
////
////		
////		
////		String decryptedString = stringEncryptor.decrypt(encryptedCardNumber);
////		System.out.println("for encryptednumber " + encryptedCardNumber + " decrypted number is " + decryptedString);
////		return decryptedString;
//	}
//	
//
//	
//	
//	
//	
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		CreditCardEncrypter cce = new CreditCardEncrypter();
//		final String psswd = "abcdefghijklmnopqrstuvxyz";
//		
//		String number = "1111111111111111";
//		System.out.println("Testing number " + number);
//		
//		String encStr = cce.encryptCardNumber(number);
//		System.out.println("Encrypted number is " + encStr);
//		
//		String decStr = cce.decryptCardNumber(encStr);
//		System.out.println("Decrypted number is " + decStr);
//		
//
//		ArrayList<String> numbers = new ArrayList<String>();
//		numbers.add("123456734545");
//		numbers.add("12334673453445");
//		numbers.add("12234567345456");
//		numbers.add("1230987734456");
//		numbers.add("1234765543456");
//		numbers.add("12342342344565");
//		numbers.add("1239875434356");
//		numbers.add("12654373423455");
//		numbers.add("12345677472345");
//		numbers.add("12345677632455");
//		
//		HashMap<String, String> numMap = new HashMap<String, String>();
//		
//		for (int i = 0; i < numbers.size(); i++) {
//			String s = numbers.get(i);
//			
//			String enc = CreditCardEncrypter.encryptCardNumber(s);
//			
//			numMap.put(enc, s);
//			
////			BigInteger b = new BigInteger(enc);
//		}
//		
//		Set<String> encKeys = numMap.keySet();
//		
//		Iterator<String> it = encKeys.iterator();
//		while ( it.hasNext() ) {
//			String s = it.next();
//			
//			String dec = CreditCardEncrypter.decryptCardNumber(s);
//			
//			if ( !dec.equals(numMap.get(s))) {
//				System.out.println("number entered was " + numMap.get(s) + " encrypted value was " + s + " and decrypted value was " + dec);
//			}
//			
////			BigInteger b = new BigInteger(dec);
//		}
//		
//		System.out.println("All clear");
//
//	}
//
//}
///**
//new JasyptURLEncryptionConfigurator() {
//
//08. 
//
//09.public void configure(StandardPBEStringEncryptor encryptor) {
//
//10.encryptor.setAlgorithm("PBEWithMD5AndDES");
//
//11.encryptor.setPassword("jasypt");
//
//12.FixedStringSaltGenerator generator = new FixedStringSaltGenerator();
//
//13.generator.setSalt("tapestry5");
//
//14.encryptor.setSaltGenerator(generator);
//
//15.encryptor.setStringOutputType("hexadecimal");
//
//16.}
//
//17.});**/
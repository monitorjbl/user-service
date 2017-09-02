package io.monitorjbl.model;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import static io.monitorjbl.model.Crypter.CipherMode.DECRYPT;
import static io.monitorjbl.model.Crypter.CipherMode.ENCRYPT;

/**
 * Encrypts/Decrypts arbitrary String content using the AES cipher.
 */
public class Crypter {

  private static Key aesKey;

  enum CipherMode {ENCRYPT, DECRYPT}

  /**
   * Sets the secret key to be used for encyption/decryption. The key must be exactly
   * 16 characters long.
   *
   * @param key The secret key
   */
  public synchronized static void setKey(String key) {
    Assert.hasLength(key, "Key must not be empty or null");
    if(key.length() != 16) {
      throw new IllegalArgumentException("Key must be 16 characters long");
    }

    aesKey = new SecretKeySpec(key.getBytes(), "AES");
  }

  /**
   * Accepts any text string and returns a Base64-encoded string with the
   * encrypted content.
   *
   * @param text Input string
   * @return Encrypted string
   */
  public static String encrypt(String text) {
    Assert.notNull(aesKey, "Must set key before using encrypt()");
    if(text == null) {
      return null;
    }

    try {
      return new String(Base64.encodeBase64(cipher(ENCRYPT).doFinal(text.getBytes())));
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException("Unable to encrypt text", e);
    }
  }

  /**
   * Accepts a Base64-encoded representation of an encrypted string and
   * returns decrypted content.
   *
   * @param text Encrypted string
   * @return Decrypted string
   */
  public static String decrypt(String text) {
    Assert.notNull(aesKey, "Must set key before using decrypt()");
    if(text == null) {
      return null;
    }

    try {
      return new String(cipher(DECRYPT).doFinal(Base64.decodeBase64(text.getBytes())));
    } catch(IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException("Unable to encrypt text", e);
    }
  }

  private static Cipher cipher(CipherMode mode) {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      switch(mode) {
        case ENCRYPT:
          cipher.init(Cipher.ENCRYPT_MODE, aesKey);
          break;
        case DECRYPT:
          cipher.init(Cipher.DECRYPT_MODE, aesKey);
          break;
      }
      return cipher;
    } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
      throw new IllegalArgumentException("Unable to initialize cipher", e);
    }
  }

}

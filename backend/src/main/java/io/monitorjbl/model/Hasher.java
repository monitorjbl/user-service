package io.monitorjbl.model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Performs one-way, salted hashing to securely store passwords
 */
public class Hasher {

  private static final SecureRandom random = new SecureRandom();
  private static final SecretKeyFactory keyFactory;

  static {
    try {
      keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    } catch(NoSuchAlgorithmException e) {
      throw new RuntimeException("Could not initialize key factory", e);
    }
  }

  /**
   * Generates a new random salt, encoded in Base64.
   *
   * @return Salt string
   */
  public static String newSalt() {
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  /**
   * Returns a salted, hashed value for the given input text and salt.
   *
   * @param text The text to hash
   * @param salt The salt to use
   * @return The hashed value
   */
  public static String hashWithSalt(String text, String salt) {
    try {
      byte[] saltBytes = Base64.getDecoder().decode(salt);
      KeySpec spec = new PBEKeySpec(text.toCharArray(), saltBytes, 65536, 128);
      byte[] hash = keyFactory.generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } catch(InvalidKeySpecException e) {
      throw new RuntimeException("Could not hash input", e);
    }
  }

  /**
   * Returns whether or not the plaintext value matches the hashed value
   *
   * @param plaintext Unhashed string to check
   * @param hashed    Hashed string
   * @param salt      Salt value used to generate hashed string
   * @return Whether plaintext matches hash
   */
  public static boolean matchesHashedValue(String plaintext, String hashed, String salt) {
    return Objects.equals(hashed, hashWithSalt(plaintext, salt));
  }

}

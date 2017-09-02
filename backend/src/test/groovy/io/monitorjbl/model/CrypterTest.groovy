package io.monitorjbl.model

import spock.lang.Specification

class CrypterTest extends Specification {

  def 'Crypter should disallow keys that are not 16 chars long'() {
    when: 'The key is less than 16 chars'
    Crypter.setKey("asdf")

    then: 'An exception should be thrown'
    thrown(IllegalArgumentException)

    when: 'The key is greater than 16 chars'
    Crypter.setKey("asdfasdfasdfasdfasdf")

    then: 'An exception should be thrown'
    thrown(IllegalArgumentException)

    when: 'The key is null'
    Crypter.setKey(null)

    then: 'An exception should be thrown'
    thrown(IllegalArgumentException)
  }

  def 'Crypter should encrypt String data and be able to decrypt it'() {
    given: 'Crypter has a valid key'
    Crypter.setKey("asdfasdfasdfasdf")

    when: 'A string is passed to encrypt'
    def testData = "im a little teapot, short and stout\nhere is my handle, here is my spout"
    def encrypted = Crypter.encrypt(testData)

    then: 'The result is not the same'
    encrypted != testData

    and: 'The result is a base64 string'
    Base64.decoder.decode(encrypted)

    when: 'The encrypted string is passed to decrypt'
    def decrypted = Crypter.decrypt(encrypted)

    then: 'The result is the same as the original input'
    decrypted == testData
  }

  def 'Crypter should return null if given null strings'() {
    when: 'A null string is passed to encrypt'
    def encrypted = Crypter.encrypt(null)

    then: 'A null string is returned'
    encrypted == null

    when: 'A null string is passed to decrypt'
    def decrypted = Crypter.decrypt(null)

    then: 'A null string is returned'
    decrypted == null
  }
}

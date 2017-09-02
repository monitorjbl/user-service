package io.monitorjbl.model

import spock.lang.Specification

class HasherTest extends Specification {

  def 'Hasher salt function should return a new value every time it is run'() {
    when: 'Salt function is called 1000 times'
    def salts = (1..1000).collect { Hasher.newSalt() }

    then: 'No two salts are the same'
    salts.find { s -> salts.findAll { it == s }.size() > 1 } == null
  }

  def 'Hasher output should be identical for identical inputs'() {
    given: 'A test string and salt'
    def input = "some test string data",
        salt = Hasher.newSalt()

    when: 'Hash function is applied'
    def firstHash = Hasher.hashWithSalt(input, salt)

    then: 'Hashed value is the same after 100 more applications'
    //Do this with parallel streams because hashing is kinda slow
    (1..100).parallelStream().map({ Hasher.hashWithSalt(input, salt) }).forEach({ hash ->
      hash == firstHash
    })
  }
}

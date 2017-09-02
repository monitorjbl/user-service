package io.monitorjbl

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import io.monitorjbl.model.User
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.DELETE
import static groovyx.net.http.Method.PUT
import static io.monitorjbl.IntegrationEnvironment.rootUri

class LoginEndpointTest extends Specification {

  def http = new HTTPBuilder(rootUri)

  def setupSpec() {
    IntegrationEnvironment.start()
  }

  def cleanupSpec() {
    IntegrationEnvironment.stop()
  }

  def 'Login with correct user creds should work'() {
    given: 'An existing user'
    def user = new User(username: 'testuser', email: 'test@gmail.com', password: 'password')
    http.post(path: '/user', body: user, requestContentType: JSON) {}

    when: 'Login is attempted with correct credentials'
    def content
    http.post(path: '/login', body: new User(username: user.username, password: user.password), requestContentType: JSON) { resp, json ->
      content = json
    }

    then: 'A token should be returned'
    content.id != null
    content.expires != null

    and: 'The users last login date should be updated'
    http.get(path: "/user/${user.username}", requestContentType: JSON) { _, json ->
      json.lastLogin != null
    }


    cleanup: 'Remove created user'
    http.request("${rootUri}/user/${user.username}", DELETE, JSON) {}
  }

  def 'Login with incorrect user creds should fail'() {
    given: 'An existing user'
    def user = new User(username: 'testuser', email: 'test@gmail.com', password: 'password')
    http.post(path: '/user', body: user, requestContentType: JSON) {}

    when: 'Login is attempted with incorrect credentials'
    def content
    http.post(path: '/login', body: new User(username: user.username, password: 'notright'), requestContentType: JSON) { resp, json -> content = json }

    then: 'A 400 should be returned'
    def ex = thrown(HttpResponseException)
    ex.response.status == 400

    cleanup: 'Remove created user'
    http.request("${rootUri}/user/${user.username}", DELETE, JSON) {}
  }

  def 'User password updates work properly'() {
    given: 'An existing user'
    def user = new User(username: 'testuser', email: 'test@gmail.com', password: 'password')
    http.post(path: '/user', body: user, requestContentType: JSON) {}

    when: 'The password is updated'
    user.password = "correcthorsebatterystaple"
    http.request("${rootUri}/user/${user.username}", PUT, JSON) { body = user }

    then: 'Logins with the new password work'
    def content
    http.post(path: '/login', body: new User(username: user.username, password: user.password), requestContentType: JSON) { resp, json -> content = json }

    and: 'A token should be returned'
    content.id != null
    content.expires != null

    cleanup: 'Remove created user'
    http.request("${rootUri}/user/${user.username}", DELETE, JSON) {}
  }
}

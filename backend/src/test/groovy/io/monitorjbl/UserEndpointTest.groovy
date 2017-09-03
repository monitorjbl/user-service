package io.monitorjbl

import groovyx.net.http.HTTPBuilder
import io.monitorjbl.model.User
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.DELETE
import static groovyx.net.http.Method.PUT
import static io.monitorjbl.IntegrationEnvironment.rootUri

class UserEndpointTest extends Specification {

  def http = new HTTPBuilder(rootUri)

  def setupSpec() {
    IntegrationEnvironment.start()
  }

  def cleanupSpec() {
    IntegrationEnvironment.stop()
  }

  def 'User date fields should be ISO8061 formatted strings'() {
    when: 'A user is created'
    def user = new User(username: 'testuser', email: 'test@gmail.com', password: 'password')
    def content
    http.post(path: '/user', body: user, requestContentType: JSON) { resp, json ->
      content = json
    }

    then: 'The returned JSON should include properly formatted ISO8061 dates'
    content.created.getClass() == String
    content.created =~ /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}(-\d{2}:\d{2}|Z)/

    cleanup: 'Remove created user'
    http.request("${rootUri}/user/${user.username}", DELETE, JSON) {}
  }

  def 'User list should be pageable'() {
    when: 'Several users are created'
    def users = (1..10).collect { i -> new User(username: "testuser${i}", email: "test${i}@gmail.com", password: 'password') }
    users.each { user ->
      http.post(path: '/user', body: user, requestContentType: JSON) {}
    }

    then: 'List endpoint should return a paginated list'
    def page = null
    http.get(path: '/user', requestContentType: JSON) { _, json -> page = json }
    page.numberOfElements == users.size()
    page.totalPages == 1
    page.last
    page.content.size() == users.size()
    page.content.eachWithIndex { user, idx ->
      user.name == users[idx].username
      user.email == users[idx].email
    }

    and: 'List endpoint should support page size limits and starting indexes'
    http.get(path: '/user', requestContentType: JSON, query: [start: 5, length: 1]) { _, json -> page = json }
    page.numberOfElements == 1
    page.totalPages == users.size()
    !page.last
    page.content.size() == 1
    page.content[0].username == users[4].username

    cleanup: 'Remove created users'
    users.each { http.request("${rootUri}/user/${it.username}", DELETE, JSON) {} }
  }

  def 'User list should be sortable'() {
    when: 'Several users are created'
    def users = (1..10).collect { i -> new User(username: "testuser${i}", email: "test${10 - i}@gmail.com", password: 'password') }
    users.each { user ->
      http.post(path: '/user', body: user, requestContentType: JSON) {}
    }

    then: 'List endpoint should return a paginated list sorted by username ascending'
    def page = null
    http.get(path: '/user', requestContentType: JSON) { _, json -> page = json }
    page.numberOfElements == users.size()
    page.totalPages == 1
    page.content.eachWithIndex { user, idx ->
      user.name == users[idx].username
    }

    and: 'List endpoint should support sorting by username descending'
    http.get(path: '/user', requestContentType: JSON, query: [sortField: 'username', sortDirection: 'DESC']) { _, json -> page = json }
    page.numberOfElements == users.size()
    page.totalPages == 1
    page.content.eachWithIndex { user, idx ->
      user.name == users[users.size() - 1 - idx].username
    }

    cleanup: 'Remove created users'
    users.each { http.request("${rootUri}/user/${it.username}", DELETE, JSON) {} }
  }

  def 'Updating user fields should be allowed'() {
    given: 'A created user'
    def user = new User(username: 'testuser', email: 'test@gmail.com', password: 'password')
    http.post(path: '/user', body: user, requestContentType: JSON) {}

    when: 'Updating the email field is attempted'
    user.email = "newemail@who.dis"
    http.request("${rootUri}/user/${user.username}", PUT, JSON) { body = user }

    then: 'It should succeed'
    def updatedUser
    http.get(path: "/user/${user.username}", requestContentType: JSON) { _, json -> updatedUser = json }
    updatedUser.email == user.email

    cleanup: 'Remove created user'
    http.request("${rootUri}/user/${user.username}", DELETE, JSON) {}
  }
}
package io.monitorjbl

import groovyx.net.http.HTTPBuilder
import io.monitorjbl.model.User
import org.springframework.context.ConfigurableApplicationContext
import spock.lang.Specification

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.DELETE

class UserEndpointTest extends Specification {

  static ConfigurableApplicationContext app
  static int port = 8080
  static String rootUri

  def http = new HTTPBuilder(rootUri)

  def setupSpec() {
    // Get unused port
    def sock = new ServerSocket(0)
    port = sock.localPort
    rootUri = "http://localhost:${port}"
    sock.close()

    // Start app
    app = Main.start(
        "--server.port=${port}",
        "--encryption.key=asdfqwerasdfqwer")

    // Wait for app to start
    for (def i = 0; i < 15; i++) {
      try {
        new Socket("localhost", port).close()
        return
      } catch (e) {
        sleep(1000)
      }
    }
    throw new RuntimeException("Application did not start up in time")
  }

  def cleanupSpec() {
    app.close()
    for (def i = 0; i < 15; i++) {
      try {
        new Socket("localhost", port).close()
        sleep(1000)
      } catch (e) {
        return
      }
    }
    throw new RuntimeException("Application did not stop in time")
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
    content.created =~ /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d{3}-\d{2}:\d{2}/

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
}
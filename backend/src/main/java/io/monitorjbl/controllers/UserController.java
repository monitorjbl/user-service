package io.monitorjbl.controllers;

import io.monitorjbl.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/user", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class UserController {

  @RequestMapping(method = GET, consumes = ALL_VALUE)
  public List<User> list() {
    throw new UnsupportedOperationException();
  }

  @RequestMapping(method = POST)
  public User create(@RequestBody User user) {
    throw new UnsupportedOperationException();
  }

  @RequestMapping(method = GET, value = "/{username}", consumes = ALL_VALUE)
  public User get() {
    throw new UnsupportedOperationException();
  }

  @RequestMapping(method = PUT, value = "/{username}")
  public User update(@RequestBody User user) {
    throw new UnsupportedOperationException();
  }

  @RequestMapping(method = DELETE, value = "/{username}", consumes = ALL_VALUE)
  public void delete() {
    throw new UnsupportedOperationException();
  }
}

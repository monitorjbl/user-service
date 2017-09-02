package io.monitorjbl.controllers;

import io.monitorjbl.dao.UserDao;
import io.monitorjbl.exceptions.BadRequestException;
import io.monitorjbl.exceptions.NotFoundException;
import io.monitorjbl.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/user", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class UserController {

  private final UserDao userDao;

  @Autowired
  public UserController(UserDao userDao) {
    this.userDao = userDao;
  }

  @RequestMapping(method = GET, consumes = ALL_VALUE)
  public Page<User> list(@RequestParam(name = "start", defaultValue = "0") int start,
                         @RequestParam(name = "length", defaultValue = "50") int length,
                         @RequestParam(name = "sortField", defaultValue = "username") String sortField,
                         @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection) {

    //TODO: figure out how to stop sorting on encrypted fields
    return userDao.findAll(new PageRequest(start, length, new Sort(Sort.Direction.fromString(sortDirection), sortField)));
  }

  @RequestMapping(method = POST)
  public User create(@RequestBody User user) {
    if(user.getPassword() == null || user.getPassword().equals("")) {
      throw new BadRequestException("Password cannot be empty");
    }
    return userDao.save(user);
  }

  @RequestMapping(method = GET, value = "/{username}", consumes = ALL_VALUE)
  public User get(@PathVariable("username") String username) {
    User user = userDao.getUserByUsername(username);
    if(user == null) {
      throw new NotFoundException("User '" + username + "' not found");
    }
    return user;
  }

  @RequestMapping(method = PUT, value = "/{username}")
  public User update(@PathVariable("username") String username, @RequestBody User user) {
    throw new UnsupportedOperationException();
  }

  @RequestMapping(method = DELETE, value = "/{username}", consumes = ALL_VALUE)
  public void delete(@PathVariable("username") String username) {
    User user = get(username);
    userDao.delete(user);
  }
}

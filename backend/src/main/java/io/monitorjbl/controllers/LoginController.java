package io.monitorjbl.controllers;

import io.monitorjbl.dao.TokenDao;
import io.monitorjbl.dao.UserDao;
import io.monitorjbl.exceptions.BadRequestException;
import io.monitorjbl.model.Hasher;
import io.monitorjbl.model.Token;
import io.monitorjbl.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(value = "Login", description = "Authentication service for users")
@RestController
@RequestMapping(value = "/login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class LoginController {

  private final UserDao userDao;
  private final TokenDao tokenDao;

  @Autowired
  public LoginController(UserDao userDao, TokenDao tokenDao) {
    this.userDao = userDao;
    this.tokenDao = tokenDao;
  }

  @ApiOperation(value = "Create a token with user credentials")
  @RequestMapping(method = POST)
  public Token login(@RequestBody User user) {
    User record = userDao.getUserByUsername(user.getUsername());
    if(record == null || !Objects.equals(record.getPasswordHash(), Hasher.hashWithSalt(user.getPassword(), record.getPasswordSalt()))) {
      throw new BadRequestException("Bad credentials");
    }

    record.setLastLogin(ZonedDateTime.now());
    userDao.save(record);
    return tokenDao.save(new Token(UUID.randomUUID().toString(), ZonedDateTime.now().plusHours(1)));
  }
}

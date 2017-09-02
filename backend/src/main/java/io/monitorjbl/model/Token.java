package io.monitorjbl.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class Token {
  @Id
  @Autowired
  private String id;
  @Column(nullable = false)
  private ZonedDateTime expires;

  public Token() {
  }

  public Token(String id, ZonedDateTime expires) {
    this.id = id;
    this.expires = expires;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ZonedDateTime getExpires() {
    return expires;
  }

  public void setExpires(ZonedDateTime expires) {
    this.expires = expires;
  }
}

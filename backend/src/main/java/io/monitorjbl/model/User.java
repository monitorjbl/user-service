package io.monitorjbl.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZonedDateTime;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotEmpty
  private String username;
  @NotEmpty
  private String email;
  private ZonedDateTime created;
  private ZonedDateTime updated;
  private ZonedDateTime lastLogin;

  @PrePersist
  void onCreate() {
    this.created = ZonedDateTime.now();
  }

  @PreUpdate
  void onUpdate() {
    this.updated = ZonedDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ZonedDateTime getCreated() {
    return created;
  }

  public void setCreated(ZonedDateTime created) {
    this.created = created;
  }

  public ZonedDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(ZonedDateTime updated) {
    this.updated = updated;
  }

  public ZonedDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(ZonedDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }
}

package io.monitorjbl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import java.time.ZonedDateTime;

@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotEmpty
  @Column(nullable = false)
  private String username;
  @NotEmpty
  @Column(nullable = false)
  private String email;
  @JsonProperty(access = Access.WRITE_ONLY)
  @Transient
  private String password;

  @JsonIgnore
  @Column(name = "password", nullable = false)
  private String passwordHash;
  @JsonIgnore
  @Column(nullable = false)
  private String passwordSalt;
  @Column(nullable = false)
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
    return Crypter.decrypt(this.email);
  }

  public void setEmail(String email) {
    this.email = Crypter.encrypt(email);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
    this.passwordSalt = Hasher.newSalt();
    this.passwordHash = Hasher.hashWithSalt(password, this.passwordSalt);
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getPasswordSalt() {
    return passwordSalt;
  }

  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
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

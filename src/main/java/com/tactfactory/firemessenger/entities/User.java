package com.tactfactory.firemessenger.entities;

import java.util.UUID;

public class User {

  private final String guid;
  private String login;

  public User() {
    this.guid = UUID.randomUUID().toString();
  }

  public User(final String guid, final String login) {
    this.guid = guid;
    this.login = login;
  }

  public String getGuid() {
    return guid;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}

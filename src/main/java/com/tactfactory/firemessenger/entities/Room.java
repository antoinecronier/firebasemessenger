package com.tactfactory.firemessenger.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Room {

  private final String id;
  private String name;
  private final List<User> users = new ArrayList<>();

  public Room(String name) {
    super();
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public List<User> getUsers() {
    return users;
  }
}

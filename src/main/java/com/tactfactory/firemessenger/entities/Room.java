package com.tactfactory.firemessenger.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Room {

  private final String guid;
  private String name;
  private final List<User> users;
  private final Map<String, String> messages;

  public Room() {
    this.guid = UUID.randomUUID().toString();
    this.users = new ArrayList<>();
    this.messages = new HashMap<>();
  }

  public Room(String name) {
    super();
    this.guid = UUID.randomUUID().toString();
    this.name = name;
    this.users = new ArrayList<>();
    this.messages = new HashMap<>();
  }

  public Room(String guid, String name, Map<String, String> messages, List<User> users) {
    this.guid = guid;
    this.name = name;
    this.messages = messages;
    this.users = users;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGuid() {
    return this.guid;
  }

  public List<User> getUsers() {
    return users;
  }

  public Map<String, String> getMessages() {
    return messages;
  }
}

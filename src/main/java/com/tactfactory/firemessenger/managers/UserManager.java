package com.tactfactory.firemessenger.managers;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.tactfactory.firemessenger.database.FirebaseManager;
import com.tactfactory.firemessenger.entities.Command;
import com.tactfactory.firemessenger.entities.Room;
import com.tactfactory.firemessenger.entities.User;
import com.tactfactory.firemessenger.utils.ScannerUtil;

public class UserManager {

  private final User user;
  private ChatManager chat = null;

  public UserManager() {
    this.user = new User();
  }

  public void run() {
    String quit = "";
    do {
      final String answer = ScannerUtil.getInstance().inputString();

      if (Command.Inputs().stream().anyMatch(x -> answer.startsWith(x))) {
        if (answer.startsWith(Command.LOGOUT.getCommand())) {
          if (Command.LOGOUT.removeCommandString(answer).isEmpty()) {
            this.logout(this.user);
          } else {
            System.err
                .println(String.format("%s commande ne doit pas contenir d'argument", Command.LOGOUT.getCommand()));
          }
        } else if (answer.startsWith(Command.LOG_ROOM.getCommand())) {
          String room = Command.LOG_ROOM.removeCommandString(answer);
          if (!room.isEmpty()) {
            this.connectToRoom(this.user, room);
          } else {
            System.err
                .println(String.format("%s commande doit contenir le guid de la room", Command.LOG_ROOM.getCommand()));
          }
        } else if (answer.startsWith(Command.LOG.getCommand())) {
          this.log(this.user, Command.LOG.removeCommandString(answer));
        } else if (answer.startsWith(Command.SHOW_ROOMS.getCommand())) {
          String room = Command.SHOW_ROOMS.removeCommandString(answer);
          if (room.isEmpty()) {
            this.showRooms();
          } else {
            System.err
                .println(String.format("%s commande ne doit pas contenir d'argument", Command.LOG_ROOM.getCommand()));
          }
        } else if (answer.startsWith(Command.SHOW_USERS.getCommand())) {
          if (Command.SHOW_USERS.removeCommandString(answer).isEmpty()) {
            this.showUsers();
          } else {
            System.err
                .println(String.format("%s commande ne doit pas contenir d'argument", Command.SHOW_USERS.getCommand()));
          }
        } else if (answer.startsWith(Command.CREATE_ROOM.getCommand())) {
          String room = Command.CREATE_ROOM.removeCommandString(answer);
          if (!room.isEmpty()) {
            this.createRoom(room);
          } else {
            System.err
                .println(String.format("%s commande doit contenir nom du salon", Command.CREATE_ROOM.getCommand()));
          }
        } else if (answer.startsWith(Command.INFO.getCommand())) {
          System.out.println(Command.info());
        } else if (answer.startsWith(Command.QUIT.getCommand())) {
          quit = answer;
          this.logout(this.user);
        }
      } else {
        if (this.chat != null) {
          this.chat.write(answer);
        }
      }

    } while (!quit.equals(Command.QUIT.getCommand()));
  }

  private void showRooms() {
    try {
      for (Room room : FirebaseManager.getInstance().getRooms()) {
        System.out.println(room.getGuid() + " : " + room.getName());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void createRoom(String roomName) {
    try {
      final Room room = new Room(roomName);
      room.getUsers().add(this.user);
      this.chat = new ChatManager(FirebaseManager.getInstance().createRoom(room));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showUsers() {
    try {
      for (User user : FirebaseManager.getInstance().getConnectedUser()) {
        System.out.println(user.getLogin());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void connectToRoom(User user, String room) {
    try {
      if (this.chat != null) {
        this.chat.remove(this.user);
      }
      this.chat = new ChatManager(FirebaseManager.getInstance().connectToRoom(user, room));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void logout(User user) {
    try {
      if (this.chat != null) {
        this.chat.remove(this.user);
      }
      FirebaseManager.getInstance().logout(user).get();
    } catch (IOException | InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

  private void log(User user, String newLogin) {
    if (user.getLogin() == null) {
      user.setLogin(newLogin);
      try {
        FirebaseManager.getInstance().log(user);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try {
        user.setLogin(newLogin);
        FirebaseManager.getInstance().log(user);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void quit() {
    this.logout(this.user);
  }
}

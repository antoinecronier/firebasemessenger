package com.tactfactory.firemessenger.entities;

import java.util.ArrayList;
import java.util.List;

public enum Command {
    LOG(1, Command.COMMAND_PREFIX + "log", "Permet de choisir ou change le nom de l'utilisateur courant"),
    LOGOUT(2, Command.COMMAND_PREFIX + "logout", "Permet de quitter un salon"),
    QUIT(3, Command.COMMAND_PREFIX + "quit", "Permet de quitter l'application"),
    SHOW_USERS(4, Command.COMMAND_PREFIX + "show_users", "Permet d'afficher tout les utilisateurs connecté"),
    CREATE_ROOM(5, Command.COMMAND_PREFIX + "create_room", "Permet de créer un salon"),
    SHOW_ROOMS(6, Command.COMMAND_PREFIX + "show_rooms", "Permet d'afficher tout les salons"),
    LOG_ROOM(7, Command.COMMAND_PREFIX + "log_room", "Permet de se connecter à un salon"),
    INFO(8, Command.COMMAND_PREFIX + "?", "Affiche les commandes");

    public static final String COMMAND_PREFIX = "/";

    private final Integer id;
    private final String command;
    private final String description;

    Command(final Integer id, final String command, final String description) {
      this.id = id;
      this.command = command;
      this.description = description;
    }

    public static String info() {
      final StringBuilder builder = new StringBuilder();

      int i = 0;
      for (; i < values().length - 1; i++) {
        builder.append(values()[i].getCommand() + " " + values()[i].getDescription() + "\n");
      }
      builder.append(values()[i].getCommand() + " " + values()[i].getDescription());

      return builder.toString();
    }

    public static List<String> Inputs(){
      final List<String> result = new ArrayList<>();

      for (Command command : values()) {
        result.add(command.getCommand());
      }

      return result;
    }

    public Integer getId() {
      return this.id;
    }

    public String getCommand() {
      return this.command;
    }

    public String getDescription() {
      return this.description;
    }

    public String removeCommandString(String input) {
      return input.replace(this.getCommand(), "").trim();
    }
}

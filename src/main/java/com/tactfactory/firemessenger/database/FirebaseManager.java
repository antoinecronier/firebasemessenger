package com.tactfactory.firemessenger.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import com.google.api.core.SettableApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tactfactory.firemessenger.entities.Room;
import com.tactfactory.firemessenger.entities.User;
import com.tactfactory.firemessenger.utils.JsonUtil;

public class FirebaseManager {

  private static final String CURRENT_USERS = "currentUser";
  private static final String CURRENT_ROOM = "currentSalon";

  private static FirebaseManager INSTANCE = null;

  /**
   * Point d'accès pour l'instance unique du singleton
   *
   * @throws IOException
   */
  public static synchronized FirebaseManager getInstance() throws IOException {
    if (INSTANCE == null) {
      INSTANCE = new FirebaseManager();
    }
    return INSTANCE;
  }

  private FirebaseManager() throws IOException {
    FileInputStream serviceAccount = new FileInputStream(
        "/home/antoine.cronier/eclipse-workspace/firebasekeys/firemessenger-be1f9-firebase-adminsdk-7iy5r-d1f10672bd.json");

    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://firemessenger-be1f9.firebaseio.com").build();

    FirebaseApp.initializeApp(options);
  }

  public void log(final User user) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_USERS).child(user.getGuid()).setValueAsync(user);
  }

  public void logout(final User user) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_USERS).child(user.getGuid()).removeValueAsync();
  }

  public DatabaseReference connectToRoom(final User user, final String roomId) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference result = database.getReference(CURRENT_ROOM).child(roomId);

    result.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          Room room = dataSnapshot.getValue(Room.class);
          room.getUsers().add(user);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

    return result;
  }

  public List<User> getConnectedUser() {
    final SettableApiFuture<List<User>> future = SettableApiFuture.create();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        final List<User> result = new ArrayList<>();
        if (dataSnapshot.exists()) {
          for (DataSnapshot subSnap : dataSnapshot.getChildren()) {
            try {
              HashMap<String, String> datas = (HashMap<String, String>) subSnap.getValue();
              result.add(new User(datas.get("guid"), datas.get("login")));
            } catch (Exception e) {
              subSnap.getRef().removeValueAsync();
            }
          }
        }
        future.set(result);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        future.set(null);
      }
    });

    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  public DatabaseReference createRoom(Room room) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_ROOM).child(room.getGuid()).setValueAsync(room);

    return database.getReference(CURRENT_ROOM + "/" + room.getGuid());
  }

  public List<Room> getRooms() {
    final SettableApiFuture<List<Room>> future = SettableApiFuture.create();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_ROOM).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        final List<Room> result = new ArrayList<>();
        if (dataSnapshot.exists()) {
          try {
            for (DataSnapshot subSnap : dataSnapshot.getChildren()) {
              result.add(subSnap.getValue(Room.class));
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        future.set(result);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        future.set(null);
      }
    });

    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
}

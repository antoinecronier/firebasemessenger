package com.tactfactory.firemessenger.database;

import java.io.FileInputStream;
import java.io.IOException;

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

public class FirebaseManager {

  private static final String CURRENT_USER = "currentUser";
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
    database.getReference(CURRENT_USER).child(user.getGuid()).setValueAsync(user);
  }

  public void logout(final User user) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    database.getReference(CURRENT_USER).child(user.getGuid()).removeValueAsync();
  }

  public DatabaseReference connectToRoom(final User user, final String roomId) {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference result = database.getReference(CURRENT_ROOM).child(roomId);

    result.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          Room room = (Room) dataSnapshot.getValue();
          room.getUsers().add(user);
        }
//        else {
//          Room room = new Room("");
//          room.getUsers().add(user);
//          result.setValueAsync(room);
//        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

    return result;
  }
}

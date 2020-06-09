package com.tactfactory.firemessenger.database;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseManager {

  public FirebaseManager() throws IOException {
    FileInputStream serviceAccount = new FileInputStream(
        "/home/antoine.cronier/eclipse-workspace/firebasekeys/firemessenger-be1f9-firebase-adminsdk-7iy5r-d1f10672bd.json");

    FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://firemessenger-be1f9.firebaseio.com").build();

    FirebaseApp.initializeApp(options);
  }
}

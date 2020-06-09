package com.tactfactory.firemessenger.managers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tactfactory.firemessenger.entities.Room;
import com.tactfactory.firemessenger.entities.User;

public class ChatManager {

  private final DatabaseReference roomDbRef;
  private final DatabaseReference roomUsersDbRef;
  private DatabaseReference roomMessageDbRef;

  private final ChildEventListener roomUserListener = new ChildEventListener() {

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
      if (snapshot.exists()) {
        if (!snapshot.hasChildren()) {
          snapshot.getRef().getParent().removeValueAsync();
        }
      }
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onCancelled(DatabaseError error) {
    }
  };

  private final ChildEventListener roomMessageListener = new ChildEventListener() {

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
      System.out.println(snapshot.getValue());
    }

    @Override
    public void onCancelled(DatabaseError error) {
    }
  };

  public ChatManager(DatabaseReference roomDbRef) {
    this.roomDbRef = roomDbRef;
    this.roomUsersDbRef = roomDbRef.child("users");
    this.roomUsersDbRef.addChildEventListener(this.roomUserListener);
    this.roomMessageDbRef = roomDbRef.child("messages");
    this.roomMessageDbRef.addChildEventListener(this.roomMessageListener);
  }

  public void write(String answer) {
    this.roomMessageDbRef.push().setValueAsync(answer);
  }

  public void remove(User user) {
    this.roomDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
          Room room = dataSnapshot.getValue(Room.class);
          room.getUsers().remove(user);
          dataSnapshot.getRef().setValueAsync(room);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }
}

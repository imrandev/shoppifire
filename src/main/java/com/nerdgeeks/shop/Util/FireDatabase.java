package com.nerdgeeks.shop.Util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FireDatabase{

    private InputStream findFile() {

        String fileName = "shop-9c056-firebase-adminsdk-z5fb7-3c23768f33.json";

        // this is the path within the jar file
        InputStream input = getClass().getResourceAsStream("/resources/" + fileName);
        if (input == null) {
            // this is how we load file within editor (eg eclipse)
            input = getClass().getClassLoader().getResourceAsStream(fileName);
        }
        return input;
    }

    public boolean ConnectFirebase() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(findFile()))
                    .setDatabaseUrl("https://shop-9c056.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FireDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(FireDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static void getDataValueEvent(DatabaseReference firebaseDatabase, OnGetDataListener dataListener){

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void getDataForSingleValueEvent(DatabaseReference firebaseDatabase, OnGetDataListener dataListener){

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataListener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dataListener.onFailure(databaseError);
            }
        });
    }
}


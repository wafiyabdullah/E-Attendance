package com.example.e_attendance;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private DatabaseReference databaseReference;
    private static FirebaseHelper instance;

    private FirebaseHelper() {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void saveAttendance(Attendance attendance) {
        // Assuming "Attendance" is the node where you want to save data
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Save under the user's ID
            databaseReference.child("Attendance").child(userId).setValue(attendance);
        }
    }
}

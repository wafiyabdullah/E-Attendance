package com.example.e_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView profileText;
    TextView profileName;
    TextView profileDepartment;
    TextView profileRole;
    FirebaseDatabase database;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.profile){
                    return true;
                }
                if(id == R.id.qrscanner){
                    Intent intent = new Intent(ProfileActivity.this, QRScannerActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
        //database
        database = FirebaseDatabase.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("User");

        profileName = (TextView) findViewById(R.id.name);
        profileDepartment = (TextView) findViewById(R.id.department);
        profileRole = (TextView) findViewById(R.id.role);

        //profile
        auth = FirebaseAuth.getInstance();
        profileText = (TextView) findViewById(R.id.textView);
        user = auth.getCurrentUser();

        if(user != null && user.getEmail() != null){
            profileText.setText(user.getEmail());
            retrieveUserData();

        }else {
            redirectToLogin();
        }

    }

    public void signout(View v){
        auth.signOut();
        finish();
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"logout", Toast.LENGTH_SHORT).show();
    }

    private void redirectToLogin(){
        auth.signOut();
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"Not Registered as Company Employee", Toast.LENGTH_SHORT).show();
    }

    private void retrieveUserData(){
        // Check if the user is not null and has an email
        if (user != null && user.getEmail() != null){
            String userEmail = user.getEmail();

            // Query the database to get user data based on email
            usersRef.orderByChild("Email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        // Iterate through the results, although there should be only one match
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            User userProfile = snapshot.getValue(User.class);

                            if (userProfile != null){
                                // Update UI with user data
                                profileName.setText(userProfile.getName());
                                profileDepartment.setText(userProfile.getDepartment());
                                profileRole.setText(userProfile.getRole());
                            }
                        }
                    }else{
                        // Handle the case when no user data is found
                        redirectToLogin();
                        Log.e("ProfileActivity", "No user data found for email: " + userEmail);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    Log.e("ProfileActivity", "Error retrieving user data: " + databaseError.getMessage());
                }
            });
        }
    }
}
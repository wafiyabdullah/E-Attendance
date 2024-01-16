package com.example.e_attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    public void loginUser(View v){
        if (email.getText().toString().equals("") && pass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Fill In Your Credential",Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"login Successfull", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(i);
                            }else {
                                Toast.makeText(getApplicationContext(), "failed login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
package com.example.e_attendance;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class QRScannerActivity extends AppCompatActivity {

    Button scan_btn;
    TextView textView;
    String scannedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        //navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.qrscanner);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.profile){
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if(id == R.id.qrscanner){
                    return true;
                }
                return false;
            }
        });

        //qr
        scan_btn = (Button) findViewById(R.id.scanner);
        textView = (TextView) findViewById(R.id.text);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(QRScannerActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan");
                integrator.setBeepEnabled(false);
                integrator.setCameraId(0);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode,Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scannedData = result.getContents();

            if (!TextUtils.isEmpty(scannedData)) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userName = currentUser.getDisplayName();
                    String scannedDate = extractDateFromQR(scannedData);

                    if (!TextUtils.isEmpty(scannedDate)) {
                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        Attendance attendance = new Attendance();
                        attendance.setDate(scannedDate);
                        attendance.setName(userName);
                        attendance.setTime(currentTime);

                        FirebaseHelper.getInstance().saveAttendance(attendance);
                        Toast.makeText(this, "Successfully Check In", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Scanning canceled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private String extractDateFromQR(String qrData) {
        // Implement logic to extract date from QR code data
        // For example, assuming the date is encoded as yyyMMdd
        if (qrData.length() >= 8) {
            return qrData.substring(0, 8);
        } else {
            return null;
        }

    }

}

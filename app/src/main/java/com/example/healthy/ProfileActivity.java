package com.example.healthy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthy.LoginAndSignUp.Login;
import com.example.healthy.LoginAndSignUp.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    //private ImageButton logout;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    public double bmi;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.eat:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sport:
                        startActivity(new Intent(getApplicationContext(), SportActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.water:
                        startActivity(new Intent(getApplicationContext(), WaterActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });


//        logout = findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileActivity.this, Login.class));
//            }
//        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://usersbase-929a4-default-rtdb.firebaseio.com/").getReference("Users");
        userID = user.getUid();

        TextView nameText = findViewById(R.id.name);
        TextView weightText = findViewById(R.id.weight);
        TextView heightText = findViewById(R.id.height);
        TextView bmiText = findViewById(R.id.bmi);
        TextView emailText = findViewById(R.id.emailtxt);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if (userProfile != null){
                    String name = userProfile.name;
                    String weight = userProfile.weight;
                    String height = userProfile.height;
                    String email = userProfile.email;


                    nameText.setText(name);
                    emailText.setText(email);
                    weightText.setText(weight + " kg");
                    heightText.setText(height + " cm");
                    bmi = calculateBmi(Integer.parseInt(height), Integer.parseInt(weight));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Something wrong happend", Toast.LENGTH_LONG).show();
            }
        });
        bmiText.setText(getCategory(bmi));

    }

    private String getCategory(double bmi) {
        if (bmi < 18.5) {
            return getString(R.string.bmi_cat_3);
        }
        if (bmi < 35) {
            return getString(R.string.bmi_cat_4);
        }
        return getString(R.string.bmi_cat_5);
    }

    public static double calculateBmi(double height, double weight) {
        return Math.round(weight / Math.pow(height, 2) * 10d) / 10d;
    }
}

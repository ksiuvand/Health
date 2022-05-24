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

    public Button edit_btn;
    public ImageButton logout;

    private String userID;
    public TextView bmiText;

    public String nameEdit, emailEdit, heightEdit, weightEdit;

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


        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, Login.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://usersbase-929a4-default-rtdb.firebaseio.com/").getReference("Users");
        userID = user.getUid();

        TextView nameText = findViewById(R.id.name);
        TextView weightText = findViewById(R.id.weight);
        TextView heightText = findViewById(R.id.height);
        bmiText = findViewById(R.id.bmi);
        TextView emailText = findViewById(R.id.emailtxt);
        edit_btn = findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditProfile.class);
                i.putExtra("fullName", nameEdit);
                i.putExtra("email", emailEdit);
                i.putExtra("weight", weightEdit);
                i.putExtra("height", heightEdit);
                Log.d("ljikhujgyhft666", weightEdit+" "+heightEdit);
                startActivity(i);
            }
        });

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userProfile = dataSnapshot.getValue(User.class);

                if (userProfile != null){
                    String name = userProfile.name;
                    nameEdit = userProfile.name;
                    String weight = userProfile.weight;
                    weightEdit = userProfile.weight;
                    String height = userProfile.height;
                    heightEdit = userProfile.height;
                    String email = userProfile.email;
                    emailEdit = userProfile.email;
                    Log.d("ljikhujgyhft444", weight+" "+height);
                    Log.d("ljikhujgyhft555", weightEdit+" "+heightEdit);


                    nameText.setText(name);
                    emailText.setText(email);
                    weightText.setText(weight + " kg");
                    heightText.setText(height + " cm");
                    calculate(Integer.parseInt(weight), Integer.parseInt(height));
                    Log.d("sdfvg", " " + bmi);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Something wrong happend", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void calculate(int w, int h){
        double a = (h * h * 0.01 * 0.01 );

        double bmi = w /a;
        Log.d("ythg", " "+ bmi);

        if(bmi >= 18.5 && bmi <= 25.0)
        {
            bmiText.setText(R.string.normal);
        }else if( bmi <=18.5){
            bmiText.setText(R.string.underweight);
        }
        else if( bmi >= 25.0 && bmi <= 30.0){
            bmiText.setText(R.string.overweight);
        }
        else if( bmi >30.0){
            bmiText.setText(R.string.obese);
        }
    }
}

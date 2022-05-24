package com.example.healthy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthy.LoginAndSignUp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    public EditText profile_name, profile_email, profile_height, profile_weight;
    public ImageView profile_image;
    public Button save_btn;
    public ImageButton backtoprofile;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String weight = data.getStringExtra("weight");
        String height = data.getStringExtra("height");
        Log.d("ljikhujgyhft111", weight+" "+height);
        auth = FirebaseAuth.getInstance();

        currentUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://usersbase-929a4-default-rtdb.firebaseio.com/");


        profile_name = findViewById(R.id.nameedit);
        profile_email = findViewById(R.id.emailedit);
        profile_height = findViewById(R.id.heightedit);
        profile_weight = findViewById(R.id.weightedit);
        profile_image = findViewById(R.id.imageedit);

        save_btn = findViewById(R.id.save_btn);
        backtoprofile = findViewById(R.id.backtoprofil);

        backtoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                String name = profile_name.getText().toString().trim();
                String weight = profile_weight.getText().toString().trim();
                String height = profile_height.getText().toString().trim();
                String email = profile_email.getText().toString().trim();
                String age = "13";

                if(name.isEmpty()){
                    profile_name.setError("Name is required");
                    profile_name.requestFocus();
                    return;
                }
                if(weight.isEmpty()){
                    profile_weight.setError("Weight is required");
                    profile_weight.requestFocus();
                    return;
                }
                if(height.isEmpty()){
                    profile_height.setError("Height is required");
                    profile_height.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    profile_email.setError("Email is required");
                    profile_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    profile_email.setError("Please provide your valid Email");
                    profile_email.requestFocus();
                    return;
                }
                Log.d("ljikhujgyhft222", weight+" "+height);

                User user=new User(name, age, email, weight, height);

                databaseReference.setValue(user);
                Log.d("ljikhujgyhft333", weight+" "+height);

                Toast.makeText(EditProfile.this,"Profile Updated Successfully!!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();

            }
        });

        profile_name.setText(fullName);
        profile_email.setText(email);
        profile_height.setText(height);
        profile_weight.setText(weight);


    }

}
package com.example.healthy.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthy.MainActivity;
import com.example.healthy.ProfileFragment;
import com.example.healthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    String user_email, user_password, con_password, user_name, user_age, user_weight, user_height;
    EditText signEmail, signPass, signCpass, signName, signAge, signWeight, signHeight;
    Button signBtn;

    private FirebaseAuth mAuth;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signBtn = view.findViewById(R.id.signup_btn);
        signCpass = view.findViewById(R.id.signup_confirm_password);
        signEmail = view.findViewById(R.id.signup_email);
        signPass = view.findViewById(R.id.signup_password);
        signAge = view.findViewById(R.id.signup_age);
        signName = view.findViewById(R.id.signup_name);
        signHeight = view.findViewById(R.id.signup_height);
        signWeight = view.findViewById(R.id.signup_weight);

        mAuth=FirebaseAuth.getInstance();

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        return view;
    }

    public void register(){
        user_email = signEmail.getText().toString().trim();
        user_password = signPass.getText().toString().trim();
        con_password = signCpass.getText().toString().trim();
        user_name = signName.getText().toString().trim();
        user_age = signAge.getText().toString().trim();
        user_weight = signWeight.getText().toString().trim();
        user_height = signHeight.getText().toString().trim();

        if(user_email.isEmpty()){
            signEmail.setError(getResources().getString(R.string.requiredEmail));
            signEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            signEmail.setError(getResources().getString(R.string.validEmail));
            signEmail.requestFocus();
            return;
        }

        if(user_name.isEmpty()){
            signName.setError(getResources().getString(R.string.requiredName));
            signName.requestFocus();
            return;
        }

        if(user_age.isEmpty()){
            signAge.setError(getResources().getString(R.string.requiredAge));
            signAge.requestFocus();
            return;
        }

        if(user_height.isEmpty()){
            signHeight.setError(getResources().getString(R.string.requiredHeight));
            signHeight.requestFocus();
            return;
        }
        if(user_weight.isEmpty()){
            signWeight.setError(getResources().getString(R.string.requiredWeight));
            signWeight.requestFocus();
            return;
        }

        if(user_password.isEmpty()){
            signPass.setError(getResources().getString(R.string.requiredPassword));
            signPass.requestFocus();
            return;
        }
        if(user_password.length()<6){
            signPass.setError(getResources().getString(R.string.Passwordcharacters));
            signPass.requestFocus();
            return;
        }
        if(con_password.isEmpty()) {
            signCpass.setError(getResources().getString(R.string.requiredPassword));
            signCpass.requestFocus();
            return;
        }
        if (!user_password.equals(con_password)){
            signCpass.setError(getResources().getString(R.string.confirm_password));
            signCpass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(user_email,user_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(user_name, user_age, user_email, user_weight, user_height);
                            FirebaseDatabase.getInstance("https://usersbase-929a4-default-rtdb.firebaseio.com/").getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "Succses", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    }else{
                                        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else{ }
                    }
                });
    }

}

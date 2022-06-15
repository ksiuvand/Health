package com.example.healthy.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthy.MainActivity;
import com.example.healthy.ProfileFragment;
import com.example.healthy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText L_email, L_pass;
    Button L_btn;
    TextView forgot_pass;
    float v =0;

    private FirebaseAuth mAuth;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        L_email = view.findViewById(R.id.login_email);
        L_pass = view.findViewById(R.id.login_password);
        L_btn = view.findViewById(R.id.login_btn);
        forgot_pass = view.findViewById(R.id.forgot);

        mAuth = FirebaseAuth.getInstance();

        L_email.setTranslationY(800);
        L_pass.setTranslationY(800);
        L_btn.setTranslationY(800);
        forgot_pass.setTranslationY(800);

        L_email.setAlpha(v);
        L_pass.setAlpha(v);
        L_btn.setAlpha(v);
        forgot_pass.setAlpha(v);

        L_email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        L_pass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        L_btn.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgot_pass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        //login
        L_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        return view;
    }

    private void userLogin() {
        String email = L_email.getText().toString().trim();
        String password = L_pass.getText().toString().trim();

        if (email.isEmpty()) {
            L_email.setError("Email is required");
            L_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            L_email.setError("Please provide your valid Email");
            L_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            L_pass.setError("Password is required");
            L_pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            L_pass.setError("Min Password length should be of 6 characters! ");
            L_pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getActivity(), MainActivity.class));

                } else {
                    Toast.makeText(getActivity(), "Failed to registr user. Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
package com.example.healthy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthy.LoginAndSignUp.Login;
import com.example.healthy.LoginAndSignUp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference databaseReference;
    public double bmi;

    public ImageButton logout, edit_btn;

    private String userID;
    public TextView bmiText;

    public String nameEdit, emailEdit, heightEdit, weightEdit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://usersbase-929a4-default-rtdb.firebaseio.com/").getReference("Users");
        userID = user.getUid();

        TextView nameText = view.findViewById(R.id.name);
        TextView weightText = view.findViewById(R.id.weight);
        TextView heightText = view.findViewById(R.id.height);
        bmiText = view.findViewById(R.id.bmi);
        TextView emailText = view.findViewById(R.id.emailtxt);
        edit_btn = view.findViewById(R.id.edit_btn);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), EditProfile.class);
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


                    nameText.setText(name);
                    emailText.setText(email);
                    weightText.setText(weight + " "+view.getResources().getString(R.string.kgg));
                    heightText.setText(height + " "+view.getResources().getString(R.string.cmm));
                    calculate(Integer.parseInt(weight), Integer.parseInt(height));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("dfvbgt", ""+databaseError.getCode());
                Toast.makeText(getActivity(), "Something wrong happend", Toast.LENGTH_LONG).show();
            }
        });
        return view;
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
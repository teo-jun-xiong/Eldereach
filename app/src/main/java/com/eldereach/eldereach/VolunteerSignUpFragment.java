package com.eldereach.eldereach;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eldereach.eldereach.volunteer.HomeVolunteerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VolunteerSignUpFragment extends Fragment {
    private EditText textEmail;
    private EditText textPassword;
    private EditText textName;
    private EditText textPhone;
    private EditText textExperience;
    private EditText textOrganizations;
    private CheckBox isRegistered;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer_sign_up, container, false);
        textEmail = view.findViewById(R.id.textEmailSignUp);
        textPassword = view.findViewById(R.id.textPasswordSignUp);
        textName = view.findViewById(R.id.textNameSignUp);
        textPhone = view.findViewById(R.id.textPhoneSignUp);
        textExperience = view.findViewById(R.id.textExperienceSignUp);
        textOrganizations = view.findViewById(R.id.textRegisteredOrganizationsSignUp);
        isRegistered = view.findViewById(R.id.checkboxIsRegisteredSignUp);
        Button buttonSignUp = view.findViewById(R.id.buttonSignUpSignUp);
        db = FirebaseFirestore.getInstance();

        textOrganizations.setVisibility(View.INVISIBLE);

        isRegistered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    textOrganizations.setVisibility(View.VISIBLE);
                } else {
                    textOrganizations.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                String emailID = textEmail.getText().toString();
                String pass = textPassword.getText().toString();
                String name = textName.getText().toString();

                if (emailID.isEmpty()) {
                    textEmail.setError("Please indicate your email.");
                    textEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    textPassword.setError("Please set a password for your Eldereach account.");
                    textPassword.requestFocus();
                } else if (name.isEmpty()) {
                    textName.setError("Please indicate your name.");
                    textName.requestFocus();
                } else if(isRegistered.isChecked() && textOrganizations.getText().toString().isEmpty()) {
                    textOrganizations.setError("Please indicate organizations that you are registered with.");
                    textOrganizations.requestFocus();
                } else {
                    createAccount();
                }
            }
        });

        return view;
    }

    private void createAccount() {
        final String emailID = textEmail.getText().toString();
        String pass = textPassword.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailID, pass).addOnCompleteListener(getActivity(), new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(),
                            "Sign up unsuccessful: " + Objects.requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> user_Info = new HashMap<>();
                    user_Info.put("email", emailID.toLowerCase());
                    user_Info.put("name", textName.getText().toString());
                    user_Info.put("isVolunteer", true);
                    user_Info.put("isRegistered", isRegistered.isChecked());
                    user_Info.put("address", "");
                    user_Info.put("phone", textPhone.getText().toString());
                    user_Info.put("registeredOrganization", textOrganizations.getText().toString());
                    user_Info.put("volunteerExperience", textExperience.getText().toString());

                    db.collection("users").document(emailID).set(user_Info);
                    startActivity(new Intent(getActivity(), HomeVolunteerActivity.class));
                }
            }
        });
    }
}

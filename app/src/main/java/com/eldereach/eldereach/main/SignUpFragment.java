package com.eldereach.eldereach.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eldereach.eldereach.R;

public class SignUpFragment extends Fragment {
    private Button buttonClient;
    private Button buttonVolunteer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        buttonClient = view.findViewById(R.id.buttonClientSignUp);
        buttonVolunteer = view.findViewById(R.id.buttonVolunteerSignUp);

        buttonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity) getActivity()).loadClientSignUpFragment();
            }
        });

        buttonVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity) getActivity()).loadVolunteerSignUpFragment();
            }
        });

        return view;
    }
}

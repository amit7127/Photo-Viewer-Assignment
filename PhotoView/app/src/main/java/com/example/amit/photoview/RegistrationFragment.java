package com.example.amit.photoview;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.photoview.models.User;

import java.util.List;

import io.realm.Realm;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_registration, null);

        nameEditText = v.findViewById(R.id.name_for_registration);
        emailEditText = v.findViewById(R.id.email_for_registration);
        passwordEditText = v.findViewById(R.id.password_for_registration);
        confirmPasswordEditText = v.findViewById(R.id.confirm_password_for_registration);

        registerButton = v.findViewById(R.id.registration_button);
        registerButton.setOnClickListener(this);

        realm = PhotoView.getPhotoView().getRealmDB(getActivity());


        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.registration_button :
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                boolean filledCorrectly = verifyFields(name, email, password, confirmPassword);
                if (filledCorrectly){
                    realm.beginTransaction();
                    User user = realm.createObject(User.class, email);
                    user.setName(name);
                    //user.setEmailId(email);
                    user.setPassword(password);
                    realm.commitTransaction();
                    Toast.makeText(getActivity(),"User Registered Successfully",Toast.LENGTH_SHORT).show();

                    //go to login page
                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.form_layout, new LoginFragment(), "LOGIN");
                    fragmentTransaction.commit();

                    //View view = getActivity().findViewById(R.id.login_or_register);
                    Button loginOrRegisterButton = (Button) getActivity().findViewById(R.id.login_or_register);
                    loginOrRegisterButton.setText(getString(R.string.register_button));

                    TextView textView = (TextView) getActivity().findViewById(R.id.registration_message);
                    textView.setVisibility(View.VISIBLE);

                    List<User> users = realm.where(User.class).findAll();
                    Log.i("users : ",users.size() + "");

                }

        }

    }

    public boolean verifyFields(String name,String email, String password ,String confirmPassword ){

        boolean filledCorrectly = false;

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(getActivity(),"All fields are mandatory",Toast.LENGTH_SHORT).show();
        } else {
            if (!password.equals(confirmPassword)){
                Toast.makeText(getActivity(),"Password and ConfirmPassword fields should be same",Toast.LENGTH_SHORT).show();
            } else {
                User user = realm.where(User.class).equalTo("emailId", email).findFirst();
                if (user == null) {
                    filledCorrectly = true;
                } else {
                    Toast.makeText(getActivity(),"This user already registered",Toast.LENGTH_SHORT).show();
                }
            }
        }
        return filledCorrectly;
    }
}

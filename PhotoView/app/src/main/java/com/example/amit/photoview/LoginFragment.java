package com.example.amit.photoview;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amit.photoview.models.User;

import io.realm.Realm;

public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText  emailEditText, passwordEditText;
    Button loginButton;
    Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.fragment_login, null);
        emailEditText = v.findViewById(R.id.emailid_for_login);
        passwordEditText = v.findViewById(R.id.password_for_login);
        loginButton = v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        realm = PhotoView.getPhotoView().getRealmDB(getActivity());

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login_button :

                boolean isError = true;
                String errorMessage = "";
                User user = null;
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    user = realm.where(User.class).equalTo("emailId", email).equalTo("password", password).findFirst();
                    if (user != null){
                        isError = false;
                    } else {
                        errorMessage = "Invalid UserName/Password";
                    }
                } else {
                    errorMessage = "UserName or Password field cannot be empty";
                }

                if (isError){
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    //save login data to shared preference
                    SharedPreferences sharedPreferences = PhotoView.getPhotoView().getSharedPreference(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(getString(R.string.is_user_loggedin), true); // Storing boolean - true/false
                    editor.putString(getString(R.string.userName), user.getName()); // Storing string
                    editor.putString(getString(R.string.userMail), user.getEmailId()); // Storing integer
                    editor.commit();

                    Intent intent = new Intent(getActivity().getApplicationContext(), Gallery.class);
                    intent.putExtra("name", user.getName());
                    intent.putExtra("email", user.getEmailId());
                    startActivity(intent);
                }
                break;
        }
    }
}

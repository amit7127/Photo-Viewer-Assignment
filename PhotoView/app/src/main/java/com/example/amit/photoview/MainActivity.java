package com.example.amit.photoview;

import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean showLogin = true;
    Button loginOrRegisterButton;
    FragmentTransaction fragmentTransaction;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginOrRegisterButton = findViewById(R.id.login_or_register);
        loginOrRegisterButton.setOnClickListener(this);

        textView = findViewById(R.id.registration_message);
        textView.setVisibility(View.VISIBLE);
        fragmentTransaction = getFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.form_layout, loginFragment, "LOGIN");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.login_or_register:

                if (loginOrRegisterButton.getText().equals(getString(R.string.login_button))) {
                    showLogin = false;
                    loginOrRegisterButton.setText(getString(R.string.register_button));
                    textView.setVisibility(View.VISIBLE);
                } else {
                    showLogin = true;
                    loginOrRegisterButton.setText(getString(R.string.login_button));
                    textView.setVisibility(View.GONE);
                }
                showFragment();
                break;
        }

    }

    public void showFragment(){

        fragmentTransaction = getFragmentManager().beginTransaction();
        if (showLogin){
            fragmentTransaction.replace(R.id.form_layout, new RegistrationFragment(), "REGISTRATION");
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.form_layout, new LoginFragment(), "LOGIN");
            fragmentTransaction.commit();
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

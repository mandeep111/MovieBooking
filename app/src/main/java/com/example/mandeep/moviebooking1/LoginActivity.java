package com.example.mandeep.moviebooking1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button login;
    CheckBox remember_me;
    ViewFlipper viewFlipper;
    TextView register_here, forget;
    ProgressDialog progressDialog;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        preferences = getSharedPreferences("remember", Context.MODE_PRIVATE);

        if (preferences.getBoolean("remember_me", false)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        viewFlipper = findViewById(R.id.viewflipper);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        remember_me = findViewById(R.id.remember_me);
        forget= findViewById(R.id.forget);
        register_here = findViewById(R.id.register_here);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        login.setOnClickListener(this);
        register_here.setOnClickListener(this);
        forget.setOnClickListener(this);

        int images[] = {R.drawable.cinema, R.drawable.cinema1, R.drawable.cinema2};
        viewFlipper = findViewById(R.id.viewflipper);
        for (int image : images) {
            flipperImages(image);
        }
    }

    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }


    @Override
    public void onClick(View view) {
        if (view == login) {
            if (isFieldEmpty(email) && (isFieldEmpty(password))) {
                if (isEmailValid(email)) {
                    userLogin();
                }
            }
        }
        if (view == register_here) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

        if(view == forget) {
            startActivity(new Intent(LoginActivity.this, UpdatePassword.class));
            finish();
        }
    }

    public Boolean isFieldEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("Field Required");
            return false;
        }
    }

    public Boolean isEmailValid(EditText view) {
        String value = view.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return true;
        } else {
            view.setError("Invalid email");
            return false;
        }

    }

    public void userLogin() {
        final String emailValue = email.getText().toString().trim();
        final String passwordValue = password.getText().toString().trim();
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")) {
                        SharedPref.getmIntances(getApplicationContext()).userLogin(
                                obj.getInt("id"),
                                obj.getString("username"),
                                obj.getString("email"),
                                obj.getString("phone"),
                                obj.getString("gender")
                        );
                        Toast.makeText(LoginActivity.this, "User login successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        if (remember_me.isChecked()) {
                            preferences.edit().putBoolean("remember_me", true).apply();

                        }
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message")
                                ,Toast.LENGTH_LONG).show();

                    }

                    } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage()
                        ,Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", emailValue);
                params.put("password", passwordValue);
                return params;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}

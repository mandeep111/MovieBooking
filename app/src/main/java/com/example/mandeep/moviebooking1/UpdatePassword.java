package com.example.mandeep.moviebooking1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdatePassword extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    EditText password, email;
    Button button;

    //    RadioGroup gender;
    ProgressDialog progressDialog;
//    Calendar myCalendar = Calendar.getInstance();
//    String dateFormat = "dd.MM.yyyy";
//    DatePickerDialog.OnDateSetListener date;
//    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        password = findViewById(R.id.new_pass);
        email = findViewById(R.id.email_addr);
        button = findViewById(R.id.button_continue);
        progressDialog = new ProgressDialog(this);

        button.setOnClickListener(this);
    }

    public void onBackPressed() {
        startActivity(new Intent(UpdatePassword.this, LoginActivity.class));
        finish();
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

    @Override
    public void onClick(View view) {
        if (view == button) {
            if ((isFieldEmpty(password)) && (isFieldEmpty(email))) {
                if (isEmailValid(email)) {
                    updatePassword();

                }

            }

        }
    }
    public void updatePassword() {
        final String passwordValue = password.getText().toString().trim();
        final String emailValue = email.getText().toString().trim();
        progressDialog.setMessage("Updating, please wait..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_UPDATE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if(!jsonObject.getBoolean("error")){
                        startActivity(new Intent(UpdatePassword.this, LoginActivity.class));
                        Toast.makeText(context, "Please login with new credentials", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", passwordValue);
                params.put("email", emailValue);
                return params;


            }

        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}

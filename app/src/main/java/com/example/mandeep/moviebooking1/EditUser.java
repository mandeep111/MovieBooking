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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditUser extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    EditText editDate, username, password, phone, email, user_id;
    Button update, delete;

//    RadioGroup gender;
    ProgressDialog progressDialog;
//    Calendar myCalendar = Calendar.getInstance();
//    String dateFormat = "dd.MM.yyyy";
//    DatePickerDialog.OnDateSetListener date;
//    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        editDate = findViewById(R.id.editDate);
//        user_id=findViewById(R.id.user_id);
//        user_id.setText(SharedPref.getmIntances(this).getUSerID());
        username = findViewById(R.id.username);
        username.setText(SharedPref.getmIntances(this).getUserName());
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        phone.setText(SharedPref.getmIntances(this).getUserPhone());
        email = findViewById(R.id.email);
        email.setText(SharedPref.getmIntances(this).getUserEmail());
        update = findViewById(R.id.update);
        delete=findViewById(R.id.delete);
//        gender = findViewById(R.id.gender);
        progressDialog = new ProgressDialog(this);

        update.setOnClickListener(this);
        delete.setOnClickListener(this);

        // init - set date to current date
//        long currentDate = System.currentTimeMillis();
//        String dateString = sdf.format(currentDate);
//        editDate.setText(dateString);
//
//        // set calendar date and update editDate
//        date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateDate();
//            }
//        };
//        editDate.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(context, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });


    }

//    private void updateDate() {
//        editDate.setText(sdf.format(myCalendar.getTime()));
//    }

    public void onBackPressed() {
        startActivity(new Intent(EditUser.this, HomeActivity.class));
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
        if (view == update) {
            if (isFieldEmpty(username) && (isFieldEmpty(password)) && (isFieldEmpty(phone)) && (isFieldEmpty(email))) {
                if (isEmailValid(email)) {
                    updateUser();
                }

            }

        }
        if(view==delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditUser.this);
            builder.setMessage("You want to delete this account?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteUser();
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
    }

    public void deleteUser() {
        final String usernameValue = SharedPref.getmIntances(this).getUserName();
        progressDialog.setMessage("Deleting Account");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if(!jsonObject.getBoolean("error")){
                        startActivity(new Intent(EditUser.this, LoginActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
               params.put("username", usernameValue);
                return getParams();
            }
        };
        Singleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void updateUser() {
        final Integer idValue = SharedPref.getmIntances(this).getUSerID();
//        Toast.makeText(context, "id is" +idValue, Toast.LENGTH_SHORT).show();
        final String usernameValue = username.getText().toString().trim();
        final String passwordValue = password.getText().toString().trim();
        final String emailValue = email.getText().toString().trim();
        final String phoneValue = phone.getText().toString().trim();
//        RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
//        final String genderValue = checkedBtn.getText().toString();


        progressDialog.setMessage("Updating, please wait..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    if(!jsonObject.getBoolean("error")){
                        startActivity(new Intent(EditUser.this, HomeActivity.class));
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
                params.put("id", String.valueOf(user_id));
                params.put("username", usernameValue);
                params.put("password", passwordValue);
                params.put("email", emailValue);
                params.put("phone", phoneValue);
//                params.put("gender", genderValue);

                return params;


            }
        };

        Singleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}

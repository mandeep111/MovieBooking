package com.example.mandeep.moviebooking1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.zip.Inflater;

public class ProfileActivity extends AppCompatActivity {
    TextView username1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        if (!SharedPref.getmIntances(this).isLoggedIn()) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }

        username1 = findViewById(R.id.username1);

        username1.setText(SharedPref.getmIntances(this).getUserName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                getSharedPreferences("remember", Context.MODE_PRIVATE).edit()
                        .putBoolean("remember_me", false).apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return true;
    }
}

package com.recipeit.recipeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Inscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
    }

    public void avatar(View view){
        Intent avatar = new Intent(Inscription.this, choixAvatar.class);
        startActivity(avatar);
    }
}

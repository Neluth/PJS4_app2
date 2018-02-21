package com.recipeit.recipeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Connexion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }

    public void inscription(View view){
        Intent inscrPage = new Intent(Connexion.this, Inscription.class);
        startActivity(inscrPage);
    }
}

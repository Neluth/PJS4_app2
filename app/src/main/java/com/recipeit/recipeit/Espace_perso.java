package com.recipeit.recipeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.recipeit.recipeit.R;

public class Espace_perso extends AppCompatActivity {
    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_perso);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView txt = findViewById(R.id.titreEspaceP2);
        mail = user.getEmail().toString();
        String [] pseudo= mail.split("@", 2);
        txt.append(" " + pseudo[0]);
    }

    public void parametre(View view){
        Intent pPage = new Intent(Espace_perso.this, ParameterActivity.class);
        startActivity(pPage);
    }

    public void ajoutRecette(View view){
        Intent inte = new Intent(Espace_perso.this, AjoutRecette.class);
        startActivity(inte);
    }

    public void modifFridge(View view){
        Intent inte = new Intent(Espace_perso.this, Accueil_Connect.class);
        inte.putExtra("forFridge", true);
        startActivity(inte);
    }
}

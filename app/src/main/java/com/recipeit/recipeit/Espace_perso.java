package com.recipeit.recipeit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Espace_perso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_perso);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView txt = findViewById(R.id.titreEspaceP2);
        txt.append(" " + user.getEmail());
    }
}

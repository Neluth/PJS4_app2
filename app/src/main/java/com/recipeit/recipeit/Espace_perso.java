package com.recipeit.recipeit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recipeit.recipeit.R;
import com.recipeit.recipeit.models.User;
import com.squareup.picasso.Picasso;

public class Espace_perso extends AppCompatActivity {
    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_perso);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final TextView txt = findViewById(R.id.titreEspaceP2);

        FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txt.append("" + user.username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

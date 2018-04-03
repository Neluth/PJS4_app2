package com.recipeit.recipeit;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.recipeit.recipeit.models.User;
import com.squareup.picasso.Picasso;

public class Accueil_Connect extends AppCompatActivity implements FFridge.OnFragmentInteractionListener, Faccueil.OnFragmentInteractionListener, FVoyage.OnFragmentInteractionListener{

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String uid;
    private FirebaseAuth auth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil__connect);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Faccueil fragment = new Faccueil();
        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                TextView pseudoTV = findViewById(R.id.pseudo);
                pseudoTV.setText(user.username);

                ImageView avatar = findViewById(R.id.compte);

                if(user.avatar.equals("default"))
                    Picasso.with(Accueil_Connect.this).load("https://storage.googleapis.com/pjs4-test.appspot.com/pp/default.png").into(avatar);
                else
                    Picasso.with(Accueil_Connect.this).load("https://storage.googleapis.com/pjs4-test.appspot.com/pp/pp"+user.avatar+".png").into(avatar);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //cas ou l'activité et lancé par le bouton fridge de l'espace perso
        try {
            Boolean goToFridge = getIntent().getExtras().getBoolean("forFridge");
            if(goToFridge) {
                findViewById(R.id.fridge).performClick();
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        // soumission d'une recherche simple (redirection recherche)
        final EditText rechSimple = (EditText) findViewById(R.id.rechercheSimple);

        rechSimple.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Intent rechSimpleIntent = new Intent(Accueil_Connect.this, RechercheActivity.class);
                    String message = rechSimple.getText().toString();
                    rechSimpleIntent.putExtra("estSimple", message);
                    startActivity(rechSimpleIntent);
                    return true;
                }
                return false;
            }

        });

    }


    public void FragVoyage(View view){
        FVoyage fragment = new FVoyage();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.commit();

        ImageView img = findViewById(R.id.home);
        img.setImageResource(R.drawable.home);

        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.more);

        img = findViewById(R.id.ajout);
        img.setImageResource(R.drawable.add);

        img = findViewById(R.id.fridge);
        img.setImageResource(R.drawable.fridge);


        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.woldactive);
    }

    public void FragAccueil(View view){
        Faccueil fragment = new Faccueil();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.commit();

        ImageView img = findViewById(R.id.home);
        img.setImageResource(R.drawable.homeactive);

        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.more);

        img = findViewById(R.id.ajout);
        img.setImageResource(R.drawable.add);

        img = findViewById(R.id.fridge);
        img.setImageResource(R.drawable.fridge);

        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.world);
    }

    public void recherche(View view){
        Intent searchPage = new Intent(Accueil_Connect.this, RechercheActivity.class);
        startActivity(searchPage);
    }

    public void ajoutRecette(View view){
        Intent intentCreerRecette = new Intent(this, AjoutRecette.class);
        startActivity(intentCreerRecette);
    }

    public void FragFridge(View view){
        FFridge fragment = new FFridge();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.commit();

        ImageView img = findViewById(R.id.home);
        img.setImageResource(R.drawable.home);

        img = findViewById(R.id.ajout);
        img.setImageResource(R.drawable.add);

        img = findViewById(R.id.fridge);
        img.setImageResource(R.drawable.fridgeactive);


        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.more);

        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.world);
    }

    public void compte(View view){
        Intent intent = new Intent(Accueil_Connect.this, Espace_perso.class);
        startActivity(intent);
    }

    public void deconnexion(View view){
        auth.signOut();
        Toast.makeText(Accueil_Connect.this, "Au revoir !", Toast.LENGTH_LONG).show();
        Intent i = new Intent(Accueil_Connect.this, Accueil.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    public void rechercheAv(View view) {
        Intent rechAv = new Intent(Accueil_Connect.this, RechercheActivity.class);
        rechAv.putExtra("estAv", true);
        startActivity(rechAv);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

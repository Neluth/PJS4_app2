package com.recipeit.recipeit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Accueil_Connect extends AppCompatActivity implements FFridge.OnFragmentInteractionListener, fajout.OnFragmentInteractionListener, Faccueil.OnFragmentInteractionListener, FVoyage.OnFragmentInteractionListener, Frecherche.OnFragmentInteractionListener{

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FirebaseUser user;
    private FirebaseAuth auth;

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
        user = auth.getCurrentUser();

        TextView pseudoTV = findViewById(R.id.pseudo);
        String pseudo = user.getEmail().toString();
        String [] split = pseudo.split("@", 2);
        pseudoTV.setText(split[0]);
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

    public void FragRecherche(View view){
        Frecherche fragment = new Frecherche();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.commit();

        ImageView img = findViewById(R.id.home);
        img.setImageResource(R.drawable.home);

        img = findViewById(R.id.ajout);
        img.setImageResource(R.drawable.add);

        img = findViewById(R.id.fridge);
        img.setImageResource(R.drawable.fridge);


        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.moreactive);

        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.world);
    }

    public void FragAjout(View view){
        fajout fragment = new fajout();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.commit();

        ImageView img = findViewById(R.id.home);
        img.setImageResource(R.drawable.home);

        img = findViewById(R.id.ajout);
        img.setImageResource(R.drawable.addactive);

        img = findViewById(R.id.fridge);
        img.setImageResource(R.drawable.fridge);


        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.more);

        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.world);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.recipeit.recipeit;

import android.net.Uri;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Accueil extends AppCompatActivity implements Faccueil.OnFragmentInteractionListener, FVoyage.OnFragmentInteractionListener, Frecherche.OnFragmentInteractionListener{

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Faccueil fragment = new Faccueil();
        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void connexion(View view){
        Intent coPage = new Intent(Accueil.this, Connexion.class);
        startActivity(coPage);
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

        img = findViewById(R.id.recherche);
        img.setImageResource(R.drawable.moreactive);

        img = findViewById(R.id.voyage);
        img.setImageResource(R.drawable.world);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

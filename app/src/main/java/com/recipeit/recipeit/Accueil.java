package com.recipeit.recipeit;

import android.net.Uri;
import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.recipeit.recipeit.fragments.FAccueil;
import com.recipeit.recipeit.fragments.FVoyage;

public class Accueil extends AppCompatActivity implements FAccueil.OnFragmentInteractionListener, FVoyage.OnFragmentInteractionListener{

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        FAccueil fragment = new FAccueil();
        fragmentTransaction.replace(R.id.contain_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Accueil.this, Accueil_Connect.class));
            finish();
        }

        // soumission d'une recherche simple (redirection recherche)
        final EditText rechSimple = (EditText) findViewById(R.id.rechercheSimple);

        rechSimple.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Intent rechSimpleIntent = new Intent(Accueil.this, RechercheActivity.class);
                    String message = rechSimple.getText().toString();
                    rechSimpleIntent.putExtra("estSimple", message);
                    startActivity(rechSimpleIntent);
                    return true;
                }
                return false;
            }

        });
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

    public void recherche(View view){
        Intent searchPage = new Intent(Accueil.this, RechercheActivity.class);
        startActivity(searchPage);
    }

    public void FragAccueil(View view){
        FAccueil fragment = new FAccueil();
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void rechercheAv(View view) {
        Intent rechAv = new Intent(Accueil.this, RechercheActivity.class);
        rechAv.putExtra("estAv", true);
        startActivity(rechAv);
    }
}

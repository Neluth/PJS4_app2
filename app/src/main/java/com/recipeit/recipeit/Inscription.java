package com.recipeit.recipeit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Inscription extends AppCompatActivity {
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtPasswordVerif;
    private String mdp1;
    private String mdp2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        txtEmailAddress = (EditText) findViewById(R.id.editTextPseudo);
        txtPassword = (EditText) findViewById(R.id.editTextMdp);
        txtPasswordVerif = (EditText) findViewById(R.id.editTextMdp2);
        mdp1 = (String) txtPassword.getText().toString();
        mdp2 = (String) txtPasswordVerif.getText().toString();
        mAuth = FirebaseAuth.getInstance();
    }

    public void Creer(View v) {
        if (TextUtils.isEmpty(txtEmailAddress.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez une adresse email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez un mot de passe !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Le mot de passe doit contenir 6 caractères.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txtPasswordVerif.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Confirmer le mot de passe !", Toast.LENGTH_SHORT).show();
            return;
        }
        /*if (mdp1!=mdp2) {
            Toast.makeText(getApplicationContext(), "Le mot de passe doit être identique.", Toast.LENGTH_SHORT).show();
            return;
        }*/
        final ProgressDialog progressDialog = ProgressDialog.show(Inscription.this, "Please wait...", "Processing...", true);
        (mAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(Inscription.this, "Vous avez maintenant un compte.", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Inscription.this, Connexion.class);
                            startActivity(i);
                        }
                        else
                        {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(Inscription.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*public void avatar(View view){
        Intent avatar = new Intent(Inscription.this, choixAvatar.class);
        startActivity(avatar);
    }*/
}

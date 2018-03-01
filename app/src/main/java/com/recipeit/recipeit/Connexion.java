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

public class Connexion extends AppCompatActivity {
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        txtEmailAddress = (EditText) findViewById(R.id.editTextPseudo);
        txtPassword = (EditText) findViewById(R.id.editTextMdp);
        mAuth = FirebaseAuth.getInstance();
    }
    /* ne marche pas mais devrait marcher */
    public void Connex(View v) {
        if (TextUtils.isEmpty(txtEmailAddress.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez une adresse email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez un mot de passe !", Toast.LENGTH_SHORT).show();
            return;
        }
        String email = txtEmailAddress.getText().toString();
        final String password = txtPassword.getText().toString();

        final ProgressDialog progressDialog = ProgressDialog.show(Connexion.this, "Please wait...", "Proccessing...", true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Connexion.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressDialog.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                Toast.makeText(Connexion.this, "Minimum 6 caractères", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Connexion.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(Connexion.this, Accueil_Connect.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(Connexion.this, "Login successful", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                /*.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(Connexion.this, "Login successful", Toast.LENGTH_LONG).show();
                           // Intent i = new Intent(Connexion.this, ProfileActivity.class);
                           // i.putExtra("Email", mAuth.getCurrentUser().getEmail());
                           // startActivity(i);
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(Connexion.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });*/

    }

    public void inscription(View view){
        Intent inscrPage = new Intent(Connexion.this, Inscription.class);
        startActivity(inscrPage);
    }
}

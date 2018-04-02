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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.recipeit.recipeit.models.Role;
import com.recipeit.recipeit.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Inscription extends AppCompatActivity {
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtPasswordVerif;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Date d;
    private SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    private String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        txtEmailAddress = (EditText) findViewById(R.id.editTextPseudo);
        txtPassword = (EditText) findViewById(R.id.editTextMdp);
        txtPasswordVerif = (EditText) findViewById(R.id.editTextMdp2);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        d = Calendar.getInstance().getTime();
        formattedDate = df.format(d);
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
        /*if (txtPassword.getText().toString()!= txtPasswordVerif.getText().toString()) {
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
                            onAuthSuccess(task.getResult().getUser());
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

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        writeNewUser(user.getUid(), username, user.getEmail(), formattedDate, txtPassword.getText().toString());
    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private void writeNewUser(String userId, String name, String email, String date, String mdp) {
        User user = new User(name, email, date, mdp);
        mDatabase.child("users").child(userId).setValue(user);
        Role role = new Role();
        mDatabase.child("users").child(userId).child("role").setValue(role.toMap());
    }

    /*public void avatar(View view){
        Intent avatar = new Intent(Inscription.this, choixAvatar.class);
        startActivity(avatar);
    }*/
}

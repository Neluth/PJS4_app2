package com.recipeit.recipeit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recipeit.recipeit.R;

public class ParameterActivity extends AppCompatActivity {
    private CheckBox cb;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
    private EditText txtOldMdp;
    private EditText txtNewMdp;
    private DatabaseReference oldMdp;
    private String checkOldMdp;
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        txtOldMdp = (EditText) findViewById(R.id.oldMdp);
        txtNewMdp = (EditText) findViewById(R.id.newMdp);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        oldMdp = mDatabase.child("users").child(userId).child("password");
        oldMdp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                checkOldMdp = (dataSnapshot.getValue().toString());
                Log.e("oldmdp", checkOldMdp);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        cb = (CheckBox) findViewById(R.id.cBAllergie);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb.setText("oui");
                    mDatabase.child("users").child(userId).child("allergique").setValue(true);
                }
                else{
                    cb.setText("non");
                    mDatabase.child("users").child(userId).child("allergique").setValue(false);
                }
            }
        });
    }

    public void reset(View v){
        if (TextUtils.isEmpty(txtOldMdp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez votre ancien mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(txtNewMdp.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Entrez votre nouveau mot de passe.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtNewMdp.getText().toString().length()<6) {
            Toast.makeText(getApplicationContext(), "Le nouveau mot de passe doit faire 6 caractères.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!txtOldMdp.getText().toString().contains(checkOldMdp)){
            Toast.makeText(getApplicationContext(), "Votre ancien mot de passe ne correspond pas", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            AuthCredential credential = EmailAuthProvider.getCredential(email, txtOldMdp.getText().toString());
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updatePassword(txtNewMdp.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Votre mot de passe à bien été changer.", Toast.LENGTH_SHORT).show();
                                    mDatabase.child("users").child(userId).child("password").setValue(txtNewMdp.getText().toString());
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Erreur.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Erreur. Votre ancien mot de passe est correcte ?", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*public void supprimer(View v){
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldMdp.toString());
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDatabase.child("users").child(userId).removeValue();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(ParameterActivity.this, Accueil.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Compte supprimé :(.", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Erreur.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }*/
}
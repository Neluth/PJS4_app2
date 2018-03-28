package com.recipeit.recipeit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AjoutRecette extends AppCompatActivity {

    private SeekBar difSK;
    private int p;
    private DatabaseReference ref;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int nbIngredients = 0;
    private int nbEtape = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_recette);

        ref = FirebaseDatabase.getInstance().getReference();
        difSK = findViewById(R.id.diff_seekBar);

        p = difSK.getProgress();
        //Toast.makeText(AjoutRecette.this, "Progress : " + p, Toast.LENGTH_SHORT).show();

        difSK.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                p = progress;
               // Toast.makeText(AjoutRecette.this, "Progress : " + progress, Toast.LENGTH_SHORT).show();
            }
        });


        //ajout d'une vie ingrédient avec une id dans le layout
        ImageView imgAjoutIngr = (ImageView) findViewById(R.id.img_add_ingr);
        imgAjoutIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewV) {
                final LinearLayout container = (LinearLayout) findViewById(R.id.contain_spinner_ingr);
                final View child = View.inflate(getApplicationContext(), R.layout.add_ingredient, null);
                ViewGroup vgChild = (ViewGroup) child;
                for (int i = 1; i < vgChild.getChildCount(); i++) {
                    final View v = vgChild.getChildAt(i);
                    v.setId((i + 1) * 1000 + nbIngredients);

                    //récupération de tout les ingrédients dans un spinner
                    if (v instanceof Spinner) {
                        ref.child("ingredients").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Is better to use a List, because you don't know the size
                                // of the iterator returned by dataSnapshot.getChildren() to
                                // initialize the array
                                final List<String> ingr = new ArrayList<>();

                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    String name = areaSnapshot.child("name").getValue(String.class);
                                    ingr.add(name);
                                    Log.e("ingr", ingr.toString());
                                }

                                final Spinner ingrSpinner = (Spinner) v;
                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ingr);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ingrSpinner.setAdapter(areasAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    if (v instanceof ImageView) {
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                container.removeView(child);
                            }
                        });
                    }

                }
                container.addView(child);
                nbIngredients++;
            }
        });

        //ajout d'une étape avec une id
        ImageView addEtape = (ImageView) findViewById(R.id.img_add_etape);

        addEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout holderEtapes = (LinearLayout) findViewById(R.id.contain_etapes);
                final View viewEtape = View.inflate(getApplicationContext(), R.layout.add_etape, null);
                ViewGroup vgEtape = (ViewGroup) viewEtape;
                for (int i = 0; i < vgEtape.getChildCount(); i++) {
                    final View viewChild = vgEtape.getChildAt(i);
                    viewChild.setId((i + 1) * 100 + nbEtape);

                    if (viewChild instanceof ImageView) {
                        viewChild.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holderEtapes.removeView(viewEtape);
                            }
                        });
                    }
                }
                holderEtapes.addView(viewEtape);
                nbEtape++;
            }
        });
    }

    public void ajoutPhotoGallery(View view){
        Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photo, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView img = findViewById(R.id.imgrecette);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        img.setImageBitmap(bit);
                    }
                    catch(Exception e){}
                    //img.setImageURI(selectedImage);
                }
                break;
        }
    }




}

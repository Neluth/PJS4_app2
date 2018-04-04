package com.recipeit.recipeit;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.recipeit.recipeit.models.Etape;
import com.recipeit.recipeit.models.Ingredients;
import com.recipeit.recipeit.models.Recettes;
import com.recipeit.recipeit.models.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AjoutRecette extends AppCompatActivity {

    private SeekBar difSK;
    private int p;
    private DatabaseReference ref;
    private StorageReference stor;
    private String userId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
    private SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    private Uri selectedImage;

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int nbIngredients = 0;
    private Spinner spinnerIng, spinnerType, spinnerOrigine;
    private ListView lvIngQuant, lvEtape;
    private List<String> lsTempIng = new ArrayList<String>();
    private List<String> lsIngredientsQuantite = new ArrayList<String>();
    private List<String>lsEtape = new ArrayList<String>();
    private ArrayAdapter<String> adapterIng, adapterEtape;
    private EditText txtQ, txtEtape, txtHour, txtMinute, txtNom, txtHistoire;
    private TextView checkPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_recette);

        ref = FirebaseDatabase.getInstance().getReference();
        stor = FirebaseStorage.getInstance().getReference();

        checkPhoto = (TextView) findViewById(R.id.titrephoto);
        txtNom = (EditText) findViewById(R.id.nom_recette);

        //histoire et type
        txtHistoire = (EditText) findViewById(R.id.Description);
        spinnerType = (Spinner) findViewById(R.id.listtype);
        ref.child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> type = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String name = areaSnapshot.child("name").getValue(String.class);
                    type.add(name);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AjoutRecette.this, android.R.layout.simple_spinner_item, type);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        spinnerOrigine = (Spinner) findViewById(R.id.listorigin);
        ref.child("origin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> origin = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String name = areaSnapshot.child("name").getValue(String.class);
                    origin.add(name);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AjoutRecette.this, android.R.layout.simple_spinner_item, origin);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOrigine.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //difficulté
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

        //Temps
        txtHour = (EditText) findViewById(R.id.etHour);
        txtMinute = (EditText) findViewById(R.id.etMinute);

        //Ingrédient + Quantite
        txtQ = (EditText) findViewById(R.id.Quant);
        spinnerIng = (Spinner) findViewById(R.id.listingr);
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
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AjoutRecette.this, android.R.layout.simple_spinner_item, ingr);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIng.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lvIngQuant = (ListView) findViewById(R.id.lvIngQuant);
        lvIngQuant.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
        lvIngQuant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = lsIngredientsQuantite.get(position);
                lsIngredientsQuantite.remove(position);
                adapterIng.notifyDataSetChanged();
            }
        });
        adapterIng = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lsIngredientsQuantite){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        lvIngQuant.setAdapter(adapterIng);
        ImageView imgAjoutIngr = (ImageView) findViewById(R.id.img_add_ingr);
        imgAjoutIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewV) {
                if (TextUtils.isEmpty(txtQ.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Entrez une quantité !", Toast.LENGTH_SHORT).show();
                    return;
                }
                String res ="";
                res += spinnerIng.getSelectedItem().toString();
                res += " -> ";
                res += txtQ.getText().toString();
                lsIngredientsQuantite.add(res);
                adapterIng.notifyDataSetChanged();
                nbIngredients++;
                txtQ.setText("");
            }
        });
        //ajout etapes
        txtEtape = (EditText) findViewById(R.id.tvEtapeInstruction);
        lvEtape = (ListView) findViewById(R.id.lvEtape);
        lvEtape.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
        lvEtape.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = lsEtape.get(position);
                lsEtape.remove(position);
                adapterEtape.notifyDataSetChanged();
                nbIngredients--;
            }
        });
        adapterEtape = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lsEtape){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        lvEtape.setAdapter(adapterEtape);

        final ImageView addEtape = (ImageView) findViewById(R.id.img_add_etape);
        addEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lsEtape.add(txtEtape.getText().toString());
                adapterEtape.notifyDataSetChanged();
                txtEtape.setText("");
            }
        });

        Button fin = (Button) findViewById(R.id.fin);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPhoto.getText().toString().contains("Photo enregistrée !")) {
                    Toast.makeText(getApplicationContext(), "Selectionner une image illustrant votre recette !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(txtNom.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Entrez le nom de la recette !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(txtHour.getText().toString())||TextUtils.isEmpty(txtMinute.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Le temps n'est pas complet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(txtHour.getText().toString())>24) {
                    Toast.makeText(getApplicationContext(), "Le temps en heures n'est pas correct", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Integer.parseInt(txtMinute.getText().toString())>60) {
                    Toast.makeText(getApplicationContext(), "Le temps en minutes n'est pas correct", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lvIngQuant.getAdapter().getCount()==0) {
                    Toast.makeText(getApplicationContext(), "Il faut au moins un ingrédient", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lvEtape.getAdapter().getCount()==0) {
                    Toast.makeText(getApplicationContext(), "Il faut au moins une etape", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i=0; i<spinnerIng.getAdapter().getCount(); i++){
                    lsTempIng.add(spinnerIng.getAdapter().getItem(i).toString());
                }
                ArrayList<String>ing = new ArrayList<String>();
                ArrayList<String>q = new ArrayList<String>();
                String[] separated;
                for (int i=0; i<lsIngredientsQuantite.size(); i++){
                    separated = lsIngredientsQuantite.get(i).split(" -> ");
                    ing.add(separated[0]);
                    q.add(separated[1]);
                }

                String nom = txtNom.getText().toString().substring(0,1).toUpperCase() + txtNom.getText().toString().substring(1);
                String currentNom = txtNom.getText().toString();
                String norm = currentNom.replaceAll("[éèàçêôâûî]","-");
                String slug = norm.replace(" ", "-");

                String key =  ref.child("recipes").push().getKey();

                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                String extension = mime.getExtensionFromMimeType(getContentResolver().getType(selectedImage));

                String img = key + "." + extension;
                Recettes recette = new Recettes(p, slug.toLowerCase(),
                        nom, userId, img, spinnerOrigine.getSelectedItem().toString(), spinnerType.getSelectedItem().toString(),
                        txtHistoire.getText().toString());
                ref.child("recipes").child(key).setValue(recette);
                ref.child("recipes").child(key).child("createdAt").setValue(ServerValue.TIMESTAMP);
                Time time = new Time();
                ref.child("recipes").child(key).child("time").setValue(time.toMap(txtHour.getText().toString(), txtMinute.getText().toString()));
                Etape etape = new Etape();
                ref.child("recipes").child(key).child("steps").setValue(etape.toMap(lsEtape));
                Ingredients Ingredients = new Ingredients();
                for (int i=0;i<ing.size();i++){
                    for (int j=0;j<lsTempIng.size();j++) {
                        if (ing.get(i).contains(lsTempIng.get(j))){
                            ref.child("recipes").child(key).child("ingredients").child(Integer.toString(i))
                                    .setValue(Ingredients.toMap(j, q.get(i), lsTempIng.get(j)));
                        }

                    }
                }


                StorageReference st = stor.child("/" + key + "." + extension);

                st.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Toast.makeText(getApplicationContext(), "Recette crée !", Toast.LENGTH_SHORT).show();

                        // creation recette reussie, envoi notification
                        Intent intent = new Intent(AjoutRecette.this, Notification.class);
                        Log.d("notifOK", "oknotif");
                        //startActivity(intent);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AjoutRecette.this, 0, intent, 0);

                    }
                });

                Intent fin = new Intent(AjoutRecette.this, Espace_perso.class);
                startActivity(fin);
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
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        img.setImageBitmap(bit);
                        checkPhoto.setText("Photo enregistrée !");
                    }
                    catch(Exception e){}
                    //img.setImageURI(selectedImage);
                }
                break;
        }
    }
}

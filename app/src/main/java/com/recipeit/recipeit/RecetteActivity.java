package com.recipeit.recipeit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recipeit.recipeit.adapter.IngredientAdapter;
import com.recipeit.recipeit.adapter.StepAdapter;
import com.recipeit.recipeit.models.Recettes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecetteActivity extends AppCompatActivity {

    private String post_key = null;
    private String post_uid = null;

    private ImageView img_v_thumbnail;
    private TextView txt_v_title;
    private TextView txt_v_history;
    private TextView txt_v_username;
    private ListView lv_ingredients;
    private ListView lv_steps;
    private TextView txt_v_time;
    private TextView txt_v_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette);

        post_key = getIntent().getExtras().getString("id");
        Log.d("id", "onCreateView: "+post_key);

        img_v_thumbnail = (ImageView) findViewById(R.id.recetteImage);
        txt_v_title = (TextView) findViewById(R.id.titreRecette);
        txt_v_username = (TextView) findViewById(R.id.createurRecette);
        lv_ingredients = (ListView) findViewById(R.id.listeIngredients);
        lv_steps = (ListView) findViewById(R.id.listeSteps);
        txt_v_time = (TextView) findViewById(R.id.tempsRecette);
        txt_v_note = (TextView) findViewById(R.id.note);

        FirebaseDatabase.getInstance().getReference("recipes").child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recettes post = dataSnapshot.getValue(Recettes.class);

                Log.d("recipe", ""+post);

                String post_image =
                        "https://storage.googleapis.com/pjs4-test.appspot.com/"
                                + post.thumbnail;
                post_uid = post.userid;


                IngredientAdapter adapter = new IngredientAdapter(getApplicationContext(), post.ingredients);
                lv_ingredients.setAdapter(adapter);

                StepAdapter stepAdapter = new StepAdapter(getApplicationContext(), post.steps);
                lv_steps.setAdapter(stepAdapter);

                setListViewHeightBasedOnChildren(lv_ingredients);
                setListViewHeightBasedOnChildren(lv_steps);

                txt_v_title.setText(post.title);
                txt_v_time.setText(post.time.hour + "h" + post.time.minute + "min");
                Picasso.with(getApplicationContext()).load(post_image).into(img_v_thumbnail);

                FirebaseDatabase.getInstance().getReference("users").child(post_uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = (String) dataSnapshot.child("username").getValue();
                        txt_v_username.setText(username);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                FirebaseDatabase.getInstance().getReference("ratings").child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("note", ""+ dataSnapshot);
                        double note = (double) dataSnapshot.child("averageRating").getValue();
                        txt_v_note.setText("" + note);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                FirebaseDatabase.getInstance().getReference("comments").child(post_uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error ddb", databaseError+"");
            }
        });


    }


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

package com.recipeit.recipeit;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recipeit.recipeit.models.Comment;
import com.recipeit.recipeit.models.Ingredients;
import com.recipeit.recipeit.models.Recettes;
import com.recipeit.recipeit.models.User;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecetteActivity extends AppCompatActivity {

    private String post_key = null;
    private String post_uid = null;

    private ImageView img_v_thumbnail;
    private ImageView img_v_difficulty;
    private TextView txt_v_title;
    private TextView txt_v_history;
    private TextView txt_v_username;
    private LinearLayout ll_ingredients;
    private LinearLayout ll_steps;
    private LinearLayout ll_comments;
    private TextView txt_v_time;
    private TextView txt_v_note;
    private TextView txt_v_empty;


    private Comment com;
    private long cpt=0;
    private List<Comment> comments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette);

        post_key = getIntent().getExtras().getString("id");

        img_v_thumbnail = (ImageView) findViewById(R.id.recetteImage);
        img_v_difficulty = (ImageView) findViewById(R.id.fourchettes);

        txt_v_title = (TextView) findViewById(R.id.titreRecette);
        txt_v_username = (TextView) findViewById(R.id.createurRecette);
        ll_ingredients = (LinearLayout) findViewById(R.id.listeIngredients);
        ll_steps = (LinearLayout) findViewById(R.id.listeSteps);
        ll_comments = (LinearLayout) findViewById(R.id.listeComments);
        txt_v_time = (TextView) findViewById(R.id.tempsRecette);
        txt_v_note = (TextView) findViewById(R.id.note);

        txt_v_empty = ll_comments.findViewById(R.id.txtEmpty);


        FirebaseDatabase.getInstance().getReference("recipes").child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recettes post = dataSnapshot.getValue(Recettes.class);

                Log.d("recipe", ""+post);

                String post_image =
                        "https://storage.googleapis.com/pjs4-test.appspot.com/"
                                + post.thumbnail;
                String post_difficulty = "https://storage.googleapis.com/pjs4-test.appspot.com/rating/difficulte"
                        + post.difficulty +".svg";
                post_uid = post.userid;



                for (Ingredients ingredient:post.ingredients) {
                    View child = getLayoutInflater().inflate(R.layout.view_ingredient, null);
                    TextView quantity = child.findViewById(R.id.txtQuantity);
                    TextView name = child.findViewById(R.id.txtName);
                    quantity.setText(ingredient.quantity);
                    name.setText(ingredient.name);

                    ll_ingredients.addView(child);
                }


                for (int j = 0; j< post.steps.size(); j++) {
                    View child = getLayoutInflater().inflate(R.layout.view_step, null);
                    TextView index = child.findViewById(R.id.txtIndex);
                    TextView text = child.findViewById(R.id.txtName);
                    text.setText(post.steps.get(j));
                    index.setText("Étape "+ (j+1));

                    ll_steps.addView(child);
                }


                txt_v_title.setText(post.title);
                txt_v_time.setText(post.time.hour + "h" + post.time.minute + "min");
                Picasso.with(getApplicationContext()).load(post_image).into(img_v_thumbnail);
                Picasso.with(getApplicationContext()).load(post_difficulty).into(img_v_difficulty);

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
                        Object note = dataSnapshot.child("averageRating").getValue();
                        if(note != null){
                            if(note instanceof Double){
                                note = (double) note;
                            }else{
                                note = (long) note;
                            }
                            txt_v_note.setText("" + note);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                FirebaseDatabase.getInstance().getReference("comments").child(post_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            com = child.getValue(Comment.class);
                            getUser(child.getKey());
                            txt_v_empty.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG", "onCancelled: "+databaseError);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error ddb", databaseError+"");
            }
        });


    }

    private void getUser(String commentKey){
        FirebaseDatabase.getInstance().getReference("users").child(commentKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.user = dataSnapshot.getValue(User.class);

                View child = getLayoutInflater().inflate(R.layout.view_comment, null);
                TextView username = (TextView) child.findViewById(R.id.txtUsername);
                TextView comment = (TextView) child.findViewById(R.id.txtComment);
                TextView note = (TextView) child.findViewById(R.id.txtNote);
                ImageView avatar = (ImageView) child.findViewById(R.id.user_avatar);

                username.setText(com.user.username);
                comment.setText(com.comment);
                note.setText(com.rating);

                String url;
                if(com.user.avatar.equals("default")){
                    url = "https://storage.googleapis.com/pjs4-test.appspot.com/pp/"+com.user.avatar+".png";
                    Log.d("thumb", "getView: "+url);
                }else{
                    url = "https://storage.googleapis.com/pjs4-test.appspot.com/pp/pp"+com.user.avatar+".png";
                    Log.d("thumb", "getView: "+url);
                }
                Picasso.with(RecetteActivity.this).load(url).into(avatar);

                ll_comments.addView(child);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("getUser", "onCancelled: ");
            }
        });
    }


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

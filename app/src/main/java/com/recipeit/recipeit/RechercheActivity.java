package com.recipeit.recipeit;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;

import java.util.ArrayList;
import java.util.List;

public class RechercheActivity extends AppCompatActivity{

    private final String    ALGOLIA_APP_ID= "S0Z5F94KGN",
            ALGOLIA_SEARCH_API_KEY= "62bf79ebd94fd5a4b8a76dc851da6f47",
            ALGOLIA_INDEX_NAME= "recipes";

    private Searcher searcher;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private Button btnFilter;
    private ConstraintLayout cl;
    private ListView lv_ings;
    private ListView lv_origin;
    private ListView lv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        cl = findViewById(R.id.recherche_cl);
        btnFilter = findViewById(R.id.btnFilters);
        lv_ings = findViewById(R.id.ing_refinements);
        lv_origin = findViewById(R.id.origin_refinements);
        lv_type = findViewById(R.id.type_refinements);



        setContentView(R.layout.activity_recherche);
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);

        List<String> ings = (ArrayList<String>)getIntent().getSerializableExtra("INGS");

        if( ings != null){
            searcher.addFacetRefinement("ingredients.name", ings, true);
        }

        InstantSearch helper = new InstantSearch(this, searcher);
        helper.search();

    }

    public void FragRecette(View view){

        Bundle b = new Bundle();

        String recipeId = (((TextView)view.findViewById(R.id.recipe_id)).getText()).toString();

        b.putString("id", recipeId );


        Intent intent = new Intent(this, RecetteActivity.class);
        intent.putExtras(b);// if its string type
        startActivity(intent);

    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        searcher.search(intent); // Show results for voice query (from intent)
    }

    public void onDestroy(){
        searcher.destroy();
        super.onDestroy();
    }

    public void onClickFilter(View view) {

        ConstraintLayout cl = findViewById(R.id.filters);

        cl.setVisibility(View.VISIBLE);
        RecetteActivity.setListViewHeightBasedOnChildren(lv_ings);
        RecetteActivity.setListViewHeightBasedOnChildren(lv_origin);
        RecetteActivity.setListViewHeightBasedOnChildren(lv_type);

    }

    public void onClickClose(View view) {
        ConstraintLayout cl= findViewById(R.id.filters);

        cl.setVisibility(View.GONE);
    }
}

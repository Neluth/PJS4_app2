package com.recipeit.recipeit;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.algolia.instantsearch.helpers.InstantSearch;
import com.algolia.instantsearch.helpers.Searcher;

public class RechercheActivity extends AppCompatActivity implements frecette.OnFragmentInteractionListener{

    private final String    ALGOLIA_APP_ID= "S0Z5F94KGN",
            ALGOLIA_SEARCH_API_KEY= "62bf79ebd94fd5a4b8a76dc851da6f47",
            ALGOLIA_INDEX_NAME= "recipes";

    private Searcher searcher;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        searcher = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY, ALGOLIA_INDEX_NAME);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onDestroy(){
        searcher.destroy();
        super.onDestroy();
    }
}

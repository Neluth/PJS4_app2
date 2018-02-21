package com.recipeit.recipeit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class choixAvatar extends AppCompatActivity {
    private List<Integer> F_cheveux;
    private List<ImageView> M_cheveux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_avatar);

        F_cheveux = new ArrayList<Integer>();
        remplirCheveuxF();

        M_cheveux = new ArrayList<ImageView>();
    }

    private void remplirCheveuxF(){
        F_cheveux.add(R.drawable.cheveux_f_1);
        F_cheveux.add(R.drawable.cheveux_f_2);

    }
}

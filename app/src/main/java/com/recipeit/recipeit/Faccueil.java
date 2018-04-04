package com.recipeit.recipeit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Faccueil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Faccueil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Faccueil extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private CarouselView carouselView;
    private ImageListener imageListener;

    private ArrayList<ArrayList<String>> recettesDuMoment;
    private ArrayList<TopRecette> topRecettes = new ArrayList<TopRecette>();
    int j = 0;

    public Faccueil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Faccueil.
     */
    // TODO: Rename and change types and number of parameters
    public static Faccueil newInstance(String param1, String param2) {
        Faccueil fragment = new Faccueil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faccueil, container, false);

        //TODO chercher les images de recettes sur firebase

        //initialiser les images top recettes et recettes du moment
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);

        ImageView imgV1 = view.findViewById(R.id.topRecette1);
        ImageView imgV2 = view.findViewById(R.id.topRecette2);
        ImageView imgV3 = view.findViewById(R.id.topRecette3);

        topRecettes.add(new TopRecette(imgV1, null, null));
        topRecettes.add(new TopRecette(imgV2, null, null));
        topRecettes.add(new TopRecette(imgV3, null, null));

        FirebaseDatabase.getInstance()
                .getReference("recipes")
                .orderByChild("createdAt")
                .limitToFirst(3)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        recettesDuMoment = new ArrayList<ArrayList<String>>();
                        for (DataSnapshot child:dataSnapshot.getChildren()) {
                            String url = "https://storage.googleapis.com/pjs4-test.appspot.com/" + child.child("thumbnail").getValue();
                            String recipeid = child.getKey();

                            recettesDuMoment.add(i, new ArrayList<String>());
                            recettesDuMoment.get(i).add(url);
                            recettesDuMoment.get(i).add(recipeid);

                            i++;
                        }

                        imageListener = new ImageListener() {
                            @Override
                            public void setImageForPosition(int position, ImageView imageView) {
                                Picasso.with(getContext()).load(recettesDuMoment.get(position).get(0)).into(imageView);
                            }
                        };


                        carouselView.setImageListener(imageListener);
                        carouselView.setPageCount(recettesDuMoment.size());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("ratings").orderByChild("averageRating").limitToFirst(3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                j = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    FirebaseDatabase.getInstance().getReference("recipes").child(child.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            topRecettes.get(j).url = "https://storage.googleapis.com/pjs4-test.appspot.com/" + dataSnapshot.child("thumbnail").getValue();
                            topRecettes.get(j).id = dataSnapshot.getKey();



                            Picasso.with(getContext()).load(topRecettes.get(j).url).into(topRecettes.get(j).imgV);
                            topRecettes.get(j).imgV.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intentRecette;
                                        intentRecette = new Intent(getActivity(), RecetteActivity.class);
                                        for (TopRecette t:topRecettes) {
                                            if(t.imgV == view)
                                                j=topRecettes.indexOf(t);
                                        }
                                        intentRecette.putExtra("id", topRecettes.get(j).id);
                                        startActivity(intentRecette);
                                    }
                                });

                            j++;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("tagrecipes", "onCancelled: " + databaseError);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("tagratings", "onCancelled: " + databaseError);
            }
        });



        
        //démarre une activité recette en lui donnant l'id d'une recette
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //Log.e("truc", getActivity().getLocalClassName());
                Intent intentRecette;
                intentRecette = new Intent(getActivity(), RecetteActivity.class);
                intentRecette.putExtra("id", recettesDuMoment.get(position).get(1));
                startActivity(intentRecette);
            }
        });
        // Inflate the layout for this fragment


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class TopRecette{
        ImageView imgV;
        String url;
        String id;

        TopRecette(ImageView imgV, String url, String id){
            this.imgV = imgV;
            this.url = url;
            this.id = id;
        }
    }
}

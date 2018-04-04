package com.recipeit.recipeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.recipeit.recipeit.R;
import com.recipeit.recipeit.RecetteActivity;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FVoyage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FVoyage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FVoyage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageListener imageListenerEurope;
    private ArrayList<ArrayList<String>> recettesEurope;
    private CarouselView carouselViewEurope;
    private CarouselView carouselViewAsie;
    private ArrayList<ArrayList<String>> recettesAsie;
    private ImageListener imageListenerAsie;

    public FVoyage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FVoyage.
     */
    // TODO: Rename and change types and number of parameters
    public static FVoyage newInstance(String param1, String param2) {
        FVoyage fragment = new FVoyage();
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
        View view = inflater.inflate(R.layout.fragment_fvoyage, container, false);

        
        // Carousel Europe
        carouselViewEurope = view.findViewById(R.id.carouselViewEurope);
        FirebaseDatabase.getInstance()
                .getReference("recipes")
                .orderByChild("createdAt")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        recettesEurope = new ArrayList<ArrayList<String>>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(child.child("origin").getValue().equals("européenne")) {
                                String url = "https://storage.googleapis.com/pjs4-test.appspot.com/" + child.child("thumbnail").getValue();
                                String recipeid = child.getKey();

                                recettesEurope.add(i, new ArrayList<String>());
                                recettesEurope.get(i).add(url);
                                recettesEurope.get(i).add(recipeid);

                                i++;
                            }
                        }

                        imageListenerEurope = new ImageListener() {
                            @Override
                            public void setImageForPosition(int position, ImageView imageView) {
                                Picasso.with(getContext()).load(recettesEurope.get(position).get(0)).into(imageView);
                            }
                        };


                        carouselViewEurope.setImageListener(imageListenerEurope);
                        carouselViewEurope.setPageCount(recettesEurope.size());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //démarre une activité recette en lui donnant l'id d'une recette
        carouselViewEurope.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //Log.e("truc", getActivity().getLocalClassName());
                Intent intentRecette;
                intentRecette = new Intent(getActivity(), RecetteActivity.class);
                intentRecette.putExtra("id", recettesEurope.get(position).get(1));
                startActivity(intentRecette);
            }
        });

        // Carousel Asie
        carouselViewAsie = view.findViewById(R.id.carouselViewAsie);
        FirebaseDatabase.getInstance()
                .getReference("recipes")
                .orderByChild("createdAt")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        recettesAsie = new ArrayList<ArrayList<String>>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            if(child.child("origin").getValue().equals("asiatique")) {
                                String url = "https://storage.googleapis.com/pjs4-test.appspot.com/" + child.child("thumbnail").getValue();
                                String recipeid = child.getKey();

                                recettesAsie.add(i, new ArrayList<String>());
                                recettesAsie.get(i).add(url);
                                recettesAsie.get(i).add(recipeid);

                                i++;
                            }
                        }

                        imageListenerAsie = new ImageListener() {
                            @Override
                            public void setImageForPosition(int position, ImageView imageView) {
                                Picasso.with(getContext()).load(recettesEurope.get(position).get(0)).into(imageView);
                            }
                        };


                        carouselViewAsie.setImageListener(imageListenerAsie);
                        carouselViewAsie.setPageCount(recettesAsie.size());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //démarre une activité recette en lui donnant l'id d'une recette
        carouselViewAsie.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //Log.e("truc", getActivity().getLocalClassName());
                Intent intentRecette;
                intentRecette = new Intent(getActivity(), RecetteActivity.class);
                intentRecette.putExtra("id", recettesAsie.get(position).get(1));
                startActivity(intentRecette);
            }
        });
                
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
}

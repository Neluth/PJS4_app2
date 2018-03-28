package com.recipeit.recipeit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link FFridge#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FFridge extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int nbIngredients = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference ref;

    public FFridge() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FFridge.
     */
    // TODO: Rename and change types and number of parameters
    public static FFridge newInstance(String param1, String param2) {
        FFridge fragment = new FFridge();
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
        ref = FirebaseDatabase.getInstance().getReference("ingredients");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ffridge, container, false);
        Button btnChercherRecettes = (Button) view.findViewById(R.id.btnChercherRecette);
        btnChercherRecettes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // do something
            }
        });
        ImageView imgAjoutIngr = (ImageView) view.findViewById(R.id.imgAddIngrFridge);
        imgAjoutIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewV) {
                /*
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                AddIngredients fragment = new AddIngredients();

                fragmentTransaction.add(R.id.containerIngrFridge, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                */
                final LinearLayout container = (LinearLayout) view.findViewById(R.id.containerIngrFridge);
                final View child = View.inflate(getContext(), R.layout.add_ingredient, null);
                ViewGroup vgChild = (ViewGroup) child;
                for(int i = 0; i < vgChild.getChildCount(); i++) {
                    final View v = vgChild.getChildAt(i);
                    v.setId(i*1000+nbIngredients);
                    if( v instanceof  Spinner) {
                        ref.addValueEventListener(new ValueEventListener() {
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

                                final Spinner ingrSpinner = (Spinner) v;
                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ingr);
                                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ingrSpinner.setAdapter(areasAdapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    if(v instanceof ImageView) {
                        Log.e("truc", "truc");
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("truc", "truc2");
                                container.removeView(child);
                            }
                        });
                    }

                }
                Log.e("truc", "truc");
                container.addView(child);
                nbIngredients++;
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

package com.recipeit.recipeit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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
    private FirebaseAuth authFirebase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DatabaseReference refFridge;
    private DatabaseReference refIngr;
    private String userId;
    private ArrayList<String> mesIngredients;
    private ArrayAdapter<String> lvAdapter;

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
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @SuppressLint("ClickableViewAccessibility")
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
                // lancer la recherche avec des ingrédients (extension)
            }
        });

        //initialise la listview (remote)
        final ListView lvIngrFridge = view.findViewById(R.id.listIngrFridge);
        //final ArrayList<String> ingrInFridge = new ArrayList<String>();
        
        
        refFridge = FirebaseDatabase.getInstance().getReference("users/" + userId + "/fridge" );//.child(userId).child("fridge");
        refFridge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> ingrInFridge = new ArrayList<String>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String ingrFridge = areaSnapshot.getValue(String.class);
                    ingrInFridge.add(ingrFridge);

                }
                setIngrInFridge(ingrInFridge);
                lvAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, ingrInFridge);
                lvIngrFridge.setAdapter(lvAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //initialise le spinner
        final Spinner spIngr = view.findViewById(R.id.spListingr);
        refIngr = FirebaseDatabase.getInstance().getReference("ingredients");
        refIngr.addValueEventListener(new ValueEventListener() {
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


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ingr);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spIngr.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //En cas de clic sur image ajout
        ImageView imgAjoutIngr = (ImageView) view.findViewById(R.id.imgAddIngrFridge);
        imgAjoutIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemToAdd = spIngr.getSelectedItem().toString();
                boolean notAdd = false;
                for(String s : getIngrInFridge()) {
                    if(s.equals(itemToAdd)) {
                        notAdd = true;
                    }
                }
                if(!notAdd) {
                    addIngrInRemoteFridge(itemToAdd);
                    lvAdapter.notifyDataSetChanged();

                }

            }
        });

        lvIngrFridge.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeIngrInRemoteFridge(getIngrInFridge().get(i));
            }
        });

        //permet à la listview de scroller dans une scrollview
        lvIngrFridge.setOnTouchListener(new ListView.OnTouchListener() {
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

        return view;
    }

    private ArrayList<String> getIngrInFridge() {
        return mesIngredients;
    }

    private void setIngrInFridge(ArrayList<String> ingrInFridge) {
        this.mesIngredients = ingrInFridge;
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

    private void addIngrInRemoteFridge(final String ingredient) {
        refIngr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name.equals(ingredient)) {
                        //return snapshot.getKey();
                        refFridge.child(snapshot.getKey()).setValue(snapshot.child("name").getValue());
                        lvAdapter.notifyDataSetInvalidated();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeIngrInRemoteFridge(final String ingredient) {
        refIngr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name.equals(ingredient)) {
                        refFridge.child(snapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

package com.github.www.allergyapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class AllergenFragment extends Fragment {

    private ArrayList<Allergen> allergens = new ArrayList<>();

    public AllergenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_allergen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        AllergenArrayAdapter adapter = new AllergenArrayAdapter(allergens, getContext(), this);

        ListView lView = (ListView)getView().findViewById(R.id.view_allergenlist);
        lView.setAdapter(adapter);


        if (allergens.size() > 0) {
            Button allergensbtn = (Button) getActivity().findViewById(R.id.button_allergens);
            allergensbtn.setText("Allergies (" + Integer.toString(allergens.size()) + ")");
        }

        final Button createAllergenBtn = (Button) view.findViewById(R.id.button_create_new);
        createAllergenBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent activityCreateAllergen = new Intent(getActivity(), CreateAllergenActivity.class);
                AllergenFragment.this.startActivity(activityCreateAllergen);
            }
        });

        final Button hideBtn = (Button) view.findViewById(R.id.button_hide);
        hideBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Button allergenBtn = getActivity().findViewById(R.id.button_allergens);
                Button OCRBtn = getActivity().findViewById(R.id.startButton);

                TextView noallergenstxt = getActivity().findViewById(R.id.noAllergensText);

                allergenBtn.setVisibility(View.VISIBLE);
                OCRBtn.setVisibility(View.VISIBLE);
                if (allergens.isEmpty()) {
                    noallergenstxt.setVisibility(View.VISIBLE);
                }

                View allergensFragment = getActivity().findViewById(R.id.allergensLayout);
                allergensFragment.setVisibility(View.GONE);

                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.setAllergens(allergens);
            }
        });
    }

    public void addAllergen(Allergen allergen) {
        allergens.add(allergen);
        // add the allergen to the main activity if necessary
        if (!((MainActivity)getActivity()).inAllergens(allergen)){
            ((MainActivity)getActivity()).setAllergens(allergens);
        }

        AllergenArrayAdapter adapter = new AllergenArrayAdapter(allergens, getContext(), this);

        ListView lView = (ListView)getView().findViewById(R.id.view_allergenlist);
        lView.setAdapter(adapter);

        Button allergensbtn = (Button) getActivity().findViewById(R.id.button_allergens);
        allergensbtn.setText("Allergies (" + Integer.toString(allergens.size()) + ")");
    }

    public ArrayList<Allergen> getAllergens() {
        return allergens;
    }

    public void updateAllergensBtn(int allergensNum) {
        ((MainActivity)getActivity()).setAllergens(allergens);
        Button allergensbtn = (Button) getActivity().findViewById(R.id.button_allergens);
        if (allergensNum > 0) {
            allergensbtn.setText("Allergies (" + Integer.toString(allergensNum) + ")");
        }
        else {
            allergensbtn.setText("Allergies");
        }
    }
}
package com.github.www.allergyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;


import java.io.File;
import java.util.Scanner;

public class CreateAllergenActivity extends AppCompatActivity {

    String selectedAllergen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_allergen);

        EditText nameInput = (EditText) findViewById(R.id.nameInput);

        Button createBtn = (Button) findViewById(R.id.createButton);
        createBtn.setEnabled(false);

        Spinner spinner = (Spinner) findViewById(R.id.allergenSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.allergens_array,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (parent.getSelectedItem().toString().equals("Other")) {
                    nameInput.setVisibility(View.VISIBLE);
                    createBtn.setEnabled(false);
                }
                else {
                    nameInput.setText("");
                    nameInput.setVisibility(View.INVISIBLE);
                    selectedAllergen = parent.getSelectedItem().toString();
                    createBtn.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                createBtn.setEnabled(false);
            }
        });

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!nameInput.getText().toString().isEmpty() || !spinner.getSelectedItem().toString().equals("Other")) {
                    createBtn.setEnabled(true);
                }
                else {
                    createBtn.setEnabled(false);
                }
            }
        });
    }


    public void selectLevel(View view) {
        RadioButton benignBtn = (RadioButton) findViewById(R.id.benignButton);
        RadioButton moderateBtn = (RadioButton) findViewById(R.id.moderateButton);
        RadioButton severeBtn = (RadioButton) findViewById(R.id.severeButton);

        switch (getResources().getResourceEntryName(view.getId())) {
            case "benignButton":
                benignBtn.setChecked(true);
                moderateBtn.setChecked(false);
                severeBtn.setChecked(false);
                break;
            case "moderateButton":
                benignBtn.setChecked(false);
                moderateBtn.setChecked(true);
                severeBtn.setChecked(false);
                break;
            case "severeButton":
                benignBtn.setChecked(false);
                moderateBtn.setChecked(false);
                severeBtn.setChecked(true);
                break;
        }
    }

    public void create(View view) {

        Bundle bundle = new Bundle();
        String allergenName;
        int allergenLvl = 1;

        EditText nameInput = (EditText) findViewById(R.id.nameInput);
        RadioButton benignBtn = (RadioButton) findViewById(R.id.benignButton);
        RadioButton moderateBtn = (RadioButton) findViewById(R.id.moderateButton);
        RadioButton severeBtn = (RadioButton) findViewById(R.id.severeButton);
        Spinner spinner = (Spinner) findViewById(R.id.allergenSpinner);

        if (spinner.getSelectedItem().toString().equals("Other")) {
            allergenName = nameInput.getText().toString().trim();
        }
        else {
            allergenName = selectedAllergen;
        }
        if (benignBtn.isChecked()) {
            allergenLvl = 1;
        } else if (moderateBtn.isChecked()) {
            allergenLvl = 2;
        } else if (severeBtn.isChecked()) {
            allergenLvl = 3;
        }

        bundle.putString("NAME", allergenName);
        bundle.putInt("LEVEL", allergenLvl);

        MainActivity.mainact.getIntent().putExtras(bundle);
        finish();
    }
}

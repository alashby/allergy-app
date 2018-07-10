package com.github.www.allergyapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static Activity mainact;
    private ArrayList<Allergen> allergens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainact = this;

        allergens = new ArrayList<Allergen>();

        try {
            InputStream inputStream = this.openFileInput("allergens.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String inputStr = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((inputStr = bufferedReader.readLine()) != null) {
                    Allergen allergen = new Allergen(inputStr.split(",")[0], Integer.parseInt(inputStr.split(",")[1]));
                    allergens.add(allergen);
                    AllergenFragment allergenFragment =
                            (AllergenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_allergens);
                    allergenFragment.addAllergen(allergen);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView noallergenstxt = (TextView) findViewById(R.id.noAllergensText);
        Button startbtn = (Button) findViewById(R.id.startButton);

        if (allergens.isEmpty()) {
            noallergenstxt.setVisibility(View.VISIBLE);
            startbtn.setEnabled(false);
        }
        else
        {
            noallergenstxt.setVisibility(View.INVISIBLE);
            startbtn.setEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().hasExtra("NAME")) {
            Bundle bundle = getIntent().getExtras();

            String allergenName = bundle.getString("NAME");
            int allergenLvl = bundle.getInt("LEVEL");

            getIntent().removeExtra("NAME");
            getIntent().removeExtra("LEVEL");

            AllergenFragment allergenFragment =
                    (AllergenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_allergens);
            Allergen allergen = new Allergen(allergenName, allergenLvl);
            allergenFragment.addAllergen(allergen);
        }
    }

    public ArrayList<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<Allergen> allergens) {
        this.allergens = allergens;

        String outputStr = "";
        // TODO: escape characters for "," and etc
        for (int i = 0; i < allergens.size(); i++) {
            outputStr += allergens.get(i).get_name() + "," + allergens.get(i).get_level() + "\n";
        }

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(this.openFileOutput("allergens.txt", this.MODE_PRIVATE));
            outputStreamWriter.write(outputStr);
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView noallergenstxt = (TextView) findViewById(R.id.noAllergensText);
        Button startbtn = (Button) findViewById(R.id.startButton);
        View allergensFragment = findViewById(R.id.allergensLayout);

        if (allergens.isEmpty()) {
            if (allergensFragment.getVisibility() != View.VISIBLE) {
                noallergenstxt.setVisibility(View.VISIBLE);
            }
            startbtn.setEnabled(false);
        }
        else
        {
            noallergenstxt.setVisibility(View.INVISIBLE);
            startbtn.setEnabled(true);
        }

    }

    public void expandAllergens(View view) {
        Button allergenBtn = findViewById(R.id.button_allergens);
        Button OCRBtn = findViewById(R.id.startButton);
        TextView noallergenstxt = (TextView) findViewById(R.id.noAllergensText);

        noallergenstxt.setVisibility(View.GONE);
        allergenBtn.setVisibility(View.GONE);
        OCRBtn.setVisibility(View.GONE);

        View allergensFragment = findViewById(R.id.allergensLayout);
        allergensFragment.setVisibility(View.VISIBLE);
    }

    public void startOCR(View view) {
        // allergenFragment = (AllergenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_allergens);
        //allergens = allergenFragment.getAllergens();

        //TODO: Add warning that you cannot start OCR without any allergies

        ArrayList<String> allergenNames = new ArrayList<>();
        ArrayList<Integer> allergenLvls = new ArrayList<>();
        for (int i = 0; i < allergens.size(); i++) {
            allergenNames.add(allergens.get(i).get_name());
            allergenLvls.add(allergens.get(i).get_level());
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("NAMES", allergenNames);
        bundle.putIntegerArrayList("LEVELS", allergenLvls);

        Intent ocrActivity = new Intent(this, OCRActivity.class);
        ocrActivity.putExtras(bundle);
        startActivity(ocrActivity);

    }
}

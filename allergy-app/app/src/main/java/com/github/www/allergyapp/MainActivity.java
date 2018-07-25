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
    AllergenMap allergyMap = new AllergenMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainact = this;

        // array list to hold user's previously selected allergies
        allergens = new ArrayList<Allergen>();
        // array to populate map with with resource array list
        String allergenarr[] = getResources().getStringArray(R.array.allergens_array);

        //fill the map with the allergens
        for(int i = 0; i < allergenarr.length; i++)
        {
            if (allergenarr[i].equals("Wheat")) {
                allergyMap.addAllergen((Integer.toString(i+1)), new Allergen(allergenarr[i],
                        0, allergyMap.returnAllergenKey("Gluten")));
            }
            else if (!allergenarr[i].equals("Other")) {
                allergyMap.addAllergen(Integer.toString(i + 1),
                        (new Allergen(allergenarr[i], 0)));
            }
        }


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
            // TODO: Add comment about change to adding to map
            int count = allergenarr.length + 1;
            for (int i = 0; i < allergens.size(); i++) {
                // If allergen not in the default list, add it
                if (allergyMap.returnAllergenKey(allergens.get(i).get_name()).equals("Allergy Not Found")) {
                    allergyMap.addAllergen(Integer.toString(count), allergens.get(i));
                    count++;
                }
                // If not, adjust the severity level in the map
                else
                {
                    allergyMap.setAllergyLevel(allergens.get(i).get_name(),
                            allergens.get(i).get_level());
                }
            }
            noallergenstxt.setVisibility(View.INVISIBLE);
            startbtn.setEnabled(true);
        }

        GlobalAllergens.selectedAllergies = allergens;
        GlobalAllergens.allergenMap = allergyMap;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On resume, if the user came from the create allergen activity add the new allergen
        if (getIntent().hasExtra("NAME")) {
            Bundle bundle = getIntent().getExtras();

            String allergenName = bundle.getString("NAME");
            int allergenLvl = bundle.getInt("LEVEL");

            getIntent().removeExtra("NAME");
            getIntent().removeExtra("LEVEL");

            AllergenFragment allergenFragment =
                    (AllergenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_allergens);
            Allergen allergen = new Allergen(allergenName, allergenLvl);
            allergens.add(allergen);
            allergenFragment.addAllergen(allergen);

            GlobalAllergens.selectedAllergies.add(allergen);
            if (allergyMap.returnAllergenKey(allergen.get_name()).equals("Allergy Not Found")) {
                allergyMap.addAllergen(Integer.toString(allergyMap.size()+1),
                        allergen);
            }
            else
            {
                allergyMap.setAllergyLevel(allergen.get_name(),
                        allergen.get_level());
            }
            GlobalAllergens.allergenMap = allergyMap;
            GlobalAllergens.selectedAllergies = allergens;
        }
    }

    public ArrayList<Allergen> getAllergens() {
        return allergens;
    }

    /**
     *
     * @param allergens Array list of allergens to set as the user's selected allergens.
     *                  Set the global allergen list and adds to text file for caching previous entries.
     */
    public void setAllergens(ArrayList<Allergen> allergens) {
        this.allergens = allergens;
        GlobalAllergens.selectedAllergies = allergens;

        // Update the map
        for (int i = 0; i < allergens.size(); i++) {
            if (allergyMap.returnAllergenKey(allergens.get(i).get_name()).equals("Allergy Not Found")) {
                allergyMap.addAllergen(Integer.toString(allergyMap.size() + 1),
                        allergens.get(i));
            } else {
                allergyMap.setAllergyLevel(allergens.get(i).get_name(),
                        allergens.get(i).get_level());
            }
        }
        GlobalAllergens.allergenMap = allergyMap;

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

        // TODO: Remove commented
        /*
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("NAMES", allergenNames);
        bundle.putIntegerArrayList("LEVELS", allergenLvls);
        */

        Intent ocrActivity = new Intent(this, OCRActivity.class);
        //ocrActivity.putExtras(bundle);
        startActivity(ocrActivity);

    }

    /**
     *
     * @param allergen - The allergen to see if the allergen is present within the activity's allergen
     *                 ArrayList
     * @return True if the allergen is already in the allergens ArrayList. False if otherwise.
     */
    public boolean inAllergens(Allergen allergen) {
        for (int i = 0; i < allergens.size(); i++) {
            if (allergens.get(i).get_name().equals(allergen.get_name())) {
                return true;
            }
        }
        return false;
    }
}

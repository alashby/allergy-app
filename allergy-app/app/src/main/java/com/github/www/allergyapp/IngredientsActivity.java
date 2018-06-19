package com.github.www.allergyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("INGREDIENTS");
        ArrayList<String> ingredients = formatIngredients(text);
        ListView listView = findViewById(R.id.ingredients);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
        listView.setAdapter(arrayAdapter);
    }

    private ArrayList<String> formatIngredients(String text) {
        ArrayList<String> ingredients = new ArrayList(Arrays.asList(text.split(":")));
        ingredients.remove(0);
        text = ingredients.toString();
        text = text.substring(1,text.length()-1).replaceAll("\n", " ");
        ingredients = new ArrayList(Arrays.asList(text.split(",|\\.")));
        return ingredients;
    }
}

package com.github.www.allergyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AllergenArrayAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Allergen> list = new ArrayList<Allergen>();
    private Context context;
    private AllergenFragment allergenFrag;

    public AllergenArrayAdapter(ArrayList<Allergen> list, Context context, AllergenFragment allergenFrag) {
        this.context = context;
        this.list = list;
        this.allergenFrag = allergenFrag;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int pos) { return list.get(pos); }

    @Override
    public long getItemId(int pos) { return 0; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.partial_allergen_list, null);
        }

        final Button deleteBtn = (Button) view.findViewById(R.id.deleteButton);

        TextView allergenName = (TextView) view.findViewById(R.id.nameText);
        allergenName.setText(list.get(position).get_name());

        TextView allergenLvl = (TextView) view.findViewById(R.id.levelText);
        switch (list.get(position).get_level()) {
            case 1:
                allergenLvl.setTextColor(context.getResources().getColor(R.color.colorGreen));
                allergenLvl.setText("Benign"); break;
            case 2:
                allergenLvl.setTextColor(context.getResources().getColor(R.color.colorYellow));
                allergenLvl.setText("Moderate"); break;
            case 3:
                allergenLvl.setTextColor(context.getResources().getColor(R.color.colorRed));
                allergenLvl.setText("Severe"); break;
        }

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                allergenFrag.updateAllergensBtn(list.size());

            }
        });

        return view;
    }
}



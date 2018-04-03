package com.recipeit.recipeit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.recipeit.recipeit.R;
import com.recipeit.recipeit.models.Ingredients;

import java.util.List;

/**
 * Created by leach on 01/04/2018.
 */

public class IngredientAdapter  extends ArrayAdapter<Ingredients>{

    public IngredientAdapter(Context context, List<Ingredients> ings) {
        super(context, 0, ings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_ingredient,parent, false);
        }

        AdvertViewHolder viewHolder = (AdvertViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new AdvertViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Ingredients ing = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        Typeface ralewaylight = Typeface.createFromAsset(getContext().getAssets(), "font/ralewaylight.ttf");
        Typeface ralewayregular = Typeface.createFromAsset(getContext().getAssets(), "font/ralewayregular.ttf");
        viewHolder.name.setText(ing.name);
        viewHolder.quantity.setText(ing.quantity);
        TextView txtDe = convertView.findViewById(R.id.txtDe);
        viewHolder.name.setTypeface(ralewayregular);
        viewHolder.quantity.setTypeface(ralewayregular);
        txtDe.setTypeface(ralewaylight);

        return convertView;
    }

    public class AdvertViewHolder {
        public TextView quantity;
        public TextView name;
    }

}

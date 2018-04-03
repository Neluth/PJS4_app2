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

public class StepAdapter extends ArrayAdapter<String> {
    
    public StepAdapter(Context context, List<String> steps) {
        super(context, 0, steps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_step,parent, false);
        }

        StepAdapter.AdvertViewHolder viewHolder = (StepAdapter.AdvertViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StepAdapter.AdvertViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.index = (TextView) convertView.findViewById(R.id.txtIndex);
            convertView.setTag(viewHolder);
        }

        String step = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        Typeface raleway = Typeface.createFromAsset(getContext().getAssets(), "font/ralewaylight.ttf");
        Typeface cookie = Typeface.createFromAsset(getContext().getAssets(), "font/cookie.ttf");
        viewHolder.index.setText("Étape "+(position+1));
        viewHolder.name.setText(step);
        viewHolder.name.setTypeface(raleway);
        viewHolder.index.setTypeface(cookie);
        viewHolder.index.setTextSize(23);

        return convertView;
    }

    public class AdvertViewHolder {
        public TextView index;
        public TextView name;
    }
}

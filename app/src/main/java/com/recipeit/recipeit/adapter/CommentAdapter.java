package com.recipeit.recipeit.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.recipeit.recipeit.R;
import com.recipeit.recipeit.models.Comment;
import com.recipeit.recipeit.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leach on 01/04/2018.
 */

public class CommentAdapter  extends ArrayAdapter<Comment>{

    public CommentAdapter(Context context, List<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_comment,parent, false);
        }

        AdvertViewHolder viewHolder = (AdvertViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new AdvertViewHolder();
            viewHolder.username = (TextView) convertView.findViewById(R.id.txtUsername);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.txtComment);
            viewHolder.note = (TextView) convertView.findViewById(R.id.txtNote);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.thumbnail_image_box);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Comment comment = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        Typeface ralewaylight = Typeface.createFromAsset(getContext().getAssets(), "font/ralewaylight.ttf");
        Typeface ralewayregular = Typeface.createFromAsset(getContext().getAssets(), "font/ralewayregular.ttf");
        Typeface cookie = Typeface.createFromAsset(getContext().getAssets(), "font/cookie.ttf");
        viewHolder.username.setText(comment.user.username);
        viewHolder.comment.setText(comment.comment);
        viewHolder.note.setText(comment.rating);
        Picasso.with(getContext()).load("https://storage.googleapis.com/pjs4-test.appspot.com/"+comment.user.avatar).into(viewHolder.avatar);
        viewHolder.username.setTypeface(cookie);
        viewHolder.comment.setTypeface(ralewaylight);
        viewHolder.note.setTypeface(ralewayregular);

        return convertView;
    }

    public class AdvertViewHolder {
        public TextView username;
        public TextView comment;
        public TextView note;
        public ImageView avatar;
    }

}

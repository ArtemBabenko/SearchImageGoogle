package dinamic_grid_google.dinamicgridgoogle.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import dinamic_grid_google.dinamicgridgoogle.R;
import dinamic_grid_google.dinamicgridgoogle.models.ImageResult;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
        super(context, android.R.layout.simple_list_item_1, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);
        }
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.load_image);
        ivImage.setImageResource(0);

        Picasso.with(getContext()).load(imageResult.getThumbUrl()).resize(600, 200).centerInside().into(ivImage);
        return convertView;
    }
}
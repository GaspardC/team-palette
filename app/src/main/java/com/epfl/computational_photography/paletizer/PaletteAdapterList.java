package com.epfl.computational_photography.paletizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.epfl.computational_photography.paletizer.palette_database.Palette;

import java.util.ArrayList;

/**
 * Created by Gasp on 15/04/16.
 */
public class PaletteAdapterList extends BaseAdapter implements Filterable {

    public Context context;
    public ArrayList<Palette> paletteArrayList;
    public ArrayList<Palette> orig;

    public PaletteAdapterList(Context context, ArrayList<Palette> paletteArrayList) {
        super();
        this.context = context;
        this.paletteArrayList = paletteArrayList;
    }


    public class PaletteHolder
    {
        TextView name;
        ImageView color1;
        ImageView color2;
        ImageView color3;
        ImageView color4;
        ImageView color5;

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Palette> results = new ArrayList<Palette>();
                if (orig == null)
                    orig = paletteArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Palette g : orig) {
                            if ((g.name).toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                paletteArrayList = (ArrayList<Palette>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return paletteArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return paletteArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PaletteHolder holder;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.row_search_palette, parent, false);
            holder=new PaletteHolder();
            holder.name=(TextView) convertView.findViewById(R.id.txtName);
            holder.color1=(ImageView) convertView.findViewById(R.id.color1);
            holder.color2=(ImageView) convertView.findViewById(R.id.color2);
            holder.color3=(ImageView) convertView.findViewById(R.id.color3);
            holder.color4=(ImageView) convertView.findViewById(R.id.color4);
            holder.color5=(ImageView) convertView.findViewById(R.id.color5);


            convertView.setTag(holder);
        }
        else
        {
            holder=(PaletteHolder) convertView.getTag();
        }

        holder.name.setText(paletteArrayList.get(position).name);
        holder.color1.setColorFilter(Color.parseColor(paletteArrayList.get(position).colors[0].toString()));
        holder.color2.setColorFilter(Color.parseColor(paletteArrayList.get(position).colors[1].toString()));
        holder.color3.setColorFilter(Color.parseColor(paletteArrayList.get(position).colors[2].toString()));
        holder.color4.setColorFilter(Color.parseColor(paletteArrayList.get(position).colors[3].toString()));
        holder.color5.setColorFilter(Color.parseColor(paletteArrayList.get(position).colors[4].toString()));

        return convertView;

    }

}
package com.epfl.computational_photography.paletizer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette_database.Color;
import com.epfl.computational_photography.paletizer.palette_database.Demo;
import com.epfl.computational_photography.paletizer.palette_database.Palette;

import java.util.ArrayList;


public class StyleActivity extends SlideMenuActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<Palette> paletteArrayList;
    private PaletteAdapterList paletteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
//        Demo demo = new Demo();
//        demo.main();


        mSearchView=(SearchView) findViewById(R.id.searchView1);
        mListView=(ListView) findViewById(R.id.listView1);

        paletteArrayList = new ArrayList<Palette>();
        paletteArrayList.add(new Palette("boat",new Color("f6ffff78"),new Color("f6995678"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f67f4488")));
        paletteArrayList.add(new Palette("elephant",new Color("f6f45f78"),new Color("f63456ee8"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f6ff9888")));
        paletteArrayList.add(new Palette("palette",new Color("f99ffff7ff"),new Color("f63dd678"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f67ff800")));
        paletteArrayList.add(new Palette("sea",new Color("f6fffa578"),new Color("f6345348"),new Color("0078ff78"),new Color("f67f22f8"),new Color("f6700888")));

        paletteAdapter =new PaletteAdapterList(StyleActivity.this, paletteArrayList);
        mListView.setAdapter(paletteAdapter);

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Palette pl = (Palette) mListView.getAdapter().getItem(position);
                View pal = findViewById(R.id.palSelected_ll);
                if (pal != null) {
                    TextView namePal = (TextView) pal.findViewById(R.id.palName);
                    ImageView col1 = (ImageView) pal.findViewById(R.id.color1Selected);
                    ImageView col2 = (ImageView) pal.findViewById(R.id.color2Selected);
                    ImageView col3 = (ImageView) pal.findViewById(R.id.color3Selected);
                    ImageView col4 = (ImageView) pal.findViewById(R.id.color4Selected);
                    ImageView col5 = (ImageView) pal.findViewById(R.id.color5Selected);


                    namePal.setText(pl.name);
                    col1.setBackgroundColor(android.graphics.Color.parseColor(pl.colors[0].toString()));
                    col2.setBackgroundColor(android.graphics.Color.parseColor(pl.colors[1].toString()));
                    col3.setBackgroundColor(android.graphics.Color.parseColor(pl.colors[2].toString()));
                    col4.setBackgroundColor(android.graphics.Color.parseColor(pl.colors[3].toString()));
                    col5.setBackgroundColor(android.graphics.Color.parseColor(pl.colors[4].toString()));


                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                }


            }
        });
    }


    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {


        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
//            paletteAdapter.getFilter().filter(null);
        } else {
            paletteAdapter.getFilter().filter(newText);

//            mListView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {

        return false;
    }
}

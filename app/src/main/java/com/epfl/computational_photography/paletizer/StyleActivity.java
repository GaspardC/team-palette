package com.epfl.computational_photography.paletizer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.epfl.computational_photography.paletizer.ColorPIcker.ColorPickerDialog;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette_database.Color;
import com.epfl.computational_photography.paletizer.palette_database.Palette;

import java.util.ArrayList;


public class StyleActivity extends SlideMenuActivity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<Palette> paletteArrayList;
    private PaletteAdapterList paletteAdapter;
    private Palette plClicked;
    private TextView namePalSel;
    private ImageView col1Sel,col2Sel,col3Sel,col4Sel,col5Sel;
    private View palSel;
    private ArrayList<ImageView> listOfImageViewOfPalSelected;


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

        setupListView();
        setupPaletteSelected();

    }

    private void setupPaletteSelected() {
         palSel = findViewById(R.id.palSelected_ll);

         namePalSel = (TextView) palSel.findViewById(R.id.palName);
         col1Sel = (ImageView) palSel.findViewById(R.id.color1Selected);
         col2Sel = (ImageView) palSel.findViewById(R.id.color2Selected);
         col3Sel = (ImageView) palSel.findViewById(R.id.color3Selected);
         col4Sel = (ImageView) palSel.findViewById(R.id.color4Selected);
         col5Sel = (ImageView) palSel.findViewById(R.id.color5Selected);


    }

    private void setupListView() {


        mListView.setAdapter(paletteAdapter);

        mListView.setTextFilterEnabled(true);
        setupSearchView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                plClicked = (Palette) mListView.getAdapter().getItem(position);
                if (palSel != null) {
                    setColorOfPalSelected(plClicked);




                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                }


            }
        });

    }

    private void setColorOfPalSelected(Palette plClicked) {

        listOfImageViewOfPalSelected = new ArrayList<ImageView>();
        namePalSel.setText(plClicked.name);
        col1Sel.setBackgroundColor(android.graphics.Color.parseColor(plClicked.colors[0].toString()));
        col2Sel.setBackgroundColor(android.graphics.Color.parseColor(plClicked.colors[1].toString()));
        col3Sel.setBackgroundColor(android.graphics.Color.parseColor(plClicked.colors[2].toString()));
        col4Sel.setBackgroundColor(android.graphics.Color.parseColor(plClicked.colors[3].toString()));
        col5Sel.setBackgroundColor(android.graphics.Color.parseColor(plClicked.colors[4].toString()));

        listOfImageViewOfPalSelected.add(col1Sel);
        listOfImageViewOfPalSelected.add(col2Sel);
        listOfImageViewOfPalSelected.add(col3Sel);
        listOfImageViewOfPalSelected.add(col4Sel);
        listOfImageViewOfPalSelected.add(col5Sel);


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

    public void modifyColorCliked(View view) {


        //het the color cliked
        final int pos = Integer.valueOf((String) view.getTag());
        String col = plClicked.colors[ pos].toString();
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, android.graphics.Color.parseColor(col), new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                // modifiy color in palette
                ImageView imSel = listOfImageViewOfPalSelected.get(pos);
                String colorHex = Integer.toHexString(color);
                colorHex = colorHex.replaceFirst("f","");
                colorHex = colorHex.replaceFirst("f","#");

                plClicked.colors[ pos] = new Color(colorHex);
                imSel.setBackgroundColor(color);
            }

        });
        colorPickerDialog.show();
    }
}

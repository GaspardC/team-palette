package com.epfl.computational_photography.paletizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.ColorPIcker.ColorPickerDialog;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette_database.Color;
import com.epfl.computational_photography.paletizer.palette_database.Demo;
import com.epfl.computational_photography.paletizer.palette_database.Palette;
import com.epfl.computational_photography.paletizer.palette_database.PaletteDB;

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
    private boolean changePhotoSource = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
//
//        PaletteDB pl = new PaletteDB(getApplicationContext());
//        Palette[] palettes = pl.getPalette("sadness");



        mSearchView=(SearchView) findViewById(R.id.searchView1);
        mListView=(ListView) findViewById(R.id.listView1);

        paletteArrayList = new ArrayList<Palette>();
//        paletteArrayList.add(new Palette("boat",new Color("f6ffff78"),new Color("f6995678"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f67f4488")));
//        paletteArrayList.add(new Palette("elephant",new Color("f6f45f78"),new Color("f63456ee8"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f6ff9888")));
//        paletteArrayList.add(new Palette("palette",new Color("f99ffff7ff"),new Color("f63dd678"),new Color("0078ff78"),new Color("f67ffff8"),new Color("f67ff800")));
//        paletteArrayList.add(new Palette("sea",new Color("f6fffa578"),new Color("f6345348"),new Color("0078ff78"),new Color("f67f22f8"),new Color("f6700888")));
//        paletteArrayList.add(new Palette("....or extract a palette from an image",new Color("ffffffff")));
//        for(int i = 0; i<palettes.length ; i++){
//            paletteArrayList.add(palettes[i]);
//        }
//        paletteAdapter =new PaletteAdapterList(StyleActivity.this, paletteArrayList);
//
//        setupListView();
//        setupPaletteSelected();
        setupSearchView();


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
                if(position == paletteArrayList.size() -1 ){
                    PhotoManager.choseFromLibrary(StyleActivity.this);

                }else{
                    if (palSel != null) {
                        setColorOfPalSelected(plClicked);




                        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                    }
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


        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        if (TextUtils.isEmpty(query)) {
            mListView.clearTextFilter();
//            paletteAdapter.getFilter().filter(null);
        } else {
            searchInDB(query);

//            paletteAdapter.getFilter().filter(query);
            mListView.clearTextFilter();

//            mListView.setFilterText(newText);
        }
        return true;
    }
    public void searchInDB(String query){

        PaletteDB pl = new PaletteDB(getApplicationContext());
        Palette[] palettes = pl.getPalette(query);
        paletteArrayList = new ArrayList<>();
        for(int i = 0; i<palettes.length ; i++){
            paletteArrayList.add(palettes[i]);
        }
        paletteArrayList.add(new Palette("....or extract a palette from an image",new Color("ffffffff")));

        paletteAdapter =new PaletteAdapterList(StyleActivity.this, paletteArrayList);

        setupListView();
        setupPaletteSelected();

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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked from Library
            if (requestCode == PaletizerApplication.RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Bitmap libraryBitmap = PhotoManager.getBitmapFromLibrary(this, requestCode, resultCode, data);
                if (libraryBitmap != null) {
                    if(changePhotoSource){
                        ImageView im = (ImageView) findViewById(R.id.imageStyleActivity) ;
                        if (im != null) {
                            im.setImageBitmap(libraryBitmap);
                        }

                    }
                    // Set the Image in ImageView after decoding the String

                }
            }

            // When an Image is taken by Camera
            if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

                Bitmap cameraBitmap = PhotoManager.getBitmapFromCamera(this, requestCode, resultCode);
                if (cameraBitmap != null) {
//                    bitmapIm.setImageBitmap(cameraBitmap);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }


    public void changePhotoSource(View view) {
        PhotoManager.choseFromLibrary(StyleActivity.this);
        changePhotoSource = true;
    }
}

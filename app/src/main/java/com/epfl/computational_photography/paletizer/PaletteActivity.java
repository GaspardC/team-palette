package com.epfl.computational_photography.paletizer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.epfl.computational_photography.paletizer.ColorPIcker.ColorPickerDialog;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;
import com.epfl.computational_photography.paletizer.palette.Extractor;
import com.epfl.computational_photography.paletizer.palette.Transferer;
import com.epfl.computational_photography.paletizer.palette_database.Color;
import com.epfl.computational_photography.paletizer.palette_database.FlickrInterface;
import com.epfl.computational_photography.paletizer.palette_database.Palette;
import com.epfl.computational_photography.paletizer.palette_database.PaletteDB;
import com.epfl.computational_photography.paletizer.palette_database.PaletteDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class PaletteActivity extends SlideMenuActivity implements SearchView.OnQueryTextListener {

    private static final String STRING_NORMAL_QUERY = "normal";
    private static final String STRING_FLICKR_QUERY = "flickr";


    static {
        System.loadLibrary("opencv_java3"); //load opencv_java lib
    }
    private SearchView mSearchView;
    private ListView mListView;
    private ArrayList<Palette> paletteArrayList;
    private PaletteAdapterList paletteAdapter;
    private Palette plClicked;
    private View palSel;
    private TextView namePalSel;
    private ArrayList<ImageView> listOfImageViewOfPalSelected;
    private boolean changePhotoSource = false;
    private boolean extractPaletteFromImage = false;
    private PaletteDB paletteDB;
    private Button buttonPlus;
    private String query = "";
    private Dialog dialogGenerate;
    private ImageView image;
    private int[] selectedColors;
    private Dialog dialogEditName;
    private String mode = STRING_NORMAL_QUERY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        buttonPlus = (Button) findViewById(R.id.buttonMorePalette);
        mSearchView=(SearchView) findViewById(R.id.searchView1);
        mListView=(ListView) findViewById(R.id.listView1);
        palSel = findViewById(R.id.palSelected_ll);
        namePalSel = (TextView) palSel.findViewById(R.id.palName);
        image = (ImageView) findViewById(R.id.imageStyleActivity);

        paletteArrayList = new ArrayList<Palette>();
        palSel = findViewById(R.id.palSelected_ll);

        setupSearchView();
        palSel.setVisibility(View.INVISIBLE);



    }


    private void setupPaletteSelected() {

         namePalSel = (TextView) palSel.findViewById(R.id.palName);
        paletteArrayList = new ArrayList<>();
        setupSearchView();
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
                    PhotoManager.choseFromLibrary(PaletteActivity.this);
                    extractPaletteFromImage = true;
                } else {
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
        selectedColors = new int[plClicked.colors.length];
        for(int i = 0; i < plClicked.colors.length; i++) {
            selectedColors[i] = plClicked.colors[i].toInt();
        }
        setSelectedPalette(selectedColors, plClicked.name);
    }

    private void setColorOfPalSelected(int[] rgbCenters) {
        setSelectedPalette(rgbCenters, "Extracted palette");
    }

    private void setSelectedPalette(int[] rgbCenters, String paletteName) {
        selectedColors = rgbCenters;
        listOfImageViewOfPalSelected = new ArrayList<>();
        namePalSel.setText(paletteName);
        LinearLayout colContainer = (LinearLayout) findViewById(R.id.colSelectedContainer);
        for(int i = 0; i < colContainer.getChildCount()-1; i++) {
            ((ImageView) colContainer.getChildAt(i)).setColorFilter(rgbCenters[i]);

//            colContainer.getChildAt(i).setBackgroundColor(rgbCenters[i]);
            listOfImageViewOfPalSelected.add((ImageView)colContainer.getChildAt(i));
        }
    }
    private Palette getSelectedPalette(){
        Palette p = new Palette(namePalSel.getText().toString(),selectedColors);
        return p;
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
//            searchInDB(query);
            if(!this.query.equals(query)){
                String [] q = new String[2];
                q[0] = query;
                q[1] = STRING_NORMAL_QUERY;
                new DownloadFilesTask().execute(q);
//            paletteAdapter.getFilter().filter(query);
                mListView.clearTextFilter();
                this.query = query;
            }


//            mListView.setFilterText(newText);
        }
        return true;
    }

    public void searchInDB(String[] query){

        paletteDB = new PaletteDB(getApplicationContext());
        Palette[] palettes = null;
        if(mode.equals(STRING_NORMAL_QUERY)){
            palettes = paletteDB.getPalette(query[0]);
        }
        else{
            // This code uses Flickr
        FlickrInterface flickr = new FlickrInterface();
        palettes = flickr.getPalettesFromQuery(query[0], 5);
        }
        setPaletteList(palettes);
    }


    private void setPaletteList(Palette[] palettes) {
        paletteArrayList = new ArrayList<>();
        for (Palette palette : palettes) {
            paletteArrayList.add(palette);
        }

        paletteArrayList.add(new Palette("..or add one from an image",new Color("ffffffff")));

        paletteAdapter = new PaletteAdapterList(PaletteActivity.this, paletteArrayList);
    }

    public void modifyColorCliked(View view) {
        //het the color clicked
        final int pos = Integer.valueOf((String) view.getTag());
        String col = plClicked.colors[ pos].toString();
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, android.graphics.Color.parseColor(col), new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                // modifiy color in palette
                String colorHex = colorIntToHex(color);
                ImageView imSel = listOfImageViewOfPalSelected.get(pos);

                plClicked.colors[ pos] = new Color(colorHex);
                imSel.setColorFilter(color);
//                imSel.setBackgroundColor(color);
            }



        });
        colorPickerDialog.show();
    }

    private String colorIntToHex(int color) {
        String colorHex = Integer.toHexString(color);
        colorHex = colorHex.replaceFirst("f","");
        return  colorHex.replaceFirst("f","#");
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
                        changePhotoSource = false;
                        image.setImageBitmap(libraryBitmap);
                    }
                    if(extractPaletteFromImage){
                        extractPaletteFromImage = false;
                        int[] rgbCenters = new Extractor().extract(libraryBitmap);
                        setColorOfPalSelected(rgbCenters);
                    }
                }
            }

            // When an Image is taken by Camera
            if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

                Bitmap cameraBitmap = PhotoManager.getBitmapFromCamera(this, requestCode, resultCode);
                if (cameraBitmap != null) {
                    image.setImageBitmap(cameraBitmap);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    public void changePhotoSource(View view) {
        PhotoManager.choseFromLibrary(PaletteActivity.this);
        changePhotoSource = true;
    }

    public void seeMorePalette(View view) {
        if(paletteDB != null){
            Palette[] pal = paletteDB.getMorePalette();
            if(pal!=null){
                setPaletteList(pal);
                setupListView();
            }
            buttonPlus.setVisibility(View.GONE);
        }

    }

    public void addPhotoByCam(View view) {
        PhotoManager.takePhoto(this);
    }

    public void goFullScreenPalette(View view) {
        ImageView im = (ImageView) findViewById(R.id.imageStyleActivity) ;
        Bitmap bitmap = ((BitmapDrawable)im.getDrawable()).getBitmap();
        Intent newActivity = new Intent(getApplicationContext(), FullScreenActivity.class);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);
        newActivity.putExtra("byteArray", bs.toByteArray());
        startActivity(newActivity);

    }

    public void saveThePalette(View view) {
        PaletteDatabase plDB = new PaletteDatabase(PaletteActivity.this);
        plDB.savePaletteInDatabase(getSelectedPalette());
        Toast.makeText(this,"palette saved",Toast.LENGTH_SHORT).show();
    }

    public void applyPalette(View view) {
        Bitmap source = Bitmap.createBitmap(selectedColors.length, 1, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < selectedColors.length; i++) {
            source.setPixel(i, 0, selectedColors[i]);
        }

        ImageView im = (ImageView) findViewById(R.id.imageStyleActivity);
        Bitmap target = ((BitmapDrawable) im.getDrawable()).getBitmap();

        Bitmap result = new Transferer().transfer(source, target);
        im.setImageBitmap(result);
    }

    public void editNamePalette(View view) {
        showCustomDialogEditName();
    }

    public void generateApaletteLib(View view) {
        palSel.setVisibility(View.VISIBLE);
        PhotoManager.choseFromLibrary(PaletteActivity.this);
        extractPaletteFromImage = true;
    }


    public void generateAPaletteCam(View view) {
        palSel.setVisibility(View.VISIBLE);
        PhotoManager.takePhoto(PaletteActivity.this);
        extractPaletteFromImage = true;
    }

    public void flickerMode(View view) {
        if(mode.equals(STRING_NORMAL_QUERY)){
            mode  = STRING_FLICKR_QUERY;
            ((ImageView) view).setColorFilter(getResources().getColor(R.color.colorPink));
        }
        else{
            mode  = STRING_NORMAL_QUERY;
            ((ImageView) view).setColorFilter(getResources().getColor(R.color.colorPrimary));
        }
        query = ""; //to reload
    }


    private class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... query) {
            searchInDB(query);

            return 10L;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            View loadingBalls = findViewById(R.id.loadingBalls);
            View ll = findViewById(R.id.listView1);
            if (loadingBalls != null && ll != null) {
                ll.setVisibility(View.VISIBLE);
                loadingBalls.setVisibility(View.GONE);
                buttonPlus.setVisibility(View.VISIBLE);
                palSel.setVisibility(View.VISIBLE);


            }
            setupListView();
            popupIfNoResultFound();
            setupPaletteSelected();
        }


        protected void onPreExecute() {
            com.github.glomadrian.loadingballs.BallView loadingBalls = (com.github.glomadrian.loadingballs.BallView) findViewById(R.id.loadingBalls);
            ListView ll = (ListView) findViewById(R.id.listView1);
            if (loadingBalls != null && ll != null) {
                ll.setVisibility(View.GONE);
                loadingBalls.setVisibility(View.VISIBLE);
                buttonPlus.setVisibility(View.GONE);
            }
        }
    }


    private void popupIfNoResultFound() {

            if(paletteArrayList.size() <3){
                showCustomDialogGenerate();
            }
    }

    protected void showCustomDialogGenerate() {

        if(dialogGenerate == null || !dialogGenerate.isShowing() ) {
            dialogGenerate = new Dialog(PaletteActivity.this,
                    android.R.style.Theme_Translucent);
            dialogGenerate.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialogGenerate.setCancelable(true);
            dialogGenerate.setContentView(R.layout.dialog_generate_new_palettes);


            dialogGenerate.show();
            ImageView lib = (ImageView) dialogGenerate.findViewById(R.id.popup_extract_lib);
            lib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogGenerate.dismiss();
                    PhotoManager.choseFromLibrary(PaletteActivity.this);
                    extractPaletteFromImage = true;
                }
            });
            ImageView cam = (ImageView) dialogGenerate.findViewById(R.id.popup_extract_cam);
            cam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogGenerate.dismiss();
                    PhotoManager.takePhoto(PaletteActivity.this);
                    extractPaletteFromImage = true;
                }
            });

            ImageView flickr = (ImageView) dialogGenerate.findViewById(R.id.flickr_button);
            flickr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String [] q = new String[2];
                    q[0] = query;
                    mode = STRING_FLICKR_QUERY;
                    new DownloadFilesTask().execute(q);
                    dialogGenerate.dismiss();
                    buttonPlus.setVisibility(View.INVISIBLE);
                }
            });
            Button noThks = (Button) dialogGenerate.findViewById(R.id.no_thanks_button);
            noThks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogGenerate.dismiss();
                }
            });
        }
    }
    protected void showCustomDialogEditName() {

        dialogEditName = new Dialog(PaletteActivity.this,
                android.R.style.Theme_Translucent);
        dialogEditName.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogEditName.setCancelable(true);
        dialogEditName.setContentView(R.layout.dialog_edit_name_palette);


        dialogEditName.show();
        ImageView lib = (ImageView) dialogEditName.findViewById(R.id.validate_edit_name);
        lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newName = (EditText) dialogEditName.findViewById(R.id.new_name_edit_palette);

                setSelectedPalette(selectedColors, newName.getText().toString());
                dialogEditName.hide();
            }
        });
        ImageView cam = (ImageView) dialogEditName.findViewById(R.id.no_thanks_button_edit_name);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGenerate.hide();
            }
        });


    }
}



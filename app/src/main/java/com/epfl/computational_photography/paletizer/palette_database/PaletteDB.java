package com.epfl.computational_photography.paletizer.palette_database;

import android.content.Context;

import com.epfl.computational_photography.paletizer.R;

/**
 * Created by Gasp on 21/04/16.
 */
public class PaletteDB {

    private final PaletteDatabase pdb;
    private  Context ctx = null;
    private Palette[] semSuggestions;
    private boolean paletteHasBeenLoaded = false;

    public PaletteDB(Context ctx){
        this.ctx = ctx;
        DatabaseConfig.prepareWordNet(ctx);

        pdb = new PaletteDatabase(ctx);
        pdb.addFromFile(DatabaseConfig.localPaletteCSV);

    }

    public Palette[] getPalette (String word){
        paletteHasBeenLoaded = true;
        SemanticSuggestor ss = new SemanticSuggestor(word, pdb);
        semSuggestions = ss.getSuggestions(20);

               Palette[] semSuggestionsBis = ss.getSuggestions();

        if (semSuggestionsBis == null){
            semSuggestionsBis = new Palette[1];
            semSuggestionsBis[0] = new Palette(ctx.getString(R.string.no_match),new Color("ffffffff"));
            System.out.println("Found no suggestions");
        }

        //MAX SIZE
        int maxSize = 5;
        Palette[] semSuggestionsTer = new Palette[maxSize];
        if(semSuggestionsBis.length>maxSize){
            System.arraycopy(semSuggestionsBis, 0, semSuggestionsTer, 0, maxSize);
            semSuggestionsBis = semSuggestionsTer;
        }

        return semSuggestionsBis;
    }

    public Palette[] getMorePalette (){
        if(paletteHasBeenLoaded){
            return semSuggestions;
        }else{
            semSuggestions = new Palette[1];
             semSuggestions[0] = new Palette("do not use getMorePalette before using getPalette(word)",new Color("ffffffff"));
            return semSuggestions;
        }
    }







    }

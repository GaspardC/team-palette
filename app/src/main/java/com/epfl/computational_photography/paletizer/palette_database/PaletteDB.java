package com.epfl.computational_photography.paletizer.palette_database;

import android.content.Context;

/**
 * Created by Gasp on 21/04/16.
 */
public class PaletteDB {

    private final PaletteDatabase pdb;
    private  Context ctx = null;
    private Palette[] colSuggestions;

    public PaletteDB(Context ctx){
        this.ctx = ctx;
        DatabaseConfig.prepareWordNet(ctx);

        pdb = new PaletteDatabase(ctx);
        pdb.addFromFile("kuler3_pos.csv");

    }

    public Palette[] getPalette (String word){
        SemanticSuggestor ss = new SemanticSuggestor(word, pdb);
        Palette[] semSuggestions = ss.getSuggestions();
        if (semSuggestions == null){
            semSuggestions = new Palette[1];
            semSuggestions[0] = new Palette("        sorry no match found for this keyword",new Color("ffffffff"));
            System.out.println("Found no suggestions");
        }
        else
        {

            for(Palette p : semSuggestions) {
                System.out.println(p);
                //System.out.println(p.getTmpScore());
            }

        }

        //MAX SIZE
        int maxSize = 7;
        if(semSuggestions.length>maxSize){
            Palette[] semSuggestionsBis = new Palette[maxSize];
            for(int i=0;i<maxSize;i++){
                semSuggestionsBis[i] = semSuggestions[i];
            }
            semSuggestions = semSuggestionsBis;
        }

        return semSuggestions;
    }


}

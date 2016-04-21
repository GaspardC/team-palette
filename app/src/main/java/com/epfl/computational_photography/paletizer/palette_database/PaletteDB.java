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
        Palette[] semSuggestions = ss.getSuggestions(5);
        if (semSuggestions == null)
            System.out.println("Found no suggestions");
        else
        {
            for(Palette p : semSuggestions) {
                System.out.println(p);
                //System.out.println(p.getTmpScore());
            }

        }
        System.out.println("\n");

        Palette pal = pdb.getDatabase().get(0);
        System.out.println("=== Suggestions from the palette database -> by RGB L2 distance:");
        System.out.println("For palette: " + pal);
        System.out.println("--------------");
        ColorspaceSuggestor cs = new ColorspaceSuggestor(pal, pdb);

        colSuggestions = cs.getSuggestions(5);
        return colSuggestions;
    }


}

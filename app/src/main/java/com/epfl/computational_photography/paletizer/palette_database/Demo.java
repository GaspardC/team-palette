package com.epfl.computational_photography.paletizer.palette_database;

import android.content.Context;

public class Demo {
	
	
    public static void run(Context ctx) {

        DatabaseConfig.prepareWordNet(ctx);

        PaletteDatabase pdb = new PaletteDatabase(ctx);
    	pdb.addFromFile(DatabaseConfig.localPaletteCSV);
    	//pdb.print();
    	//System.out.println("\n");
    	
    	
    	String word = "sadness";
    	System.out.println("=== Suggestions from the palette database -> by semantic relatedness:");
    	System.out.println("For the word: " + word);
    	System.out.println("--------------");
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
    	Palette[] colSuggestions = cs.getSuggestions(5);
    	for(Palette p : colSuggestions) {
    		System.out.println(p);
    	}

    }
}

package com.epfl.computational_photography.paletizer.palette_database;

public class Demo {
	
    public static void main() {
    	PaletteDatabase pdb = new PaletteDatabase();
    	pdb.addFromFile("src/main/res/data/kuler1.csv");
    	//pdb.print();
    	
    	String word = "boat";
    	System.out.println("Suggestions from the palette database -> by semantic relatedness:");
    	System.out.println("For the word: " + word);
    	System.out.println("--------------");
    	SemanticSuggestor ss = new SemanticSuggestor(word, pdb);    	
    	Palette[] semSuggestions = ss.getSuggestions(5);
    	for(Palette p : semSuggestions) {
    		System.out.println(p);
    	}
    	System.out.println("\n");
    	
    	Palette pal = pdb.getDatabase().get(0);
    	System.out.println("Suggestions from the palette database -> by RGB L2 distance:");
    	System.out.println("For palette: " + pal);
    	System.out.println("--------------");
    	ColorspaceSuggestor cs = new ColorspaceSuggestor(pal, pdb);    	
    	Palette[] colSuggestions = cs.getSuggestions(5);
    	for(Palette p : colSuggestions) {
    		System.out.println(p);
    	}

    }
}

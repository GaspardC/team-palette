package com.epfl.computational_photography.paletizer.palette_database;

import java.util.Collections;

public class ColorspaceSuggestor {
	
	PaletteDatabase pdb;
	Palette pRef;
	
	public ColorspaceSuggestor(Palette p, PaletteDatabase pdb0) {
		pdb = pdb0;
		pRef = p;
	}
	
    private double computeScore(Palette p) {
    	int sum = 0;
    	
    	for(int i = 0; i < p.colors.length; i++)
    	{
       	 	sum += pRef.colors[i].L2SquareDistanceTo(p.colors[i]);
    	}
    	
    	return sum;
    }
    
    private void sortDatabase() {
    	for(Palette p : pdb.getDatabase()) {
    		double score = computeScore(p);
    		p.setTmpScore(score);
    	}
    	    	
    	Collections.sort(pdb.getDatabase(), new PaletteComparator());
    }
    
    // Sorts the database every time (TODO: remove redundancy)
    public Palette[] getSuggestions(int size) {
    	Palette[] res = new Palette[size];
    	sortDatabase();
    	for(int i = 0; i < size; i++)
    	{
    		res[i] = pdb.getDatabase().get(i);
    	}
    	
    	return res;    	
    }

}

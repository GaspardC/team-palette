package com.epfl.computational_photography.paletizer.palette_database;

import java.util.Collections;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;


public class SemanticSuggestor {

    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator rc = new Resnik(db);
    //private static RelatednessCalculator rc = new WuPalmer(db);
    //private static RelatednessCalculator rc = new Path(db);

	private String refWord = "";
	private PaletteDatabase pdb;

    public SemanticSuggestor(String word, PaletteDatabase pdb0) {
    	refWord = word;
    	pdb = pdb0;
    }
    
    private double computeScore(String name) {
    	String[] parts = name.split(" ");
    	double max = -1;
    	
    	for(String part : parts)
    	{
       	 	double score = rc.calcRelatednessOfWords(part, refWord);
       	 	if (score > max)
       	 		max = score;
    	}
    	
    	return -max;
    }
    
    private void sortDatabase() {
    	for(Palette p : pdb.getDatabase()) {
    		double score = computeScore(p.name);
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

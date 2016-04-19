package com.epfl.computational_photography.paletizer.palette_database;

import java.util.Collections;

import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.Resnik;


public class SemanticSuggestor {

    public static RelatednessCalculator rc = new Resnik(DatabaseConfig.db);
	private static final String[] POS_TAGS = {"n", "a", "v", "r"};

	private PaletteDatabase pdb;
	private String refWord = "";
	private Concept[] refConcepts = new Concept[POS_TAGS.length];
	

    public SemanticSuggestor(String word, PaletteDatabase pdb0) {
    	refWord = word;
    	pdb = pdb0;
    	set_concepts();
    }
    
    private void set_concepts() {
    	for(int i = 0; i < POS_TAGS.length; i++)
    	{
    		refConcepts[i] = DatabaseConfig.db.getMostFrequentConcept(refWord, POS_TAGS[i]);
    	}
    }
        
    public double computeScore(Palette p) {
    	double max = -1;
    	for(Descriptor pdes : p.descriptors) {
       	 	double score = rc.calcRelatednessOfSynset(pdes.concept, refConcepts[Descriptor.POS_to_int(pdes.pos)]).getScore();
       	 	if (score > max)
       	 		max = score;
    	}
    	return -max;
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
    	
    	if (res[0].getTmpScore() > -0.01)
    		return null;
    	
    	return res;    	
    }
    
    public Palette[] getSuggestions() {
       	sortDatabase();
       	int count = 0;
    	while(pdb.getDatabase().get(count).getTmpScore() < -7.) count++;
    	
    	if (count == 0)
    		return null;
    	
    	Palette[] res = new Palette[count];
    	for(int i = 0; i < count; i++)
    	{
    		res[i] = pdb.getDatabase().get(i);
    	}
    	return res;
    }
}

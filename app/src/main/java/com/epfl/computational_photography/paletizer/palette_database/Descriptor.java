package com.epfl.computational_photography.paletizer.palette_database;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.data.Concept;

public class Descriptor {
	public String word;
	public POS pos;
	public Concept concept;
		
	public Descriptor(String word, String spos) {
		this.word = word;
		this.pos = string_to_POS(spos);
		this.concept = this.getConcept();
	}
	
    private Concept getConcept() {
    	String spos = POS_to_string(pos);
    	Concept c = DatabaseConfig.db.getMostFrequentConcept(word, spos);
    	
    	// dogs -> dog
    	if(c == null && spos.equals("n") && word.substring(word.length()-1).equals("s")) {
    		word = word.substring(0, word.length()-1);
    		c = DatabaseConfig.db.getMostFrequentConcept(word, "n");
    	}
    	// sandy (first name) -> sandy (adj)
    	//if(c == null && spos.equals("n")) {
    	//	pos = POS.a;
    	//	c = DatabaseConfig.db.getMostFrequentConcept(word, "a");
    	//}
    	// olive
    	if(c == null && spos.equals("a")) {
    		pos = POS.n;
    		c = DatabaseConfig.db.getMostFrequentConcept(word, "n");
    	}
    	// evening
    	if(c == null && spos.equals("v") && word.length() >= 3 && word.substring(word.length()-3).equals("ing")) {
    		pos = POS.n;
    		c = DatabaseConfig.db.getMostFrequentConcept(word, "n");
    	}
    	// amazing 
    	//if(c == null && spos.equals("v") && word.length() >= 3 && word.substring(word.length()-3).equals("ing")) {
    	//	pos = POS.a;
    	//	c = DatabaseConfig.db.getMostFrequentConcept(word, "a");
    	//}
    	// wine
    	if(c == null && spos.equals("v")) {
    		pos = POS.n;
    		c = DatabaseConfig.db.getMostFrequentConcept(word, "n");
    	}

    	return c;
    }

	public static POS string_to_POS(String spos) {
		if (spos.equals("n")) return POS.n;
		else if (spos.equals("v")) return POS.v;
		else if (spos.equals("a")) return POS.a;
		else if (spos.equals("r")) return POS.r;
		else {
			System.out.println("ERROR string to POS");
			return POS.n;
		}
	}
	
	public static String POS_to_string(POS pos) {
		if (pos.equals(POS.n)) return "n";
		else if (pos.equals(POS.v)) return "v";
		else if (pos.equals(POS.a)) return "a";
		else if (pos.equals(POS.r)) return "r";
		else return "NA";
	}
	
	public static int POS_to_int(POS pos) {
		if (pos.equals(POS.n)) return 0;
		else if (pos.equals(POS.a)) return 1;
		else if (pos.equals(POS.v)) return 2;
		else if (pos.equals(POS.r)) return 3;
		else return -1;

	}
	public String toString() {
		return word + '(' + POS_to_string(pos) + ')';
	}

	public String toCSVString() {
		return "," + word + "," + POS_to_string(pos);
	}
}

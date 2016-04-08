package com.epfl.computational_photography.paletizer.palette_database;

import java.util.Comparator;

// Requires setting a tmpScore for each Palette first
public class PaletteComparator implements Comparator<Palette>{

	public int compare(Palette p1, Palette p2) {
		double s1 = p1.getTmpScore();
		double s2 = p2.getTmpScore();
		return s1 < s2 ? -1 : s1 == s2 ? 0 : 1;
	}
	
}

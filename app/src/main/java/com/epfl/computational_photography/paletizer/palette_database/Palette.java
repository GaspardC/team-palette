package com.epfl.computational_photography.paletizer.palette_database;

/*
import org.apache.commons.lang3.text.WordUtils;
*/

public class Palette {
	
	public String name;
	public Color[] colors = new Color[5];
	
	// Used for sorting
	private double tmpScore = 0;

	public static String formatName(String s) {
		return s.toLowerCase();
	}

	public Palette(String name0, Color c0) {
		name = formatName(name0);
		colors[0] = c0;
		colors[1] = c0;
		colors[2] = c0;
		colors[3] = c0;
		colors[4] = c0;
	}

	public Palette(String name0, Color c0, Color c1, Color c2, Color c3, Color c4) {
		name = formatName(name0);
		colors[0] = c0;
		colors[1] = c1;
		colors[2] = c2;
		colors[3] = c3;
		colors[4] = c4;
	}
	
	public String toString() {
		return name + " : " + colors[0] + ", "
							+ colors[1] + ", "
							+ colors[2] + ", "
							+ colors[3] + ", "
							+ colors[4];
	}
	
	public void setTmpScore(double value) {
		tmpScore = value;
	}
	
	public double getTmpScore() {
		return tmpScore;
	}

	
}

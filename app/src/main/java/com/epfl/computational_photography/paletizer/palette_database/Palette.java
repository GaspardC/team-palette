package com.epfl.computational_photography.paletizer.palette_database;

import java.util.LinkedList;
import java.util.List;

public class Palette {
	
	public String name;
	public Color[] colors = new Color[5];
	
	public List<Descriptor> descriptors;
	
	// Used for sorting
	private double tmpScore = 0;

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
		descriptors = new LinkedList<Descriptor>();
	}
	
	public String toString() {
		String s1 =  name + " : " + colors[0] + ", "
							+ colors[1] + ", "
							+ colors[2] + ", "
							+ colors[3] + ", "
							+ colors[4];
				
		return s1;
	}
	
	public static String formatName(String s) {
		return s;
	}
	
	public void setTmpScore(double value) {
		tmpScore = value;
	}
	
	public double getTmpScore() {
		return tmpScore;
	}
	
	public void addDescriptor(Descriptor des) {
		this.descriptors.add(des);
	}

	
}

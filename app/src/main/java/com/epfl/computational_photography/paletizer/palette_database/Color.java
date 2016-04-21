package com.epfl.computational_photography.paletizer.palette_database;

public class Color {
	
	public int r, g, b;
	public Color(int r0, int g0, int b0) {
		r = r0;
		g = g0;
		b = b0;
	}
	
	public Color(String hex) {
		r = Integer.valueOf( hex.substring( 1, 3 ), 16);
        g = Integer.valueOf( hex.substring( 3, 5 ), 16);
        b = Integer.valueOf( hex.substring( 5, 7 ), 16);
	}
	
	public String toString() {
		return String.format("#%02X%02X%02X", r, g, b);
	}
	
	public double L2SquareDistanceTo(Color c) {
		return (r - c.r)*(r - c.r) + (g - c.g)*(g - c.g) + (b - c.b)*(b - c.b);
	}
}

package com.epfl.computational_photography.paletizer.palette_database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PaletteDatabase {
	
	private ArrayList<Palette> palettes;
    Context ctx;

    public PaletteDatabase(Context context) {
        ctx = context;
        palettes = new ArrayList<Palette>();
    }
	
	public void addPalette(Palette p) {
		palettes.add(p);
	}
	
	public ArrayList<Palette> getDatabase() {
		return palettes;
	}
	
	public void addFromFile(String inputFile) {
        System.out.println("Loading " + inputFile + " database...");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		try {
            br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(inputFile)));
			while ((line = br.readLine()) != null) {

				String[] data = line.split(cvsSplitBy);
				Palette p = new Palette(data[0], new Color(data[1]),
												 new Color(data[2]),
												 new Color(data[3]),
												 new Color(data[4]),
												 new Color(data[5]));
				
				int nb_descriptors = (data.length - 6) / 2;
				for(int i = 0; i < nb_descriptors; i++)
				{
					Descriptor des = new Descriptor(data[6 + 2*i], data[7 + 2*i]);
					if (des.concept != null)
						p.addDescriptor(des);
					//else System.out.println("null descriptor: " + des.word);
				}
				
				this.addPalette(p);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        System.out.println("Done loading database");
	}

	public void print() {
		for(Palette p : palettes) {
			System.out.println(p);
			//for(Descriptor des : p.descriptors) {
				//System.out.println("|   " + des.toString());
				//System.out.println(des.concept);
			//}
		}
	}

}

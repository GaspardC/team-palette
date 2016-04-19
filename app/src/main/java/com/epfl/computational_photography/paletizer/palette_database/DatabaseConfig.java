package com.epfl.computational_photography.paletizer.palette_database;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.cmu.lti.jawjaw.util.Configuration;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;

/**
 * Created by Etienne on 19/04/2016.
 */
public class DatabaseConfig {

    private final static String DBFilename = "wnjpn-0.9.db";
    public static File localWordNetDB = null;
    public static ILexicalDatabase db = new NictWordNet();

    public static void prepareWordNet(Context ctx) {
        System.out.println("Preparing Word Net...");

        if (localWordNetDB == null) {
            localWordNetDB = new File(ctx.getExternalFilesDir(null), DBFilename);
            Configuration.bd_path = localWordNetDB.getPath();
        }

        if (!localWordNetDB.exists()) {
            extractWordNetDBfromAssets(ctx);
        }

        bootWordNetSQL();

        System.out.println("Done Preparing Word Net");
    }

    public static void extractWordNetDBfromAssets(Context ctx) {
        System.out.println("Extracting DB from assets...");
        AssetManager assetManager = ctx.getAssets();
        InputStream in = null;
        OutputStream out = null;

        try {
            in =  assetManager.open(DBFilename);
            out = new FileOutputStream(localWordNetDB);
            copyFile(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }
        System.out.println("DB path : " + localWordNetDB.getPath());
        System.out.println("Done Extracting DB from assets");
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static void bootWordNetSQL() {
        //db.getAllConcepts("pencil", "n");
    }


}

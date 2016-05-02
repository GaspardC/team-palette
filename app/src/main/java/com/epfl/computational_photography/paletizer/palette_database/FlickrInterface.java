package com.epfl.computational_photography.paletizer.palette_database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.epfl.computational_photography.paletizer.palette.Extractor;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Etienne on 02/05/2016.
 */
public class FlickrInterface {

    private String apiKey = "94084a2e5ecc73d7e8f348a907ea5f77";
    private String sharedSecret = "a469926564f14847";
    private Flickr f = null;
    private PhotosInterface photosInterface = null;

    public FlickrInterface() {
        try {
            f = new Flickr(apiKey, sharedSecret, new REST());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        photosInterface = f.getPhotosInterface();
    }

    public Bitmap getBitmapFromPhoto(Photo photo) {
        InputStream rawImageStream;
        BufferedInputStream bis;
        Bitmap image = null;

        try {
            rawImageStream = photosInterface.getImageAsStream(photo, 10);
            bis = new BufferedInputStream(rawImageStream);
            image = BitmapFactory.decodeStream(bis);
            bis.close();
            rawImageStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        }

        return image;
    }

    public PhotoList searchPhotos(String word, int nbResults) {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setText(word);
        searchParameters.setSort(SearchParameters.RELEVANCE);

        PhotoList list = null;
        try {
            list = photosInterface.search(searchParameters, nbResults, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Palette[] getPalettesFromQuery(String word, int nbResults) {
        Palette[] res = new Palette[nbResults];
        PhotoList list = searchPhotos(word, nbResults);
        Extractor paletteExtractor = new Extractor();

        for(int i = 0; i < nbResults; i++) {
            Bitmap image = getBitmapFromPhoto(list.get(i));
            int[] colors = paletteExtractor.extract(image);
            res[i] = paletteFromIntArray(word, colors);
        }
        return res;
    }

    private Color colorFromInt(int c) {
        return new Color(android.graphics.Color.red(c), android.graphics.Color.green(c), android.graphics.Color.blue(c));
    }

    private Palette paletteFromIntArray(String word, int[] colors) {
        return new Palette(word, colorFromInt(colors[0]), colorFromInt(colors[1]), colorFromInt(colors[2]), colorFromInt(colors[3]), colorFromInt(colors[4]));
    }
}

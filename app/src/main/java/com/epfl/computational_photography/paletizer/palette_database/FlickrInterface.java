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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Etienne on 02/05/2016.
 */
public class FlickrInterface {

    private String apiKey = "94084a2e5ecc73d7e8f348a907ea5f77";
    private String sharedSecret = "a469926564f14847";
    private Flickr f = null;
    private PhotosInterface photosInterface = null;
    private REST transport;

    public FlickrInterface() {
        try {
            transport = new REST();
            f = new Flickr(apiKey, sharedSecret, transport);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        photosInterface = f.getPhotosInterface();
    }

    private InputStream getThumbnailAsStream(Photo photo) throws IOException {
        String urlStr = photo.getThumbnailUrl();

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        if(transport.isProxyAuth()) {
            conn.setRequestProperty("Proxy-Authorization", "Basic " + transport.getProxyCredentials());
        }

        conn.connect();
        return conn.getInputStream();
    }

    public Bitmap getBitmapFromPhoto(Photo photo) {
        InputStream rawImageStream;
        BufferedInputStream bis;
        Bitmap image = null;

        try {
            rawImageStream = getThumbnailAsStream(photo);
            //rawImageStream = photosInterface.getImageAsStream(photo, 10);
            bis = new BufferedInputStream(rawImageStream);
            image = BitmapFactory.decodeStream(bis);
            bis.close();
            rawImageStream.close();
        } catch (IOException e) {
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
        PhotoList list = searchPhotos(word, nbResults);
        if(list!=null){
            Palette[] res = new Palette[list.size()];

        Extractor paletteExtractor = new Extractor();

        for(int i = 0; i < list.size(); i++) {
            Bitmap image = getBitmapFromPhoto(list.get(i));
            int[] colors = paletteExtractor.extract(image);
            res[i] = paletteFromIntArray(word, colors);
        }
        return res;
        }
        Palette[] res  = new Palette[1];
        res[0] = new Palette("flickr connection lost",new Color("#ffffff"));
        return res;
    }

    private Color colorFromInt(int c) {
        return new Color(android.graphics.Color.red(c), android.graphics.Color.green(c), android.graphics.Color.blue(c));
    }

    private Palette paletteFromIntArray(String word, int[] colors) {
        return new Palette(word, colorFromInt(colors[0]), colorFromInt(colors[1]), colorFromInt(colors[2]), colorFromInt(colors[3]), colorFromInt(colors[4]));
    }
}

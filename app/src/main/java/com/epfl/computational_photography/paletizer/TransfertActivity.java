package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TransfertActivity extends SlideMenuActivity {

    Bitmap bitSource;
    Bitmap bitTarget;
    private Bitmap bitmapResult;

    private ImageView focusImage;
    boolean isSource;
    static {
        System.loadLibrary("opencv_java3"); //load opencv_java lib
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);
    }

    /**
     * @param view when user click on the button Library
     */
    public void transfertChoseFromLibrary(View view) {
        if ("source".equals(view.getTag().toString())){
            focusImage = (ImageView) findViewById(R.id.transfertSourceImage);
            isSource = true;
        }
        else{
            focusImage = (ImageView) findViewById(R.id.transfertTargetImage);
            isSource = false;
        }

        PhotoManager.choseFromLibrary(this);
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked from Library
            if (requestCode == PaletizerApplication.RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                Bitmap libraryBitmap = PhotoManager.getBitmapFromLibrary(this, requestCode, resultCode, data);
                if (libraryBitmap != null) {
                    // Set the Image in ImageView after decoding the String
                    focusImage.setImageBitmap(libraryBitmap);
                    if(isSource){
                        bitSource = libraryBitmap;
                    }
                    else{
                        bitTarget = libraryBitmap;
                    }
                }
            }

            // When an Image is taken by Camera
            if(requestCode == PaletizerApplication.TAKE_PHOTO_CODE && resultCode == RESULT_OK) {

                Bitmap cameraBitmap = PhotoManager.getBitmapFromCamera(this, requestCode, resultCode);
                if (cameraBitmap != null) {
                    focusImage.setImageBitmap(cameraBitmap);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public void computeTransfert(View view) {
        if(bitSource == null || bitTarget == null){
            Toast.makeText(this, "Chose 2 photos before", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        else{
            Mat sourceMat = new Mat();
            Utils.bitmapToMat(bitSource, sourceMat);
            Mat targetMat = new Mat();
            Utils.bitmapToMat(bitSource, targetMat);

            Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_RGB2Lab, 0);
            Imgproc.cvtColor(targetMat, targetMat, Imgproc.COLOR_RGB2Lab, 0);


            List listSoure = new ArrayList();
            List listTarget = new ArrayList();

            listSoure = getStdMean(sourceMat);
            listTarget = getStdMean(targetMat);

            List<Mat> llab = new ArrayList<Mat>(3);
            Core.split(targetMat, llab);
            Mat L = llab.get(0);
            Mat A = llab.get(1);
            Mat B = llab.get(2);

            Scalar tlm = Scalar.all((Double)listTarget.get(0));
            Scalar tam = Scalar.all((Double)listTarget.get(1));
            Scalar tbm = Scalar.all((Double)listTarget.get(2));
            Scalar slm = Scalar.all((Double)listSoure.get(0));
            Scalar sam = Scalar.all((Double)listSoure.get(1));
            Scalar sbm = Scalar.all((Double)listSoure.get(2));
            Scalar tls = Scalar.all((Double)listTarget.get(3));
            Scalar tas = Scalar.all((Double)listTarget.get(4));
            Scalar tbs = Scalar.all((Double)listTarget.get(5));
            Scalar sls = Scalar.all((Double)listSoure.get(3));
            Scalar sas = Scalar.all((Double)listSoure.get(4));
            Scalar sbs = Scalar.all((Double)listSoure.get(5));

            Double negtlm = - (Double) listTarget.get(0);
            Double negtam = - (Double) listTarget.get(1);
            Double negtbm = - (Double) listTarget.get(2);

            Core.add(L, (Scalar.all(negtlm)), L);
            Core.add(A, (Scalar.all(negtam)), A);
            Core.add(B, (Scalar.all(negtbm)), B);

            Scalar r = Scalar.all((Double)listTarget.get(3) / (Double)listSoure.get(3));
            Core.multiply(L, r, L);
            r = Scalar.all((Double)listTarget.get(4) / (Double)listSoure.get(4));
            Core.multiply(A,r , A);
            r = Scalar.all((Double)listTarget.get(5) / (Double)listSoure.get(5));
            Core.multiply(A, r, A);

            Core.add(L, slm, L);
            Core.add(A,sam,A);
            Core.add(B,sbm,B);

            Mat result = new Mat();
            ArrayList<Mat> list = new ArrayList<>();
            list.add(0,L);             list.add(1,A);            list.add(2, B);

            Core.merge(list, result);
            Imgproc.cvtColor(result, result, Imgproc.COLOR_Lab2RGB);

            // Find the correct scale value. It should be the power of 2.
            int size = 400;
            int scale = 1;
            while(result.cols() / scale / 2 >= size &&
                    result.rows() / scale / 2 >= size) {
                scale *= 2;
            }
//            bitmapResult = Bitmap.createBitmap(result.rows()/scale, result.cols()/scale, Bitmap.Config.RGB_565);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;



            bitmapResult = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(result, bitmapResult);
            ImageView imRes = (ImageView) (findViewById(R.id.transfertImageResult));
            imRes.setImageBitmap(bitmapResult);





           /*
                l -= tlm
                  a -= tam
                  b -= tbm

             l = (tls / sls) * l
            a = (tas / sas) * a
            b = (tbs / sbs) * b

            l += slm
            a += sam
            b += sbm*/




        }

    }

    private List getStdMean( Mat sourceMat) {
        List<Mat> llab = new ArrayList<Mat>(3);
        Core.split(sourceMat, llab);
        Mat L = llab.get(0);
        Mat A = llab.get(1);
        Mat B = llab.get(2);

        MatOfDouble meanL = new MatOfDouble();
        MatOfDouble stdL = new MatOfDouble();
        MatOfDouble meanA = new MatOfDouble();
        MatOfDouble stdA = new MatOfDouble();
        MatOfDouble meanB = new MatOfDouble();
        MatOfDouble stdB = new MatOfDouble();

        Core.meanStdDev(L, stdL, meanL);
        Core.meanStdDev(A, stdA, meanA);
        Core.meanStdDev(B,stdB,meanB);

        double dStdL = stdL.get(0,0)[0];
        double dStdA = stdA.get(0,0)[0];
        double dStdB = stdB.get(0,0)[0];
        double dMeanL = meanL.get(0,0)[0];
        double dMeanA = meanA.get(0,0)[0];
        double dMeanB = meanB.get(0,0)[0];




        List list = new ArrayList<>();
        list.add(0,dStdL);
        list.add(1,dStdA);
        list.add(2,dStdB);
        list.add(3,dMeanL);
        list.add(4,dMeanA);
        list.add(5,dMeanB);
        return list;
    }

    private int[] rgbToLab(int redValue, int greenValue, int blueValue) {
        Matrix a = new Matrix();
        Matrix b = new Matrix();
        Matrix c = new Matrix();

        float valuesA [] =
                {(float) (1/Math.sqrt(3)),0,0,
                0, (float) (1/ Math.sqrt(6)),0,
                0,0,(float) (1/Math.sqrt(2))};
        float valuesB [] =
                {1,1,1,
                 1,1,-2,
                 1,-1,0};

        float valuesC [] =
                {0.3811f,0.5783f,0,0402f,
                 0.1967f,0.7244f,0.0782f,
                 0.0241f,0.1288f,0.8444f};

        a.setValues(valuesA);
        b.setValues(valuesB);
        c.setValues(valuesC);



        return new int[0];
    }
}

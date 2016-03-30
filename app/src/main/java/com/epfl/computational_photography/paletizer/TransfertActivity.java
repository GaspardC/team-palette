package com.epfl.computational_photography.paletizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.epfl.computational_photography.paletizer.SlideMenu.SlideMenuActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
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
        focusImage = (ImageView) findViewById(R.id.transfertSourceImage);
        bitSource = ((BitmapDrawable)focusImage.getDrawable()).getBitmap();
        focusImage = (ImageView) findViewById(R.id.transfertTargetImage);
        bitTarget = ((BitmapDrawable)focusImage.getDrawable()).getBitmap();


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
            Utils.bitmapToMat(bitTarget, targetMat);

            Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_RGB2Lab, 0);
            Imgproc.cvtColor(targetMat, targetMat, Imgproc.COLOR_RGB2Lab, 0);
            sourceMat.convertTo(sourceMat, CvType.CV_32FC3);
            targetMat.convertTo(targetMat,CvType.CV_32FC3);

            List listSoure = getStdMean(sourceMat);
            List listTarget = getStdMean(targetMat);

            List<Mat> llab = new ArrayList<Mat>(3);
            Core.split(targetMat, llab);
            Mat L = llab.get(0);
            Mat A = llab.get(1);
            Mat B = llab.get(2);

            Double tlm = (Double) listTarget.get(0);
            Double tam = (Double)listTarget.get(1);
            Double tbm = (Double)listTarget.get(2);
            Double slm = (Double)listSoure.get(0);
            Double sam = (Double)listSoure.get(1);
            Double sbm = (Double)listSoure.get(2);

            Double tls = (Double)listTarget.get(3);
            Double tas = (Double)listTarget.get(4);
            Double tbs = (Double)listTarget.get(5);
            Double sls = (Double)listSoure.get(3);
            Double sas = (Double)listSoure.get(4);
            Double sbs = (Double)listSoure.get(5);

            Mat ones;
            ones = Mat.ones(L.size(), L.type());

            Core.add(L, multiplyScalar(ones, -tlm), L);
            Core.add(A, multiplyScalar(ones, -tam), A);
            Core.add(B, multiplyScalar(ones, -tbm), B);


            L = multiplyScalar(L, (tls / sls));
            A = multiplyScalar(A, (tas / sas));
            B = multiplyScalar(B, (tbs / sbs));

           /* L = ones.mul(L);
            A = ones.mul(A);
            B = ones.mul(B);*/

          /*  L = L.mul(multiplyScalar(ones, (tls / sls)));
            A = A.mul(multiplyScalar(ones, (tas / sas)));
            B = B.mul(multiplyScalar(ones, (tbs / sbs)));
*/


            /*Core.multiply(ones, multiplyScalar(ones, (tls / sls)), L);
            Core.multiply(ones, multiplyScalar(ones, (tas / sas)), A);
            Core.multiply(ones, multiplyScalar(ones, (tbs / sbs)), B);*/

            Core.add(L,multiplyScalar(ones,slm), L);
            Core.add(A, multiplyScalar(ones,sam), A);
            Core.add(B,multiplyScalar(ones,sbm), B);
            L = convert(L);
            A = convert(A);
            B = convert(B);

            Mat result = new Mat();
            ArrayList<Mat> list = new ArrayList<>();
            list.add(0,L);             list.add(1,A);            list.add(2, B);

            Core.merge(list, result);
            Imgproc.cvtColor(result, result, Imgproc.COLOR_Lab2RGB);

            // Find the correct scale value. It should be the power of 2.
            int size = 800;
            int scale = 1;
            while(result.cols() / scale / 2 >= size &&
                    result.rows() / scale / 2 >= size) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            bitmapResult = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(result, bitmapResult);
            ImageView imRes = (ImageView) (findViewById(R.id.transfertImageResult));
            imRes.setImageBitmap(bitmapResult);
        }

    }

    private Mat convert(Mat L) {
        Core.MinMaxLocResult minMax = Core.minMaxLoc(L);
        double max = minMax.maxVal;
        double min = minMax.minVal;
        Mat R = new Mat();
        Core.normalize(L,R,0,255,Core.NORM_MINMAX,CvType.CV_8U);
//        L.convertTo(R,CvType.CV_8UC1,255.0 / (max - min), - min * 255.0 / (max - min));
        return R;
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

    public Mat multiplyScalar(Mat m, double i)
    {
        /*Mat eyes = new Mat();
        eyes = Mat.eye(m.size(), m.type());

        Mat zeros = new Mat();
        zeros = Mat.zeros(m.size(), m.type());*/


        m  = m.mul(new Mat((int)m.size().height, (int)m.size().width, m.type(), new Scalar(i)));

        /*Core.multiply(eyes,new Scalar(i),eyes);
        Mat tmp = new Mat();
        Core.scaleAdd(m,i,zeros,tmp);
*/

        return m;

    }
}

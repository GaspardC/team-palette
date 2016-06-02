package com.epfl.computational_photography.paletizer.palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Transferer {

    public Bitmap transfer(Bitmap source, Bitmap target) {
        Mat sourceMat = new Mat();
        Utils.bitmapToMat(source, sourceMat);
        Mat targetMat = new Mat();
        Utils.bitmapToMat(target, targetMat);

        Imgproc.cvtColor(sourceMat, sourceMat, Imgproc.COLOR_RGB2Lab, 0);
        Imgproc.cvtColor(targetMat, targetMat, Imgproc.COLOR_RGB2Lab, 0);


        sourceMat.convertTo(sourceMat, CvType.CV_32FC3);
        targetMat.convertTo(targetMat,CvType.CV_32FC3);

        List listSoure = getStdMean(sourceMat);
        List listTarget = getStdMean(targetMat);

        List<Mat> llab = new ArrayList<>(3);
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


        Core.add(L,multiplyScalar(ones,slm), L);
        Core.add(A, multiplyScalar(ones,sam), A);
        Core.add(B,multiplyScalar(ones,sbm), B);

        Mat result = new Mat();
        ArrayList<Mat> list = new ArrayList<>();
        list.add(0,L);             list.add(1, A);            list.add(2, B);

        Core.merge(list, result);
        result.convertTo(result,CvType.CV_8UC3);

        Imgproc.cvtColor(result, result, Imgproc.COLOR_Lab2RGB);



        // Find the correct scale value. It should be the power of 2.
        int size = 400;
        int scale = 1;
        while(result.cols() / scale / 2 >= size &&
                result.rows() / scale / 2 >= size) {
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        Bitmap bitResult = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result, bitResult);
        return bitResult;
    }

    private List getStdMean( Mat sourceMat) {
        List<Mat> llab = new ArrayList<>(3);
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
        return m.mul(new Mat((int)m.size().height, (int)m.size().width, m.type(), new Scalar(i)));
    }
}

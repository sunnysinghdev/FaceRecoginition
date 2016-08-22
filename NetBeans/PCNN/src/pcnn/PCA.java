package pcnn;



import java.io.IOException;
import java.util.Vector;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import javax.imageio.ImageIO;



public class PCA {

    static File[] imageFiles;
    int[] averageImage;
    static int NumOfImages, w, h;
    static Vector TrainingImageVector;
    static DoubleMatrix2D eigenVectCovMatrix;
    static DoubleMatrix2D diffImageMatrix;
    static DoubleMatrix2D weightMatrix;
     
      static DoubleMatrix2D wtMatrix;
      ///--------edit start-------
     // static DoubleMatrix2D forTrainingMatrix;
      static DoubleMatrix2D forInputMatrix;
       //---end-------
    static Vector diffImageVector;
    static Vector EigenVector;
    static double twoNorm[];
    static BufferedImage[] ImgA;
    int smallestTwoNorm;
    BufferedImage inputImage;
    /*public static void main(String[] arg) throws Exception{

    new PCA("gallery","test/check2.png");
    }*/
     public int[] getInputImage(String testImage,int i) throws Exception{
        BufferedImage img= ImageIO.read(new File(testImage));
        return findDeviated(grabPixels(img));
    }
    public DoubleMatrix2D getInputImage(String testImage) throws Exception{
        BufferedImage img= ImageIO.read(new File(testImage));
        return ConvertArrayToMatrix(findDeviated(grabPixels(img)));
    }
    public DoubleMatrix2D getAllTrainingImages(String trainingDr)throws Exception{
        Vector ImageVector;
        DoubleMatrix2D forTrainingMatrix;
        int[] avgImage;
        File f = new File(trainingDr);
        File[] imageFilesAll=f.listFiles();
        imageFiles = f.listFiles();

        NumOfImages = imageFilesAll.length;

        System.out.println("Total number of training images : " + NumOfImages);
        diffImageVector = new Vector();
        ImageVector = loadImgsIntoVector();
        System.out.println("Calculating avg:");
        averageImage = findAverageImage(ImageVector);
        System.out.println("Calculating diff:");
        for (int a = 0; a < NumOfImages; a++) {
            diffImageVector.addElement(findDeviated((int[]) ImageVector.elementAt(a)));
        }
        System.out.println("Calculating cov:");
        //---------------edit-----
        forTrainingMatrix=ConvertVectorIntoMatrix(diffImageVector);
        Algebra Algebric_Functions = new Algebra();
        forTrainingMatrix= Algebric_Functions.transpose(forTrainingMatrix);
        return forTrainingMatrix;

    }

    PCA(){}

    public PCA(String trainingDr,String inputImg) throws Exception {

        File f = new File(trainingDr);
        imageFiles = f.listFiles();
       
        NumOfImages = imageFiles.length;

        System.out.println("Total number of training images : " + NumOfImages);
        diffImageVector = new Vector();
        TrainingImageVector = loadImgsIntoVector();
        System.out.println("Calculating avg:");
        averageImage = findAverageImage(TrainingImageVector);
        System.out.println("Calculating diff:");
        for (int a = 0; a < NumOfImages; a++) {
            diffImageVector.addElement(findDeviated((int[]) TrainingImageVector.elementAt(a)));
        }
        System.out.println("Calculating cov:");
    
        diffImageMatrix = ConvertVectorIntoMatrix(diffImageVector);
        //-----------end--------
        eigenVectCovMatrix = findEigenVector(diffImageMatrix);
        System.out.println("Calculating weight:");
        weightMatrix = project(diffImageMatrix);
        int match = FindMatch(inputImg);
        if (match == -1) {
            System.out.println("does not match with dataset images");
        } else {
            System.out.println("Matched with" + match);
            //new dispaly(inputImage, ImgA[match]);
        }
    }

    public Vector loadImgsIntoVector() throws IOException {

        Vector imageVec = new Vector();
        ImgA = new BufferedImage[NumOfImages];

        for (int i = 0; i < imageFiles.length; i++) {

            ImgA[i] = loadImageFromFile(imageFiles[i].getAbsolutePath());
            imageVec.addElement(grabPixels(ImgA[i]));

        }
        System.out.println("vec" + imageVec.size());
        System.out.println("" + imageFiles.length);
        /*        int[] tempa = (int[]) imageVec.get(0);
        for(int i=0;i<tempa.length;i++){
        System.out.println(tempa[i]+"\t");
        }*/
        return imageVec;
    }

    public BufferedImage loadImageFromFile(String fileName) throws IOException {
        File imgfile=new File(fileName);
        BufferedImage img = ImageIO.read(imgfile);
        return img;
    }

    public int[] grabPixels(BufferedImage pi) {


        Raster inputRaster;
        SampleModel sm;

        w = pi.getWidth();
        h = pi.getHeight();
        sm = pi.getSampleModel();
        inputRaster = pi.getData();
        System.out.println("Size of each image:");
        System.out.println(w + "x" + h);
        int[] pixels = new int[w * h];
        inputRaster.getPixels(0, 0, w, h, pixels);
        return pixels;

    }

    public int[] findAverageImage(Vector v) {
        if (v.size() == 0) {
            return null;
        }

        int[] temp = (int[]) v.elementAt(0);
        int[] avg = new int[temp.length];

        for (int i = 0; i < v.size(); i++) {
            temp = (int[]) v.elementAt(i);

            for (int j = 0; j < temp.length; j++) {
                avg[j] += temp[j];
            }

        }


        for (int i = 0; i < avg.length; i++) {
            avg[i] = avg[i] / v.size();
        }


        return avg;

    }

    public int[] findDeviated(int[] img) {
        int[] temp = new int[img.length];

        for (int x = 0; x < temp.length; x++) {
            temp[x] = img[x] - averageImage[x]; //deviated value of the pixel
        }

        return temp;

    }

    public DoubleMatrix2D ConvertVectorIntoMatrix(Vector vec) {

        int rows = ((int[]) vec.elementAt(0)).length;
        DoubleMatrix2D temp = new DenseDoubleMatrix2D(rows, vec.size());

        // Store i'th diff entry as i'th column of A
        for (int column = 0; column < vec.size(); column++) {
            int[] diffTmp = (int[]) vec.get(column);
            for (int row = 0; row < diffTmp.length; row++) {
                temp.set(row, column, (double) diffTmp[row]);
            }
        }

        return temp;

    }

    public DoubleMatrix2D findEigenVector(DoubleMatrix2D diffImageMatrix) {

        DoubleMatrix2D eigenVectReducedSystem;
        DoubleMatrix2D transposed;
        DoubleMatrix2D Atranspose_mult_A;
        EigenvalueDecomposition EigenObject;

        Algebra Algebric_Functions = new Algebra();
        transposed = Algebric_Functions.transpose(diffImageMatrix);
        Atranspose_mult_A = Algebric_Functions.mult(transposed, diffImageMatrix);


        /*        System.out.println("A^T*A=");
        System.out.println(Atranspose_mult_A);*/

        EigenObject = new EigenvalueDecomposition(Atranspose_mult_A);
        eigenVectReducedSystem = EigenObject.getV();

        System.out.println("Eigenvectors for A^T*A=");
        //System.out.println(eigenVectReducedSystem);

        /*System.out.println("Eigenvalues for A^T*A=");
        System.out.println(EigenObject.getD());*/


        eigenVectCovMatrix = Algebric_Functions.mult(diffImageMatrix, eigenVectReducedSystem);


        return eigenVectCovMatrix;

    }

    public DoubleMatrix2D project(DoubleMatrix2D differenceImageMatrix) {


        Algebra algebra = new Algebra();
        //  DoubleMatrix2D tr = algebra.transpose(eigenVectCovMatrix);
        System.out.println("Transposed");
        DoubleMatrix2D projection = algebra.mult(algebra.transpose(eigenVectCovMatrix), differenceImageMatrix);


        //  System.out.println("Weights=\n" + projection);

        return projection;


    }

    public DoubleMatrix2D ConvertArrayToMatrix(int[] temp) {

        DoubleMatrix2D tempMtrx;
        tempMtrx = new DenseDoubleMatrix2D(w * h, 1);

        for (int row = 0; row < temp.length; row++) {
            tempMtrx.set(row, 0, temp[row]);
        }
        forInputMatrix=tempMtrx;
        return tempMtrx;

    }

    public int FindMatch(String testImage) throws Exception {
        /*
         * Convert image into a matrix im
         * signature=projection(im-averageImageMatrix)
         * Find the distance between the signature and the signatures of the training images
         * for each column of weightMatrix
         * find the 2-norm of signature-column
         * end for
         * return the index of the closest image as well as the distance
         */
        DoubleMatrix2D testProjectionMatrix;
        DoubleMatrix2D temp;
        Algebra algebra = new Algebra();
        twoNorm = new double[NumOfImages];
        inputImage = ImageIO.read(new File(testImage));//JAI.create("fileload", testImage);
        int checkInput = check_input_Image(inputImage);
        if (checkInput == 1) {
            testProjectionMatrix = project(ConvertArrayToMatrix(findDeviated(grabPixels(inputImage))));

            twoNorm = find2Norm(weightMatrix, testProjectionMatrix);
            //System.out.println(" WeightRows : "+weightMatrix.rows()+" Cols :"+weightMatrix.columns());
            //double[][] trainWT=weightMatrix.toArray();
//-----Edited By SUNNY SINGH---for ANN purpose----------------------------------
           // forInputMatrix=testProjectionMatrix;
           // Algebra Algebric_Functions = new Algebra();
          //  forTrainingMatrix= Algebric_Functions.transpose(forTrainingMatrix);
//-----------end of editing----------------------------------------------------
            smallestTwoNorm = findNearestImage(twoNorm);
            /*get an image having index smallestTwoNorm from an ImgA array;
             * and pass it as a parameter to the display_JAI();
             */
            if (smallestTwoNorm == -1) {
                //System.out.println("Does not match with a dataset images");
                return -1;
            } else {
                return smallestTwoNorm;
            }
        } else {
            System.out.println("Input image dimensions does not match with the dataset images");
            return -1;
        }
    }

    public int check_input_Image(BufferedImage inputImage) {
        if (inputImage.getHeight() == 150 && inputImage.getWidth() == 125) {
            return 1;
        } else {
            return 0;
        }
    }

    public double[] find2Norm(DoubleMatrix2D setImgs, DoubleMatrix2D testImg) {

        DoubleMatrix2D temp2 = new DenseDoubleMatrix2D(setImgs.rows(), 1);

        DoubleMatrix2D temp23 = new DenseDoubleMatrix2D(setImgs.rows(), 1);

        Algebra algebra = new Algebra();
        double[] tempTwoNorm = new double[NumOfImages];

        System.out.println("Rows = " + setImgs.rows() + " Columns = " + setImgs.columns());
        for (int c = 0; c < setImgs.columns(); c++) {
            for (int r = 0; r < setImgs.rows(); r++) {
                temp2.set(r, 0, setImgs.get(r, c));

            }
//System.out.println(temp2.rows()+"temp2");
//System.out.println(testImg.rows()+"testImg");
            temp23 = minus(temp2, testImg);
            tempTwoNorm[c] = find2Norm(temp23);
            //tempTwoNorm[c] = algebra.norm2(cern.colt.matrix.doublealgo.Transform.minus(temp2, testImg));

            System.out.println(tempTwoNorm[c]);
            /* 1>do substraction(temp2,testImg);
             * 2>Find 2-norm of the resultant matrix;
             * 3>For 2-norm:-3.1>Find max.of the above matrix;
             *               3.2>Take square root of that max and retrn this;
             */
        }

        return tempTwoNorm;
    }

    public DoubleMatrix2D minus(DoubleMatrix2D temp2, DoubleMatrix2D testImg) {
        DoubleMatrix2D temp24 = new DenseDoubleMatrix2D(temp2.rows(), 1);
        double m = 0, n = 0;
        for (int i = 0; i < temp2.columns(); i++) {
            for (int j = 0; j < temp2.rows(); j++) {
                m = temp2.get(j, i);
                n = testImg.get(j, i);
                temp24.set(j, i, m - n);
            }
        }
        return temp24;
    }

    public double find2Norm(DoubleMatrix2D temp23) {
        double temp = temp23.get(0, 0);

        for (int i = 0; i < temp23.columns(); i++) {
            for (int j = 0; j < temp23.rows(); j++) {
                if (temp < temp23.get(j, i)) {
                    temp = temp23.get(j, i);
                }
            }
        }
        return Math.sqrt(temp);
    }

    public int findNearestImage(double[] temp) {
        double smallest = temp[0];
        int j = 0;

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] < smallest) {
                smallest = temp[i];
                j = i;
            }
        }
            System.out.println(j+"Smaller="+smallest);

        


        //System.out.println("Image -> " + j);
       /*if (smallest <= 2595.711526188179) {
        return j;
        } else {
        return -1;
        }*/
            return j;
    }
}

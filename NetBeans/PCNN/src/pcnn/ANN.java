
package pcnn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataPair;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
/**
 *
 * @author Vivek
 */
public class ANN {

Propagation trainM=null;
BasicNetwork network=null;
int out_layer=1;
    //private Object pair;


ANN(int in,int out,int hid){
    out_layer=out;
    getNN(in,out,hid);
}

    public ANN() {
        System.out.print(this);
        
    }


    public void getNN(int inputs,int outputs,int hidden){

        network=new BasicNetwork();
        network.addLayer(new BasicLayer(null,true,inputs));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,hidden));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,outputs));
        network.getStructure().finalizeStructure();
        network.reset();
    }


    public int train(double[][] input,double[][] ideal,double LR,double M) throws Exception{
        double learnRate=LR;
        double momentum=M;

        NeuralDataSet Tset=new BasicNeuralDataSet(input, ideal);

        trainM=new Backpropagation(network, Tset, learnRate, momentum);
        int epoch=1;
        double prev=1.0,next=0.0;

        do{
            if(prev==next)
              //  break;
            trainM.iteration();
            System.out.println("Epoch#<"+epoch+"> Error:"+trainM.getError());
            epoch++;
            prev=trainM.getError();

             trainM.iteration();
            System.out.println("Epoch#<"+epoch+"> Error:"+trainM.getError());
            epoch++;
            next=trainM.getError();


        }while(trainM.getError()>0.01||epoch<200);

        //----Write Trained Network into File Train.ann------------------
        FileOutputStream fout=new FileOutputStream(new File("train.ann"));
        ObjectOutputStream out=new ObjectOutputStream(fout);
        out.writeObject(network);
        return 1;
    }
    public void test(double[][] in,double[][] idealput){

        NeuralDataSet set=new BasicNeuralDataSet(in, idealput);
        MLData out1=null;
        for( MLDataPair pair: set)
        {
           out1=new BasicMLData(network.compute(pair.getInput()));

           for(int i=0;i<out1.getData().length;i++)
               System.out.print("  "+ out1.getData(i));
           System.out.println("\n");
        }
    }

    public static int test(double[] in,BasicNetwork net){
        double[] out=new double[in.length];
        net.compute(in, out);
            for(int i=0;i<in.length;i++)
                System.out.print(" "+out[i]);

        return index(out);
     }

    public static int index(double[] value){

        int index_no=0;
        double max=value[0];
        for(int i=1;i<value.length;i++)
            if(max<value[i]){
                max=value[i];
                index_no=i;
            }
        System.out.println("\nMaximu="+max);
        return index_no+1;
    }
    public double[][] getIdealOutput(int num){
        double[][] ideal=new double[num][num];
        for(int i=0;i<num;i++)
        {
            for(int j=0;j<num;j++)
                ideal[i][j]=0;
            ideal[i][i]=1;
        }
        return ideal;
    }
   
    public static int TrainNetwork(String dirPath,int noOfImages) throws Exception{
        int num=125*150;//----Fixed Image Dimension-----------------
        PCA pca=new PCA();
        double LR=0.7;
        double M=0.3;
        ANN mc=new ANN(num,noOfImages,200);//-----may change

        return
                mc.train(pca.getAllTrainingImages(dirPath).toArray(),mc.getIdealOutput(noOfImages),LR,M);
       
    }
    public int Perform() throws Exception {

        //ANN mc=new ANN(125*150,17,200);
        PCA pca=new PCA("gallery","normal/check.png");
        int num=125*150;//PCA.NumOfImages;---Dimension of Image-----

        File f=new File("train.ann");
        if(f.exists()==true)
        {
            FileInputStream fin=new FileInputStream(f);
            ObjectInputStream in=new ObjectInputStream(fin);
            BasicNetwork net=(BasicNetwork) in.readObject();

            double[] input=new double[num];
            double[][] inputImage=pca.getInputImage("normal/check.png").toArray();//Average is static in PCA

            for(int i=0;i<num;i++)
                input[i]=inputImage[i][0];

            num=ANN.test(input, net);
            System.out.println("\nOutput Matched="+num);

            return num;
        }
        else
            return -1;
        // <editor-fold defaultstate="collapsed" desc="Training Network Code">
/*else
        {
        mc.train(PCA.forTrainingMatrix.toArray(),mc.getIdealOutput(num));
        double[][] inputImage=PCA.forInputMatrix.toArray();
        double[] input=new double[num];
        for(int i=0;i<num;i++)
        input[i]=inputImage[i][0];
        num=mc.test(input, mc.network);
        System.out.println("\nOutput Matched="+num);
        return num;
        }*/// </editor-fold>
    }
}




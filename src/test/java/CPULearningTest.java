import Data.ComplexDataset;
import Data.Helper;
import Data.Pairs.RealPair;
import Data.RealDataset;
import Network.Calculation.ICalculationStrategy;
import Network.Calculation.SingleThreadCalculation;
import Network.FeedForwardNet;
import Network.Layer.Neuron.ActivationFunction;
import Network.Learning.CPULearning;
import Network.Learning.Propagation.SingleThreadPropagation;
import Network.SeviException;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.linear.FieldVector;
import org.junit.Test;

/**
 * Created by Severin on 25.04.2015.
 */
public class CPULearningTest {

    private ComplexDataset getAndDataset() throws SeviException{
        RealDataset set = new RealDataset();
        double[] in1 = {0.1,0.1,0.9};
        double[] out1 = {0.1};
        double[] in2 = {0.9,0.1,0.9};
        double[] out2 = {0.1};
        double[] in3 = {0.1, 0.9,0.9};
        double[] out3 = {0.1};
        double[] in4 = {0.9,0.9,0.9};
        double[] out4 = {0.9};
        set.add(new RealPair(in1,out1));
        set.add(new RealPair(in2,out2));
        set.add(new RealPair(in3,out3));
        set.add(new RealPair(in4,out4));
        return set.transform();
    }
    private ComplexDataset getXorDataset() throws SeviException{
        RealDataset set = new RealDataset();
        double[] in1 = {0.1,0.1,0.9};
        double[] out1 = {0.1};
        double[] in2 = {0.9,0.1,0.9};
        double[] out2 = {0.9};
        double[] in3 = {0.1, 0.9,0.9};
        double[] out3 = {0.9};
        double[] in4 = {0.9,0.9,0.9};
        double[] out4 = {0.1};
        set.add(new RealPair(in1,out1));
        set.add(new RealPair(in2,out2));
        set.add(new RealPair(in3,out3));
        set.add(new RealPair(in4,out4));
        return set.transform();
    }
    @Test
    public void iterationTest() throws Exception {
        ComplexDataset andSet = getAndDataset();
        FeedForwardNet net = new FeedForwardNet();
        net.addLayer(3, ActivationFunction.Sigmoid);
        net.addLayer(1, ActivationFunction.Sigmoid);

        SingleThreadCalculation calculation = new SingleThreadCalculation(net);
        SingleThreadPropagation propagation = new SingleThreadPropagation(net);
        propagation.setEta(0.6);

        CPULearning learning = new CPULearning(net,calculation,propagation);
        learning.setDataset(andSet);

        for(int i = 0; i < 10; i++) {
            learning.iteration(10000);
            //LayerTest.out(net.getLayers().get(0).getMatrixtoNextLayer());
             System.out.println("Error: " + learning.getError());
        }

        double[] testinput = {0,0,1};
        System.out.println("0,0:");
        testen(testinput, calculation);

        double[] testinput1 = {0,1,1};
        System.out.println("0,1:");
        testen(testinput1, calculation);

        double[] testinput2 = {1,0,1};
        System.out.println("1,0:");
        testen(testinput2,calculation);

        double[] testinput3= {1,1,1};
        System.out.println("1,1:");
        testen(testinput3,calculation);
    }
    private void testen(double[] testinput, ICalculationStrategy calculation) throws SeviException{

        FieldVector<Complex> output = calculation.calculate(Helper.transform(testinput));
        double[] out = Helper.transform(output);

        for(int i = 0; i < out.length; i++) {
            System.out.print(out[i]);
        }
        System.out.println();
    }
}

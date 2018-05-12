///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package NeuralNetwork;
//
///**
// *
// * @author DAN
// */
//public class ElmanTest {
//    int INPUT_NEURON;
//    int HIDDEN_NEURON;
//    int CONTEXT_NEURON;
//    int OUTPUT_NEURON;
//    int TIMESTEP;
//    
//    double[][][] bobotTIH;
//    double[][][] bobotTCH;
//    double[][] bobotBiasTIH;
//    double[][] bobotHO;
//    double[] bobotBiasHO;
//    
//    int JUMLAH_SAMPLE_TESTING;
//    public ElmanTest(double[][][] bobotTIH, 
//            double[][][] bobotTCH,
//            double[][] bobotBiasTIH,
//            double[][] bobotHO,
//            double[] bobotBiasHO,
//            int JUMLAH_SAMPLE_TESTING, 
//            double[][][] sampleTes, 
//            double[][] sampleDataTargetTes){
//        //set variable individu
//        this.TIMESTEP = bobotTIH.length;
//        this.INPUT_NEURON = bobotTIH[0].length;
//        this.HIDDEN_NEURON = bobotTIH[0][0].length;
//        this.CONTEXT_NEURON = bobotTIH[0][0].length;
//        this.OUTPUT_NEURON = bobotHO[0].length;
//        
//        //set bobot
//        this.bobotTIH = bobotTIH;
//        this.bobotTCH = bobotTCH;
//        this.bobotBiasTIH = bobotBiasTIH;
//        this.bobotHO = bobotHO;
//        this.bobotBiasHO = bobotBiasHO;
//        
//        this.JUMLAH_SAMPLE_TESTING = JUMLAH_SAMPLE_TESTING;
//        
//        this.dataInput = new double[bobotTIH.length][bobotTIH[0].length];
//        this.contextTC = new double[bobotTIH.length][bobotTIH[0][0].length]; //TC = Timestep, Context
//        this.aktifHiddenT = new double[bobotTIH.length][bobotTIH[0][0].length];
//    }
//    
//    public void feedForward(){
//            for (int t = 0; t < TIMESTEP; t++) {
////            System.out.println("TIMESTEP "+t);
//            for (int h = 0; h < HIDDEN_NEURON; h++) {
////                System.out.println(t+" \t HIDDEN "+h);
//
//
//                //input->hidden
//                double sumInput = 0.0;
//                for (int i = 0; i < INPUT_NEURON; i++) {
//                    sumInput = sumInput + (dataInput[t][i] * bobotTIH[t][i][h]);
//                }
//
//                //context->hidden
//                double sumContext = 0.0;
//                for (int c = 0; c < HIDDEN_NEURON; c++) {
//                    sumContext = sumContext + (contextTC[t][c] * bobotTCH[t][c][h]);
//                }
//
//                if (t == TIMESTEP-1){
//                    //hitung hidden terakhir
//                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
//                }else{
//                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
//                    contextTC[t+1][h] = aktifHiddenT[t][h];
////                    System.out.println(aktifHiddenT[t][h] +"  |  "+ contextTC[t+1][h]);
//                }
//            }
//
//        }
//
//        //hitung output
//        for (int o = 0; o < OUTPUT_NEURON; o++) {
//            double sum = 0.0;
//            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                sum = sum + (aktifHiddenT[TIMESTEP-1][h] * bobotHO[h][o]);
//            }
//            netOutput[o] = sum + bobotBiasHO[o];
//        }
//
//        //menghitung softmax
//        System.out.print("Hasil Output : ");
//        for (int o = 0; o < OUTPUT_NEURON; o++) {
//            aktifOutput[o] = softmax(netOutput[o]);
////            commented for saving memory
//            System.out.print(aktifOutput[o]+" \t"); 
//        }
//        System.out.println();
//
//        
//    }
//}

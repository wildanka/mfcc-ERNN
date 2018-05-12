package NeuralNetwork;

import Interface.HalamanERNN;
import java.text.DecimalFormat;

/**
 * Created by DAN on 9/17/2017.
 */
public class ElmanSRN {
   
    int confussionMatrixTrain[][] = new int[10][10];
    int confussionMatrixTest[][] = new int[10][10];

    public int[][] getConfussionMatrixTrain() {
        return confussionMatrixTrain;
    }

    public int[][] getConfussionMatrixTest() {
        return confussionMatrixTest;
    }
     
    //untuk kepentingan interface saja
    int dataPelatihanDikenali;
    int dataPelatihanJumlah;
    int dataTestingDikenali;
    int dataTestingJumlah;
    
    private int TIMESTEP;
    private int INPUT_NEURON = 12;
    private int HIDDEN_NEURON;
    private int OUTPUT_NEURON = 10;
    private double LEARNING_RATE;
    private int JUMLAH_EPOCH;
    private int JUMLAH_SAMPLE_TRAINING;
    private int JUMLAH_SAMPLE_TESTING;

    //private double[][] dataTraining = {{0,0,1}, {1,0,1}, {0,1,0}, {0,1,0}};
    private double[][] dataInput;
    private double[] dataTarget = new double[OUTPUT_NEURON];

    private double[][][] sampleDataTraining;
    private double[][] sampleDataTarget;
    private double[][][] sampleTes;
    private double[][] sampleDataTargetTes;
    
    private double[][][] bobotTIH;
    private double[][][] bobotTCH;
    private double[][] bobotHO;
    private double[][] bobotBiasTIH;
    private double[] bobotBiasHO = new double[OUTPUT_NEURON];

    private double[][] aktifHiddenT;
    private double[][] contextTC; //TC = Timestep, Context
    private double[] netOutput = new double[OUTPUT_NEURON];
    private double[] aktifOutput = new double[OUTPUT_NEURON];
    private double[] oGrad = new double[OUTPUT_NEURON];

    private double[][] thGrad;

    public double[][][] getBobotTIH() {
        return bobotTIH;
    }

    public double[][][] getBobotTCH() {
        return bobotTCH;
    }

    public double[][] getBobotHO() {
        return bobotHO;
    }

    public double[][] getBobotBiasTIH() {
        return bobotBiasTIH;
    }

    public double[] getBobotBiasHO() {
        return bobotBiasHO;
    }

    public int getDataPelatihanDikenali() {
        return dataPelatihanDikenali;
    }

    public int getDataPelatihanJumlah() {
        return dataPelatihanJumlah;
    }

    public int getDataTestingDikenali() {
        return dataTestingDikenali;
    }

    public int getDataTestingJumlah() {
        return dataTestingJumlah;
    }

    
    
    //CONSTRUCTOR
    //Constructor untuk melakukan pelatihan

    /**
     *
     * @param HIDDEN_NEURON - jumlah Hidden Neuron pada arsitektur ERNN
     * @param TIMESTEP - banyaknya jumlah Timestep
     * @param LEARNING_RATE - alpha
     * @param JUMLAH_EPOCH - jumlah epoch
     * @param JUMLAH_SAMPLE_TRAINING - jumlah sample data training
     * @param sampleDataTraining - array 3D dataTraining
     * @param sampleDataTargetTraining - array 2D untuk menentukan target softmax (0.25, 0.833, dst.)
     * @param JUMLAH_SAMPLE_TESTING - jumlah sample data testing
     * @param sampleTes - array 3D yang akan diuji (dataTesting)
     * @param sampleDataTargetTes - array 2D untuk menentukan target softmax data testing seharusnya
     */
    public ElmanSRN(int HIDDEN_NEURON, int TIMESTEP, double LEARNING_RATE, int JUMLAH_EPOCH, 
            int JUMLAH_SAMPLE_TRAINING, 
            double[][][] sampleDataTraining, 
            double[][] sampleDataTargetTraining, 
            int JUMLAH_SAMPLE_TESTING, 
            double[][][] sampleTes, 
            double[][] sampleDataTargetTes){
        this.sampleDataTraining = sampleDataTraining;
        this.sampleDataTarget = sampleDataTargetTraining;
        this.sampleDataTargetTes = sampleDataTargetTes;
        this.sampleTes = sampleTes;
        
        this.TIMESTEP = TIMESTEP;
        this.LEARNING_RATE = LEARNING_RATE;
        this.JUMLAH_EPOCH = JUMLAH_EPOCH;
        this.JUMLAH_SAMPLE_TRAINING = JUMLAH_SAMPLE_TRAINING;
        this.JUMLAH_SAMPLE_TESTING = JUMLAH_SAMPLE_TESTING;
        /*
        karena @param TIMESTEP digunakan untuk menentukan ukuran pada beberapa array (bobotTIH,bobotTCH, dst. ) 
        maka array tersebut harus dideklarasikan langsung dari CONSTRUCTOR
        */
        this.bobotTIH = new double[TIMESTEP][INPUT_NEURON][HIDDEN_NEURON];
        this.bobotTCH = new double[TIMESTEP][HIDDEN_NEURON][HIDDEN_NEURON];
        this.bobotBiasTIH = new double[TIMESTEP][HIDDEN_NEURON];
        this.dataInput = new double[TIMESTEP][INPUT_NEURON];
        this.thGrad = new double[TIMESTEP][HIDDEN_NEURON];
        this.contextTC = new double[TIMESTEP][HIDDEN_NEURON]; //TC = Timestep, Context
        this.aktifHiddenT = new double[TIMESTEP][HIDDEN_NEURON];
        
        this.HIDDEN_NEURON = HIDDEN_NEURON;
        this.bobotHO = new double[HIDDEN_NEURON][OUTPUT_NEURON];
    }
    
    /**
     * Constructor untuk melakukan pengujian
     * @param bobotTIH bobot TIMESTEP-INPUT-HIDDEN
     * @param bobotTCH bobot TIMESTEP-CONTEXT-HIDDEN
     * @param bobotBiasTIH bobot BIAS INPUT - HIDDEN (BIAS HIDDEN/CONTEXT)
     * @param bobotHO bobot HIDDEN-OUTPUT
     * @param bobotBiasHO bobot BIAS HIDDEN - OUTPUT (BIAS OUTPUT)
     * @param JUMLAH_SAMPLE_TESTING Jumlah data testing
     * @param sampleTes data testing
     * @param sampleDataTargetTes target testing yang seharusnya
     */
    public ElmanSRN(
            double[][][] bobotTIH, 
            double[][][] bobotTCH,
            double[][] bobotBiasTIH,
            double[][] bobotHO,
            double[] bobotBiasHO,
            int JUMLAH_SAMPLE_TESTING, 
            double[][][] sampleTes, 
            double[][] sampleDataTargetTes)
    {
        //this.sampleDataTesTarget = sampleDataTesTarget;        
        this.bobotTIH = bobotTIH;
        this.bobotTCH = bobotTCH;
        this.bobotBiasTIH = bobotBiasTIH;
        this.bobotHO = bobotHO; //2D
        this.bobotBiasHO = bobotBiasHO; //1D
        this.TIMESTEP = bobotTIH.length;
        this.INPUT_NEURON = bobotTIH[0].length;
        this.sampleTes = sampleTes;
        this.sampleDataTargetTes = sampleDataTargetTes;
        /*
        TIMESTEP = bobotTIH.length\
        INPUT_NEURON = bobotTIH[0].length
        HIDDEN_NEURON = bobotTIH[0][0].length
        */
        this.JUMLAH_SAMPLE_TESTING = sampleTes.length;
        this.dataInput = new double[bobotTIH.length][bobotTIH[0].length];
        this.contextTC = new double[bobotTIH.length][bobotTIH[0][0].length]; //TC = Timestep, Context
        this.aktifHiddenT = new double[bobotTIH.length][bobotTIH[0][0].length];
    }    

    private void generateBobot() {
        DecimalFormat formatTiga = new DecimalFormat("#.###");
        System.out.println(bobotTIH.length+" = "+bobotTIH[0].length+" = "+bobotTIH[0][0].length);
        
        for (int t = 0; t < TIMESTEP; t++) {
            double binput = 0.09;
            double bcontext = 0.04;
            double bbias = 0.15;

            for (int i = 0; i < INPUT_NEURON; i++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    binput = binput + 0.001;
                    bobotTIH[t][i][h] = binput;
//                    System.out.println(t+"=="+i+"=="+h);
                }
                //System.out.println();
            }

            //bobot bias input-hidden (untuk setiap timestep)
            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                bobotBiasTIH[t][h] = bbias + (h * 0.001);
//                System.out.println(t+"===="+h);

            }

            for (int c = 0; c < HIDDEN_NEURON; c++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    bcontext = bcontext + 0.001;
                    //bobotTCH[t][c][h] = bcontext;
                    
//                    System.out.println(t+"=="+c+"=="+h);
                }
            }
        }

        //bobot hidden->output
        double awal = 0.09;
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                awal = awal + 0.001;
                bobotHO[h][o] = awal;
            }
        }

        //bobot bias hidden -> output (untuk setiap timestep)
        double bbias = 0.15;
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            bobotBiasHO[o] = bbias + (o * 0.001);
        }
    }

    private void feedForward(){
        for (int t = 0; t < TIMESTEP; t++) {
//            System.out.println("TIMESTEP "+t);
            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                System.out.println(t+" \t HIDDEN "+h);

                //input->hidden
                double sumInput = 0.0;
                for (int i = 0; i < INPUT_NEURON; i++) {
                    sumInput = sumInput + (dataInput[t][i] * bobotTIH[t][i][h]);
                }

                //context->hidden
                double sumContext = 0.0;
                for (int c = 0; c < HIDDEN_NEURON; c++) {
                    sumContext = sumContext + (contextTC[t][c] * bobotTCH[t][c][h]);
                }

                if (t == TIMESTEP-1){
                    //hitung hidden terakhir
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                }else{
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                    contextTC[t+1][h] = aktifHiddenT[t][h];
//                    System.out.println(aktifHiddenT[t][h] +"  |  "+ contextTC[t+1][h]);
                }
            }

        }

        //hitung output
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double sum = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                sum = sum + (aktifHiddenT[TIMESTEP-1][h] * bobotHO[h][o]);
            }
            netOutput[o] = sum + bobotBiasHO[o];
        }

        //menghitung softmax
        System.out.print("Hasil Output : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            aktifOutput[o] = softmax(netOutput[o]);
//            commented for saving memory
            System.out.print(aktifOutput[o]+" \t"); 
        }
        System.out.println();

        hitungError();
    }
    
    //just for saving memory in training mode
    private void feedForward(String training){
        for (int t = 0; t < TIMESTEP; t++) {
//            System.out.println("TIMESTEP "+t);
            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                System.out.println(t+" \t HIDDEN "+h);

                //input->hidden
                double sumInput = 0.0;
                for (int i = 0; i < INPUT_NEURON; i++) {
                    sumInput = sumInput + (dataInput[t][i] * bobotTIH[t][i][h]);
                }

                //context->hidden
                double sumContext = 0.0;
                for (int c = 0; c < HIDDEN_NEURON; c++) {
                    sumContext = sumContext + (contextTC[t][c] * bobotTCH[t][c][h]);
                }

                if (t == TIMESTEP-1){
                    //hitung hidden terakhir
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                }else{
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                    contextTC[t+1][h] = aktifHiddenT[t][h];
//                    System.out.println(aktifHiddenT[t][h] +"  |  "+ contextTC[t+1][h]);
                }
            }

        }

        //hitung output
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double sum = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                sum = sum + (aktifHiddenT[TIMESTEP-1][h] * bobotHO[h][o]);
            }
            netOutput[o] = sum + bobotBiasHO[o];
        }

        //menghitung softmax
//            commented for saving memory
//        System.out.print("Hasil Output : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            aktifOutput[o] = softmax(netOutput[o]);
//            commented for saving memory
//            System.out.print(aktifOutput[o]+" \t"); 
        }
//        System.out.println();

        hitungError();
    }

    private void feedForward(double[][] dataInput)
    {
        for (int t = 0; t < TIMESTEP; t++) {
//            System.out.println("TIMESTEP "+t);
            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                System.out.println(t+" \t HIDDEN "+h);

                //input->hidden
                double sumInput = 0.0;
                for (int i = 0; i < INPUT_NEURON; i++) {
                    sumInput = sumInput + (dataInput[t][i] * bobotTIH[t][i][h]);
                }

                //context->hidden
                double sumContext = 0.0;
                for (int c = 0; c < HIDDEN_NEURON; c++) {
                    sumContext = sumContext + (contextTC[t][c] * bobotTCH[t][c][h]);
                }

                if (t == TIMESTEP-1){
                    //hitung hidden terakhir
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                }else{
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                    contextTC[t+1][h] = aktifHiddenT[t][h];
//                    System.out.println(aktifHiddenT[t][h] +"  |  "+ contextTC[t+1][h]);
                }
            }

        }

        //hitung output
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double sum = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                sum = sum + (aktifHiddenT[TIMESTEP-1][h] * bobotHO[h][o]);
            }
            netOutput[o] = sum + bobotBiasHO[o];
        }

        //menghitung softmax
        System.out.print("Hasil Output : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            aktifOutput[o] = softmax(netOutput[o]);
            System.out.print(aktifOutput[o]+" \t");
        }
        System.out.println();
    }
    
    private void hitungError(){
        //HITUNG ERROR OUTPUT
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            //hitung derivative
            oGrad[o] = (aktifOutput[o] * (1-aktifOutput[o])) * (dataTarget[o] - aktifOutput[o]) ;
//            System.out.println(oGrad[o]);
        }

        //HITUNG ERROR HIDDEN PADA TIMESTEP
        for (int t = TIMESTEP-1; t >= 0; t--) {
//            System.out.println("TIMESTEP KE- "+ t);
            double derivative = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                double sum = 0.0;
//                System.out.print("HIDDEN (T, H) "+t+", "+h+" \t");

                if (t == TIMESTEP-1){
                    //derivative
                    derivative = (1 - aktifHiddenT[t][h]) * (1 + aktifHiddenT[t][h]);
                    //sum
                    for (int o = 0; o < OUTPUT_NEURON; o++) {
                        sum = sum + (oGrad[o] * bobotHO[h][o]);
                    }
                    thGrad[t][h] = derivative * sum;
//                    System.out.println(thGrad[t][h]);
                }else{
                    //derivative
                    derivative = (1 - aktifHiddenT[t][h]) * (1 + aktifHiddenT[t][h]);
                    //sum
                    for (int c = 0; c < HIDDEN_NEURON; c++) {
                        sum = sum + (thGrad[t+1][c] * bobotTCH[t+1][h][c]);
                    }
                    thGrad[t][h] = sum * derivative;
//                    System.out.print(thGrad[t][h]);
//                    System.out.println();
                }
            }
        }
    }

    private void hitungDelta(){
        DecimalFormat formatTiga = new DecimalFormat("#.####### ##");

//        System.out.println("============ HITUNG DELTA & UPDATE BOBOT ====================");
        for (int t = 0; t < TIMESTEP; t++) {
//            System.out.println("TIMESTEP "+t);
            //INPUT -> HIDDEN
            for (int i = 0; i < INPUT_NEURON; i++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    double delta = 0.0;
                    delta = LEARNING_RATE * dataInput[t][i] * thGrad[t][h];
                    bobotTIH[t][i][h] = delta + bobotTIH[t][i][h];
//                    System.out.print(formatTiga.format(bobotTIH[t][i][h])+" \t");
                }
//                System.out.println();
            }

            //HIDDEN -> OUTPUT
            for (int c = 0; c < HIDDEN_NEURON; c++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    double delta = 0.0;
                    delta = LEARNING_RATE * contextTC[t][c] * thGrad[t][h];
                    bobotTCH[t][c][h] = delta + bobotTCH[t][c][h];
//                    System.out.print(formatTiga.format(bobotTCH[t][c][h])+" \t");
                }
//                System.out.println();
            }

            //hitung delta untuk setiap bias pada timestep -> hidden
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                double delta = 0.0;
                delta = LEARNING_RATE * 1 * thGrad[t][h];
                bobotBiasTIH[t][h] = delta + bobotBiasTIH[t][h];
            }
        }

        //update bobot pada HIDDEN -> OUTPUT
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                double delta = 0.0;
                delta = LEARNING_RATE * aktifHiddenT[TIMESTEP-1][h] * oGrad[o];

                //update bobot
                bobotHO[h][o] = delta + bobotHO[h][o];
            }
        }

        //update bobot bias pada HIDDEN -> OUTPUT
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double delta = 0.0;
            delta = LEARNING_RATE * 1 * oGrad[o];
            bobotBiasHO[o] = delta + bobotBiasHO[o];
        }
    }

    private double softmax(double val){
        double totalExp = 0.0;
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            totalExp = totalExp + Math.exp(netOutput[o]);
        }
        return Math.exp(val)/totalExp;
    }

    private int maximum(final double[] vector)
    {
        // This function returns the index of the maximum of vector().
        int indeksMax = 0;
        double max = vector[indeksMax];

        for(int index = 0; index < OUTPUT_NEURON; index++)
        {
            if(vector[index] > max){
                max = vector[index];
                indeksMax = index;
            }
        }
        return indeksMax;
    }

    public void main(){
        neuralNetwork();
    }

    public void neuralNetwork(){
        int sample = 0; // (indeks/pointer) data sample yang akan dilatih oleh ERNN
        generateBobot();

        //melatih sebanyak epoch yang diinginkan untuk sejumlah dataset yang telah ditentukan
        for (int epoch = 0; epoch< JUMLAH_EPOCH; epoch++) {
            //adjusting the pointer of input data sample
            if (sample == sampleDataTarget.length){
                sample = 0;
            }

            //do the training process
            System.out.println(sample+", EPOCH KE-"+epoch);

            //select the data input to train
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataInput[j][k] = sampleDataTraining[sample][j][k];
                }
            }

            //select the output pattern for related data sample
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTarget[sample][j];
            }

            feedForward("training");
            hitungDelta();

            sample++; //add the pointer for input data sample by 1
        }

        //hitung akurasi dari hasil pelatihan terhadap dataTraining (getTrainingState)
        getTrainingState();

        //test dengan data uji
        //System.out.println("test dengan data uji");
        System.out.println("=========================== PENGUJIAN =======================================");
        System.out.println("=========================== PENGUJIAN =======================================");
        testDenganDataUji();
        
//        System.out.println("===================== BOBOT HASIL PELATIHAN ===============================");
//        System.out.println("BOBOT T- IH");
//        for (int i = 0; i < bobotTIH.length; i++) {
//            System.out.println("TIMESTEP T- IH"+i);
//            for (int j = 0; j < bobotTIH[0].length; j++) {
//                for (int k = 0; k < bobotTIH[0][0].length; k++) {
//                    System.out.print(bobotTIH[i][j][k]+"\t");
//                }
//                System.out.println("");
//            }
//        }
//        
//        System.out.println("BOBOT Bias T- IH");        
//        for (int i = 0; i < bobotBiasTIH.length; i++) {
//            for (int j = 0; j < bobotBiasTIH[0].length; j++) {
//                System.out.print(bobotBiasTIH[i][j]+"\t");
//            }
//            System.out.println("");
//        }
//        
//        System.out.println("BOBOT T- CH");
//        for (int i = 0; i < bobotTCH.length; i++) {
//            System.out.println("TIMESTEP - "+i);
//            for (int j = 0; j < bobotTCH[0].length; j++) {
//                for (int k = 0; k < bobotTCH[0][0].length; k++) {
//                    System.out.print(bobotTCH[i][j][k]+"\t");
//                }
//                System.out.println("");
//            }
//        }
    }

    public void getTrainingState(){
        double sum = 0.0;
        for (int i = 0; i < JUMLAH_SAMPLE_TRAINING; i++) {
        
            //masukkan sample input pada data training pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataInput[j][k] = sampleDataTraining[i][j][k];
                }
            }

            //masukkan output yang diharapkan dari sample data input tersebut
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTarget[i][j];
            }

            //lakukan feedforward (pengujian)
            System.out.println("Data Training ke-"+i);
            feedForward();

            //hitung akurasi yang dihasilkan terhadap data latih
            if(maximum(aktifOutput) == maximum(dataTarget)){
                sum += 1;
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }else{
                System.out.println("ini data yang salah dikenali");
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }
            
            confussionMatrixTraining(maximum(aktifOutput), maximum(dataTarget));
        }
        
        System.out.println("Network is " + ((double)sum / (double)JUMLAH_SAMPLE_TRAINING * 100.0) + "% correct.");
        dataPelatihanJumlah = JUMLAH_SAMPLE_TRAINING;
        dataPelatihanDikenali = (int) sum;
        
        //tampilkan confussion matrix
        for (int i = 0; i < 10; i++) {
            System.out.print(i+" | ");
            for (int j = 0; j < 10; j++) {
                System.out.print(confussionMatrixTrain[i][j]+" \t");
            }
            System.out.println("");
        }
        
    }

    public void getTestingState(double[][][] sampleTes){       
        System.out.println("===================== BUTTON PENGUJIAN ===============================");
        double sum = 0.0;
        for (int i = 0; i < sampleTes.length; i++) {
            //masukkan output yang diharapkan dari sample data input tersebut
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTargetTes[i][j];
            }
            
            //masukkan sampleTes kedalam dataInput pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataInput[j][k] = sampleTes[i][j][k];
                }
            }
            System.out.println("Data uji ke-"+i);
            //lakukan feedforward (pengujian)
            feedForward(dataInput);

            //hitung akurasi yang dihasilkan terhadap data latih
            if(maximum(aktifOutput) == maximum(dataTarget)){
                sum += 1;
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }else{
                System.out.println("ini data yang salah dikenali");
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }
        }
        System.out.println(sum+" | "+JUMLAH_SAMPLE_TESTING);
        System.out.println("Network is " + (((double)sum / (double)JUMLAH_SAMPLE_TESTING) * 100.0) + "% correct.");
        dataTestingDikenali = (int) sum;
        dataTestingJumlah = JUMLAH_SAMPLE_TESTING;
    }
    
    public void testDenganDataUji(){
        double sum = 0.0;
        for (int i = 0; i < sampleTes.length; i++) {
            //masukkan output yang diharapkan dari sample data input tersebut
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTargetTes[i][j];
            }
            
            //masukkan sampleTes kedalam dataInput pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataInput[j][k] = sampleTes[i][j][k];
                }
            }
            System.out.println("Data uji ke-"+i);
            //lakukan feedforward (pengujian)
            feedForward();

            //hitung akurasi yang dihasilkan terhadap data latih
            if(maximum(aktifOutput) == maximum(dataTarget)){
                sum += 1;
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }else{
                System.out.println("ini data yang salah dikenali");
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }
            confussionMatrixTesting(maximum(aktifOutput), maximum(dataTarget));

/*            //tampilkan hasil pengenalan
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    System.out.print(dataInput[j][k]+"\t");;
                }
            }
            System.out.print("\nOutput: " + maximum(aktifOutput) + "\n");            
*/            
        }
        System.out.println("Network is " + ((double)sum / (double)JUMLAH_SAMPLE_TESTING * 100.0) + "% correct.");
        dataTestingDikenali = (int) sum;
        dataTestingJumlah = JUMLAH_SAMPLE_TESTING;
    }
    
    public void testDenganDataUji(double[][][] sampleTes, double[][]sampleDataTargetTes){
        double sum = 0.0;
        for (int i = 0; i < sampleTes.length; i++) {
            //masukkan output yang diharapkan dari sample data input tersebut
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTargetTes[i][j];
            }
            
            //masukkan sampleTes kedalam dataInput pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataInput[j][k] = sampleTes[i][j][k];
                }
            }
            System.out.println("Data uji ke-"+i);
            //lakukan feedforward (pengujian)
            feedForward(dataInput);

            //hitung akurasi yang dihasilkan terhadap data latih
            if(maximum(aktifOutput) == maximum(dataTarget)){
                sum += 1;
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }else{
                System.out.println("ini data yang salah dikenali");
                for (int j = 0; j < dataInput.length; j++) {
                    for (int k = 0; k < dataInput[0].length; k++) {
                        System.out.print(dataInput[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }
/*            //tampilkan hasil pengenalan
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    System.out.print(dataInput[j][k]+"\t");;
                }
            }
            System.out.print("\nOutput: " + maximum(aktifOutput) + "\n");            
*/            
        }
        System.out.println("Network is " + ((double)sum / (double)JUMLAH_SAMPLE_TESTING * 100.0) + "% correct.");
        dataTestingDikenali = (int) sum;
        dataTestingJumlah = JUMLAH_SAMPLE_TESTING;
    }
    public void cek(){
        System.out.println("bobotTIH = "+bobotTIH.length+" , "+bobotTIH[0].length+" , "+bobotTIH[0][0].length);
        for (int i = 0; i < bobotTIH.length; i++) {
            for (int j = 0; j < bobotTIH[0].length; j++) {
                for (int k = 0; k < bobotTIH[0][0].length; k++) {
                    System.out.print(bobotTIH[i][j][k]+"\t");
                }
                System.out.println("");
            }
        }
        System.out.println("bobotTCH = "+bobotTCH.length+" , "+bobotTCH[0].length+" , "+bobotTCH[0][0].length);
        System.out.println("bobotBiasTIH = "+bobotBiasTIH.length+" , "+ bobotBiasTIH[0].length);
        System.out.println("bobotHO = "+bobotHO.length+" , "+bobotHO[0].length);
        System.out.println("bobotBiasHO = "+bobotBiasHO.length);
        System.out.println("JUMLAH SAMPLE TRAINING= "+JUMLAH_SAMPLE_TRAINING);
        System.out.println("dataInput = "+dataInput.length+" , "+dataInput[0].length);
    }
    
    void confussionMatrixTraining(int dataSeharusnya, int dataOutput){
        confussionMatrixTrain[dataSeharusnya][dataOutput] = confussionMatrixTrain[dataSeharusnya][dataOutput]+1;
    }
    
    private void confussionMatrixTesting(int dataSeharusnya, int dataOutput){
        confussionMatrixTest[dataSeharusnya][dataOutput] = confussionMatrixTest[dataSeharusnya][dataOutput]+1;

    }
    
    
}
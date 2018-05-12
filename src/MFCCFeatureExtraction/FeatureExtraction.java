package MFCCFeatureExtraction;

import Interface.DetilMFCC;
import Interface.HalamanEkstraksiCiri;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by HP on 4/22/2017.
 */
public class FeatureExtraction {
    private int SAMPLE_RATE, SAMPLE_POINT, panjangSinyalAudio;
    private double frameRate;
    
    int JUMLAH_FRAME_SETELAH_DIBAGI;
    ArrayList<Double> temp2 = new ArrayList<Double>();
    HalamanEkstraksiCiri f = new HalamanEkstraksiCiri();
    public static double[][] frame; 
//    public double[][] windowingSignal;
    public double[][] magnitudeSemua;
    public double[][] melFreqEnergy;
    public double[][] dctCepstrum;
    public double[] normalisasiMFCC;
    

    
    /**
     * @param SAMPLE_RATE = jumlah sample dalam sebuah frame yang akan diolah
     * @param SAMPLE_POINT = ukuran dari frame (frame size)
     * @param frameRate = jumlah frame yang akan dibuat
     */
    public FeatureExtraction(int SAMPLE_RATE, int SAMPLE_POINT, int frameRate) {
        this.frame = new double[frameRate][SAMPLE_POINT];
//        this.windowingSignal = new double[frameRate][SAMPLE_POINT];
        this.magnitudeSemua = new double[frameRate][SAMPLE_POINT];
        this.SAMPLE_RATE = SAMPLE_RATE;
        this.SAMPLE_POINT = SAMPLE_POINT;
        this.frameRate = frameRate;
    }

    
    public double[][] getMagnitudeSemua() {
        return magnitudeSemua;
    }
    
//    public int getJumlahFrame() {
//        return jumlahFrame;
//    }
//
//    public int getPanjangFrame() {
//        return panjangFrame;
//    }
    
    public void frameBlocking(ArrayList<Double> preEmphasisSignal){
        /**
        * jumlah frame = ((I-N)/M)+1
        * I = sample rate  --> jumlah sample (keseluruhan) yang akan diolah (I)
        * N = sample point --> 16(bit per sample) * 25 (ms) = (N) 
        * M = N/2 (overlapping 50%)
        * 1 ms = 16 sample
        * */
        
        int M = SAMPLE_POINT/2;
        //frameRate = (SAMPLE_RATE - SAMPLE_POINT) / M -1; //jumlah frame dalam satu detik
        System.out.println("Jumlah Frame (setelah frame blocking) = "+String.valueOf(frameRate));
        
        //Frame Blocking                
        System.out.println("sample point : "+SAMPLE_POINT+", framerate : "+(frameRate)+", M = "+M);
       
        int start, end;
        
        for (int i = 0; i < frameRate; i++) {
            //buat(bagi) frame sebanyak framerate
            start = M*i;
            end = (i+1) * SAMPLE_POINT;
            //textareaFrameBlocking.append(String.valueOf(i)+"\t");

            for (int j = start; j < start+SAMPLE_POINT; j++) {
                if (preEmphasisSignal!=null) {
                    frame[i][j-start] = preEmphasisSignal.get(j);
                } else {
                    frame[i][j-start] = 0.0;
                }
            }                       
        }   
    }

   
    /**
     * @param sample = jumlah sample dalam sebuah frame yang akan diolah
     * @param jumlahFrame = banyaknya frame (total frame)
     * @param panjangFrame = ukuran dari frame (frame size)
     * @return windowingSignal = array 2d hasil windowing 
     */
    //public void windowing(double sample[][], int jumlahFrame, int panjangFrame){
    public double[][] windowing(double sample[][]){
        int panjangFrame = sample[0].length;
        int jumlahFrame = sample.length;
        double fungsiWindowingSignal[][] = new double[jumlahFrame][panjangFrame];
        double windowingSignal[][] = new double[jumlahFrame][panjangFrame];
        /*
        * lakukan Hamming Window :
        *   w(n) = 0.54 - 0.46 cos( (2*phi*n) / (M-1) )
        * dimana :
        *   M = panjang frame
    *    *   n = 0, 1, ..., M-1
        */        
        for (int i = 0; i < jumlahFrame; i++) {
            for (int j = 0; j < panjangFrame; j++) {      
                double RUMUS_HAMMING_WINDOW = (2 * 3.14 * j) / ( panjangFrame - 1 );
                //double FUNGSI_HAMMING_WINDOW = ((0.54 - 0.46) * Math.cos(RUMUS_HAMMING_WINDOW));
                double FUNGSI_HAMMING_WINDOW = (0.54 - 0.46 * Math.cos(RUMUS_HAMMING_WINDOW));
                fungsiWindowingSignal[i][j] = FUNGSI_HAMMING_WINDOW;
            }            
        }               

        /*
        * representasikan fungsi window terhadap sinyal
        *  x(n) = xi(n)* w(n)
        * dimana :
        *  n = 0, 1, ..., N-1
        *  x(n) nilai sampel signal hasil windowing
        *  xi(n) = nilai sampel dari frame signal ke-i (hasil pre emphasis)
        *  w(n) = fungsi window
        *  N = frame size
        */
        for (int i = 0; i < jumlahFrame; i++) {
            for (int j = 0; j < panjangFrame; j++) {      
                double HASIL_WINDOWING = sample[i][j] * fungsiWindowingSignal[i][j]; //sample = preEmphasisi
                //System.out.println(i+", "+j+" = "+fungsiWindowingSignal[i][j]);

                windowingSignal[i][j] = HASIL_WINDOWING;
                
                //System.out.println("windowing ke"+i+","+j+" : "+windowingSignal[i][j]);
                //System.out.println("W-"+ i +","+j+" = "+ sample[i][j]+" * "+fungsiWindowingSignal[i][j]+" = "+windowingSignal[i][j]);
            }            
        }    
        //Done       
        return windowingSignal;        
    }

    public double[][] fastFourierTransform(double sample[][]){        
        double hcos = 0;
        double hsin = 0;
        
        //double magnitudeSample[][] = new double[panjangFrame][panjangFrame];
        double nilaiAbsolut;
        double re, imaj;
        double[][] hasilFFT = new double[sample.length][sample[0].length];
        
        
        for (int i = 0; i < hasilFFT.length; i++) {
            for (int n = 0; n < hasilFFT[0].length; n++) {
                double totCos = 0;
                double totSin = 0;
                for (int k = 0; k < hasilFFT[0].length; k++) {
                    hcos = sample[i][k]*(Math.cos((2*3.14*n*k)/hasilFFT[0].length));
                    hsin = sample[i][k]*(Math.sin((2*3.14*n*k)/hasilFFT[0].length));
                    totCos = totCos + hcos;
                    totSin = totSin + hsin;
                    //System.out.println("FRAME ke-"+i+", FFT ("+n+","+k+") = "+totCos+" "+totSin+"Sample asli : "+sample[i][k]);
                }
                re = Math.pow(totCos, 2);
                imaj = Math.pow(totSin, 2) *(-1); // 3i^2 = 3 * -1 = -3  
                nilaiAbsolut = Math.abs(re +imaj);
                //magnitudeSample[i][n] = Math.abs(re +imaj);
                //magnitudeSemua[i][n] = nilaiAbsolut;
                hasilFFT[i][n] = nilaiAbsolut;
            }            
        }
        
        return hasilFFT;
    }
    
//    public double[] discreteFourierTransform(double[] sample){
//        double hcos = 0;
//        double hsin = 0;
//        
//        //double magnitudeSample[][] = new double[panjangFrame][panjangFrame];
//        double nilaiAbsolut;
//        double re, imaj;
//        double[] hasilFFT = new double[sample.length];
//        
//        for (int n = 0; n < hasilFFT.length; n++) {
//            double totCos = 0;
//            double totSin = 0;
//            for (int k = 0; k < hasilFFT.length; k++) {
//                hcos = sample[k]*(Math.cos((2*3.14*n*k)/hasilFFT.length));
//                hsin = sample[k]*(Math.sin((2*3.14*n*k)/hasilFFT.length));
//                totCos = totCos + hcos;
//                totSin = totSin + hsin;
//                //System.out.println("FRAME ke-"+i+", FFT ("+n+","+k+") = "+totCos+" "+totSin+"Sample asli : "+sample[i][k]);
//            }
//            re = Math.pow(totCos, 2);
//            imaj = Math.pow(totSin, 2) *(-1); // 3i^2 = 3 * -1 = -3  
//            nilaiAbsolut = Math.abs(re +imaj);
//            //magnitudeSample[i][n] = Math.abs(re +imaj);
//            //magnitudeSemua[i][n] = nilaiAbsolut;
//            hasilFFT[n] = nilaiAbsolut;
//        }  
//        return hasilFFT;
//    }
    
    /**
     *
     * @param f frekuensi mel filterbank yang akan dihitung
     * @return nilai mel filterbank dari argument f yang diinputkan
     */
    private double melFilterbank(double f){
        double melFilterbank = 2595 * Math.log10(1+(f/700));
        //format desimal
        DecimalFormat formatEmpat = new DecimalFormat("#.####");
        
        //return formatEmpat.format(melFilterbank);
        return melFilterbank;
    }
    
    private double melFilterbankInvers(double f){
        double a = (f/2595);
        double pangkat = Math.pow(10, a);
        double melFilterbankInvers = 700 * (pangkat-1);
        
        //format desimal
        DecimalFormat formatEmpat = new DecimalFormat("#.####");
        
        //return formatEmpat.format(melFilterbankInvers);
        return melFilterbankInvers;
    }
     
    /**
     *
     * @param sample sample masukkan (hasil FFT)
     * @param SAMPLING_RATE frekuensi sampling (biasanya 8,16,22.5.44.1 Khz)
     * @param noOfFilterbank jumlah filterbank yang diinginkan
     * @param fLow frekuensi terendah dalam sebuah filterbank
     * @param fHigh frekuensi tertinggi dalam sebuah filterbank
     */
    public double[][] filterbank(double[][] sample, int SAMPLING_RATE, int noOfFilterbank, double fLow, double fHigh){
        double[] filterbank = new double[noOfFilterbank+1];
        double c = (double) sample[0].length / (double)SAMPLING_RATE; // NS/FS
        int banyakFrame = sample.length;
        //membuat filterbank
        System.out.println("\nNFBank = "+noOfFilterbank);
        System.out.println("fLow= "+fLow);
        System.out.println("fHigh = "+fHigh);
        System.out.println("panjang frame= "+sample[0].length);
        System.out.println("SAMPLING RATE = "+SAMPLING_RATE);

        for (int i = 0; i < filterbank.length; i++) {
            double b = (i* (melFilterbank(fHigh) - melFilterbank(fLow))/(noOfFilterbank-1));
            double a = melFilterbank(fLow);   
            double kanan = a + b;
            filterbank[i] = c * melFilterbankInvers(a+b);
            
            System.out.println("filterbank ke-"+i+" = "+filterbank[i]);
        }
        
        //membuat triangular filterbank
        double[][] triangularMelFilterbank = new double[sample[0].length][noOfFilterbank]; // H[k,i]
        
        //buat nomor untuk tampilan saja
        System.out.print("\t");
        for (int i = 0; i < triangularMelFilterbank[0].length; i++) {
            System.out.print(i+"\t");
        }
        System.out.println();
        //hitung H(i,k)
        for (int k = 0; k < sample[0].length; k++) { //k 30
//            System.out.print(k+"\t");
            for (int i = 0; i < triangularMelFilterbank[0].length; i++) {
                //jika k = 0
                if (k==0) {
                    triangularMelFilterbank[k][i] = 0;
                }else{
                    //buat H(k,i)
                    if (i==0) {
                        if (k<0) {
                            triangularMelFilterbank[k][i] = 0;
                        }
                        if ((0<=k) && (k < filterbank[i])){
                            triangularMelFilterbank[k][i] = (k-0) / (filterbank[i] - 0);
                        }
                        if ((filterbank[i]<=k) && (k < filterbank[i+1])){
                            triangularMelFilterbank[k][i] = (filterbank[i+1]-k)/(filterbank[i+1] - 0);
                        }
                        if(k>filterbank[i+1]){
                            triangularMelFilterbank[k][i] = 0;
                        }
//                        System.out.print((k < filterbank[i+1])+" ,"+k+"|"+filterbank[i]);
                    }else if(i==triangularMelFilterbank[0].length-1){
                        if (k<filterbank[i-1]) {
                            triangularMelFilterbank[k][i] = 0;
                        }
                        if ((filterbank[i-1]<=k) && (k < filterbank[i])){
                            triangularMelFilterbank[k][i] = (k-filterbank[i-1]) / (filterbank[i] - filterbank[i-1]);
                        }
                        if ((filterbank[i]<=k) && (k<filterbank[i+1])){
                            triangularMelFilterbank[k][i] = (filterbank[i+1]-k)/(filterbank[i+1] - filterbank[i-1]);
                        }
                    }else{
                        if (k<filterbank[i-1]) {
                            triangularMelFilterbank[k][i] = 0;
                        }
                        if ((filterbank[i-1]<=k) && (k < filterbank[i])){
                            triangularMelFilterbank[k][i] = (k-filterbank[i-1]) / (filterbank[i] - filterbank[i-1]);
                        }
                        if ((filterbank[i]<=k) && (k < filterbank[i+1])){
                            triangularMelFilterbank[k][i] = (filterbank[i+1]-k)/(filterbank[i+1] - filterbank[i-1]);
                        }
                        if(k>filterbank[i+1]){
                            triangularMelFilterbank[k][i] = 0;
                        }
                    }

                }
                
                //tampilkan
                DecimalFormat fEmpat = new DecimalFormat("#.####");
//                System.out.print(fEmpat.format(triangularMelFilterbank[k][i])+"\t");
            }
//            System.out.println("");
        }
        
        System.out.println("===========");
        double melFreqEnergy[][] = new double[sample.length][noOfFilterbank];
        //hitung melfreq energy 
        //untuk setiap frame yang ada
        for (int i = 0; i < sample.length; i++) {
            //dapatkan melfreq energy, caranya
            //pada setiap log filterbank yang ada, lakukan perhitungan dengan :
            for (int j = 0; j < noOfFilterbank; j++) {
                // sigma dari semua nilai fft dikalikan nilai filterbanknya untuk nilai tersebut.
                double melEnergy=0;
                double totalMelEnergy = 0;
//                System.out.print("\nFRAME ke-"+i+"filterbank ke-"+j+" = ");
                for (int k = 0; k < triangularMelFilterbank.length; k++) { //banyaknya nilai hasil fft
                    melEnergy = sample[i][k]*triangularMelFilterbank[k][j];
                    totalMelEnergy = melEnergy + totalMelEnergy;
//                    System.out.println(melEnergy);

                }
                melFreqEnergy[i][j] = totalMelEnergy;
//                System.out.print("totalnya -"+j+" = "+melFreqEnergy[i][j]+"\t");
                System.out.print(melFreqEnergy[i][j]+"\t");
                //System.out.print(melEnergy+"\t");
                
            }
            System.out.println("");
        }
        System.out.println(sample.length);
        
        return melFreqEnergy;
    }

    /**
     *
     * @param melFreqEnergy hasil fari mel filterbank (mel frequency)
     * @param jumlahFrame jumlah frame yang ada (baris) = frameRate
     * @param panjangFrame panjang dari masing-masing frame (kolom) = samplePoint
     */
/*    public void discreteCosineTransform(double[][] melFreqEnergy,int koefisien){
        
        //int koefisien = 13;
        double[][] dct = new double[melFreqEnergy.length][koefisien];

        for (int k = 0; k < melFreqEnergy.length; k++) {
            for (int i = 0; i < koefisien; i++) {
                double temp = 0;
                for (int j = 0; j < melFreqEnergy[0].length; j++) {
                    //rumus
                    temp = temp + (Math.log10(melFreqEnergy[k][j])* Math.cos(i*(j-0.5)*3.14/(koefisien)));
                    //System.out.print(melFreqEnergy[k][j]+"\t");
//                    System.out.print(temp+"\t");
                }
                dct[k][i] = temp;
//                System.out.println("");
//                System.out.print("DCT-"+k+","+i+" : "+dct[k][i]);
            }
//            System.out.println("");
        }
        dctCepstrum = dct;
    }
*/
    
    
    public void discreteCosineTransform(double[][] melFreqEnergy,int koefisien){
        
        //int koefisien = 13;
        double[][] dct = new double[melFreqEnergy.length][koefisien]; //3,12

        //melFreqEnergy[3][30]
        int jumlahFrame = melFreqEnergy.length ; //jumlah frame = 3
        int jumlahFilterbank = melFreqEnergy[0].length; ////jumlah filterbank 30
        System.out.println("koefisien = "+koefisien+" , Jumlah Frame yg dihitung = "+jumlahFrame+" , Jumlah NFilterbank = "+melFreqEnergy[0].length+" , COS 0 = "+Math.cos(0)+" NGETES = "+( Math.log10(melFreqEnergy[0][1]) * Math.cos(1.0 * ((2.0*1.0-1.0)/2.0) * (3.14/30.0))));
        System.out.println(Math.sqrt(2/ (double)jumlahFilterbank));
        for (int i = 0; i < jumlahFrame; i++) {
            for (int n = 0; n < koefisien; n++) {
                double totalDCT_n = 0 ;
                for (int k = 0; k < jumlahFilterbank; k++) {
                    //System.out.print(n+","+k+"\t");
                    //System.out.print(n+","+k+","+melFreqEnergy[i][k]+"\t");
                    totalDCT_n = totalDCT_n + ( Math.log10(melFreqEnergy[i][k]) * Math.cos(n* ((2.0*k-1.0)/2.0) * (3.14/(double) jumlahFilterbank)) );
                }
                dct[i][n] = Math.sqrt( 2/(double)jumlahFilterbank ) * totalDCT_n;
                System.out.println("DCT "+i+","+n+" : "+dct[i][n]);                
            }
            System.out.println("===");
        }


        dctCepstrum = dct;
    }
    
    public double[] rata(){
        System.out.println("rata2----------------------");
        System.out.println(dctCepstrum.length+" "+dctCepstrum[0].length);
        
        //menghitung total
        double[] sum = new double[dctCepstrum[0].length];
        for (int i = 0; i < dctCepstrum.length; i++) {     //16
            for (int j = 0; j < dctCepstrum[0].length; j++) { //13
                sum[j] += dctCepstrum[i][j];
            }                        
        }
        
        for (int i = 0; i < sum.length; i++) {
            sum[i] = sum[i]/ (double) dctCepstrum.length;
            //System.out.println(i+" "+sum[i]);
        }
        
        return sum;
    }    
        
    public void normalisasiDanThresholding(){
        double[][] hasil = new double[dctCepstrum.length][dctCepstrum[0].length-1];
        double[][] threshold = new double[dctCepstrum.length][dctCepstrum[0].length-1];
        double[][] normal = new double[dctCepstrum.length][dctCepstrum[0].length-1];
        
        System.out.println(dctCepstrum.length+" "+dctCepstrum[0].length);
        //normalisasi perframe
        //cari nilai max dan min tiap frame
        
        for (int i = 0; i < dctCepstrum.length; i++) {
            double max = -999;
            double min = 9999;
            //cari max pada frame
            for (int j = 1; j < dctCepstrum[0].length; j++) {
                if (dctCepstrum[i][j]>max) {
                    max = dctCepstrum[i][j];
                }                     
            }
            //cari min pada frame
            for (int j = 1; j < dctCepstrum[0].length; j++) {
                if (dctCepstrum[i][j]<min) {
                    min = dctCepstrum[i][j];
                }   
            }
            System.out.println("max :"+max+" min : "+min);
            
            //ambil beberapa koefisien dari mfcc
            for (int j = 1; j < dctCepstrum[0].length; j++) {

                hasil[i][j-1]= (0.8 * (dctCepstrum[i][j]-min)/(max-min)+0.1); //lakukan normalisasi
                normal[i][j-1] = hasil[i][j-1];
                threshold[i][j-1] = thresholding(hasil[i][j-1]); //lakukan thresholding
            }
        }
        DetilMFCC.setNormal(normal);
        DetilMFCC.setThreshold(threshold);
        HalamanEkstraksiCiri.setNormal(normal);
        HalamanEkstraksiCiri.setThreshold(threshold);
    }
    
 
    private int thresholding(double input){
        if(input>=0.5){
            return 1;
        }
        return 0;
    }       
}

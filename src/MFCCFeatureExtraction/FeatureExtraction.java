package MFCCFeatureExtraction;

import Interface.HalamanEkstraksiCiri;
import java.util.ArrayList;

/**
 * Created by HP on 4/22/2017.
 */
public class FeatureExtraction {
    private int SAMPLE_RATE, SAMPLE_POINT, panjangSinyalAudio;
    private double frameRate;
    int jumlahFrame;
    int panjangFrame;
    
    int JUMLAH_FRAME_SETELAH_DIBAGI;
    ArrayList<Double> temp2 = new ArrayList<Double>();
    HalamanEkstraksiCiri f = new HalamanEkstraksiCiri();
    public static double[][] frame; 
    public double[][] windowingSignal;
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
        this.windowingSignal = new double[frameRate][SAMPLE_POINT];
        this.magnitudeSemua = new double[frameRate][SAMPLE_POINT];
        this.SAMPLE_RATE = SAMPLE_RATE;
        this.SAMPLE_POINT = SAMPLE_POINT;
        this.frameRate = frameRate;
    }
    
    public double[][] getMagnitudeSemua() {
        return magnitudeSemua;
    }
    
    public int getJumlahFrame() {
        return jumlahFrame;
    }

    public int getPanjangFrame() {
        return panjangFrame;
    }
    
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
    public double[][] windowing(double sample[][], int jumlahFrame, int panjangFrame){
        this.panjangFrame = panjangFrame;
        this.jumlahFrame = jumlahFrame;
        double fungsiWindowingSignal[][] = new double[jumlahFrame][panjangFrame];
        /*
        * lakukan Hamming Window :
        *   w(n) = 0.54 - 0.46 cos( (2*phi*n) / (M-1) )
        * dimana :
        *   M = panjang frame
    *    *   n = 0, 1, ..., M-1
        */        
        for (int i = 0; i < jumlahFrame; i++) {
            for (int j = 0; j < panjangFrame; j++) {      
                double RUMUS_HAMMING_WINDOW = (2 * 3.14 * i) / ( panjangFrame - 1 );
                double FUNGSI_HAMMING_WINDOW = ((0.54 - 0.46) * Math.cos(RUMUS_HAMMING_WINDOW));
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
                windowingSignal[i][j] = HASIL_WINDOWING;
                
                //System.out.println("windowing ke"+i+","+j+" : "+windowingSignal[i][j]);
                //System.out.println("W-"+ i +","+j+" = "+ sample[i][j]+" * "+fungsiWindowingSignal[i][j]+" = "+windowingSignal[i][j]);
            }            
        }    
        //Done       
        return windowingSignal;        
    }

    public void fastFourierTransform(double sample[][], int jumlahFrame, int panjangFrame){        
        double hcos,hsin;
        double magnitudeSample[][] = new double[panjangFrame][panjangFrame];
        double nilaiAbsolut;
        double re, imaj;
        
        for (int i = 0; i < jumlahFrame; i++) {
            for (int n = 0; n < panjangFrame; n++) {
                double totCos = 0;
                double totSin = 0;
                for (int k = 0; k < panjangFrame; k++) {
                    hcos = sample[i][k]*(Math.cos((2*3.14*n*k)/panjangFrame));
                    hsin = sample[i][k]*(Math.sin((2*3.14*n*k)/panjangFrame));
                    totCos = totCos + hcos;
                    totSin = totSin + hsin;                    
                }
                re = Math.pow(totCos, 2);
                imaj = Math.pow(totSin, 2) *(-1); // 3i^2 = 3 * -1 = -3  
                nilaiAbsolut = Math.abs(re +imaj);
                magnitudeSample[i][n] = Math.abs(re +imaj);
                magnitudeSemua[i][n] = nilaiAbsolut;
            }            
        }           
    }
    
    /**
     *
     * @param sample sample hasil FFT
     * @param freq frekuensi sample (sampling rate)
     * @param jumlahFrame jumlah frame yang ada (baris)
     * @param panjangFrame panjang dari masing-masing frame (kolom)
     */
    public void filterbank(double[][] sample, int freq, int jumlahFrame, int panjangFrame){
    //public void filterbank(){                
        //dapatkan nilai maksimal dari setiap frame
//        double[] nilaiMax = new double[jumlahFrame];
//        for (int i = 0; i < jumlahFrame; i++) {
//            nilaiMax[i] = 0;
//            for (int j = 0; j < panjangFrame; j++) {
//                if (sample[i][j]>nilaiMax[i]) {
//                    nilaiMax[i] = sample[i][j];
//                }
//            }            
//        }
        
        System.out.println("filterbank--------;-------");
        double[][] koefisienFilterbank = new double[jumlahFrame][panjangFrame];
        for (int i = 0; i < jumlahFrame; i++) {
            for (int j = 0; j < panjangFrame; j++) {
                double cek = (double) 2595*Math.log10(1.0+(1000.0/700.0));
                koefisienFilterbank[i][j] = cek/(sample[i][j]/2);
//                System.out.println(i+" "+j+" "+koefisienFilterbank[i][j]);
                koefisienFilterbank[i][j] = (2595 * Math.log10(1 + 1000.0/700.0))/(sample[i][j]/2);
                //System.out.println(i+", "+j+" : "+cek+", xi = "+sample[i][j]+", xi/2 = "+sample[i][j]/2+", koefisien = "+koefisienFilterbank[i][j]+" , a="+cek);
                //koefisienFilterbank[i][j] = (2595 * Math.log10(1 + nilaiMax[i]/700))/(sample[i][j]/2);                
                //melFilterFreq[i][j] = koefisienFilterbank[i][j];;               
                //System.out.println(melFilterFreq[i][j]);
            }            
        }        
        melFreqEnergy = koefisienFilterbank;
    }
    
    /**
     *
     * @param melFreqEnergy hasil fari mel filterbank (mel frequency)
     * @param jumlahFrame jumlah frame yang ada (baris) = frameRate
     * @param panjangFrame panjang dari masing-masing frame (kolom) = samplePoint
     */
    public void discreteCosineTransform(double[][] melFreqEnergy, int jumlahFrame, int panjangFrame, int koefisien){
        
        //int koefisien = 13;
        double[][] dct = new double[jumlahFrame][koefisien];

        for (int k = 0; k < jumlahFrame; k++) {
            for (int i = 0; i < koefisien; i++) {
                double temp = 0;    
                for (int j = 0; j < panjangFrame; j++) {                
                    //rumus
                    temp = temp + (Math.log10(melFreqEnergy[k][j])* Math.cos(i*(j-0.5)*3.14/(koefisien)));
                }       
                dct[k][i] = temp;
            }
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
        
    public double[][] normalisasiDanThresholding(){
        double[][] hasil = new double[dctCepstrum.length][dctCepstrum[0].length-1];
        
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
                hasil[i][j-1] = thresholding(hasil[i][j-1]); //lakukan thresholding
            }
        }
        return hasil;
    }
    
 
    private int thresholding(double input){
        if(input>=0.5){
            return 1;
        }
        return 0;
    }       
}

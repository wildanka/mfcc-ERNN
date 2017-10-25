/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WavFileProcessor;

import WavFileProcessor.WavFile;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class ReadWav {    
    double min;
    double max;
    int nilaiN = 0;
    private ArrayList<Double> nilaiSample = new ArrayList<Double>();
    
    public ArrayList<Double> getNilaiSample() {
        return nilaiSample;
    }

    public void clearNilaiSample(){
        nilaiSample.clear();
    }
        
    public void bacaFile(String lokasiFile){
        try
        {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(lokasiFile));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();

            // Create a buffer of 100 frames
            double[] buffer = new double[300 * numChannels];

            int framesRead;
            min = Double.MAX_VALUE;
            max = Double.MIN_VALUE;
            int count = 0;

            do
            {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 300);

                // Loop through frames and look for minimum and maximum value
                for (int j = 0; j < buffer.length; j++) {
                    double d = buffer[j];
                }
                
                for (int s=0 ; s<framesRead * numChannels ; s++)
                {            
                    if (buffer[s] > max) max = buffer[s];
                    if (buffer[s] < min) min = buffer[s];
                    nilaiSample.add(buffer[s]);
                }
            }
            while (framesRead != 0);

            // Close the wavFile
            wavFile.clearBuffer();            
            wavFile.close();
            // Output the minimum and maximum value
            System.out.printf("Min: %f, Max: %f\n", min, max);            
        }
        catch (Exception e)
        {
            System.err.println(e);
        }       
    }

    public ArrayList<Double> silenceRemoval(ArrayList<Double> withSilence,int frekuensiSampling){
    //public void silenceRemoval(ArrayList<Double> withSilence,int frekuensiSampling){
        //array baru untuk menampung sinyal nonsilence
        ArrayList<Double> nonSilence = new ArrayList<>();
        int count = 0;
        
        double frame_duration = 0.025;
        double samplePoint = frame_duration * frekuensiSampling;
        int N = withSilence.size();
        int num_frames = (int) (N/samplePoint);        
        double[][] frame = new double[num_frames][(int) samplePoint];
        double[] maxvalFrame = new double[num_frames];
        double max_val=0;
        
        int end = 0;
        for (int i = 0; i < num_frames; i++) {                        
            //pecah kedalam beberapa frame
            for (int j = 0; j < samplePoint; j++) {
                frame[i][j] = withSilence.get((i*(int)samplePoint)+j);
                max_val = Math.max(max_val,withSilence.get((i*(int)samplePoint)+j));                
            }
            maxvalFrame[i] = max_val;
            max_val = 0; //kembalikan ke awal
            
            end = i * (int) samplePoint;
        }
        
        System.out.println("===========");
        for (int i = 0; i < num_frames; i++) {            
            if (maxvalFrame[i] > 0.03) {
                //nonSilence.get(end);
                for (int j = 0; j < samplePoint; j++) {
                    nonSilence.add(frame[i][j]);
                }
            }            
        }
        return nonSilence;
    }
    
}
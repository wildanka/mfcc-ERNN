/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSVIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DAN
 */
public class CSVReader {    
    public int hitungJumlahDataTraining(String lokasiDataTraining){
        int index = 0;
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(lokasiDataTraining));
            while ((line=br.readLine()) != null) {
                index++;                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return index;
    }
    
    /**
     *
     * @param lokasiDataTraining lokasi file training
     * @param jumlahIndeks jumlah sample data training pada file CSV
     * @param INPUT_NEURON jumlah input neuron (fitur yang dimiliki oleh dataTraining)
     * @return array2d dari data training
     */
    public double[][] bacaDataTraining(String lokasiDataTraining, int jumlahIndeks, int INPUT_NEURON) {
        //String csvFile = "E:\\MACHINELEARNING\\Iris dataset training.csv";
        double[][] dataTraining = new double[jumlahIndeks][INPUT_NEURON];
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int hitung = 0;
        int MAX_EPOCH = 10000;
        
        try {       
            br = new BufferedReader(new FileReader(lokasiDataTraining));
            int index = 0;

            while ((line = br.readLine()) != null){
                String[] data = line.split(csvSplitBy);
                
                dataTraining[index][0] = Double.valueOf(data[0]);
                dataTraining[index][1] = Double.valueOf(data[1]);
                dataTraining[index][2] = Double.valueOf(data[2]);
                dataTraining[index][3] = Double.valueOf(data[3]);
                
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataTraining;
    }
    
    /**
     *
     * @param lokasiDataTraining file location of CSV file
     * @param jumlahIndeks banyaknya sample data
     * @param OUTPUT_NEURON jumlah output neuron
     * @return array target pelatihan untuk tiap-tiap sample data training
     */
    public double[][] bacaDataTarget(String lokasiDataTraining, int jumlahIndeks, int OUTPUT_NEURON){
        double[][] dataTarget = new double[jumlahIndeks][OUTPUT_NEURON];
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int hitung = 0;
        int MAX_EPOCH = 10000;
        
        try {       
            br = new BufferedReader(new FileReader(lokasiDataTraining));
            int index = 0;

            while ((line = br.readLine()) != null){
                String[] data = line.split(csvSplitBy);
                
                if(data[84].equalsIgnoreCase("atas")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][0] = 0.25;                    
                }else if(data[84].equalsIgnoreCase("bawah")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][1] = 0.25;
                }else if(data[84].equalsIgnoreCase("kanan")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][2] = 0.25;
                }else if(data[84].equalsIgnoreCase("kiri")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][3] = 0.25;
                }else if(data[84].equalsIgnoreCase("naik")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][4] = 0.25;
                }else if(data[84].equalsIgnoreCase("turun")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][5] = 0.25;
                }else if(data[84].equalsIgnoreCase("hapus")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][6] = 0.25;
                }else if(data[84].equalsIgnoreCase("salin")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][7] = 0.25;
                }else if(data[84].equalsIgnoreCase("potong")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][8] = 0.25;
                }else{ //tempel
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][9] = 0.25;
                }
                                
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataTarget;
    }
}

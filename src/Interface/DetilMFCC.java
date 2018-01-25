/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import MFCCFeatureExtraction.FeatureExtraction;
import MFCCFeatureExtraction.SpeechProcessing;
import WavFileProcessor.ReadWav;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author DAN
 */
public class DetilMFCC extends javax.swing.JFrame {
    private int CF, MFCC_COEFF_NUMBER;
    private double SF, EF;
    private String lokasiFile;
    
    /**
     * Creates new form DetilMFCC
     */
    public DetilMFCC() {
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     *
     * @param MFCC_COEFF_NUMBER Jumlah Koefisien MFCC yang diinginkan 
     * @param CF Considered Frame
     * @param SF Start Point Frame
     * @param EF End Point Frame
     */
    public DetilMFCC(int MFCC_COEFF_NUMBER, int CF, double SF, double EF){
        initComponents();
        this.MFCC_COEFF_NUMBER = MFCC_COEFF_NUMBER;
        this.CF = CF;
        this.SF = SF;
        this.EF = EF;
        
        txtLokasiFileAudio.setText("Koefisien : "+String.valueOf(MFCC_COEFF_NUMBER)+
                "CF : "+CF+
                "SF : "+SF+
                "EF : "+EF);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private double[][] hitungMFCC(String lokasi, int CF, double SP, double EP, int koefisienMFCC){       
        DecimalFormat formatEmpat = new DecimalFormat("#0.0000"); //format angka
        DecimalFormat formatEnam = new DecimalFormat("#0.000000"); //format angka
        DecimalFormat formatSembilan = new DecimalFormat("#0.000000000"); //format angka
     
        //lakukan pre-processing (sampling)
        ReadWav pre = new ReadWav();
        pre.bacaFile(lokasi); 
        pre.getNilaiSample();        
        
        //output nilai sample
        for (int i = 0; i < pre.getNilaiSample().size(); i++) {            
            txtAreaSinyalAsli.append("("+i+") \t"+String.valueOf(formatSembilan.format(pre.getNilaiSample().get(i)))+" \t NILAI SAMPLE AWAL \n");
        }                 

        //silencer
        ArrayList<Double> nonSilence = new ArrayList<>();
        nonSilence = pre.silenceRemoval(pre.getNilaiSample(), 16000);        
        // output hasil silence removal

        for (int i = 0; i < nonSilence.size(); i++) {                        
            txtAreaSilenceRemoval.append("("+i+") \t"+String.valueOf(formatSembilan.format(nonSilence.get(i)))+"\t HASIL SILENCE REMOVAL \n");
        }
        
        //lakukan Speech processing
        SpeechProcessing sp = new SpeechProcessing();
        
        //just for displaying dcRemoval Result Signal sake!!!!
        ArrayList<Double> DCRemovalSignal = sp.dcRemoval(nonSilence);
        
        for (int i = 0; i < DCRemovalSignal.size(); i++) {
            txtAreaDCRemoval.append("("+i+") \t"+String.valueOf(formatSembilan.format(DCRemovalSignal.get(i)))+"\t HASIL SILENCE REMOVAL -> DC REMOVAL\n");
        }
        
        // DC REMOVAL - PRE EMPHASIS 
        ArrayList<Double> preEmphasisSignal = sp.preEmphasis(DCRemovalSignal);
         
        for (int i = 0; i < preEmphasisSignal.size(); i++) {
            txtAreaPreEmp.append("("+i+") \t"+String.valueOf(formatSembilan.format(preEmphasisSignal.get(i)))+"\t HASIL DC REMOVAL -> PRE EMPHASIS \n");
        }

        // FRAME BLOCKING
        //tentukan jumlah frame blocking (frameRate)
        // jika frame sebesar 25 ms = 16 * 25 = 400
        int samplePoint = 16 * 25; 
        int M = samplePoint/2;  //M
        int frameRate = (nonSilence.size() - samplePoint) / M +1; //jumlah frame dalam satu detik
        FeatureExtraction mfcc = new FeatureExtraction(nonSilence.size(),samplePoint,frameRate);        
        mfcc.frameBlocking(preEmphasisSignal);   
      
        //output hasil frame blocking
        int start;
        
        for (int i = 0; i < frameRate; i++) {
            //buat(bagi) frame sebanyak framerate
            start = (samplePoint/2)*i;            
            txtAreaFrameBlocking.append(String.valueOf(i)+"\t");
            for (int j = start; j < start+samplePoint; j++) {
                if (preEmphasisSignal!=null) {
                    mfcc.frame[i][j-start] = preEmphasisSignal.get(j);
                } else {
                    mfcc.frame[i][j-start] = 0.0;
                }
                txtAreaFrameBlocking.append(String.valueOf(formatEmpat.format(mfcc.frame[i][j-start]))+"\t");
            }
            txtAreaFrameBlocking.append("\n");
        }
        
        //TIME ALIGNMENT
//        int CF = 5;
//        double SP = 0.06;
//        double EP = 0.94;
        SP *= frameRate;
        EP *= frameRate;
        System.out.println(SP);
        System.out.println(EP);

        //double step = (Math.round(EP) - Math.round(SP)) / (double) (CF-1);
        double step = Math.floor((Math.round(EP) - Math.round(SP)) / (double) (CF-1));
        System.out.println("EP - SP ("+(Math.round(EP)-Math.round(SP))+") , \n"
                + "Nilai EP Asli = "+EP+" -> Nilai Pembulatan EP = "+Math.round(EP)+" \n"
                + "Nilai SP Asli = "+SP+", -> Nilai Pembulatan SP = "+Math.round(SP)+" \n"
                + "STEP = "+Math.round(step));
        //ambil frame tertentu sebanyak CF
        int[] consideredFrame = new int[CF];
        for (int i = 0; i < CF; i++) {
            if (i==0) {
                //jika i == 0 maka Masukkan nilai SP seperti yang sudah ditentukan
                //SP = (int) Math.round(SP) - 1;
                SP = (int) Math.round(SP);
            }else{
                //selanjutnya geser nilai SP sesuai nilai step yang telah didapatkan
                SP = (int) (Math.round(SP) + Math.round(step));
            }
            //masukkan nilai SP tersebut kedalam index consideredFrame yang akan digunakan
            consideredFrame[i] = (int) SP;
            System.out.print(consideredFrame[i]+", ");
        }
        
        //bentuk sinyal baru yang telah dipilih
        double[][] choosenFrame = new double[consideredFrame.length][samplePoint];
       
        //isi nilai choosenFrame
        //for displaying sakeee
        for (int i = 0; i < consideredFrame.length; i++) {
            int indexCF = consideredFrame[i];
            txtAreaTimeAlignment.append(Integer.toString(i)+"\t");
            for (int j = 0; j < mfcc.frame[0].length; j++) {
                txtAreaTimeAlignment.append(String.valueOf(mfcc.frame[indexCF][j])+"\t");
                choosenFrame[i][j] = mfcc.frame[indexCF][j];
            }
            txtAreaTimeAlignment.append("\n");
        }
        
//        System.out.println("TIME ALIGNMENT -> CHOOSEN FRAME");
//        for (int i = 0; i < consideredFrame.length; i++) {
//            for (int j = 0; j < mfcc.frame[0].length; j++) {
//                System.out.print(choosenFrame[i][j]+"\t");
//            }
//            System.out.println("");
//            
//        }

//        for (int i = 0; i < choosenFrame.length; i++) {
//            for (int j = 0; j < choosenFrame[0].length; j++) {
//                System.out.print(choosenFrame[i][j]+"\t");
//            }
//            System.out.println("");
//        }
        
        
        // WINDOWING
        double[][] windowingResult = mfcc.windowing(choosenFrame);

        for (int i = 0; i < CF; i++) {
            txtAreaWindowing.append(String.valueOf(i)+"\t");
            for (int j = 0; j < samplePoint; j++) {
                txtAreaWindowing.append(String.valueOf(formatEnam.format(windowingResult[i][j])+"\t"));
            }
            txtAreaWindowing.append("\n");
        }

        //lakukan Ekstraksi Ciri
        //FFT               
        double[][] fftResult= mfcc.fastFourierTransform(windowingResult);
        
        for (int i = 0; i < CF; i++) {
            txtAreaFFT.append(String.valueOf(i)+"\t");
            for (int j = 0; j < samplePoint; j++) {
                txtAreaFFT.append(String.valueOf(formatEnam.format(fftResult[i][j]))+"\t");
               //System.out.println("FRAME KE"+i+"FFT ke"+j+" = "+mfcc.magnitudeSemua[i][j]);
               //textareaFFT.append(i+"-"+j+"\t");
            }
            txtAreaFFT.append("\n");
        }
      
        //mel-filterbank
        //mfcc.filterbank(mfcc.magnitudeSemua, 22);
        // framerate = jumlah frame yang ada
        // samplePoint = jumlah point(sample dalam 1 frame)
        
        System.out.println("baris : "+fftResult.length+" kolom : "+fftResult[0].length);

        double[][] melFreqEnergy = mfcc.filterbank(fftResult,16000,30,130,6800);

        DecimalFormat fEnam = new DecimalFormat("#.######");
        for (int i = 0; i < melFreqEnergy.length; i++) {
            txtAreaMelFreq.append(String.valueOf(i)+"\t");
            for (int j = 0; j < melFreqEnergy[0].length; j++) {               
                txtAreaMelFreq.append(String.valueOf(fEnam.format(melFreqEnergy[i][j]))+"\t");
            }
            txtAreaMelFreq.append("\n");
        }     

        //DCT
        mfcc.discreteCosineTransform(melFreqEnergy, koefisienMFCC);
        System.out.println(mfcc.dctCepstrum.length+" "+mfcc.dctCepstrum[0].length);

        for (int i = 0; i < mfcc.dctCepstrum.length; i++) {
            txtAreaDCT.append(String.valueOf(i)+"\t");
            for (int j = 0; j < mfcc.dctCepstrum[0].length; j++) {               
               txtAreaDCT.append(String.valueOf(formatEmpat.format(mfcc.dctCepstrum[i][j]))+"\t");
            }
            txtAreaDCT.append("\n");
        }
        
        //normalisasi
        double[][] hasil = mfcc.normalisasiDanThresholding();
        //untuk nomor
        for (int i = 0; i < hasil[0].length; i++) {
            txtAreaThresholding.append(String.valueOf(i)+" \t");
        }
        txtAreaThresholding.append("\n");
        for (int i = 0; i < hasil.length; i++) {
            System.out.print(String.valueOf(i)+"\t");            
            for (int j = 0; j < hasil[0].length; j++) {
                System.out.print(String.valueOf(hasil[i][j])+" \t");
                txtAreaThresholding.append(String.valueOf(hasil[i][j]+"\t"));
            }
            txtAreaThresholding.append("\n");
            System.out.println("");
        }
        
        //return mfcc.normalisasiMFCC;
        return hasil;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtLokasiFileAudio = new javax.swing.JTextField();
        btnAnalisis = new javax.swing.JButton();
        btnBrowseFile = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        grupPreProcessing = new javax.swing.JPanel();
        tabPanePreProcessing = new javax.swing.JTabbedPane();
        panelSinyalAsli = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaSinyalAsli = new javax.swing.JTextArea();
        panelSilenceRemoval = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSilenceRemoval = new javax.swing.JTextArea();
        panelDCRemoval = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAreaDCRemoval = new javax.swing.JTextArea();
        panelPreEmphasis = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtAreaPreEmp = new javax.swing.JTextArea();
        panelFrameBlocking = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtAreaFrameBlocking = new javax.swing.JTextArea();
        panelTimeAlignment = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtAreaTimeAlignment = new javax.swing.JTextArea();
        panelWindowing = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtAreaWindowing = new javax.swing.JTextArea();
        grupEkstraksiCiri = new javax.swing.JPanel();
        tabPaneMFCC = new javax.swing.JTabbedPane();
        panelFFT = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtAreaFFT = new javax.swing.JTextArea();
        panelMelFreq = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtAreaMelFreq = new javax.swing.JTextArea();
        panelDCT = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtAreaDCT = new javax.swing.JTextArea();
        panelNormalisasi = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtAreaNormalisasi = new javax.swing.JTextArea();
        panelThresholding = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtAreaThresholding = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detil Ekstraksi Ciri");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Choose File .wav (16Khz)");

        btnAnalisis.setText("Analyze MFCC");
        btnAnalisis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalisisActionPerformed(evt);
            }
        });

        btnBrowseFile.setText("Browse File");
        btnBrowseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addComponent(txtLokasiFileAudio, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBrowseFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAnalisis)
                .addGap(82, 82, 82))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtLokasiFileAudio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAnalisis)
                    .addComponent(btnBrowseFile))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        grupPreProcessing.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "PreProcessing"));

        txtAreaSinyalAsli.setColumns(20);
        txtAreaSinyalAsli.setRows(5);
        jScrollPane1.setViewportView(txtAreaSinyalAsli);

        javax.swing.GroupLayout panelSinyalAsliLayout = new javax.swing.GroupLayout(panelSinyalAsli);
        panelSinyalAsli.setLayout(panelSinyalAsliLayout);
        panelSinyalAsliLayout.setHorizontalGroup(
            panelSinyalAsliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelSinyalAsliLayout.setVerticalGroup(
            panelSinyalAsliLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Original Signal", panelSinyalAsli);

        txtAreaSilenceRemoval.setColumns(20);
        txtAreaSilenceRemoval.setRows(5);
        jScrollPane2.setViewportView(txtAreaSilenceRemoval);

        javax.swing.GroupLayout panelSilenceRemovalLayout = new javax.swing.GroupLayout(panelSilenceRemoval);
        panelSilenceRemoval.setLayout(panelSilenceRemovalLayout);
        panelSilenceRemovalLayout.setHorizontalGroup(
            panelSilenceRemovalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelSilenceRemovalLayout.setVerticalGroup(
            panelSilenceRemovalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Silence Rmoval", panelSilenceRemoval);

        txtAreaDCRemoval.setColumns(20);
        txtAreaDCRemoval.setRows(5);
        jScrollPane3.setViewportView(txtAreaDCRemoval);

        javax.swing.GroupLayout panelDCRemovalLayout = new javax.swing.GroupLayout(panelDCRemoval);
        panelDCRemoval.setLayout(panelDCRemovalLayout);
        panelDCRemovalLayout.setHorizontalGroup(
            panelDCRemovalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelDCRemovalLayout.setVerticalGroup(
            panelDCRemovalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("DC Removal", panelDCRemoval);

        txtAreaPreEmp.setColumns(20);
        txtAreaPreEmp.setRows(5);
        jScrollPane4.setViewportView(txtAreaPreEmp);

        javax.swing.GroupLayout panelPreEmphasisLayout = new javax.swing.GroupLayout(panelPreEmphasis);
        panelPreEmphasis.setLayout(panelPreEmphasisLayout);
        panelPreEmphasisLayout.setHorizontalGroup(
            panelPreEmphasisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelPreEmphasisLayout.setVerticalGroup(
            panelPreEmphasisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Pre Emphasis", panelPreEmphasis);

        txtAreaFrameBlocking.setColumns(20);
        txtAreaFrameBlocking.setRows(5);
        jScrollPane5.setViewportView(txtAreaFrameBlocking);

        javax.swing.GroupLayout panelFrameBlockingLayout = new javax.swing.GroupLayout(panelFrameBlocking);
        panelFrameBlocking.setLayout(panelFrameBlockingLayout);
        panelFrameBlockingLayout.setHorizontalGroup(
            panelFrameBlockingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelFrameBlockingLayout.setVerticalGroup(
            panelFrameBlockingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Frame Blocking", panelFrameBlocking);

        txtAreaTimeAlignment.setColumns(20);
        txtAreaTimeAlignment.setRows(5);
        jScrollPane6.setViewportView(txtAreaTimeAlignment);

        javax.swing.GroupLayout panelTimeAlignmentLayout = new javax.swing.GroupLayout(panelTimeAlignment);
        panelTimeAlignment.setLayout(panelTimeAlignmentLayout);
        panelTimeAlignmentLayout.setHorizontalGroup(
            panelTimeAlignmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelTimeAlignmentLayout.setVerticalGroup(
            panelTimeAlignmentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Time Alignment", panelTimeAlignment);

        txtAreaWindowing.setColumns(20);
        txtAreaWindowing.setRows(5);
        jScrollPane7.setViewportView(txtAreaWindowing);

        javax.swing.GroupLayout panelWindowingLayout = new javax.swing.GroupLayout(panelWindowing);
        panelWindowing.setLayout(panelWindowingLayout);
        panelWindowingLayout.setHorizontalGroup(
            panelWindowingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        panelWindowingLayout.setVerticalGroup(
            panelWindowingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
        );

        tabPanePreProcessing.addTab("Windowing", panelWindowing);

        javax.swing.GroupLayout grupPreProcessingLayout = new javax.swing.GroupLayout(grupPreProcessing);
        grupPreProcessing.setLayout(grupPreProcessingLayout);
        grupPreProcessingLayout.setHorizontalGroup(
            grupPreProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanePreProcessing)
        );
        grupPreProcessingLayout.setVerticalGroup(
            grupPreProcessingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grupPreProcessingLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabPanePreProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        grupEkstraksiCiri.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Feature Extraction & Thresholding"));

        txtAreaFFT.setColumns(20);
        txtAreaFFT.setRows(5);
        jScrollPane9.setViewportView(txtAreaFFT);

        javax.swing.GroupLayout panelFFTLayout = new javax.swing.GroupLayout(panelFFT);
        panelFFT.setLayout(panelFFTLayout);
        panelFFTLayout.setHorizontalGroup(
            panelFFTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        panelFFTLayout.setVerticalGroup(
            panelFFTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        tabPaneMFCC.addTab("FFT", panelFFT);

        txtAreaMelFreq.setColumns(20);
        txtAreaMelFreq.setRows(5);
        jScrollPane10.setViewportView(txtAreaMelFreq);

        javax.swing.GroupLayout panelMelFreqLayout = new javax.swing.GroupLayout(panelMelFreq);
        panelMelFreq.setLayout(panelMelFreqLayout);
        panelMelFreqLayout.setHorizontalGroup(
            panelMelFreqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        panelMelFreqLayout.setVerticalGroup(
            panelMelFreqLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        tabPaneMFCC.addTab("Mel Frequency Warping", panelMelFreq);

        txtAreaDCT.setColumns(20);
        txtAreaDCT.setRows(5);
        jScrollPane11.setViewportView(txtAreaDCT);

        javax.swing.GroupLayout panelDCTLayout = new javax.swing.GroupLayout(panelDCT);
        panelDCT.setLayout(panelDCTLayout);
        panelDCTLayout.setHorizontalGroup(
            panelDCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        panelDCTLayout.setVerticalGroup(
            panelDCTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        tabPaneMFCC.addTab("DCT", panelDCT);

        txtAreaNormalisasi.setColumns(20);
        txtAreaNormalisasi.setRows(5);
        jScrollPane12.setViewportView(txtAreaNormalisasi);

        javax.swing.GroupLayout panelNormalisasiLayout = new javax.swing.GroupLayout(panelNormalisasi);
        panelNormalisasi.setLayout(panelNormalisasiLayout);
        panelNormalisasiLayout.setHorizontalGroup(
            panelNormalisasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        panelNormalisasiLayout.setVerticalGroup(
            panelNormalisasiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        tabPaneMFCC.addTab("Normalisasi", panelNormalisasi);

        txtAreaThresholding.setColumns(20);
        txtAreaThresholding.setRows(5);
        jScrollPane8.setViewportView(txtAreaThresholding);

        javax.swing.GroupLayout panelThresholdingLayout = new javax.swing.GroupLayout(panelThresholding);
        panelThresholding.setLayout(panelThresholdingLayout);
        panelThresholdingLayout.setHorizontalGroup(
            panelThresholdingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        panelThresholdingLayout.setVerticalGroup(
            panelThresholdingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
        );

        tabPaneMFCC.addTab("Thresholding", panelThresholding);

        javax.swing.GroupLayout grupEkstraksiCiriLayout = new javax.swing.GroupLayout(grupEkstraksiCiri);
        grupEkstraksiCiri.setLayout(grupEkstraksiCiriLayout);
        grupEkstraksiCiriLayout.setHorizontalGroup(
            grupEkstraksiCiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grupEkstraksiCiriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPaneMFCC))
        );
        grupEkstraksiCiriLayout.setVerticalGroup(
            grupEkstraksiCiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grupEkstraksiCiriLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabPaneMFCC))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grupPreProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(grupEkstraksiCiri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(grupPreProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(grupEkstraksiCiri, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalisisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalisisActionPerformed
        // TODO add your handling code here:
        lokasiFile = txtLokasiFileAudio.getText();
        
        hitungMFCC(lokasiFile, CF, SF, EF, MFCC_COEFF_NUMBER+1);
    }//GEN-LAST:event_btnAnalisisActionPerformed

    private void btnBrowseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseFileActionPerformed
        JFileChooser jFileChooser1 = new JFileChooser("E:\\_REKAMAN");
        jFileChooser1.showOpenDialog(null);
        File file = jFileChooser1.getSelectedFile();
        
        String filename = file.getAbsolutePath();
        txtLokasiFileAudio.setText(filename);
    }//GEN-LAST:event_btnBrowseFileActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DetilMFCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetilMFCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetilMFCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetilMFCC.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DetilMFCC().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalisis;
    private javax.swing.JButton btnBrowseFile;
    private javax.swing.JPanel grupEkstraksiCiri;
    private javax.swing.JPanel grupPreProcessing;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPanel panelDCRemoval;
    private javax.swing.JPanel panelDCT;
    private javax.swing.JPanel panelFFT;
    private javax.swing.JPanel panelFrameBlocking;
    private javax.swing.JPanel panelMelFreq;
    private javax.swing.JPanel panelNormalisasi;
    private javax.swing.JPanel panelPreEmphasis;
    private javax.swing.JPanel panelSilenceRemoval;
    private javax.swing.JPanel panelSinyalAsli;
    private javax.swing.JPanel panelThresholding;
    private javax.swing.JPanel panelTimeAlignment;
    private javax.swing.JPanel panelWindowing;
    private javax.swing.JTabbedPane tabPaneMFCC;
    private javax.swing.JTabbedPane tabPanePreProcessing;
    private javax.swing.JTextArea txtAreaDCRemoval;
    private javax.swing.JTextArea txtAreaDCT;
    private javax.swing.JTextArea txtAreaFFT;
    private javax.swing.JTextArea txtAreaFrameBlocking;
    private javax.swing.JTextArea txtAreaMelFreq;
    private javax.swing.JTextArea txtAreaNormalisasi;
    private javax.swing.JTextArea txtAreaPreEmp;
    private javax.swing.JTextArea txtAreaSilenceRemoval;
    private javax.swing.JTextArea txtAreaSinyalAsli;
    private javax.swing.JTextArea txtAreaThresholding;
    private javax.swing.JTextArea txtAreaTimeAlignment;
    private javax.swing.JTextArea txtAreaWindowing;
    private javax.swing.JTextField txtLokasiFileAudio;
    // End of variables declaration//GEN-END:variables
}

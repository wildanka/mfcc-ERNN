/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import ArrayManipulator.ArrayManipulator;
import CSVIO.CSVWriter;
import MFCCFeatureExtraction.FeatureExtraction;
import MFCCFeatureExtraction.SpeechProcessing;
import WavFileProcessor.ReadWav;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author DAN
 */
public class HalamanEkstraksiCiri extends javax.swing.JFrame {
    double EP,SP;
    private final int FREKUENSI_SAMPLING = 16000;
    int CF, koefisienMFCC;
    String lokasi = "";
    String lokasiCSVTarget = "";
    private static double[][] normal;
    private static double[][] threshold;
    
    /**
     * Creates new form HalamanEkstraksiCiri
     */
    public HalamanEkstraksiCiri() {
        initComponents();
    }

    public static void setNormal(double[][] normal) {
        HalamanEkstraksiCiri.normal = normal;
    }

    public static void setThreshold(double[][] threshold) {
        HalamanEkstraksiCiri.threshold = threshold;
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
        btnHome = new javax.swing.JButton();
        btnDetilMFCC = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        txtAreaFolderLocation = new javax.swing.JTextField();
        btnExtract = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtAreaCF = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtAreaCSVTarget = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        textAreaMFCCCoefficient = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("MFCC Feature Extraction");

        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnDetilMFCC.setText("MFCC Detail");
        btnDetilMFCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetilMFCCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnHome)
                .addGap(206, 206, 206)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDetilMFCC, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(btnDetilMFCC, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Insert folderpath which contain .wav file to be extracted");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        txtAreaFolderLocation.setText("F:\\_PENGUJIAN");

        btnExtract.setText("Extract with MFCC");
        btnExtract.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExtractActionPerformed(evt);
            }
        });

        jLabel3.setText("Considered Frame");

        jLabel4.setText("Start Frame Index(SFI)");

        jSlider1.setMajorTickSpacing(10);
        jSlider1.setMaximum(50);
        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setToolTipText("adf");
        jSlider1.setValue(5);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("-");

        jLabel6.setText("-");

        jLabel7.setText("CSV File Target (with filepath) ");

        jLabel8.setText("SF");

        jLabel9.setText("EF");

        jLabel10.setText("No. of MFCC Coefficient");

        textAreaMFCCCoefficient.setText("12");

        jButton1.setText("Browse File");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Browse...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnExtract, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtAreaCSVTarget, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAreaCF, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textAreaMFCCCoefficient, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtAreaFolderLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAreaFolderLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(textAreaMFCCCoefficient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtAreaCF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAreaCSVTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExtract)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        HalamanUtama hUtama = new HalamanUtama();
        hUtama.setVisible(true);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnExtractActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtractActionPerformed
        // TODO add your handling code here:
        //Update the parameter (if there's a change)
        SP = jSlider1.getValue() * 0.01;
        EP = (1-SP);
        CF = Integer.valueOf(txtAreaCF.getText());
        koefisienMFCC = Integer.valueOf(textAreaMFCCCoefficient.getText());
        lokasi = txtAreaFolderLocation.getText();
        lokasiCSVTarget = txtAreaCSVTarget.getText();
        int FREKUENSI_SAMPLING = 16000;
        /*
        ReadWav readwav = new ReadWav();
        readwav.bacaFile(lokasi);
        readwav.silenceRemoval(readwav.getNilaiSample(), FREKUENSI_SAMPLING);
        */
        //hitungMFCC(lokasi);
        
        
        //lakukan ekstraksi ciri terhadap semua file didalam folder
        File folder = new File(lokasi);
        File[] listOfFiles = folder.listFiles();
        File[] file = listOfFiles;
        double[][] convertArray = new double[listOfFiles.length][CF * koefisienMFCC];
        ArrayManipulator am = new ArrayManipulator();
        CSVWriter csvw = new CSVWriter();
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";

        for (int i = 0; i < listOfFiles.length; i++) {
            System.out.println(i+" ================== "+lokasi+" fileName : "+file[i].getName()+" | Path : "+file[i].getAbsolutePath());
            //simpan hasil perhitungan MFCC kedalam file CSV
            // #1 insert the result of MFCC with time alignment to 2dimensional array
//            hitungMFCC(file[i].getAbsolutePath(), CF, SP, EP, koefisienMFCC+1);
            double[] fitur = am.matrixToArray(hitungMFCC(file[i].getAbsolutePath(), CF, SP, EP, koefisienMFCC+1));
            try {
                FileWriter fileWriter = new FileWriter(lokasiCSVTarget,true);
                // #2 save the array into csvFile as 1 dimensional (1 row per 1 data sample)
                for (int j = 0; j < fitur.length; j++) {
                    fileWriter.append(String.valueOf(fitur[j]));
                    fileWriter.append(COMMA_DELIMITER);
                }
                String upTo3Characters = file[i].getName().substring(0, Math.min(file[i].getName().length(), 3));
                String upTo4Characters = file[i].getName().substring(0, Math.min(file[i].getName().length(), 4));
                String upTo5Characters = file[i].getName().substring(0, Math.min(file[i].getName().length(), 5));
                String upTo6Characters = file[i].getName().substring(0, Math.min(file[i].getName().length(), 6));
                String kelasTarget;
                if (upTo6Characters.equalsIgnoreCase("adalah")) {
                    kelasTarget = "adalah";
                }else if (upTo3Characters.equalsIgnoreCase("ada")) {
                    kelasTarget = "ada";
                }else if (upTo4Characters.equalsIgnoreCase("beri")) {
                    kelasTarget = "beri";
                }else if (upTo4Characters.equalsIgnoreCase("bisa")) {
                    kelasTarget = "bisa";
                }else if (upTo4Characters.equalsIgnoreCase("guna")) {
                    kelasTarget = "guna";
                }else if (upTo4Characters.equalsIgnoreCase("jadi")) {
                    kelasTarget = "jadi";
                }else if (upTo4Characters.equalsIgnoreCase("laku")) {
                    kelasTarget = "laku";
                }else if (upTo5Characters.equalsIgnoreCase("milik")) {
                    kelasTarget = "milik";
                }else if (upTo4Characters.equalsIgnoreCase("rupa")) {
                    kelasTarget = "rupa";
                }else{
                    kelasTarget = "sebut";
                }
                
                jTextArea1.append("Fitur MFCC dari file "+file[i].getName()+" telah didapatkan, termasuk kedalam kategori "+kelasTarget+" ==> "+i+"\n");
                fileWriter.append(kelasTarget);
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.flush();
                if (i == listOfFiles.length-1) {
                    fileWriter.close();
                }
            } catch (Exception ex) {
                Logger.getLogger(HalamanEkstraksiCiri.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("hahahahaha");
            }
            // #3 Bingo! you got the MFCC extraction result in CSVFile
        }
    }//GEN-LAST:event_btnExtractActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        // TODO add your handling code here:
        SP = jSlider1.getValue() * 0.01;
        EP = (1-SP);
        //CF = Integer.valueOf(txtAreaCF.getText());

        DecimalFormat formatDua = new DecimalFormat("#.##");
        jLabel5.setText(formatDua.format(SP));
        jLabel6.setText(formatDua.format(EP));
    }//GEN-LAST:event_jSlider1StateChanged

    private void btnDetilMFCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetilMFCCActionPerformed
        //DetilMFCC d = new DetilMFCC();
        CF = Integer.valueOf(txtAreaCF.getText());
        koefisienMFCC = Integer.valueOf(textAreaMFCCCoefficient.getText());
        DetilMFCC d = new DetilMFCC(koefisienMFCC,CF,SP,EP);
        d.setVisible(true);
    }//GEN-LAST:event_btnDetilMFCCActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (txtAreaFolderLocation.getText()==null) {
            JFileChooser jFileChooser1 = new JFileChooser("E:\\_REKAMAN");
            jFileChooser1.showOpenDialog(null);
            String filename = String.valueOf(jFileChooser1.getCurrentDirectory());
            txtAreaFolderLocation.setText(filename);
        }else{
            JFileChooser jFileChooser1 = new JFileChooser(txtAreaFolderLocation.getText());
            jFileChooser1.showOpenDialog(null);
            String filename = String.valueOf(jFileChooser1.getCurrentDirectory());
            txtAreaFolderLocation.setText(filename);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        JFileChooser jFileChooser1 = new JFileChooser();
        jFileChooser1.showOpenDialog(null);
        
        String filename = String.valueOf(jFileChooser1.getSelectedFile().getAbsolutePath());
        

        txtAreaCSVTarget.setText(filename);
    }//GEN-LAST:event_jButton2ActionPerformed

    private double[][] hitungMFCC(String lokasi, int CF, double SP, double EP, int koefisienMFCC){       
        DecimalFormat formatEmpat = new DecimalFormat("#0.0000"); //format angka
        DecimalFormat formatEnam = new DecimalFormat("#0.000000"); //format angka
        DecimalFormat formatSembilan = new DecimalFormat("#0.000000000"); //format angka
     
        //lakukan pre-processing (sampling)
        ReadWav pre = new ReadWav();
        pre.bacaFile(lokasi); 
        pre.getNilaiSample();        
        
        //output nilai sample
//        for (int i = 0; i < pre.getNilaiSample().size(); i++) {            
//            txtAreaSinyalAsli.append("("+i+") \t"+String.valueOf(formatSembilan.format(pre.getNilaiSample().get(i)))+" \t NILAI SAMPLE AWAL \n");
//        }                 

        //silencer
        ArrayList<Double> nonSilence = new ArrayList<>();
        nonSilence = pre.silenceRemoval(pre.getNilaiSample(), 16000);        
        // output hasil silence removal

//        for (int i = 0; i < nonSilence.size(); i++) {                        
//            txtAreaSilenceRemoval.append("("+i+") \t"+String.valueOf(formatSembilan.format(nonSilence.get(i)))+"\t HASIL SILENCE REMOVAL \n");
//        }
        
        //lakukan Speech processing
        SpeechProcessing sp = new SpeechProcessing();
        
        //just for displaying dcRemoval Result Signal sake!!!!
        ArrayList<Double> DCRemovalSignal = sp.dcRemoval(nonSilence);
        
//        for (int i = 0; i < DCRemovalSignal.size(); i++) {
//            txtAreaDCRemoval.append("("+i+") \t"+String.valueOf(formatSembilan.format(DCRemovalSignal.get(i)))+"\t HASIL SILENCE REMOVAL -> DC REMOVAL\n");
//        }
        
        // DC REMOVAL - PRE EMPHASIS 
        ArrayList<Double> preEmphasisSignal = sp.preEmphasis(DCRemovalSignal);
         
//        for (int i = 0; i < preEmphasisSignal.size(); i++) {
//            txtAreaPreEmp.append("("+i+") \t"+String.valueOf(formatSembilan.format(preEmphasisSignal.get(i)))+"\t HASIL DC REMOVAL -> PRE EMPHASIS \n");
//        }

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
//            txtAreaFrameBlocking.append(String.valueOf(i)+"\t");
            for (int j = start; j < start+samplePoint; j++) {
                if (preEmphasisSignal!=null) {
                    mfcc.frame[i][j-start] = preEmphasisSignal.get(j);
                } else {
                    mfcc.frame[i][j-start] = 0.0;
                }
//                txtAreaFrameBlocking.append(String.valueOf(formatEmpat.format(mfcc.frame[i][j-start]))+"\t");
            }
//            txtAreaFrameBlocking.append("\n");
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
//            txtAreaTimeAlignment.append(Integer.toString(i)+"\t");
            for (int j = 0; j < mfcc.frame[0].length; j++) {
//                txtAreaTimeAlignment.append(String.valueOf(mfcc.frame[indexCF][j])+"\t");
                choosenFrame[i][j] = mfcc.frame[indexCF][j];
            }
//            txtAreaTimeAlignment.append("\n");
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

//        for (int i = 0; i < CF; i++) {
//            txtAreaWindowing.append(String.valueOf(i)+"\t");
//            for (int j = 0; j < samplePoint; j++) {
//                txtAreaWindowing.append(String.valueOf(formatEnam.format(windowingResult[i][j])+"\t"));
//            }
//            txtAreaWindowing.append("\n");
//        }

        //lakukan Ekstraksi Ciri
        //FFT               
        double[][] fftResult= mfcc.fastFourierTransform(windowingResult);
        
//        for (int i = 0; i < CF; i++) {
//            txtAreaFFT.append(String.valueOf(i)+"\t");
//            for (int j = 0; j < samplePoint; j++) {
//                txtAreaFFT.append(String.valueOf(formatEnam.format(fftResult[i][j]))+"\t");
//               //System.out.println("FRAME KE"+i+"FFT ke"+j+" = "+mfcc.magnitudeSemua[i][j]);
//               //textareaFFT.append(i+"-"+j+"\t");
//            }
//            txtAreaFFT.append("\n");
//        }
      
        //mel-filterbank
        //mfcc.filterbank(mfcc.magnitudeSemua, 22);
        // framerate = jumlah frame yang ada
        // samplePoint = jumlah point(sample dalam 1 frame)
        
        System.out.println("baris : "+fftResult.length+" kolom : "+fftResult[0].length);

        double[][] melFreqEnergy = mfcc.filterbank(fftResult,16000,30,130,6800);

//        DecimalFormat fEnam = new DecimalFormat("#.######");
//        for (int i = 0; i < melFreqEnergy.length; i++) {
//            txtAreaMelFreq.append(String.valueOf(i)+"\t");
//            for (int j = 0; j < melFreqEnergy[0].length; j++) {               
//                txtAreaMelFreq.append(String.valueOf(fEnam.format(melFreqEnergy[i][j]))+"\t");
//            }
//            txtAreaMelFreq.append("\n");
//        }     

        //DCT
        mfcc.discreteCosineTransform(melFreqEnergy, koefisienMFCC);
        System.out.println(mfcc.dctCepstrum.length+" "+mfcc.dctCepstrum[0].length);

//        for (int i = 0; i < mfcc.dctCepstrum.length; i++) {
//            txtAreaDCT.append(String.valueOf(i)+"\t");
//            for (int j = 0; j < mfcc.dctCepstrum[0].length; j++) {               
//               txtAreaDCT.append(String.valueOf(formatEmpat.format(mfcc.dctCepstrum[i][j]))+"\t");
//            }
//            txtAreaDCT.append("\n");
//        }
        
        //normalisasi
        mfcc.normalisasiDanThresholding();
//        for (int i = 0; i < hasil.length; i++) {
//            System.out.print(String.valueOf(i)+"\t");            
//            for (int j = 0; j < hasil[0].length; j++) {
//                System.out.print(String.valueOf(hasil[i][j])+" \t");
//                txtAreaThresholding.append(String.valueOf(hasil[i][j]+"\t"));
//            }
//            txtAreaThresholding.append("\n");
//            System.out.println("");
//        }
        
        //return mfcc.normalisasiMFCC;
        return threshold;
    }
    
    
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
            java.util.logging.Logger.getLogger(HalamanEkstraksiCiri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalamanEkstraksiCiri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalamanEkstraksiCiri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalamanEkstraksiCiri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalamanEkstraksiCiri().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetilMFCC;
    private javax.swing.JButton btnExtract;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField textAreaMFCCCoefficient;
    private javax.swing.JTextField txtAreaCF;
    private javax.swing.JTextField txtAreaCSVTarget;
    private javax.swing.JTextField txtAreaFolderLocation;
    // End of variables declaration//GEN-END:variables
}

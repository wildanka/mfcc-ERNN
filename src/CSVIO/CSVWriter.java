/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSVIO;

import java.io.FileWriter;
import java.util.Arrays;

public class CSVWriter {
    String COMMA_DELIMITER = ",";
    String NEW_LINE_SEPARATOR = "\n";
    //String FILE_HEADER="1,2,3,4,5";
    
    public void tulis2D(String lokasi, double[][] data) throws Exception {        
        FileWriter fileWriter = new FileWriter(lokasi);
        for (int i = 0; i < data.length; i++) {                    
            for (int h = 0; h < data[0].length; h++) {
                fileWriter.append(String.valueOf(data[i][h]));
                fileWriter.append(COMMA_DELIMITER);
            }            
            fileWriter.append(NEW_LINE_SEPARATOR);
        }
        fileWriter.flush();
        fileWriter.close();
    }

    public void tulis1D(String lokasi, double[] data) throws Exception {        
        FileWriter fileWriter = new FileWriter(lokasi);
        for (int i = 0; i < data.length; i++) {                    
                fileWriter.append(String.valueOf(data[i]));
                fileWriter.append(COMMA_DELIMITER);
        }
        fileWriter.flush();
        fileWriter.close();
    }
    
    public void writeMatrixInARow(String lokasi, double[][] data)throws Exception{
        FileWriter fileWriter = new FileWriter(lokasi);
        for (int i = 0; i < data.length; i++) {                    
            for (int h = 0; h < data[0].length; h++) {
                fileWriter.append(String.valueOf(data[i][h]));
                fileWriter.append(COMMA_DELIMITER);
            }            
        }
        fileWriter.flush();
        fileWriter.close();        
    }
}

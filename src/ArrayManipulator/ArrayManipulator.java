/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ArrayManipulator;

/**
 *
 * @author DAN
 */
public class ArrayManipulator {
    public double[] matrixToArray(double[][] data){        
        double[] converted = new double[data.length  * data[0].length];
        int last = 0; //the pointer of the last index that has been inputed into 1d array
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                System.out.println(last);
                converted[last] = data[i][j];
                last++;
            }
        }        
        return converted;
    }
}

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
    
    /**
     *
     * @param data matrix (array 2 dimensi) yang akan diconvert kedalam array 1 dimensi
     * @return array 1 dimensi dri matrix tersebut secara berurutan
     */
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
    
    /**
     *
     * @param data matrix (array 2 dimensi) yang akan diconvert kedalam array 3 dimensi
     * @param timestep
     * @param row
     * @param column
     * @return array 3 dimensi dari matrix yang diinput
     */
    public double[][][] array2Dto3D(double[][] data, int timestep, int row, int column){
        double array3D[][][] = new double[timestep][row][column];
        
        int indexRow = 0;
        int indexColumn = 0;
        int splitBy = data.length / timestep;
        System.out.println(splitBy);
        for (int i = 0; i < timestep; i++) {
//            System.out.println("TIMESTEP = "+i);
            int lastIndex = splitBy*i;
//            System.out.println(lastIndex);
            for (int j = 0; j < splitBy; j++) {
                for (int k = 0; k < column; k++) {
                    array3D[i][j][k] = data[j+lastIndex][k];
//                    System.out.print(array3D[i][j][k]+" \t");
                }
//                System.out.println("");
            }            
        }
        return array3D;
    }
}

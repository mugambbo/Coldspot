package hotspot;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Mugambbo
 */
public class Secure {
    String [] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "/", "\\", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "<", ">", ",", ".", ";", ":", "\"", "'", "?", "{", "}", "[", "]", "|", "`", "~", " "};
    //, "\\", "(", ")", "@", "#", "%"
    int [] numbers = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78};
    private ArrayList alphabetsList;
    public String serial (String name){
        String cName = name.toUpperCase();
        String [] newCName = new String [cName.length()];
        for (int i = 0; i < cName.length(); i++){
            for (int j = 0; j <alphabets.length; j++){
                if (cName.substring(i, i+1).equals(alphabets[j])){
                    newCName [i] = Integer.toString(numbers[j]);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < cName.length(); k++){
//            System.out.print(newCName[k]+", ");
            sb.append(newCName[k]);
        }
        String serial = sb.toString();
//        System.out.println(serial);
        return serial;
    }
    
    public String key (String serial){
        String [] name = new String [serial.length()/2];        
        for (int i = 0; i < serial.length(); i+=2){
            String k = serial.substring(i, i+2);
            int l = Integer.parseInt(k);
            for (int j = 0; j <numbers.length; j++){
                if (numbers[j] == l){
                    name [i/2] = alphabets[j];
                }
            }            
        }
        
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < name.length; k++){
            sb.append(name[k]);
        }
        String keyed = sb.toString();
        return keyed;
    }
    
    public String noToAlphabets (String no){
        StringBuilder toAlphabets = new StringBuilder();
        
        for (int i = 0; i < no.length(); i++){
            toAlphabets.append(alphabets[Integer.parseInt(no.substring(i, i+1))]);
        }
        
        return toAlphabets.toString();
    }
    public String alphabetsToNo (String alphabets){
        alphabetsList = new ArrayList();
        alphabetsList.addAll(Arrays.asList(this.alphabets));
        StringBuilder toNo = new StringBuilder();
        
        for (int i = 0; i < alphabets.length(); i++){
            toNo.append(alphabetsList.indexOf(alphabets.substring(i, i+1)));
        }
        
        return toNo.toString();
    }
    
    public String convertFromBaseToBase (String number, int base1, int base2){
        String equivalent = null; 
        try {
            equivalent = Integer.toString(Integer.parseInt(number, base1), base2);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Invalid Key Entered", "Coldspot: Key Error", JOptionPane.ERROR_MESSAGE);
        }        
        return equivalent;
    }
}

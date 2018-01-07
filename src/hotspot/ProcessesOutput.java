/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hotspot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Mugambbo
 */
public class ProcessesOutput {
    public String output (Process process) throws IOException{
        String line;
        StringBuilder sb;
        BufferedReader bre;
        try (BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            sb = new StringBuilder();
            bre = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line=bri.readLine())!=null){
                sb.append(line).append("\n");
            }
            while ((line = bre.readLine())!=null){
                sb.append(line).append("\n");                  
            }
        }
                bre.close();          
                return sb.toString();
    }
}

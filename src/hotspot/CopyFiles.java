package hotspot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import javax.swing.JOptionPane;

public class CopyFiles {
    public void copyStream (File source, File dest) throws FileNotFoundException, IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte [] buf = new byte [1024];
            int bytesRead;
            while ((bytesRead = input.read(buf))>0){
                output.write(buf, 0, bytesRead);
            }
        }
        finally {
        input.close();
        output.close();
    }
    }
    public void copyStream (InputStream source, File dest) throws FileNotFoundException, IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = source;
            output = new FileOutputStream(dest);
            byte [] buf = new byte [1024];
            int bytesRead;
            while ((bytesRead = input.read(buf))>0){
                output.write(buf, 0, bytesRead);
            }
        }
        finally {
        input.close();
        output.close();
    }
    }
    public void copyStreamRecursively (InputStream source, File dest) throws FileNotFoundException, IOException {
        OutputStream output = null;
        File src = new File (source.toString());
        if(src.isDirectory()){
            if(!dest.exists()){
                dest.mkdir();
            }
            String files[] = new File (source.toString()).list();
            for (String file:files){
                File srcFile = new File (src, file);
                File destFile = new File (dest, file);
                InputStream is = new FileInputStream (srcFile);
                copyStreamRecursively(is, destFile);
                
            }
        }
        else {
            try {
                output = new FileOutputStream(dest);
                byte [] buf = new byte [1024];
                int bytesRead;
                while ((bytesRead = source.read(buf))>0){
                    output.write(buf, 0, bytesRead);
                }
            }
            finally {
                source.close();
                output.close();
            }            
        }

    }
    public void copyChannels (File source, File dest) throws FileNotFoundException, IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
        finally {
        inputChannel.close();
        outputChannel.close();
    }
    }
    public void copyChannels (FileInputStream source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = source.getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
        catch (IOException err){
            JOptionPane.showMessageDialog(null, "Impossible: "+err.getMessage());
        }
        finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
    
    
}

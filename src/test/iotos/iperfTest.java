package test.iotos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class iperfTest {
    /**
     * run on the node, which link is single hop to the "10.0.0.1" Network Interface Card
     */
    public static void main(String args[]) {
        String cmd = "iperf -c 10.0.0.1";
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream inputStream = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine())!= null){
                    System.out.println(line);
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }
}
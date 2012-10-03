import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class ComputeRTT {


	public static int rtt_finder(){
    double avgRTT =0;
	String ip = sClient_Communicator.tail_ip;
	String pingResult = "";

	String pingCmd = "ping " + ip;
    int num_iter = 10; // gather 10 samples to take average
	try {
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(pingCmd);

		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String inputLine;

        inputLine = in.readLine();
        for(int i=0;i<num_iter;i++){
            if((inputLine = in.readLine()) != null){
               // System.out.println(inputLine);
                pingResult += inputLine;
                StringTokenizer strtok = new StringTokenizer(inputLine);
                String nextStr="";
                do{
                    nextStr = strtok.nextToken();
                    //System.out.println(nextStr);
                    if(nextStr.startsWith("time")){
                        avgRTT += Double.parseDouble(nextStr.substring(5));
                        //System.out.println(""+avgRTT+"ms");
                        break;
                    }
                }while(!nextStr.startsWith("time"));
            }
        }
        avgRTT /= num_iter;
        System.out.println("RTT to "+ ip+":"+avgRTT+"ms");
		in.close();
	}//try
	catch (IOException e) {
		System.out.println(e);
	}
        catch(NoSuchElementException e){
                System.out.println(e);
        }
        return (int)(avgRTT);
	}
}
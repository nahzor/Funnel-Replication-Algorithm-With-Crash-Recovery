/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/23/12
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.net.*;
import java.util.*;

class message implements Serializable
{
    int msg_seq;
    int client_id;
    String message;
    int mtype;
    String head_ip;
    String head_port;
    int tail_check;
    int timestamp;
    int head_ts;
    String Sender_id;
    String head_id;
    int tail_ts;

    public message(int mseq, int cid, String msg,int type, String hip,
                   String hport,int tailc,int ts,int hts,String sid,String hid,int tts)
    {
        msg_seq=mseq;
        client_id=cid;
        message=msg;
        mtype=type;
        head_ip=hip;
        head_port=hport;
        timestamp=ts;
        tail_check=tailc;
        head_ts = hts;
        Sender_id=sid;
        head_id=hid;
        tail_ts=tts;

    }


}

class crashlog extends Thread
{
    String port;
    ServerSocket welcomeSocket;
    public crashlog(String p)
    {
        port=new String(p);
    }

    public void run()
    {
        try {
            welcomeSocket = new ServerSocket(Integer.parseInt(port));


        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            int snum=Integer.parseInt(in.readLine());
             String ips=null,port=null;
            int tempk=0;
            ///////////////////////
                           try {
    BufferedReader input =  new BufferedReader(new FileReader("../p2s/cfg.txt"));
                 try {
                        String line = null;
                        while (tempk<snum){
                        line = input.readLine();
                        ips=new String(line);
                        line = input.readLine();
                        port=new String(line);
                            tempk++;
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
            ///////////////////////

            System.out.println("This server crashed :: "+ips+" : "+port);

           for(int i=0;i<client.s_ip.size();i++)
         {
             if(client.s_ip.get(i).equals(ips) && client.s_port.get(i).equals(port))
             {
                 client.s_ip.remove(i);
                 client.s_port.remove(i);
             }
         }

            Iterator iter=client.s_port.iterator();

            while(iter.hasNext())
            {
                System.out.println("#### : "+iter.next());

            }

        }
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}

public class client
{
        public static ArrayList<String> s_ip = new ArrayList<String>();
        public static ArrayList<String> s_port = new ArrayList<String>();
        static int ClientID,serport;
        public static String message_val;
       public static void main(String args[]) throws Exception
       {
           Random rand=new Random();
           int rand_int;
           int rw_rand;


           System.out.println("Enter ClientID : ");
           BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
           ClientID=Integer.parseInt(inFromUser.readLine());

           System.out.println("Enter Client Server port number : ");
           BufferedReader inFromUser1 = new BufferedReader( new InputStreamReader(System.in));
           serport=Integer.parseInt(inFromUser.readLine());

           FileWriter outFile1 = new FileWriter("cser.txt",true);

           PrintWriter out1 = new PrintWriter(outFile1);

              try{
                 InetAddress ownIP=InetAddress.getLocalHost();
                 System.out.println("IP of my system is := "+ownIP.getHostAddress());
                  out1.println(ownIP.getHostAddress());
                 }catch (Exception e){
                 System.out.println("Exception caught ="+e.getMessage());
                 }
           out1.println(serport);
           out1.close();

        crashlog clog=new crashlog((""+serport));
        Thread thread= new Thread(clog);
              thread.start();

               try {
             BufferedReader input =  new BufferedReader(new FileReader("../p2s/cfg.txt"));
                 try {
                        String line = null;
                        while (( line = input.readLine()) != null){
                        s_ip.add(line);
                        line = input.readLine();
                        s_port.add(line);
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

           FileWriter outFile = new FileWriter("client_sync.txt",true);
           PrintWriter out = new PrintWriter(outFile);
           System.out.println("Enter your Message:");
           BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
           message_val= in.readLine();
           out.println("client");out.close();
            int count=0; String tline;
            while(count<2)
            {
             count=0;
             BufferedReader input =  new BufferedReader(new FileReader("client_sync.txt"));
              while (( tline = input.readLine()) != null){ count++;  }

            }


            int k=0;
           while(k<1000)
           {
              // for(int temp=0;temp<10000;temp++);
            k++;
            System.out.println("<<<< K : "+ k +" >>>>>>");
            System.out.println("Selecting a random server..");
            rand_int=rand.nextInt(1000);
            rw_rand=1;//rand.nextInt(1000)%2; // 0=read, 1=write
            rand_int=rand_int%(s_ip.size()-1);
            System.out.println(s_ip.get(rand_int)+"/"+Integer.parseInt(s_port.get(rand_int)));
           try{
            Socket clientSocket = new Socket(s_ip.get(rand_int), Integer.parseInt(s_port.get(rand_int)));
               clientSocket.setSoTimeout(10*1000);
               ObjectOutputStream oos= new ObjectOutputStream(clientSocket.getOutputStream());
               oos.flush();
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

           // ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream()) ;
          //  Socket sock=new Socket();
            message m=new message(0,ClientID,message_val,rw_rand
                    ,s_ip.get(rand_int),s_port.get(rand_int),0,0,0,"","",0);

            System.out.println("Sending Request:" + rw_rand);
            oos.writeObject(m);
            oos.flush();
               try{
            String modifiedSentence = (String) inFromServer.readObject();
            System.out.println("FROM SERVER: " + modifiedSentence);
               }catch(Exception ee){
                   System.out.println("RECEIVE TIMEOUT");}
            oos.close();
            inFromServer.close();
            clientSocket.close();
           }catch (Exception eee){
               System.out.println("FINAL TIMEOUT");

           }
               System.out.println("###################################");
           }

       }


}


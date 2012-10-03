/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/23/12
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class cserver {
    public static String fname;
    String port1;
    public static int time=0;
    public static FileWriter fw;
    public static Request_Array ra=new Request_Array();
    public static ArrayList<Integer> xy_mseq= new ArrayList<Integer>();
    public static ArrayList<ObjectOutputStream> xy_moos=new ArrayList<ObjectOutputStream>();
    public static ArrayList<Integer> xy_count=new ArrayList<Integer>();

    public cserver(String f, String p) throws IOException {
        fname=f;
        port1=p;
        System.out.println("ROSHAN : "+fname);
        fw = new FileWriter(fname,true);

            System.out.println("Clearing file :"+fname);
            File yourFile = new File(fname);
            yourFile.delete();
            File yourNewFile = new File(fname);
            yourNewFile.createNewFile();

    }

    public static synchronized void xy_edit(int mseq,ObjectOutputStream o,int count, message cdata, int flag) throws IOException {
        if (flag==0)    //add
        {
             xy_mseq.add(mseq);
             xy_moos.add(o);
             xy_count.add(count);
        }
        if(flag==1)       //write
        {
            String clientSentence="xyz";
            if(cserver.xy_count.get(cserver.xy_mseq.indexOf(cdata.msg_seq))<3-Vector_Handle.sub_val)
            {
                int p=cserver.xy_count.get(cserver.xy_mseq.indexOf(cdata.msg_seq));
                p++;
                System.out.println("pval:"+p);
                cserver.xy_count.set(cserver.xy_mseq.indexOf(cdata.msg_seq), p);
            }
            if(cserver.xy_count.get(cserver.xy_mseq.indexOf(cdata.msg_seq))>=3-Vector_Handle.sub_val)
            {
               System.out.println("Write Request Received:3");
                xyWriter.xy_write(0,cdata);

             clientSentence="WRITE_COMPLETE";

          System.out.println("B4 ) Seq:Sock:Count="+cserver.xy_mseq.size()+
                  ":"+cserver.xy_moos.size()+":"+cserver.xy_count.size());
             cserver.ra.remove_from_it(cdata);
            cserver.sock_send(cdata,clientSentence);
                cserver.xy_moos.remove(cserver.get_sock(cdata.msg_seq));

                cserver.xy_count.remove(cserver.xy_mseq.indexOf(cdata.msg_seq));
               cserver.xy_mseq.remove(cserver.xy_mseq.indexOf(cdata.msg_seq));

                System.out.println("Aft ) Seq:Sock:Count="+cserver.xy_mseq.size()+
                  ":"+cserver.xy_moos.size()+":"+cserver.xy_count.size());

            }

        }

        if(flag==2)   //
        {
                           cserver.xy_moos.remove(cserver.get_sock(cdata.msg_seq));
                           cserver.xy_count.remove(cserver.xy_mseq.indexOf(cdata.msg_seq));
                           cserver.xy_mseq.remove(cserver.xy_mseq.indexOf(cdata.msg_seq));
        }

    }

    public void Client_Handler() throws Exception {


            Random r=new Random(100000);
            ServerSocket welcomeSocket;
            String clientSentence;
            String capitalizedSentence;
            String line = null;

           FileWriter outFile = new FileWriter("cfg.txt",true);


           PrintWriter out = new PrintWriter(outFile);

             welcomeSocket = new ServerSocket(Integer.parseInt(port1));
              try{
                 InetAddress ownIP=InetAddress.getLocalHost();
                 //System.out.println("IP of my system is := "+ownIP.getHostAddress());
                  out.println(ownIP.getHostAddress());
                 }catch (Exception e){
                 System.out.println("Exception caught ="+e.getMessage());
                 }
           out.println(port1);
           out.close();

        //Start server for client to communicate

         while(true)
         {
             System.out.println("Waiting for Request..");
            Socket connectionSocket = welcomeSocket.accept();
            ObjectInputStream ois=new ObjectInputStream(connectionSocket.getInputStream()) ;

           ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());

            message cdata=(message)ois.readObject();

            //Have to send it to all the servers
            sClient_Communicator scom=new sClient_Communicator();
            cdata.tail_check=0;
            time++;
             cdata.head_ts=time;
             cdata.Sender_id=cHandle_Thread.SerID;
             cdata.msg_seq=r.nextInt(100000);
             cdata.head_id=cHandle_Thread.SerID;
             cdata.head_ip=sClient_Communicator.myip;
             cdata.head_port=sClient_Communicator.myPort;

            if(cdata.mtype==1)
            {
             ra.add_to_it(cdata);
             xy_edit(cdata.msg_seq,outToClient,0,cdata,0);
             scom.Sender(cdata);
            }
            else if(cdata.mtype==0)
            {
                if(ra.arrlt.size()>0)
                {
                    cdata.mtype=3;
                    cdata.tail_check=0;
                    xy_edit(cdata.msg_seq, outToClient, 0,cdata, 0);
                    scom.Tail_Sender(cdata);

                }
                else
                {
                   clientSentence="NO_VALUE";
                    System.out.println("Read request received");
                  try {
                    BufferedReader input =
                       new BufferedReader(new FileReader(fname));
                        try {

                        while (( line = input.readLine()) != null){
                                    clientSentence=line;
                                     }
                            }
                         finally {
                       input.close();
                                 }
                            }
                         catch (IOException ex){
                          ex.printStackTrace();
                        }

            System.out.println("Request Completed. Replying to Client : " + clientSentence);
            outToClient.writeObject(clientSentence);
             outToClient.flush();
                   // outToClient.close();
                }

            }

         }
    }

    public static ObjectOutputStream get_sock(int m)
    {                                    int k=0;
        for(int i=0;i<xy_mseq.size();i++)
        {
           if(xy_mseq.get(i)==m){
               System.out.println("Got the SOCKET");
               k=i;break;}
        }
      return xy_moos.get(k);
    }

    public static void sock_send(message cdata,String clientSentence) throws IOException {
         System.out.println("Sending:"+clientSentence);
         ObjectOutputStream otc = get_sock(cdata.msg_seq);
         otc.writeObject(clientSentence);
                        otc.flush();
       // otc.close();
        System.out.println("MSG sent:"+clientSentence);
    }

}


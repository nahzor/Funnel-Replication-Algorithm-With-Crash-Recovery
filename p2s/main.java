import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/25/12
 * Time: 2:04 AM
 * To change this template use File | Settings | File Templates.
 */


class cHandle_Thread extends Thread
{
   String fn;
   String port1;
    public static String SerID;

   public cHandle_Thread() throws IOException {
       System.out.println("Choose file (1/2/3/4/5) : ");
       BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
       SerID= in.readLine();
       fn="f"+SerID+".txt";

       System.out.println("Enter cServer port number:");
       port1=in.readLine();
   }

    public void run()
    {
        cserver s= null;
        try {
            s = new cserver(fn,port1);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            s.Client_Handler();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}

class sHandle_Server_Thread extends Thread
{
    String pnum1;
    public sHandle_Server_Thread(String p) throws IOException {
        pnum1=p;
    }

    public void run()
    {

        try {
            sServer ss=new sServer(pnum1);
            ss.sSer();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

class sSendFunc extends Thread
{
    public sSendFunc() {
    }

    public void run()
    {
        while(true)
        {
        message temp=new message(-1,0,"",0,"","",0,0,0,"","",0);
        try {
            xyWriter.xy_write(1,temp);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
         }
    }
}
////////////////////////

class sComm extends Thread
{
   String portval;

    public sComm(String p)
    {
        portval=p;
    }

    public void run()
    {
        sClient_Communicator scc= null;
        try {
            scc = new sClient_Communicator(portval);
        } catch (UnknownHostException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            scc.Communicator();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}

class masterComm extends Thread
{
    String masterip;
    String masterport;
    public masterComm()
    {

    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new FileReader("../p2ms/master.txt"));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    masterip = new String(line);
                    line = input.readLine();
                    masterport = new String(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(masterip + " : " + masterport);

        Socket clientSocket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream inFromServer = null;
        try {
            clientSocket = new Socket(masterip, Integer.parseInt(masterport));
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

          while(true)
          {
              try {
                  oos.writeObject("hi");
                  oos.flush();
              } catch (IOException e) {
                  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

          }

    }

}    }


///////////%%%%%%%%%%%%%%%%%%

class masterReceiver extends Thread
{
    String port;
    ServerSocket welcomeSocket;
    public static String ipp;
    public static String portp;
    public static int crasher=0;
    public masterReceiver(String p)
    {
        port=new String(p);
    }

    public void run()
    {

        FileWriter outFile = null;
        try {
            outFile = new FileWriter("cfg4master.txt",true);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        PrintWriter out = new PrintWriter(outFile);

              try{
                 InetAddress ownIP=InetAddress.getLocalHost();
                 System.out.println("IP of my system is := "+ownIP.getHostAddress());
                  out.println(ownIP.getHostAddress());
                 }catch (Exception e){
                 System.out.println("Exception caught ="+e.getMessage());
                 }
           out.println(port);
           out.close();

        try {
            welcomeSocket = new ServerSocket(Integer.parseInt(port));


        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            int snum=Integer.parseInt(in.readLine());
            crasher=snum;
             String ips=null,port=null;
            int tempk=0;
            ///////////////////////
                           try {
    BufferedReader input =  new BufferedReader(new FileReader("../p2s/cfg4ser.txt"));
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
              if(ips.equals(sClient_Communicator.tail_ip) && port.equals(sClient_Communicator.tail_port))
            {  Vector_Handle.sub_val++;
                System.out.println("THE TAIL SERVER CRASHED !!");
                if(Integer.parseInt(cHandle_Thread.SerID)==4)
                {
                    //i am the replacement
                    System.out.println("M THE NEW TAIL");
                    sClient_Communicator.tail_ip=new String(sClient_Communicator.myip);
                    sClient_Communicator.tail_port=new String(sClient_Communicator.myPort);


                }
                else
                {
                    //not the replacement

                    sClient_Communicator.tail_ip=sClient_Communicator.s_ip.remove(sClient_Communicator.s_ip.size()-1);
                    sClient_Communicator.tail_port=sClient_Communicator.s_port.remove(sClient_Communicator.s_port.size()-1);
                    System.out.println("THE NEW TAIL IS : "+sClient_Communicator.tail_ip+":"+sClient_Communicator.tail_port);

                     cserver.ra.read_and_send();
                    //scom.Tail_Sender(cdata);
                }

            }
            else
              {
                  System.out.println("A SERVER CRASHED!!!!!");
            Vector_Handle.sub_val++;
            //what to do here?
        for(int i=0;i<sClient_Communicator.s_ip.size();i++)
         {
             System.out.println("Checking : "+sClient_Communicator.s_port.get(i) + ":"+port);
             if(sClient_Communicator.s_ip.get(i).equals(ips) && sClient_Communicator.s_port.get(i).equals(port))
             {
                 System.out.println("Removing from s_port : "+sClient_Communicator.s_port.get(i));
                 sClient_Communicator.s_ip.remove(i);
                 sClient_Communicator.s_port.remove(i);
             }
         }

            if(sClient_Communicator.myip.equals(sClient_Communicator.tail_ip) && sClient_Communicator.myPort.equals(sClient_Communicator.tail_port))
            {
                System.out.println("M TAIL");
                message mtemp=null;
                sServer.vh.vf_choice(mtemp,3);
            }
            else
            {
                System.out.println("NOT TAIL");
                for(int i=0;i<10*ComputeRTT.rtt_finder()*Integer.parseInt(cHandle_Thread.SerID);i++); //temporary RTT waiting
                if(Integer.parseInt(cHandle_Thread.SerID)==3)
                {
                    //own and forward
               for(int i=0;i<cserver.ra.arrlt.size();i++)
                 {
               if(Integer.parseInt(cserver.ra.arrlt.get(i).head_id)==crasher)
                    {
                System.out.println("Got IT!!!!@@");
                        cserver.ra.arrlt.get(i).head_id=cHandle_Thread.SerID;
                        cserver.ra.arrlt.get(i).head_ip=sClient_Communicator.myip;
                      }
                       }
                    //change in xy also and forward

                }
                else
                {
                     //search and delete
            for(int i=0;i<Request_Array.arrlt.size();i++)
                 {
            if(Integer.parseInt(Request_Array.arrlt.get(i).head_id)==crasher)
            {
                System.out.println("Got IT!!");
                Request_Array.arrlt.remove(i);break;
            }
             }

             //removing from xy_list
                    xyWriter.xy_write(2,null);


                }

            }
        }//if
        } //while
        }   //try
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}

//////////%%%%%%%%%%%%%%%%%%%%

public class main {

    public static void main(String args[]) throws Exception
    {

        cHandle_Thread cht=new cHandle_Thread();
        Thread t1=new Thread(cht);

        sSendFunc ssf=new sSendFunc();
        Thread t4=new Thread(ssf);

        System.out.println("Enter port number to communicate with the master : ");
        BufferedReader in1=new BufferedReader(new InputStreamReader(System.in));
        String master_port=in1.readLine();

        masterReceiver mr = new masterReceiver(master_port);
        Thread t6=new Thread(mr);

        System.out.println("Enter sServer port num:");
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String pnum1=in.readLine();

        sHandle_Server_Thread shst=new sHandle_Server_Thread(pnum1);
        Thread t2=new Thread(shst);

        sComm sc=new sComm(pnum1);
        Thread t3= new Thread(sc);

        masterComm mc=new masterComm();
        Thread t5= new Thread(mc);

        t1.start();    //cHandle
        t2.start();    //sHandle_Srever
        t3.start();    //scomm
        t4.start();    //sSendFunc
        t5.start();    //masterComm
        t6.start();    //master receiver

    }

}


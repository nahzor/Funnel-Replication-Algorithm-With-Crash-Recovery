/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/25/12
 * Time: 2:04 AM
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;



public class sServer {
            String pnum;
            ServerSocket sersock;
            public static Vector_Handle vh=new Vector_Handle();

    class checker extends Thread
{
  public checker(){}

  public void run()
  {
      message temp=new message(-1,0,"",0,"","",0,0,0,"","",0);
      while(true)
      {
          try {
              vh.vf_choice(temp,2);
          } catch (Exception e) {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
          }
      }
  }

}

            public sServer(String p) throws IOException {
                pnum=p;

           FileWriter outFile = new FileWriter("cfg4ser.txt",true);

           PrintWriter out = new PrintWriter(outFile);

              try{
                 InetAddress ownIP=InetAddress.getLocalHost();
                 System.out.println("IP of my system is := "+ownIP.getHostAddress());
                  out.println(ownIP.getHostAddress());
                 }catch (Exception e){
                 System.out.println("Exception caught ="+e.getMessage());
                 }
           out.println(pnum);
           out.close();

            }

    public sServer(){}



    public void sSer() throws IOException, Exception {

        ServerSocket ssock=new ServerSocket(Integer.parseInt(pnum));

        checker chkr=new checker();
        Thread trd=new Thread(chkr);
        trd.start();



        while(true)
        {
            Socket sercom=ssock.accept();
            ObjectInputStream ois=new ObjectInputStream(sercom.getInputStream()) ;

            ObjectOutputStream outToClient = new ObjectOutputStream(sercom.getOutputStream());

            message cdata;
            cdata = (message)ois.readObject();
            System.out.println("Received: " + cdata.message + cdata.client_id);
            sClient_Communicator s=new sClient_Communicator();

          if(!s.my_tail())      //non tail section
          {
            if(cdata.tail_check==1)       //
            {

                //tail sent the message
                System.out.println("Got reply from tail : " + cdata.tail_ts);
                        if(cdata.mtype==1)
                             {

                                 xyWriter.xy_write(0,cdata);
                              }
                sClient_Communicator sccom=new sClient_Communicator();
                cdata.tail_check=2;
                cserver.ra.remove_from_it(cdata);
                sccom.head_Sender(cdata);

            }
            else if(cdata.tail_check==2)   //head is receiving
            {
                System.out.println("Got reply from tail(indirect) : " + cdata.tail_ts);
                             String clientSentence="NO_VALUE";
                            String line;
                       if(cdata.mtype==0)      //read
                          {
                              System.out.println("I never receive here");
                         System.out.println("Read request received");
                          try {
                           BufferedReader input =
                             new BufferedReader(new FileReader(cserver.fname));
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

                          }   //if
                     else if(cdata.mtype==1)     //write
                     {
                         FileOutputStream fos = new FileOutputStream("t.tmp");
                        ObjectOutputStream o = new ObjectOutputStream(fos);
                         cserver.xy_edit(cdata.msg_seq,o,0,cdata,1);
                         }
                    else if(cdata.mtype==3)
                       {
                          FileOutputStream fos = new FileOutputStream("t.tmp");
                            ObjectOutputStream o = new ObjectOutputStream(fos);
                            clientSentence=cdata.message;
                            cserver.sock_send(cdata,clientSentence);
                            cserver.xy_edit(cdata.msg_seq,o,0,cdata,2);

                             //roshan
                       }



                //reply to client using client_sock
            }
            else
            {
                 sClient_Communicator scom=new sClient_Communicator();

                 cdata.timestamp=++cserver.time;
                 cdata.Sender_id=cHandle_Thread.SerID;
                 cserver.ra.add_to_it(cdata);

                 scom.Tail_Sender(cdata);

            }

          }     //########## NON TAIL PROCESSING :END
            else    // tail
          {
              System.out.println("CData Received in tail");
              if(cdata.mtype!=3)
              vh.vf_choice(cdata,1);
              else  //elel
              {
                  String line,clientSentence="NO_VALUE";
                  System.out.println("Read request received");
                          try {
                           BufferedReader input =
                             new BufferedReader(new FileReader(cserver.fname));
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
                sClient_Communicator scom=new sClient_Communicator();
                  cdata.message=clientSentence;
                  scom.head_Sender(cdata);

              }  //elel

          }

        }



    }


}


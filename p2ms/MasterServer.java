import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 5/4/12
 * Time: 4:32 PM
 * To change this template use File | Settings | File Templates.
 */

class receiver extends Thread
{
    ObjectInputStream infromser;
    int sernum;
     public receiver(ObjectInputStream in,int k)
     {
         infromser=in;
         sernum=k;
     }

    public void run()
    {
        while (true)
        {
            try {
                String modifiedSentence = (String) infromser.readObject();

            } catch (Exception e) {
                System.out.println("This server crashed : "+ sernum);
                ///////////////  sendign to client
                               try {
             BufferedReader input =  new BufferedReader(new FileReader("../p2c/cser.txt"));
                 try {
                        String line1 = null;
                      String line2 = null;
                        while (( line1 = input.readLine()) != null){
                        line2 = input.readLine();
                         Socket clientSocket = new Socket(line1, Integer.parseInt(line2));
                       PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                            out.println(sernum);

                        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
                //////////////     sending to server
       try {
             BufferedReader input =  new BufferedReader(new FileReader("../p2s/cfg4master.txt"));
                 try {
                        String line1 = null;
                      String line2 = null;
                     int temper=1;
                        while (( line1 = input.readLine()) != null){

                        line2 = input.readLine();
                            try{
                         Socket clientSocket = new Socket(line1, Integer.parseInt(line2));
                       PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                            out.println(sernum);
                        }  catch (Exception e1){}
                        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
                //method
                break;
            }
        }


    }

}

public class MasterServer {
     public static void main(String args[]) throws IOException {
       /*
        System.out.println("Would you like to clear all config. files(y/n) : ");
        BufferedReader bin=new BufferedReader(new InputStreamReader(System.in));

        if(bin.readLine().equals("y"))
        {   */
            System.out.println("Clearing all config files.");
            File yourFile = new File("master.txt");
            yourFile.delete();
            File yourNewFile = new File("master.txt");
            yourNewFile.createNewFile();
            yourFile = new File("../p2s/cfg.txt");
            yourFile.delete();
            yourNewFile = new File("../p2s/cfg.txt");
            yourNewFile.createNewFile();
            yourFile = new File("../p2c/cser.txt");
            yourFile.delete();
            yourNewFile = new File("../p2c/cser.txt");
            yourNewFile.createNewFile();
            yourFile = new File("../p2s/cfg4master.txt");
            yourFile.delete();
            yourNewFile = new File("../p2s/cfg4master.txt");
            yourNewFile.createNewFile();
            yourFile = new File("../p2s/cfg4ser.txt");
            yourFile.delete();
            yourNewFile = new File("../p2s/cfg4ser.txt");
            yourNewFile.createNewFile();
            yourFile = new File("../p2c/client_sync.txt");
            yourFile.delete();
            yourNewFile = new File("../p2c/client_sync.txt");
            yourNewFile.createNewFile();


           FileWriter outFile = new FileWriter("master.txt",true);
           PrintWriter out = new PrintWriter(outFile);

         System.out.println("Enter socket to start the master in : ");
         BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
         String port=in.readLine();

           ServerSocket masterSocket;
                      masterSocket = new ServerSocket(Integer.parseInt(port));
              try{
                 InetAddress ownIP=InetAddress.getLocalHost();
                 System.out.println("IP of master server is := "+ownIP.getHostAddress());
                  out.println(ownIP.getHostAddress());
                 }catch (Exception e){
                 System.out.println("Exception caught ="+e.getMessage());
                 }
           out.println(port);
           out.close();

         int i=1;

         while(i<6)
         {

             System.out.println("Waiting for Request..");
             if(i==1)
             {
                 Socket connectionSocket1 = masterSocket.accept();
                 ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket1.getInputStream());
                     receiver cht=new receiver(inFromServer,i);
                     Thread t1=new Thread(cht);
                     t1.start();

             }
             if(i==2)
             {
                 Socket connectionSocket2 = masterSocket.accept();
                 ObjectInputStream inFromServer1 = new ObjectInputStream(connectionSocket2.getInputStream());
                 receiver cht1=new receiver(inFromServer1,i);
                                      Thread t2=new Thread(cht1);
                                      t2.start();

             }
             if(i==3)
             {
                 Socket connectionSocket3 = masterSocket.accept();
              ObjectInputStream inFromServer2 = new ObjectInputStream(connectionSocket3.getInputStream());
                 receiver cht2=new receiver(inFromServer2,i);
                                      Thread t3=new Thread(cht2);
                                      t3.start();
             }
             if(i==4)
             {
                 Socket connectionSocket4 = masterSocket.accept();
             ObjectInputStream inFromServer3 = new ObjectInputStream(connectionSocket4.getInputStream());
                 receiver cht3=new receiver(inFromServer3,i);
                                      Thread t4=new Thread(cht3);
                                      t4.start();
             }
             if(i==5)
             {
                 Socket connectionSocket5 = masterSocket.accept();
              ObjectInputStream inFromServer4 = new ObjectInputStream(connectionSocket5.getInputStream());
                 receiver cht4=new receiver(inFromServer4,i);
                                      Thread t5=new Thread(cht4);
                                      t5.start();
             }
              i++;
             //
         }

         System.out.println("OUT!");




     }

}

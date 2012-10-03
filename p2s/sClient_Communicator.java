import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/25/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class sClient_Communicator
{
    public static String myPort;
    public static String myip;
    public static String tail_ip;
    public static String tail_port;

    public static ArrayList<String> s_ip = new ArrayList<String>();
    public static ArrayList<String> s_port = new ArrayList<String>();

    public sClient_Communicator(String portval) throws UnknownHostException {
          myPort=portval;
          InetAddress ownIP=InetAddress.getLocalHost();
          myip=ownIP.getHostAddress();
    }

    public sClient_Communicator()
    {}

    public void Communicator() throws Exception
    {
       String tline = null;
        int count=0;
       while(count<10)
       {
           count=0;
           BufferedReader input =  new BufferedReader(new FileReader("cfg4ser.txt"));
           while (( tline = input.readLine()) != null){ count++;  }

       }


       try {
             BufferedReader input =  new BufferedReader(new FileReader("cfg4ser.txt"));
                 try {
                        String line = null;
                        while (( line = input.readLine()) != null){
                        s_ip.add(line);
                        line = input.readLine();
                        s_port.add(line);
        }   //while

      }    //2nd try
      finally {
        input.close();
      }
    }    //first try
    catch (IOException ex){
      ex.printStackTrace();
    }
        System.out.println("I have the required contacts now!!");

        tail_ip=s_ip.remove(4);
        tail_port=s_port.remove(4);

         for(int i=0;i<s_ip.size();i++)
         {
             if(s_ip.get(i).equals(myip) && s_port.get(i).equals(myPort))
             {
                 s_ip.remove(i);
                 s_port.remove(i);
             }
         }

        Iterator i=s_ip.iterator() ;
        Iterator j=s_port.iterator() ;

        System.out.println("Peer Servers : ");
        while(i.hasNext() && j.hasNext())
        {
            System.out.println(i.next() + "/" + j.next());
        }

        System.out.println("Tail Server : ");
        System.out.println(tail_ip+"/"+tail_port);



    }

    public void Sender(message m) throws Exception
    {
         for(int i=0;i<s_ip.size();i++)
         {
               Socket s = new Socket(s_ip.get(i), Integer.parseInt(s_port.get(i)));
               ObjectOutputStream oos= new ObjectOutputStream(s.getOutputStream());
               oos.flush();

        System.out.println("Sending Request to peer : " + s_ip.get(i)+"/"+s_port.get(i));
            oos.writeObject(m);
            oos.flush();
             oos.close();
            s.close();
        }

    }

        public void nh_Sender(message m) throws Exception
    {
        System.out.println(m.head_port+"/"+m.head_ip);
         for(int i=0;i<s_ip.size();i++)
         {
            if(s_ip.get(i).equals(m.head_ip) && s_port.get(i).equals(m.head_port)){}
             else
            {
               Socket s = new Socket(s_ip.get(i), Integer.parseInt(s_port.get(i)));
               ObjectOutputStream oos= new ObjectOutputStream(s.getOutputStream());
               oos.flush();

        System.out.println("Sending Request to peer : " + s_ip.get(i)+"/"+s_port.get(i));
            oos.writeObject(m);
            oos.flush();
             s.close();
            }
        }

    }


    public void Tail_Sender(message m) throws Exception
    {
              try{
               Socket s = new Socket(tail_ip, Integer.parseInt(tail_port));
               ObjectOutputStream oos= new ObjectOutputStream(s.getOutputStream());
               oos.flush();

        System.out.println("Sending Request to tail.");
            oos.writeObject(m);
            oos.flush();
        s.close();
              }catch(Exception e5){}

    }

        public void head_Sender(message m) throws Exception
    {

               Socket s = new Socket(m.head_ip, Integer.parseInt(m.head_port));
               ObjectOutputStream oos= new ObjectOutputStream(s.getOutputStream());
               oos.flush();
               m.tail_check=2;
        System.out.println("Sending Reply to HEAD.");
            oos.writeObject(m);
            oos.flush();
             s.close();


    }

    public boolean my_tail()
    {
        if(myip.equals(tail_ip) && myPort.equals(tail_port))
            return true;
        else
            return false;
    }

}


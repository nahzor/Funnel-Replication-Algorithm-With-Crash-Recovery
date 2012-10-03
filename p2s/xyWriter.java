import java.util.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 4/26/12
 * Time: 1:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class xyWriter {

    public static ArrayList<message> send_list=new ArrayList<message>();

    public xyWriter(){}

    public static void addd(message m)
    {
        System.out.println("Added cdata to RA:"+m.msg_seq);
      send_list.add(m);
    }

    public static void rem() throws IOException
    {
        //iterating through an arraylist and writing in order
          if(send_list.size()>2)
          {
           int ts=1000;
            int msgseq=0;
            message m = null;
           Iterator iterator = send_list.iterator();
           while(iterator.hasNext())
           {
               message p= (message) iterator.next();
                if(p.tail_ts<ts)
                {
                    ts=p.tail_ts;
                   m=p;
                }

           }
          if(m!=null)
          {
           FileWriter fw = new FileWriter(cserver.fname,true);
           System.out.println("HWriting to : "+cserver.fname+" : "+m.tail_ts);
           PrintWriter out1 = new PrintWriter(fw);//roshan
           String print_val="Message Seq:"+m.msg_seq +" || Client_Id:Client"+ m.client_id+" || Head_Server_Id:"
                    +m.head_id+" || Server_Timestamp:"+m.head_ts+" || Tail_Timestamp:"+m.tail_ts;
           out1.println(print_val);
           out1.close();
           send_list.remove(m);
          }
          }
    }

    public static synchronized void xy_write(int flag,message cdata) throws IOException {
        if(flag==0)
        {addd(cdata);}
        else if(flag==1)
        {
            rem();
        }
        else if(flag==2)
        {
           Iterator iterator = send_list.iterator();
           while(iterator.hasNext())
           {
               message p= (message) iterator.next();
                if(Integer.parseInt(p.head_id)<masterReceiver.crasher)
                {
                   send_list.remove(p);
                }

           }
        }

    }

}

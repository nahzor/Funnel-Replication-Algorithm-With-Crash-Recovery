import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/27/12
 * Time: 11:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class Request_Array {

    public static ArrayList <message> arrlt=new ArrayList<message>();

    public Request_Array(){}

    public void add_to_it(message m)
    {
        System.out.println("Added cdata to RA:"+m.msg_seq);
      arrlt.add(m);
    }

    public void remove_from_it(message m)
    {
        System.out.println("Removing cdata:"+m.msg_seq);
        for(int i=0;i<arrlt.size();i++)
        {
            if(arrlt.get(i).msg_seq==m.msg_seq)
            {
                System.out.println("Got IT!!");
                arrlt.remove(i);break;
            }
        }
    }

    public void read_and_send() throws Exception {
        sClient_Communicator scom=new sClient_Communicator();
        for(int i=0;i<arrlt.size();i++)
        {
            if(sClient_Communicator.myip.equals(arrlt.get(i).head_ip) && sClient_Communicator.myPort.equals(arrlt.get(i).head_port))
            {}
            else{
            scom.Tail_Sender(arrlt.get(i)); }

        }

    }

}


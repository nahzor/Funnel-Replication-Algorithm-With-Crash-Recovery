import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/26/12
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector_Handle implements Cloneable{

    int [][] ts_vector=new int[2][5];
    message [] object_list=new message[2];
    int not_found=0;
    public int count=0;
    int tail_tss=0;
    public static int sub_val=0;

    int []mseq_count={0,0};

    public Vector_Handle()
    {
        object_list[0]=new message(-1,0,"",0,"","",0,0,0,"","",0);
        object_list[1]=new message(-1,0,"",0,"","",0,0,0,"","",0);
    }

    public void vector_writer(message m)
    {
        not_found=0;
        for(int i=0;i<2;i++)
        {
            if(object_list[i].msg_seq==m.msg_seq && object_list[i].client_id==m.client_id)   //potential cause
            {
               ts_vector[i][Integer.parseInt(m.Sender_id)]=m.timestamp;
               ts_vector[i][Integer.parseInt(m.head_id)]=m.head_ts;
               mseq_count[i]++;
               not_found=0;
                break;
            }
            else
            {

                not_found=1;
            }

        }

        if(not_found==1)
        {
            System.out.println("NOT FOUND : "+m.client_id);
                for(int j=0;j<2;j++)
                {
                    if(object_list[j].msg_seq<0)
                    {
                         object_list[j]=m;
                          ts_vector[j][Integer.parseInt(m.Sender_id)]=m.timestamp;
                          ts_vector[j][Integer.parseInt(m.head_id)]=m.head_ts;
                          mseq_count[j]++;
                          not_found=0;
                          break;
                    }
                }
        }

        disp();
    }

    public void disp()
    {
        for(int i=0;i<2;i++)
        {
            System.out.println(object_list[i].client_id + ":::"+object_list[i].msg_seq + ":::"+mseq_count[i]);
            for(int j=1;j<5;j++)
            {
                System.out.println(j+":"+ts_vector[i][j]);
            }
            System.out.println();
        }
    }

    public void vector_size_check() throws Exception {
       // System.out.println("checker");
        count=0;
        int flag=0;
        for(int i=0;i<2;i++)
           {
               if(mseq_count[i]>=3-sub_val)
               {
                   count++;
                   System.out.println("Got ONE wid full SET!!");
               }
           }
        if(count==1)
        {
            System.out.println("COUNT : "+count);
           for(int i=0;i<2;i++)
           {
               if(mseq_count[i]>=3-sub_val)
               {
                   count=0;
                   tail_tss++;
                   object_list[i].tail_ts=tail_tss;
                   System.out.println("Chose : "+object_list[i].client_id+" : "+object_list[i].msg_seq+" : "+object_list[i].tail_ts);
                   fsender(object_list[i]);
                   object_list[i]=new message(-1,0,"",0,"","",0,0,0,"","",0);
                   System.out.println("Just one!! processed and cleared");
                   mseq_count[i]=0;
                   for(int j=1;j<5;j++)
                    {
                    ts_vector[i][j]=0;
                    }

                   disp();
                   break;
               }
           }
        }
        if(count==2)
        {
           System.out.println("COUNT : "+count);
           count=1;
            int choice=-1;
           //compare send and remove
           int comparison1=0;
            int comparison2=0;
            for(int j=1;j<5;j++)
            {
                if(ts_vector[0][j]>=ts_vector[1][j])
                {
                    comparison1++;
                }

            }
            for(int j=1;j<5;j++)
            {
                if(ts_vector[1][j]>=ts_vector[0][j])
                {
                    comparison2++;
                }

            }

            if(comparison1==4 && comparison2==4)
            {
               if(Integer.parseInt(object_list[0].head_id)>Integer.parseInt(object_list[1].head_id))
               {   tail_tss++;
                   object_list[0].tail_ts=tail_tss;
                   fsender(object_list[0]); choice=0;
               }
                else {
                   tail_tss++;
                   object_list[1].tail_ts=tail_tss;
                   fsender(object_list[1]);choice=1;
               }
            }
            else if(comparison1==4)
            {
                                  tail_tss++;
                   object_list[1].tail_ts=tail_tss;
                fsender(object_list[1]);choice=1;
            }
            else if(comparison2==4)
            {
                   tail_tss++;
                   object_list[0].tail_ts=tail_tss;
                fsender(object_list[0]); choice=0;
            }
            else
            {
                 if(Integer.parseInt(object_list[0].head_id)>Integer.parseInt(object_list[1].head_id))
                 {                      tail_tss++;
                   object_list[0].tail_ts=tail_tss;
                     fsender(object_list[0]);     choice=0;
                 }
                else {
                                        tail_tss++;
                   object_list[1].tail_ts=tail_tss;
                     fsender(object_list[1]);  choice=1;       }
            }
            System.out.println("Chose : "+object_list[choice].client_id+" : "+object_list[choice].msg_seq+" : "+object_list[choice].tail_ts);
            object_list[choice]=new message(-1,0,"",0,"","",0,0,0,"","",0);
                   mseq_count[choice]=0;
                               for(int j=1;j<5;j++)
                    {
                    ts_vector[choice][j]=0;
                    }
            System.out.println("TWO of them! compared, processed and cleared");
            disp();

        }

    }

    public synchronized void vf_choice(message m, int f) throws Exception {
        if(f==1)
        {
             this.vector_writer(m);
        }
        else if (f==2)
        {
            this.vector_size_check();
        }
        else if(f==3)
        {

           for(int i=0;i<2;i++)
           {
               if(object_list[i].head_ip.equals(masterReceiver.ipp) && object_list[i].head_port.equals(masterReceiver.portp))
               {

                   System.out.println("Chosing111 : "+object_list[i].client_id+" : "+object_list[i].msg_seq+" : "+object_list[i].tail_ts);

                  // fsender(object_list[i]);
                   object_list[i]=new message(-1,0,"",0,"","",0,0,0,"","",0);
                   System.out.println("Just one!! processed and cleared");
                   mseq_count[i]=0;
                   for(int j=1;j<5;j++)
                    {
                    ts_vector[i][j]=0;
                    }

                   disp();
                   break;
               }
           }

        }
    }

    public void fsender(message mm) throws Exception {
        System.out.println("Inside FSend");
        if(mm.mtype==1)
        {
            {
                 FileWriter fw = new FileWriter(cserver.fname,true);
                 System.out.println("Writing to:"+cserver.fname);
                 PrintWriter out1 = new PrintWriter(fw);
                 String print_val="Message Seq:"+mm.msg_seq +" || Client_Id:Client"+ mm.client_id+" || Head_Server_Id:"
                                    +mm.head_id+" || Server_Timestamp:"+mm.head_ts+" || Tail_Timestamp:"+mm.tail_ts;
                            out1.println(print_val);
                 out1.close();
             }
        }
        //send to servers other than head
        sClient_Communicator sccom=new sClient_Communicator();
        mm.tail_check=1;
        sccom.nh_Sender(mm);

    }

}


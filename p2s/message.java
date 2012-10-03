import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Roshan
 * Date: 3/25/12
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
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

    } }
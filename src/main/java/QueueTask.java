import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.collections.map.HashedMap;
import org.jdom2.JDOMException;
import org.mortbay.log.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueTask {
    private PoolingDB DB;
    private Server_Tetst ST;
    private final MyThread threads;
    private final LinkedList queue;
    private ExecutorService service;
    private ChannelFuture f;

    private Map<String,ChannelHandlerContext> conectList;

    public void join() throws InterruptedException {
        threads.join();
    }

    public void start(Server_Tetst S, PoolingDB D, int port) throws InterruptedException {

        DB = D;
        ST = S;
        DB.Connect();
        threads.QT = this;
        threads.start();
        f = ST.bind(port);
        conectList = new HashedMap();
    }

     public QueueTask() {

      //  service = Executors.newCachedThreadPool();

        service = Executors.newFixedThreadPool(10);

        queue = new LinkedList();
        threads = new MyThread();
        threads.setPriority(Thread.MAX_PRIORITY);

    }

    public void addTask(MyTask r) {
        synchronized (queue) {
            queue.addLast(r);
            queue.notify();
        }
    }

    private class MyThread extends Thread {
         QueueTask QT;
        public void run() {
            MyTask msg;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    msg = (MyTask) queue.removeFirst();
                }

                try {
                    if (msg.MSG == MyTask.PACKEGE_MSG) {

                        MyXML myXMLParser = new MyXML((byte[])msg.str);

                        String token = myXMLParser.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);

                        if(token!=null){
                            if (token.length()>5){
                                conectList.put(token, msg.ctx);
                            }
                        }
                      // System.out.println(conectList.size());

                        //Log.info("PACKEGE_MSG", "PACKEGE_MSG" +myXMLParser.getTypeXML() );
                        if (MSG.XML_TYPE_REQUEST.equals(myXMLParser.getTypeXML())) {
                            switch (myXMLParser.getIdActionsXML()) {
                                case MSG.XML_USER_LOGIN:
                                    Log.info("PACKEGE_MSG", "XML_USER_LOGIN");
                                    service.submit(new Runnable_USER_LOGIN(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_USER_REGISTRATION:
                                    Log.info("PACKEGE_MSG", "XML_USER_REGISTRATION");
                                    service.submit(new Runnable_USER_REG(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_CONTACT_ADD:
                                    Log.info("PACKEGE_MSG", "XML_CONTACT_ADD");
                                    service.submit(new Runnable_CONTACT_ADD(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_GET_USERS_FROM_LIKE:
                                    Log.info("PACKEGE_MSG", "XML_GET_USERS_FROM_LIKE");
                                    service.submit(new Runnable_GET_USER_FROM_LIKE(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_CONVERSATION_ADD:
                                    Log.info("PACKEGE_MSG", "XML_CONVERSATION_ADD");
                                    service.submit(new Runnable_CONVERSATION_ADD(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_GET_ALL_CONVERSATION:
                                    Log.info("PACKEGE_MSG", "XML_GET_ALL_CONVERSATION");
                                    service.submit(new Runnable_GET_ALL_CONVERSATION(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_GET_MESSAGES_FO_DATE:
                                    Log.info("PACKEGE_MSG", "XML_GET_MESSAGES_FO_DATE");
                                    service.submit(new Runnable_GET_MESSAGES_FO_TIME(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_ADD_MESSAGES :
                                    Log.info("PACKEGE_MSG", "XML_ADD_MESSAGES");
                                    service.submit(new Runnable_ADD_MESSAGES(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_SING_OUT :
                                    Log.info("PACKEGE_MSG", "Runnable_USER_SING_OUT");
                                    service.submit(new Runnable_USER_SING_OUT(myXMLParser, msg.ctx, DB, QT));
                                    break;
                                case MSG.XML_CONVERSATION_ADD_USERS :
                                    Log.info("PACKEGE_MSG", "Runnable_CONVERSATION_ADD_USERS");
                                    service.submit(new Runnable_CONVERSATION_ADD_USERS(myXMLParser, msg.ctx, DB, QT));
                                    break;

                                default:

                                    Log.info("PACKEGE_MSG", "XML_OTHER");
                                    break;

                            }


                        }


                    } else if( msg.MSG == MyTask.SYSTEM_MSG) {

                        if (msg.str instanceof Messages){
                            Log.info("SYSTEM_MSG", "Runnable_SEND_MESSAGES_NOTIFICATION");
                            service.submit(new Runnable_SEND_MESSAGES_NOTIFICATION((Messages) msg.str,conectList, DB  ));

                        }
                        else if (msg.str instanceof Conversation){
                            Log.info("SYSTEM_MSG", "Runnable_SEND_CONVERSATION_NOTIFICATION");
                            service.submit(new Runnable_SEND_CONVERSATION_NOTIFICATION((Conversation) msg.str, msg.arg1, conectList, DB  ));

                        }

                    }


                } catch (RuntimeException e) {
                    // You might want to log something here
                } catch (JDOMException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

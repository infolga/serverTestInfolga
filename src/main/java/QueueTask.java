import io.netty.channel.ChannelFuture;
import org.jdom2.JDOMException;
import org.mortbay.log.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueTask {
    private PoolingDB DB;
    private Server_Tetst ST;
    private final MyThread threads;
    private final LinkedList queue;
    private ExecutorService service;
    private ChannelFuture f;

    public void join() throws InterruptedException {
        threads.join();
    }

    public void start(Server_Tetst S, PoolingDB D, int port) throws InterruptedException {

        DB = D;
        ST = S;
        DB.Connect();
        threads.start();
        f = ST.bind(port);

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

                        MyXML myXMLParser = new MyXML(msg.str);

                        Log.info("PACKEGE_MSG", "PACKEGE_MSG" +myXMLParser.getTypeXML() );
                        if (MSG.XML_TYPE_REQUEST.equals(myXMLParser.getTypeXML())) {
                            switch (myXMLParser.getIdActionsXML()) {
                                case MSG.XML_USER_LOGIN:
                                    Log.info("PACKEGE_MSG", "XML_USER_LOGIN");
                                    service.submit(new Runnable_USER_LOGIN(myXMLParser, msg.ctx, DB));
                                    break;

                                case MSG.XML_USER_REGISTRATION:
                                    Log.info("PACKEGE_MSG", "XML_USER_REGISTRATION");
                                    service.submit(new Runnable_USER_REG(myXMLParser, msg.ctx, DB));
                                    break;
                                case MSG.XML_CONTACT_ADD:
                                    Log.info("PACKEGE_MSG", "XML_CONTACT_ADD");
                                    service.submit(new Runnable_CONTACT_ADD(myXMLParser, msg.ctx, DB));
                                    break;
                                case MSG.XML_GET_USERS_FROM_LIKE:
                                    Log.info("PACKEGE_MSG", "XML_GET_USERS_FROM_LIKE");
                                    service.submit(new Runnable_GET_USER_FROM_LIKE(myXMLParser, msg.ctx, DB));
                                    break;
                                case MSG.XML_CONVERSATION_ADD:
                                    Log.info("PACKEGE_MSG", "XML_CONVERSATION_ADD");
                                    service.submit(new Runnable_CONVERSATION_ADD(myXMLParser, msg.ctx, DB));
                                    break;
                                case MSG.XML_GET_ALL_CONVERSATION:
                                    Log.info("PACKEGE_MSG", "XML_GET_ALL_CONVERSATION");
                                    service.submit(new Runnable_GET_ALL_CONVERSATION(myXMLParser, msg.ctx, DB));
                                    break;


                                default:

                                    Log.info("PACKEGE_MSG", "XML_OTHER");
                                    break;

                            }


                        }


                    } else {


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

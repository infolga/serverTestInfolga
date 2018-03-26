import io.netty.channel.ChannelFuture;
import org.jdom2.JDOMException;

import java.io.IOException;
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
        service = Executors.newCachedThreadPool();
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

                        MyXMLParser myXMLParser = new MyXMLParser(msg.str);
                        System.out.println(myXMLParser.getTypeXML());
                        if (MSG.XML_TYPE_REQUEST.equals(myXMLParser.getTypeXML())) {
                            switch (myXMLParser.getIdActionsXML()){

                                case MSG.XML_USER_LOGIN:

                                    System.out.println("sfdsg");

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
                }
            }
        }
    }
}

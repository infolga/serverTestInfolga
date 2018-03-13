import java.io.*;
import java.net.Socket;


public class Worker implements Runnable {
    private boolean isStopped=false;
    protected Socket clientSocket = null;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();

            DataInputStream in = new DataInputStream(input);
            DataOutputStream out = new DataOutputStream(output);
            String line = null;
            while(!isStopped) {

                System.out.println("жду 1 поток :\n");
                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                //int i = input.read();

                //input.read();
                System.out.println("принял 1 поток  : " + line);

                out.writeUTF("qqqq "+line);
                out.flush(); // заставляем поток закончить передачу данных.

                System.out.println();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}


import java.io.*;
import java.net.Socket;


public class Worker implements Runnable {
    private boolean isStopped = false;
    private Socket clientSocket = null;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();

            DataInputStream in = new DataInputStream(input);
            DataOutputStream out = new DataOutputStream(output);
            String line;
            while (!isStopped) {
                System.out.println(clientSocket);

                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("wait 1 поток :\n");
                //int i = input.read();

                //input.read();
                System.out.println("thred 1 поток  : " + line);

                out.writeUTF("yyyy " + line);
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


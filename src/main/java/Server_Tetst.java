

import java.net.*;
        import java.io.*;
public class Server_Tetst {
    public static void main(String[] ar)    {
        int port = 6666; // случайный порт (может быть любое число от 1025 до 65535)
        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту
            System.out.println(" \u0061");

            Socket socket = ss.accept();
            new Thread(new Worker(socket)).start();

            // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("клиент подключился");
            System.out.println();

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
         /*   while(true) {
                line = in.readUTF(); // ожидаем пока клиент пришлет строку текста.
                System.out.println("принял основной поток   : " + line);
                out.writeUTF(line); // отсылаем клиенту обратно ту самую строку текста.
                out.flush(); // заставляем поток закончить передачу данных.
               System.out.println("жду основной поток");
                System.out.println();
          }*/
        } catch(Exception x) { x.printStackTrace(); }
    }
}


import java.net.*;
        import java.io.*;
public class Server_Tetst {
    public static void main(String[] ar)    {
        int port = 8080; // случайный порт (может быть любое число от 1025 до 65535)
        try {

            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader inn = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            String ip = inn.readLine(); //you get the IP as a String
            System.out.println(ip);

            while (true) {


            System.out.println("готов port" + port);

            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту


            Socket socket = ss.accept();
            new Thread(new Worker(socket)).start();

            // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
            System.out.println("клиент connect");
            System.out.println();
        }
            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
          /*  InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
         *//*   while(true) {
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
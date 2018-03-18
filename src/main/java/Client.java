import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] ar) throws IOException {
        int serverPort = 8080; // здесь обязательно нужно указать порт к которому привязывается сервер.
        //String address = "ec2-52-41-213-200.us-west-2.compute.amazonaws.com"; // это IP-адрес компьютера, где исполняется наша серверная программа.
        String address = "34.218.147.163";
        // Здесь указан адрес того самого компьютера где будет исполняться и клиент.
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader inn = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = inn.readLine(); //you get the IP as a String
        System.out.println(ip);
        try {
            //InetAddress ipAddress = InetAddress.getByName(new URL(address).getHost());
         InetAddress ipAddress = InetAddress.getByName( address );
            System.out.println(" готов к работе ");
            System.out.println(ipAddress.getHostAddress());
            Socket socket = new Socket(address, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("подключился");

            // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом. 
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            // Создаем поток для чтения с клавиатуры.
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            System.out.println("готов ");
            System.out.println();

            while (true) {
                line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                System.out.println("отправляю");
                out.writeUTF(line); // отсылаем введенную строку текста серверу.
                out.flush(); // заставляем поток закончить передачу данных.
                // ждем пока сервер отошлет строку текста.

                line = in.readUTF();
                System.out.println("отправляю");
                System.out.println(line);

            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
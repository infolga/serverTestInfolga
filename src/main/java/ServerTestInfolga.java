import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerTestInfolga {


    public static void main(String[] ar) {

        try {
            String url;
            String user;
            String password;
            PoolingDB DB;
            Server_Tetst ST;
            QueueTask QT = new QueueTask();

            FileReader file = new FileReader("Seting.txt");
            BufferedReader reader = new BufferedReader(file);
            url = reader.readLine();
            user = reader.readLine();
            password = reader.readLine();
            file.close();
            System.out.println(url);
            System.out.println(user);
            System.out.println("psaa " + password);

            DB = new PoolingDB(url, user, password);

            String PORT = System.getProperty("port", "8080");
            int port = Integer.parseInt(PORT);
            ST = new Server_Tetst(QT);

            QT.start(ST, DB, port);
            System.out.println("redy");
            QT.join();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}

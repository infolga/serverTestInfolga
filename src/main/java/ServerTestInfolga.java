import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.io.*;

public class ServerTestInfolga {


    public static void main(String[] args) throws Exception {
        try {

            FileInputStream serviceAccount = new FileInputStream("servertestinfolga-firebas.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://servertestinfolga.firebaseio.com/")
                .build();

            FirebaseApp.initializeApp(options);

//            String registrationTo = "fdkxeUvnxng:APA91bElIpe653LM-xtwdN25lltJ3Ts4C2tjHbaDpGe9dbdCO_gGjzKzk1a-jDxoHuZDtLFZXJbExK6x-_srZsud7dNRu1CokMMaVTkg_3ytIPqlwbVNq0gpq1IcSo-BOUyA9vPYEO17";
//            String registrationToken = "cPA6__gV6sQ:APA91bFPgzkNBZlEqElzlyLdK8ySsnfRMB3SDke8mD0oIJ23KZATF4ytQDZzoC-PB9oZLP-VqC8FT1HlYlKYpEis_oy0OyTsEezt4nQrfxkgpiR50iLGM5mEqK0H_4Vwlu4Sa-Rq8ED-";
//            Message message = Message.builder()
//
//
//                .setNotification(new Notification("тест ", "ХА ХА?"))
//
//
//                .setToken(registrationToken)
//                .setToken(registrationTo)
//
//                .build();
//
//
//            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
//
//            System.out.println("Successfully sent message: " + response);


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

            String PORT = MyProperties.instans().getProperty("port", "8080");
            int port = Integer.parseInt(PORT);
            ST = new Server_Tetst(QT);

            QT.start(ST, DB, port);
            System.out.println("ready");


            // QT.join();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}



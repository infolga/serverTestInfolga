import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ServerTestInfolga {


    public static void main(String[] args) throws Exception {
        try {

            FileInputStream serviceAccount = new FileInputStream("servertestinfolga-firebas.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://servertestinfolga.firebaseio.com/")
                .build();

            FirebaseApp.initializeApp(options);

//
//            // This registration token comes from the client FCM SDKs.
//            String registrationToken = "cPA6__gV6sQ:APA91bFPgzddkNBZlEqElzlyLdK8ySsnfRMB3SDke8mD0oIJ23KZATF4ytQDZzoC-PB9oZLP-VqC8FT1HlYlKYpEis_oy0OyTsEezt4nQrfxkgpiR50iLGM5mEqK0H_4Vwlu4Sa-Rq8ED-";
//            //String registrationTok ="drQiuaLXn0Q:APA91bG6iWjR_UZvRBsszYrLonbMPdX94gq3LrRim_FFBAfrDOZ3_BlE13HlAjtSAC0qKLYNHTuKsFZD2UF_tlkRiZSCEs4C5c1R1iyhn98yhDlm5RyQc_KwevR7f6kgqtfUOCc8tWQl";
//            // See documentation on defining a message payload.
////            Message message = Message.builder()
////                .putData("title", "title123")
////                .putData("body", "body123")
////
////                .setNotification(new Notification("тест ", "ХА ХА?"))
////
////                // .setToken(registrationTok)
////                .setToken(registrationToken)
////
////
////                .build();
//
//
//            Message message = Message.builder()
//                .setAndroidConfig(AndroidConfig.builder()
//                    //.setTtl(3600 * 1000) // 1 hour in milliseconds
//                    .setPriority(AndroidConfig.Priority.NORMAL)
//                    .setNotification(AndroidNotification.builder()
//                        .setTitle("$GOOG up 1.43% on the day")
//                        .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.")
//                       // .setIcon("stock_ticker_update")
//                        .setColor("#f45342")
//                        .build())
//                    .build())
//
//                .setToken(registrationToken)
//                .build();
//
//            System.out.println(message.toString());
//
//
//             String response = FirebaseMessaging.getInstance().sendAsync(message).get();
//
//             System.out.println("Successfully sent message: " + response);


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



import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerTestInfolga {


    public static void main(String[] args) throws Exception {
        try {



            FileInputStream serviceAccount = new FileInputStream("servertestinfolga-firebas.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://servertestinfolga.firebaseio.com/")
                .build();

            FirebaseApp.initializeApp(options);


//            // This registration token comes from the client FCM SDKs.
//            String registrationToken = "f5bjAmunmzQ:APA91bHMfTjXDeJK4zrRE7iq1G0TVLzhUw5w8UTIk3sVP_IWndCOycmn1KcRA2MGVHFoCzXbEZz9ap4JuvEAwsymlrdbJhS4muQDTk13N8Pm8yw-fo6z4fycHYVK60tB2E39juy0HAON";
//            //String registrationTok ="drQiuaLXn0Q:APA91bG6iWjR_UZvRBsszYrLonbMPdX94gq3LrRim_FFBAfrDOZ3_BlE13HlAjtSAC0qKLYNHTuKsFZD2UF_tlkRiZSCEs4C5c1R1iyhn98yhDlm5RyQc_KwevR7f6kgqtfUOCc8tWQl";
//            // See documentation on defining a message payload.
//            Message message = Message.builder()
//                .putData("title", "title123")
//                .putData("body", "body123")
//
//                //.setNotification(new Notification("тест ", "ХА ХА?"))
//
//                // .setToken(registrationTok)
//                .setToken(registrationToken)
//
//
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



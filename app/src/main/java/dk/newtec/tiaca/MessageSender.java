package dk.newtec.tiaca;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jl on 6/12/16.
 */
public class MessageSender {


    private String SERVER_IP = "192.168.18.20";
    private int SERVER_PORT = 5001;
    private static final int THREAD_DELAY = 500;
    private Thread senderThread = null;
    private boolean killSenderThread = false;

    private List<ResponseListener> responseListeners = new ArrayList<ResponseListener>();

    private Socket socket = null;
private List<String> dataQueue = new ArrayList<String>();

    private static MessageSender instance;

    private MessageSender(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);

        SERVER_PORT = prefs.getInt("server_port",5001);
        SERVER_IP = prefs.getString("server_ip","10.10.10.10");

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {

            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                System.out.println("Settings key changed: " + key);
            }
        };



        prefs.registerOnSharedPreferenceChangeListener(listener);


        senderThread = new Thread() {

            public void run() {

                while (!killSenderThread) {

                    if (!dataQueue.isEmpty()) {
                        sendToServer(dataQueue.remove(0));
                    }
                    synchronized (this) {
                        try {
                            Thread.currentThread().wait(THREAD_DELAY);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (socket != null && !socket.isClosed())
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                socket = null;

            }

        };
        senderThread.start();
    }


    public void sendData(String data){
        dataQueue.add(data);

    }
    public static MessageSender getInstance(){
       if (instance == null){
           instance = new MessageSender();
       }

        return instance;
    }
    public void addReponseListener(ResponseListener responseListener){
        responseListeners.add(responseListener);
    }



    private void sendToServer(String data) {

        if (socket == null || socket.isClosed()) {
            System.out.println("TrackPuckActivity.sendToServer create socket");
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                System.out.println(socket.isConnected());
            } catch (Exception e) {
                System.err.print("ERROR ");
                // e.printStackTrace();

            }
        }
        if (socket != null && socket.isConnected()){
            try {

                System.out.println(data);
                socket.getOutputStream().write(data.getBytes());
                byte[] bytes = new byte[1024];
                int read = socket.getInputStream().read(bytes);
                String reponse = new String(bytes,0,read);
                for (ResponseListener responseListener: responseListeners){
                    responseListener.handleResponse(reponse);
                }

            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    // e1.printStackTrace();
                }
                socket = null;
                // e.printStackTrace();
            }
        }
    }


}

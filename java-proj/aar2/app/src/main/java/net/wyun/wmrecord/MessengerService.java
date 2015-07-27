package net.wyun.wmrecord;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by guof on 7/25/2015.
 */
public class MessengerService extends Service {

    static final String APP_NAME = AppConstant.ServiceTag.APP_NAME;

    String LOG_TAG = "MessengerService"; //getApplicationInfo().className;

    /**
     * 用于Handler里的消息类型
     */
    static final int MSG_SAY_HELLO = 1;
    static final int MSG_NOTICE = 2;
    static final int MSG_START_SERVICE = 101;
    static final int MSG_END_SERVICE = 102;
    static final int MSG_START_RECORDING = 201;
    static final int MSG_END_RECORDING = 202;

    static boolean IsRecording = false;
    static boolean IsRunning = false;

    /**
     * 在Service处理Activity传过来消息的Handler
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_NOTICE:
                    Toast.makeText(getApplicationContext(), "MSG_NOTICE", Toast.LENGTH_LONG).show();

                    break;
                case MSG_START_SERVICE:
                    Toast.makeText(getApplicationContext(), "MSG_START_SERVICE", Toast.LENGTH_SHORT).show();
                    Notice("已启动服务",
                            APP_NAME,
                            "服务启动 ..."
                    );
                    break;
                case MSG_END_SERVICE:
                    IsRunning = false;
                    Toast.makeText(getApplicationContext(), "MSG_END_SERVICE", Toast.LENGTH_SHORT).show();
                    Notice("已结束服务",
                            APP_NAME,
                            "服务已结束 ..."
                    );
                    break;

                case MSG_START_RECORDING:
                    IsRecording = true;

                    Toast.makeText(getApplicationContext(), "MSG_START_RECORDING", Toast.LENGTH_SHORT).show();

                    Notice("已开始录音",
                            APP_NAME,
                            "正在录音 ..."
                    );
                    break;
                case MSG_END_RECORDING:
                    IsRecording = false;
                    Toast.makeText(getApplicationContext(), "MSG_END_RECORDING", Toast.LENGTH_SHORT).show();
                    Notice("已结束录音",
                            APP_NAME,
                            "录音已结束 ..."
                    );
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void Notice(String ticketTextMsg,
                        String title,
                        String content) {
        String service = NOTIFICATION_SERVICE;
        NotificationManager nm;
        nm = (NotificationManager) getSystemService(service);
        //实例化Notification
        Notification n;
        n = new Notification();
        //设置显示图标，该图标会在状态状态栏显示
        int icon = n.icon = R.drawable.icon;
        //设置显示提示信息，该信息会在状态栏显示
        String tickerText = ticketTextMsg;
        //显示时间
        long when = System.currentTimeMillis();
        n.icon = icon;
        n.tickerText = tickerText;
        n.when = when;


        Intent intent = new Intent(getApplication(), MainActivity.class);
        //获取pendingIntent
        PendingIntent pi = PendingIntent.getActivity(getApplication(), 0, intent, 0);
        //设置事件信息
        n.setLatestEventInfo(getApplication(), title, content, pi);
        //发出通知
        nm.notify(101, n);
    }

    /**
     * 这个Messenger可以关联到Service里的Handler，Activity用这个对象发送Message给Service，Service通过Handler进行处理。
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * 当Activity绑定Service的时候，通过这个方法返回一个IBinder，Activity用这个IBinder创建出的Messenger，就可以与Service的Handler进行通信了
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand() executed");

        if (!IsRunning) {
            IsRunning = true;
            Toast.makeText(getApplicationContext(), "MSG_START_SERVICE", Toast.LENGTH_SHORT).show();
            Notice("已启动服务",
                    APP_NAME,
                    "服务启动 ..."
            );
        } else
            return super.onStartCommand(intent, flags, startId);

        Thread recordThread = new Thread(new Runnable() {

            @Override
            public void run() {

                new Thread(new Runnable() {

                    public void run() {
                        int i = 0;
                        while (IsRunning) {
                            i++;
                            Log.i("Run service循环", "for启动成功：" + i);
                            int j = 0;
                            try {
                                Thread.sleep(1000);

                                while (IsRecording) {
                                    j++;
                                    Log.i("Run Recording 循环", "now ：" + j);

                                    startRecord();

                                    Thread.sleep(500);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        }
                        IsRunning = false;

                    }

                }).start();

            }

        });

        recordThread.start();
        IsRunning = true;
        //Notice2();
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate() executed");
    }

    @Override
    public void onDestroy() {
        this.IsRecording = false;
        this.IsRunning = false;
    }

    private static String KEY_IP = "prefIhostIp";
    private String serverAddr = "172.16.0.1";
    private static Integer[] freqset = {11025};
    public static DatagramSocket socket;
    Vibrator vibrator = null;
    private int port = 8888;

    private String getSetting(String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String value = sharedPrefs.getString(KEY_IP, "");
        return value;
    }

    private void updateServerIP() {
        //get ihost ip if available
        String ip = this.getSetting(KEY_IP);
        serverAddr = ip.isEmpty() ? serverAddr : ip;
        Log.d(this.LOG_TAG, "audio server: " + serverAddr);
    }

    private void startRecord() throws UnknownHostException {

        int pktCount = 0;
        this.updateServerIP();
        final InetAddress destination = InetAddress.getByName(serverAddr);
        Log.d(this.LOG_TAG, "Address retrieved: " + serverAddr);

        //int selectedFreqInd = spFrequency.getSelectedItemPosition();
        //int sampleFreq = freqset[selectedFreqInd];

        int sampleFreq = freqset[0];

        AudioRecord audioRecord = null;
        try {

            int minBufferSize = AudioRecord.getMinBufferSize(sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            Log.d(this.LOG_TAG, "min buffer size for audio recording: " + minBufferSize);
            if (minBufferSize < 8192) minBufferSize = 8192;  //to get better recording, 8192

            minBufferSize = 1024;

            byte[] audioData = new byte[minBufferSize];

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();
            socket = new DatagramSocket();

            //vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_START, -1);

            while (IsRecording)
            {
                //Log.i(LOG_TAG,"running ....");
                int numberOfShort = audioRecord.read(audioData, 0, minBufferSize);

                //putting buffer in the packet
                /*
                for(int bInd = 0; bInd < 7; bInd++){
                    byte[] slice = Arrays.copyOfRange(audioData, bInd * 1280, (bInd + 1) * 1280);
                    DatagramPacket packet = new DatagramPacket(slice, slice.length, destination, port);
                    socket.send(packet);
                    Log.d(this.LOG_TAG, "send pkt to server.");
                }
                */

                DatagramPacket packet = new DatagramPacket(audioData, audioData.length, destination, port);

                socket.send(packet);
                Log.i(this.LOG_TAG, "send pkt to server.");

                pktCount += 1;
                if (pktCount == 15000) {
                    long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                    //vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_REMIND, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                    pktCount = 0;
                }

            }
            socket = null;
            audioRecord.stop();

        } catch (IOException e) {
            e.printStackTrace();
            String msg = e.getMessage();
            Log.i(this.LOG_TAG, msg);
        } finally {
            //release resource
            Log.d(this.LOG_TAG, "remember release resource here");
            if (audioRecord != null) audioRecord.release();

            //vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_STOP, -1);
            IsRecording = false;
            //startRec.setEnabled(true);
            //stopRec.setEnabled(false);
        }

    }
}


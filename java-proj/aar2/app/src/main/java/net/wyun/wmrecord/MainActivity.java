package net.wyun.wmrecord;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.wyun.wmrecord.API.macapi;
import net.wyun.wmrecord.API.wmsapi;
import net.wyun.wmrecord.model.macmodel;
import net.wyun.wmrecord.model.wmsmodel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    float br = (float) 0.01;
    float brn = (float) 0.5;

    private static String[] freqText = {"11.025 KHz"}; //, "16.000 KHz", "22.050 KHz", "44.100 KHz (Highest)"};
    private static Integer[] freqset = {11025}; //, 16000, 22050, 44100};
    private ArrayAdapter<String> adapter;

    Spinner spFrequency;
    Button btnStartService, btnEndService, setting;

    Button btnStartRecording,btnEndRecording;

    Button setPhone, setiHostIP, moreSettings;

    Boolean recording = false;

    public static DatagramSocket socket;
    private String serverAddr = "172.16.0.1";
    private int port = 8888;

    Vibrator vibrator = null;

    Button click;
    Button buttonwms;
    TextView tv;
    EditText edit_user;
    ProgressBar pbar;

    String wmsAPI = "http://" + serverAddr + ":8080";

    private String phone;
    private String mac;

    Button btnPost;
    Context mcontext;

    /**
     * 向Service发送Message的Messenger对象
     */
    Messenger mService = null;

    /**
     * 判断有没有绑定Service
     */
    boolean mBound;

    boolean IsRecording=false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Activity已经绑定了Service
            // 通过参数service来创建Messenger对象，这个对象可以向Service发送Message，与Service进行通信
            mService = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 绑定Service
        bindService(new Intent(this, MessengerService.class), mConnection,
                Context.BIND_AUTO_CREATE);


        //Toast.makeText(getApplicationContext(), "测试启动服务成功完成... ", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
        // 解绑
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
*/
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Message msg;
        IsRecording=IsRecording;
        if(IsRecording) {

            msg = Message.obtain(null, MessengerService.MSG_START_RECORDING, 0, 0);

        }
        else
        {
            msg = Message.obtain(null, MessengerService.MSG_END_RECORDING, 0, 0);
        }
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 解决在4.0版本下，不能使用HttpClient的问题。
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        // 禁止程序休眠
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 将亮度调至最低
        // UtilHelper.brightnessPreview(this, br);

        //Log.d(this.LOG_TAG, "aar activity created.");

        mcontext = this.getApplicationContext();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /*
        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnEndService = (Button) findViewById(R.id.btnStopService);


        btnStartService.setOnClickListener(startRecOnClickListener);
        btnEndService.setOnClickListener(stopRecOnClickListener);
        */


        btnStartService = (Button) findViewById(R.id.btnStartService);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sayHello();
                Log.i(LOG_TAG, "Start service command.");
                //Hello();
                //if (isServiceRunning(getApplicationContext(), "net.wyun.wmrecord.MessengerService"))
                if (isServiceRunning(getApplicationContext(), getLocalClassName().toString()))
                {
                    Toast.makeText(getApplicationContext(), "监听已经启动成功-----无需再启动", Toast.LENGTH_LONG).show();
                } else {
                    Intent s = new Intent(getApplicationContext(), net.wyun.wmrecord.MessengerService.class);
                    getApplicationContext().startService(s);
                    Toast.makeText(getApplicationContext(), "监听启动成功---------", Toast.LENGTH_LONG).show();
                }


//                if (!mBound) return;

                if (!mBound) return;
                /*
                Context context = getApplicationContext();
                Intent s = new Intent(context, net.wyun.wmrecord.MessengerService.class);
                context.startService(s);
                */

                // 向Service发送一个Message
                Message msg = Message.obtain(null, MessengerService.MSG_START_SERVICE, 0, 0);
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // Notice2();
            }
        });

        btnEndService = (Button) findViewById(R.id.btnEndService);
        btnEndService.setEnabled(false);
        btnEndService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mBound) return;
                // 向Service发送一个Message
                Message msg = Message.obtain(null, MessengerService.MSG_END_SERVICE, 0, 0);
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // Notice2();
            }
        });



        btnStartRecording = (Button) findViewById(R.id.btnStartRecording);
        btnStartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sayHello();
                Log.i(LOG_TAG, "MSG_START_RECORDING service command.");

//                if (!mBound) return;

                if (!mBound) return;

                // 向Service发送一个Message
                Message msg = Message.obtain(null, MessengerService.MSG_START_RECORDING, 0, 0);

                try {
                    mService.send(msg);
                    IsRecording=true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // Notice2();
            }
        });

        btnEndRecording = (Button) findViewById(R.id.btnEndRecording);
        btnEndRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mBound) return;
                // 向Service发送一个Message
                Message msg = Message.obtain(null, MessengerService.MSG_END_RECORDING, 0, 0);
                try {
                    mService.send(msg);
                    IsRecording = false;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // Notice2();
            }
        });

        //setting.setOnClickListener(playBackOnClickListener);

        /*
        spFrequency = (Spinner) findViewById(R.id.frequency);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, freqText);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);
        //spFrequency.setFocusable(false);
        */
        btnEndService.setEnabled(false);

        // restful webservice
        serverAddr = UtilHelper.getSetting(this, "prefIhostIp");
        wmsAPI = "http://" + serverAddr + ":8080";
        click = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.tv);
        edit_user = (EditText) findViewById(R.id.edit);
        pbar = (ProgressBar) findViewById(R.id.pb);
        pbar.setVisibility(View.INVISIBLE);
        buttonwms = (Button) findViewById(R.id.buttonwms);
        buttonwms.setOnClickListener(buttonWmsServerListner);


        // 设置手机号码
        setPhone = (Button) findViewById(R.id.btnSetphone);
        setPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(v.getContext());
                String tmp = UtilHelper.getSetting(mcontext,"prefUserPhone");
                inputServer.setText(tmp);
                inputServer.selectAll();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Set User Phone").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String txt = inputServer.getText().toString();
                        UtilHelper.setSetting(mcontext, "prefUserPhone", txt);
                        UpdateSettings();
                        ShowMessage("New User Phone: " + txt);
                    }
                });
                builder.show();
            }
        });

        // 设置 iHost IP
        setiHostIP = (Button) findViewById(R.id.btnSetIpAddress);
        setiHostIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(v.getContext());
                 String tmp = UtilHelper.getSetting(mcontext,"prefIhostIp");
                inputServer.setText(tmp);
                inputServer.selectAll();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Set iHost IP").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String txt = inputServer.getText().toString();
                        UtilHelper.setSetting(mcontext, "prefIhostIp", txt);
                        UpdateSettings();
                        ShowMessage("New iHost IP: " + txt);
                    }
                });
                builder.show();
            }
        });

        // 更多设置
        moreSettings = (Button) findViewById(R.id.btnMoreSettings);
        moreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setClassName("net.wyun.wmrecord",
                        "net.wyun.wmrecord.UserSettingActivity");
                startActivity(intent);
            }
        });

        phone = UtilHelper.getSetting(this, "prefUserPhone");

        mac = UtilHelper.getSetting(this, "prefMac");

        UpdateSettings();



    }

    private void UpdateSettings() {
        serverAddr = UtilHelper.getSetting(this, "prefIhostIp");
        wmsAPI = "http://" + serverAddr + ":8080";
        phone = UtilHelper.getSetting(this, "prefUserPhone");

        updateServerIP();
        //Toast.makeText(getApplicationContext(), wmsAPI, Toast.LENGTH_LONG).show();
    }


    View.OnClickListener buttonWmsServerListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "buttonWmsServerListner");
            HttpPostDataGetWms();
        }
    };

    private void HttpPostDataCreateReception() {
        try {
            pbar.setVisibility(View.VISIBLE);
            Log.i(LOG_TAG, "HttpClient init");
            SetMessage("HttpClient init");
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            String uri = wmsAPI;
            String postUrl = uri + "/reception";
            HttpPost httppost = new HttpPost(postUrl);

            //添加http头信息
            Log.i(LOG_TAG, "添加http头信息");
            //httppost.addHeader("Authorization", "your token"); //认证token
            httppost.addHeader("Content-Type", "application/json");
            //httppost.addHeader("User-Agent", "imgfornote");
            httppost.addHeader("Charset", "UTF-8");

            //http post的json数据格式：  {"name": "your name","parentId": "id_of_parent"}
            Log.i(LOG_TAG, "http post的json数据格式");
            JSONObject obj = new JSONObject();
            obj.put("phone", phone);
            obj.put("mac", mac);

            httppost.setEntity(new StringEntity(obj.toString()));
            //httppost.setEntity(new StringEntity( "{\"phone\": \"5016548878\",\"mac\":\"88-36-AA-FF-FF-BB\"}"));

            HttpResponse response;
            response = httpclient.execute(httppost);

            //检验状态码，如果成功接收数据
            Log.i(LOG_TAG, "检验状态码，如果成功接收数据");
            int code = response.getStatusLine().getStatusCode();
            Log.i(LOG_TAG, "状态码: " + String.valueOf(code));

            String rString = response.toString();
            Log.i(LOG_TAG, "response: " + rString);

            SetMessage("状态码: " + String.valueOf(code));

            if (code == 200) {
                String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
                obj = new JSONObject(rev);
                String id = obj.getString("id");
                String version = obj.getString("version");
            }

            if (code == 201) {
                Log.i(LOG_TAG, "Created");
            }

            Log.i(LOG_TAG, "完成");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "ClientProtocolException: " + e.getMessage());
            SetMessage("ClientProtocolException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "IOException: " + e.getMessage());
            SetMessage("IOException: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            SetMessage("Exception: " + e.getMessage());
        } finally {
            pbar.setVisibility(View.INVISIBLE);
        }
    }

    private String HttpPostDataGetWms() {
        try {
            String user = edit_user.getText().toString();
            pbar.setVisibility(View.VISIBLE);
            Log.i(this.LOG_TAG, "wmsAPI: " + wmsAPI);
            tv.setText(wmsAPI);

            // Retrofit section start from here...
            // create an adapter for retrofit with base url
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(wmsAPI).build();


            // creating a service for adapter with our GET class
            wmsapi git = restAdapter.create(wmsapi.class);

            // Now ,we need to call for response
            // Retrofit using gson for JSON-POJO conversion

            git.getFeed(new Callback<wmsmodel>() {
                @Override
                public void success(wmsmodel gitmodel, Response response) {
                    // we get json object from github server to our POJO or model class

                    tv.setText("Wms Name :" + gitmodel.getWms() +
                            "\nVersion :" + gitmodel.getVersion());

                    Log.i(LOG_TAG, "wms: " + gitmodel.getWms());
                    pbar.setVisibility(View.INVISIBLE); // disable progressbar
                    Toast.makeText(getApplicationContext(), "测试连通性成功完成... ", Toast.LENGTH_LONG).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    tv.setText(error.getMessage());
                    pbar.setVisibility(View.INVISIBLE); // disable progressbar
                    Log.i(LOG_TAG, "wms error: ");
                    Toast.makeText(getApplicationContext(), "测试连通性失败: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            SetMessage("Exception: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "测试连通性异常: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            pbar.setVisibility(View.INVISIBLE);
            return "";
        }
    }

    private String HttpPostDataGetMac() {
        try {
            String user = edit_user.getText().toString();
            pbar.setVisibility(View.VISIBLE);

            // Retrofit section start from here...
            // create an adapter for retrofit with base url

            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(wmsAPI).build();

            // creating a service for adapter with our GET class
            macapi git = restAdapter.create(macapi.class);

            // Now ,we need to call for response
            // Retrofit using gson for JSON-POJO conversion

            git.getFeed(new Callback<macmodel>() {
                @Override
                public void success(macmodel gitmodel, Response response) {
                    // we get json object from github server to our POJO or model class

                    tv.setText("Github Name :" + gitmodel.getMac());

                    Log.i(LOG_TAG, "mac: " + gitmodel.getMac());
                    mac = gitmodel.getMac();
                    UtilHelper.setSetting(mcontext,"prefMac",mac);
                    pbar.setVisibility(View.INVISIBLE); // disable progressbar
                }

                @Override
                public void failure(RetrofitError error) {
                    tv.setText(error.getMessage());
                    pbar.setVisibility(View.INVISIBLE); // disable progressbar
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            SetMessage("Exception: " + e.getMessage());
        } finally {
            pbar.setVisibility(View.INVISIBLE);
            return "";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private static final int RESULT_SETTINGS = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, UserSettingActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static String KEY_IP = "prefIhostIp";

    private String getSetting(String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String value = sharedPrefs.getString(KEY_IP, "");
        return value;
    }

    View.OnClickListener startRecOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            try {
                UtilHelper.brightnessPreview(MainActivity.this, br);

                HttpPostDataGetMac();

                HttpPostDataCreateReception();

                Thread recordThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        recording = true;
                        try {
                            startRecord();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }

                });

                recordThread.start();
                btnStartService.setEnabled(false);
                btnEndService.setEnabled(true);
            } catch (Exception e) {
                ShowMessage("Exp gone when started: " + e.getMessage());
            }
        }
    };

    View.OnClickListener stopRecOnClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            UtilHelper.brightnessPreview(MainActivity.this, brn);
            recording = false;
            btnStartService.setEnabled(true);
            btnEndService.setEnabled(false);
        }
    };

    View.OnClickListener playBackOnClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startSettingActivity();
        }

    };

    private void startSettingActivity() {
        Intent i = new Intent(this, UserSettingActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
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

            minBufferSize = 1280;

            byte[] audioData = new byte[minBufferSize];

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleFreq,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize);

            audioRecord.startRecording();
            socket = new DatagramSocket();

            vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_START, -1);

            while (recording) {
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
                Log.d(this.LOG_TAG, "send pkt to server.");

                pktCount += 1;
                if (pktCount == 15000) {
                    long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                    vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_REMIND, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
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

            vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_STOP, -1);
            recording = false;
            //startRec.setEnabled(true);
            //stopRec.setEnabled(false);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(this.LOG_TAG, "key code: " + String.valueOf(keyCode));
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                Log.i(this.LOG_TAG, "KEYCODE_VOLUME_DOWN");
                ShowMessage("KEYCODE_VOLUME_DOWN is pressed.");
                return true;

            case KeyEvent.KEYCODE_MENU:
                //onOptionsItemSelected()
                ShowMessage("KEYCODE_MENU is pressed.");
                return true;

            case KeyEvent.KEYCODE_VOLUME_UP:
                Log.i(this.LOG_TAG, "KEYCODE_VOLUME_UP");
                Context mcon = getApplicationContext();

                if (!recording) {
                    Thread recordThread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            recording = true;
                            try {
                                startRecord();
                            } catch (UnknownHostException e) {
                                e.printStackTrace();
                            }
                        }

                    });

                    recordThread.start();
                    btnStartService.setEnabled(false);
                    btnEndService.setEnabled(true);
                } else {
                    vibrator.vibrate(AppConstant.ServiceTag.VIBRATOR_PATTERN_REMIND, -1);
                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:

                ShowMessage("KEYCODE_VOLUME_MUTE is pressed.");
                return true;
            case KeyEvent.KEYCODE_ENTER:
                Log.i(this.LOG_TAG, "Enter");
                ShowMessage("KEYCODE_ENTER is pressed.");
                return false;
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    private void ShowMessage(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    // public methods
    private void SetMessage(String msg) {
        tv.setText(msg);
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            String serviceName=serviceList.get(i).service.getClassName();
            //Log.i("this",serviceName);
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}

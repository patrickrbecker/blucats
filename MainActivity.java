package com.bluecats.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import com.bluecats.services.SmsAsyncTask;
import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManager;
import com.bluecats.sdk.BCBeaconManagerCallback;
import com.bluecats.sdk.BCMicroLocation;
import com.bluecats.sdk.BCSite;
import com.bluecats.sdk.BCTriggeredEvent;
import com.bluecats.sdk.BlueCatsSDK;
import com.bluecats.services.interfaces.BlueCatsSDKInterfaceService;
import com.bluecats.services.interfaces.IBlueCatsSDKInterfaceServiceCallback;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.codec.binary.Base64;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private TextView mTxtMessage;
    private TextView mTxtMessage1;
    //private
    private Button mButton;
    private Button mButton2;
    private Button showNotificationBut, stopNotificationBut, alertButton, sendEmail;

    NotificationManager notificationManager;

    boolean isNotificActive = false;

    int notifID = 33;

    BCBeaconManager mBCBeaconManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNotificationBut = (Button) findViewById(R.id.showNotificationBut);
        stopNotificationBut = (Button) findViewById(R.id.stopNotificationBut);
        alertButton = (Button)findViewById(R.id.alertButton) ;
        sendEmail = (Button) findViewById(R.id.sendEmail);


        mTxtMessage = (TextView)findViewById(R.id.txt_message);
        mTxtMessage1 = (TextView)findViewById(R.id.txt_message1);
        mButton = (Button)findViewById(R.id.btn);
        mButton2 = (Button)findViewById(R.id.btn2);
        mButton.setOnClickListener(this);
        mButton2.setOnClickListener(this);

        MainApplication.runSDK(this.getApplicationContext());
        mBCBeaconManager = new BCBeaconManager();
        mBCBeaconManager.registerCallback(mCallback);

        sendEmail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                try {
                    mTxtMessage1.setText("Sending SMS");
                    sendSMS();
                }
                catch (Exception e){}
                }

        });
        /*sendEmail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                sendEmail();
            }
        });*/

    }




    protected void sendEmail() {

        Log.i("Send email", "");
        String[] TO = {"deepguru80@gmail.com"};
        String[] CC = {"kabir8090@gmail.com"};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,TO);
        emailIntent.putExtra(Intent.EXTRA_CC,CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Beacon found");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Beacon with patient found");

        try {
            startActivity(Intent.createChooser(emailIntent,"Send mail ...."));
            finish();
            Log.i("Finished sending email ", " ");
        }

        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }


    }

    protected void sendSMS () throws Exception {

    try {
        String phoneNumber = "2152061469";
        String appKey = "08449194-2f50-4868-bef0-b1d0131e4624";
        String appSecret = "m6fvr1rM2Eeo1Okx1sAOGg==";
        String message = "Hello, world!";

        URL url = new URL("https://messagingapi.sinch.com/v1/sms/" + phoneNumber);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String userCredentials = "application\\" + appKey + ":" + appSecret;
        byte[] encoded = Base64.encodeBase64(userCredentials.getBytes());
        String basicAuth = "Basic " + new String(encoded);
        connection.setRequestProperty("Authorization", basicAuth);

        String postData = "{\"Message\":\"" + message + "\"}";
        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());

        StringBuilder response = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            response.append(line);

        br.close();
        os.close();

        System.out.println(response.toString());

    } catch (Exception e) {
        e.printStackTrace();
    }

}

    public void showNotification(View v) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this );

        builder.setSmallIcon(R.drawable.ic_launcher1);
        builder.setContentTitle("My Notification");
        builder.setContentText("This is my first notificaation...  ");
        builder.setTicker("Alert New Message");
        builder.setSmallIcon(R.drawable.ic_launcher1);

        Intent intent = new Intent(this,SecondClass.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(SecondClass.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NM.notify(0,builder.build());

        isNotificActive = true;


    }


    public void stopNotification(View v) {

        if (isNotificActive)
        {
            notificationManager.cancel(notifID);
        }
    }


    BCBeaconManagerCallback mCallback = new BCBeaconManagerCallback() {
        @Override
        public void didEnterSite(BCSite site) {
            super.didEnterSite(site);
            Log.d(TAG, "didEnterSite "+site.getName());
        }

        @Override
        public void didExitSite(BCSite site) {
            super.didExitSite(site);
            Log.d(TAG, "didExitSite "+site.getName());
        }

        @Override
        public void didDetermineState(BCSite.BCSiteState state, BCSite forSite) {
            super.didDetermineState(state, forSite);
            Log.d(TAG, "didDetermineState "+forSite.getName());
        }

        @Override
        public void didEnterBeacons(List<BCBeacon> beacons) {
            super.didEnterBeacons(beacons);
            Log.d(TAG, "didEnterBeacons "+getBeaconNames(beacons));


            for(BCBeacon beacon : beacons)
                if(beacon.getSerialNumber().equals("5000050e"))
                    new SmsAsyncTask().execute("In range of beacon 5000050e");
        }

        @Override
        public void didExitBeacons(List<BCBeacon> beacons) {
            super.didExitBeacons(beacons);
            Log.d(TAG, "didExitBeacons "+getBeaconNames(beacons));
            sendEmail(); //trigger email when beacon leaves proximity
            //sendSMS(); //trigger email when beacon leaves proximity
        }

        @Override
        public void didDetermineState(BCBeacon.BCBeaconState state, BCBeacon forBeacon) {
            super.didDetermineState(state, forBeacon);
            Log.d(TAG, "didDetermineState "+forBeacon.getSerialNumber());
        }

        @Override
        public void didRangeBeacons(List<BCBeacon> beacons) {
            super.didRangeBeacons(beacons);
            Log.d(TAG, "didRangeBeacons "+getBeaconNames(beacons));

            for(BCBeacon beacon : beacons) {
               // if (beacon.getName().equalsIgnoreCase("Hermes_Deepak_2")) {
                    if (beacon.getName().equalsIgnoreCase("Hermes_Pat_2")) {

                    new Thread(){
                        @Override
                        public void run(){
                            try {
                                Thread.sleep(10000); // 1 Second (i think)
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sendEmail();
                        }
                    }.start();  // might be .start();
                    //new SmsAsyncTask().execute("In range of beacon 5000050e");
                    //sendEmail();
                }
            }

        }

        @Override
        public void didRangeBlueCatsBeacons(List<BCBeacon> beacons) {
            super.didRangeBlueCatsBeacons(beacons);
            Log.d(TAG, "didRangeBlueCatsBeacons "+getBeaconNames(beacons));
        }

        @Override
        public void didRangeNewbornBeacons(List<BCBeacon> newBornBeacons) {
            super.didRangeNewbornBeacons(newBornBeacons);
            Log.d(TAG, "didRangeNewbornBeacons "+getBeaconNames(newBornBeacons));
        }



        @Override
        public void didRangeEddystoneBeacons(List<BCBeacon> eddystoneBeacons) {
            super.didRangeEddystoneBeacons(eddystoneBeacons);
            Log.d(TAG, "didRangeEddystoneBeacons "+getBeaconNames(eddystoneBeacons));
        }

        @Override
        public void didDiscoverEddystoneURL(URL eddystoneUrl) {
            super.didDiscoverEddystoneURL(eddystoneUrl);
            Log.d(TAG, "didDiscoverEddystoneURL "+eddystoneUrl.toString());
        }
    };

    private String getBeaconNames(List<BCBeacon> beacons) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (BCBeacon beacon: beacons) {
            sb.append(beacon.getSerialNumber());
            sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        BlueCatsSDKInterfaceService.registerBlueCatsSDKServiceCallback(MainActivity.this.getClass().getName(), mBlueCatsSDKInterfaceServiceCallback);

        BlueCatsSDKInterfaceService.didEnterForeground();
    }

    @Override
    protected void onPause() {
        super.onPause();

        BlueCatsSDKInterfaceService.unregisterBlueCatsSDKServiceCallback(MainActivity.this.getClass().getName());

        BlueCatsSDKInterfaceService.didEnterBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mBCBeaconManager.unregisterCallback(mCallback);
        BlueCatsSDKInterfaceService.unregisterBlueCatsSDKServiceCallback(MainActivity.this.getClass().getName());

        BlueCatsSDKInterfaceService.didEnterBackground();
    }

    private IBlueCatsSDKInterfaceServiceCallback mBlueCatsSDKInterfaceServiceCallback = new IBlueCatsSDKInterfaceServiceCallback() {
        @Override
        public void onDidEnterSite(BCSite site) {



        }

        @Override
        public void onDidExitSite(BCSite site) {



        }

        @Override
        public void onDidUpdateNearbySites(List<BCSite> sites) {

        }

        @Override
        public void onDidRangeBeaconsForSiteID(BCSite site, List<BCBeacon> beacons) {

        }

        @Override
        public void onDidUpdateMicroLocation(List<BCMicroLocation> microLocations) {

        }

        @Override
        public void onTriggeredEvent(BCTriggeredEvent triggeredEvent) {
            final BCBeacon beacon = triggeredEvent.getFilteredMicroLocation().getBeacons().get(0);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTxtMessage.setText("Closest to beacon " + beacon.getSerialNumber());

                }
            });
        }
    };




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {

        } else if (v.getId() == R.id.btn2) {

        }
    }
    public void setAlarm (View v) {

        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        Intent alertIntent = new Intent(this, AlertReceiver.class);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,PendingIntent.getBroadcast(this,1, alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));
    }

}

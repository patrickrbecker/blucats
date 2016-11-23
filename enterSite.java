package com.example.pat.gps;

import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bluecats.sdk.BCBeacon;
import com.bluecats.sdk.BCBeaconManager;
import com.bluecats.sdk.BCBeaconManagerCallback;
import com.bluecats.sdk.BCLocalNotification;
import com.bluecats.sdk.BCLocalNotificationManager;
import com.bluecats.sdk.BCSite;
import com.bluecats.sdk.BlueCatsSDK;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


   // @Override
 /**   protected void onCreate(Bundle savedInstanceState) {
  *      super.onCreate(savedInstanceState);
 *       setContentView(R.layout.activity_main);
*
        *BlueCatsSDK.startPurringWithAppToken(getApplicationContext(), "df2d4eb3-32e7-463b-87d3-f6bad45f7d97");
       * // ATTENTION: This was auto-generated to implement the App Indexing API.
      *  // See https://g.co/AppIndexing/AndroidStudio for more information.
     *   client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

/*    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
*/
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }

    @Override
    protected void onCreate( final Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.sites );

        BlueCatsSDK.startPurringWithAppToken( getApplicationContext(), "df2d4eb3-32e7-463b-87d3-f6bad45f7d97" );

        final BCBeaconManager beaconManager = new BCBeaconManager();
        BCBeaconManagerCallback mBeaconManagerCallback = null;
        beaconManager.registerCallback( mBeaconManagerCallback );

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick (View v){

              //  TextView textView = (TextView) findViewById(R.id.textView);
              //  textView.setText("didEnterBeacons" + "found");

            }
        });

    }

    private final BCBeaconManagerCallback mBeaconManagerCallback = new BCBeaconManagerCallback() {
        @Override
        public void didEnterBeacons(List<BCBeacon> beacons) {
            super.didEnterBeacons(beacons);

            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText("didEnterBeacons" + beacons.size() + "found");
        }

        @Override
        public void didExitBeacons(List<BCBeacon> beacons) {
            super.didExitBeacons(beacons);
        }

    };

    //public class SiteEntry {


        public void didEnterSite(final BCSite site) {
            final int notificationID = 1;

            final BCLocalNotification notification = new BCLocalNotification(notificationID);
            notification.setAlertContentTitle("Welcome to " + site.getName());
            notification.setAlertContentText("View Specials");
            notification.setAlertSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            BCLocalNotificationManager.getInstance().scheduleLocalNotification(notification);
        }
    }

//}

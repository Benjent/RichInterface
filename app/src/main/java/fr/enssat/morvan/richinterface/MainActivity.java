package fr.enssat.morvan.richinterface;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;

/*
    The main activity view is composed of two parts : the video and the data - landscape-wise.
    The video is on the left of the screen with its chapters below.
    The data are on the right of the screen split between the web view on top and the map view below.
 */
public class MainActivity extends AppCompatActivity {

    private static final String MAPVIEW_BUNDLE_KEY = "MAP_VIEW_BUNDLE_KEY";
    VideoView videoView;
    WebView webView;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Video metadata
        VideoMetadataParser parser = new VideoMetadataParser(this);
        final VideoMetadata videoMetadata = parser.parse();

        // Views
        videoView = findViewById(R.id.VideoView);
        webView = findViewById(R.id.WebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videoMetadata.getTags().get(0).getUrl());
        mapView = findViewById(R.id.MapView);

        // Media controller
        final MediaController mediacontroller;
        try {
            // Start the MediaController
            mediacontroller = new MediaController(MainActivity.this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(videoMetadata.getUrl());
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Play the video
                public void onPrepared(MediaPlayer mp) {
                    mediacontroller.setAnchorView(videoView);
                    videoView.start();
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        // Chapters
        LinearLayout chapters = (LinearLayout) findViewById(R.id.ChaptersView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Iterator<Tag> i = videoMetadata.getTags().iterator(); i.hasNext(); ) {
            Tag item = i.next();
            Button button = new Button(this);
            button.setTag(item);
            button.setText(item.getLabel());
            button.setLayoutParams(layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final int position = (int)v.getTag();
                    final Tag metaTag = (Tag) v.getTag();
                    videoView.seekTo(metaTag.getTimeStamp() * 1000); // Ensure * 1000
                    webView.loadUrl(metaTag.getUrl());
                }
            });
            chapters.addView(button);
        }

        // Map view
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                long lat = 0;
                long lon = 0;
                String label = "";
                int timestamp = 0;

                for (Iterator<Waypoint> i = videoMetadata.getWaypoints().iterator(); i.hasNext(); ) {
                    Waypoint item = i.next();

                    lat = item.getLat();
                    lon = item.getLon();
                    label = item.getLabel();
                    timestamp = item.getTimeStamp();

                    Log.e("COUCOU", item.getLat().toString());

                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat,lon))
                            .title(label));
                    marker.setTag(timestamp);
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int timestamp = (int)marker.getTag();
                        videoView.seekTo(timestamp * 1000);
                        return false;
                    }
                });

                // Set default position to USA
                CameraUpdate point = CameraUpdateFactory.newLatLng(new LatLng(38, -92));
                googleMap.moveCamera(point);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Handle the google API KEY for the map view

        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle != null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

}

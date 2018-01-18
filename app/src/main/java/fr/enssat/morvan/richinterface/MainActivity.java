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

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Video metadata
        VideoMetadataParser parser = new VideoMetadataParser(this);
        final VideoMetadata videoMetadata = parser.parse();

        videoView = findViewById(R.id.VideoView);
        webView = findViewById(R.id.WebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(videoMetadata.getTags().get(0).getUrl());

        // TODO handle MediaFormat class

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
        LinearLayout chapters = (LinearLayout)findViewById(R.id.ChaptersView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        for (Iterator<Tag> i = videoMetadata.getTags().iterator(); i.hasNext();) {
            Tag item = i.next();
            Button button = new Button(this);
            button.setTag(item);
            button.setText(item.getLabel());
            button.setLayoutParams(layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //final int position = (int)v.getTag();
                    final Tag metaTag = (Tag)v.getTag();
                    videoView.seekTo(metaTag.getTimeStamp() * 1000); // Ensure * 1000
                    webView.loadUrl(metaTag.getUrl());
                }
            });
            chapters.addView(button);
        }

        // ********** TAGS ********** //
        /*ListView tagListView = (ListView) findViewById(R.id.ChaptersView);

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = videoMetadata.getTags().get(position);

                wikiView.loadUrl(tag.getUrl());
                videoView.seekTo(tag.getTimeStamp() * 1000);
            }
        });*/

    }

}

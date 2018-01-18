package fr.enssat.morvan.richinterface;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by benjent on 11/01/18.
 */

public class VideoMetadataParser {

    private Context mContext;

    public VideoMetadataParser(Context context) {
        this.mContext = context;
    }

    public VideoMetadata parse() {

        VideoMetadata videoMetadata = null;

        try {
            InputStream is = mContext.getResources().openRawResource(R.raw.video);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                is.close();
            } catch(Exception e) {
                e.printStackTrace();
            }

            String metadataJSON = writer.toString();
            JSONObject metadata = new JSONObject(metadataJSON);

            String videoName = metadata.getString("name");
            String videoUrl = metadata.getString("url");
            JSONArray tags = metadata.getJSONArray("tags");

            /*for (int i = 0; i < tags.length(); i++) {
                JSONObject videoMetadataJSON = (JSONObject) tags.get(i);

                VideoMetadata videoMetadata = new VideoMetadata(videoMetadataJSON);

                videoMetadataArray.add(videoMetadata);
            }*/

            videoMetadata = new VideoMetadata(metadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videoMetadata;
    }

}

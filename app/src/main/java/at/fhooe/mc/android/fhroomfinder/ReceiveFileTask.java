package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ReceiveFileTask extends AsyncTask<String, Void, Void> {

    private Context mContext; // TODO leak?
    private TimetableFragment ttFragment;

    ReceiveFileTask(Context c, TimetableFragment timetableFragment) {
        mContext = c;
        ttFragment = timetableFragment;
    }

    @Override
    protected Void doInBackground(String... _url) {
        int count;
        try {
            URL url = new URL(_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            Log.i("FHRoomFinder", "starting to download"); // TODO remove

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            File file = new File(mContext.getFilesDir(), "ical.ics");
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            while ((count = input.read(data)) != -1) {
                // writing data to file
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            Log.i("FHRoomFinder", "file downloaded!"); // TODO remove
            ttFragment.readCal();

        } catch (Exception e) {
            Log.e("FHRoomFinder", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ttFragment.updateUI();
    }
}

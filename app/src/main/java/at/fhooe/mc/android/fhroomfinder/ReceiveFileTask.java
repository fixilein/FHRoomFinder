package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReceiveFileTask extends AsyncTask<String, Void, Void> {

    private WeakReference<Context> mContext; // TODO leak?
    private TimetableFragment ttFragment;

    ReceiveFileTask(Context _c, TimetableFragment _timetableFragment) {
        mContext = new WeakReference<>(_c);
        ttFragment = _timetableFragment;
    }

    @Override
    protected Void doInBackground(String... _url) {
        Context context = mContext.get();
        if (context != null) {
            File f = new File(context.getFilesDir(), "ical.ics");
            f.delete();

            int count;
            try {
                URL url = new URL(_url[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // 20 sec timeout
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);

                Log.i("FHRoomFinder", "starting to download"); // TODO remove

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                File file = new File(context.getFilesDir(), "ical.ics");
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
                ttFragment.setInvalidURL(false);
                ttFragment.readCal();

                connection.disconnect();

            } catch (Exception e) {
                Log.e("FHRoomFinder", e.getMessage());
                if (e.getMessage().contains("no protocol")) {
                    ttFragment.setInvalidURL(true);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!ttFragment.isDetached())
            ttFragment.updateUI();
    }
}

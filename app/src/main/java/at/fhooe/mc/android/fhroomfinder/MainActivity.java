package at.fhooe.mc.android.fhroomfinder;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ListActivity {

    static final String TAG = "FH_Room_Finder";

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        fillRoomList();
    }

    private void fillRoomList() {
        RoomAdapter adapter = new RoomAdapter(this);

        List<Room> list = readRawTextFile(getApplicationContext(), R.raw.fh2eb4);

        adapter.addAll(list);
        setListAdapter(adapter);
    }

    public static List<Room> readRawTextFile(Context _ctx, int _resId) {
        List<Room> list = new LinkedList<>();

        InputStream inputStream = _ctx.getResources().openRawResource(_resId);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;

        try {
            while ((line = buffreader.readLine()) != null) {
                list.add(Room.fromLine(line));
            }
        } catch (IOException e) {
            return null;
        }

        return list;
    }


    @Override
    protected void onListItemClick(ListView _l, View _v, int _pos, long _id) {
        ListAdapter list = getListAdapter();

        Room room = (Room) list.getItem(_pos);
        Toast.makeText(this, "Clicked Room: " + room.getName() + " "
                + room.getNumber(), Toast.LENGTH_SHORT).show();
    }
}

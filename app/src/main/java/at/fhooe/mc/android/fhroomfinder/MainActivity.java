package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "FH_Room_Finder";
    private RoomAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        fillRoomList();

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle("Räume");
        setSupportActionBar(tb);
    }

    private void fillRoomList() {
        listView = findViewById(R.id.activity_main_list_view);
        List<Room> list = new LinkedList<>();

        list.addAll(readRawTextFile(getApplicationContext(), R.raw.fh2eb4));
        list.addAll(readRawTextFile(getApplicationContext(), R.raw.fh2eb3));

        adapter = new RoomAdapter(this, list);
        adapter.addAll(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int _pos, long id) {
                Room room = adapter.getItem(_pos);
                Toast.makeText(getApplicationContext(), "Clicked Room: " + room.getName() + " "
                        + room.getFullNumber(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<Room> readRawTextFile(Context _context, int _resId) {
        List<Room> list = new LinkedList<>();

        InputStream inputStream = _context.getResources().openRawResource(_resId);
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
    public boolean onCreateOptionsMenu(Menu _menu) {

        getMenuInflater().inflate(R.menu.activity_main_menu, _menu);

        MenuItem myActionMenuItem = _menu.findItem(R.id.search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String _s) {
                if (TextUtils.isEmpty(_s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(_s);
                }
                return true;
            }
        });
        return true;
    }
}

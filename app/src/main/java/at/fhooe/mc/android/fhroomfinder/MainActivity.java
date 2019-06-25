package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "FH_Room_Finder";
    static final String ROOM_INTENT = "RoomIntent";
    public static final String FLOOR_INTENT = "Floor_section_toLocator";
    public static final String BUILDING_INTENT = "building_section_toLocator";
    private RoomAdapter adapter;
    private ListView listView;
    SearchView searchView;
    List<Room> list;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle(R.string.app_name);
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);

        fillRoomList();
        enableSelectionFragment(true);
    }

    private void enableSelectionFragment(boolean _select) {
        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction t = mgr.beginTransaction();
        View sep = findViewById(R.id.activity_main_separator);
        if (_select) {
            t.replace(R.id.activity_main_selection_frame, new FloorPlanSelectionFragment());
            sep.setVisibility(View.VISIBLE);
        } else {
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.activity_main_selection_frame);
            if (fragmentById != null)
                t.remove(fragmentById);
            sep.setVisibility(View.GONE);
        }
        t.commit();
    }

    private void fillRoomList() {
        list = new LinkedList<>();
        parseXml();

        adapter = new RoomAdapter(this, list);
        adapter.addAll(list);
        listView = findViewById(R.id.activity_main_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int _pos, long id) {
                Room room = adapter.getItem(_pos);
                Intent i = new Intent(MainActivity.this, LocatorActivity.class);
                i.putExtra(ROOM_INTENT, room);
                startActivity(i);
            }
        });
    }

    private void parseXml() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("rooms.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);
        } catch (XmlPullParserException e) {
        } catch (IOException e) {
        }
    }

    private void processParsing(XmlPullParser _parser) throws IOException, XmlPullParserException {
        Room currentRoom = null;
        int eventType = _parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;
            if (eventType == XmlPullParser.START_TAG) {
                eltName = _parser.getName();

                if ("room".equals(eltName)) {
                    currentRoom = new Room();
                    list.add(currentRoom);
                } else if (currentRoom != null) {
                    if ("name".equals(eltName)) {
                        currentRoom.setName(_parser.nextText());
                    } else if ("tok".equals(eltName)) {
                        String s = _parser.nextText();
                        currentRoom.setBuilding(Character.getNumericValue(s.charAt(2)));
                        currentRoom.setFloor(Character.getNumericValue(s.charAt(4)));
                        currentRoom.setNumber(Character.getNumericValue(s.charAt(5)) * 10 +
                                Character.getNumericValue(s.charAt(6)));
                    } else if ("x".equals(eltName)) {
                        currentRoom.setX(Integer.valueOf(_parser.nextText()));
                    } else if ("y".equals(eltName)) {
                        currentRoom.setY(Integer.valueOf(_parser.nextText()));
                    }
                }
            }
            eventType = _parser.next();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, _menu);

        MenuItem menuItemSearch = _menu.findItem(R.id.activity_main_menu_search);
        searchView = (SearchView) menuItemSearch.getActionView();
        searchView.setQueryHint(getString(R.string.room_name_or_number));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String _s) {
                adapter.filter(_s);
                if (_s.equals("")) enableSelectionFragment(true);
                else enableSelectionFragment(false);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem _item) {
        if (_item.getItemId() == R.id.activity_main_menu_about) {
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(i);
        }
        return true;
    }
}

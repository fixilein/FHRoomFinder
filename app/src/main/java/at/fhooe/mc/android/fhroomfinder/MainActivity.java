package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private RoomAdapter mAdapter;
    SearchView mSearchView;
    public List<Room> mList;
    TimetableFragment mTimetableFragment;
    FloorPlanSelectionFragment mFloorPlanSelectionFragment;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setTitle(R.string.app_name);
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);

        fillRoomList();

        mFloorPlanSelectionFragment = new FloorPlanSelectionFragment();
        mTimetableFragment = new TimetableFragment();
        mTimetableFragment.setList(mList);
        enableFragments(true);
    }

    private void enableFragments(boolean _select) {
        SharedPreferences sp = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        String url = sp.getString(getString(R.string.shared_prefs_ical_link), "undefined");
        boolean enableTimeTable = !url.equals("undefined") && !url.equals("");

        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction t = mgr.beginTransaction();
        View sep = findViewById(R.id.activity_main_separator);
        View sep2 = findViewById(R.id.activity_main_separator2);
        if (_select) {
            t.replace(R.id.activity_main_selection_frame, mFloorPlanSelectionFragment);
            sep.setVisibility(View.VISIBLE);

            if (enableTimeTable) {
                t.replace(R.id.activity_main_timetable_frame, mTimetableFragment);
                sep2.setVisibility(View.VISIBLE);
            }
        } else {
            t.remove(mFloorPlanSelectionFragment);
            t.remove(mTimetableFragment);
            sep.setVisibility(View.GONE);
            sep2.setVisibility(View.GONE);
        }
        t.commit();
    }

    private void fillRoomList() {
        mList = new LinkedList<>();
        parseXml();

        mAdapter = new RoomAdapter(this, mList);
        mAdapter.addAll(mList);
        ListView listView = findViewById(R.id.activity_main_list_view);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int _pos, long id) {
                Room room = mAdapter.getItem(_pos);
                Intent i = new Intent(MainActivity.this, LocatorActivity.class);
                i.putExtra(getString(R.string.intent_room), room);
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
                    mList.add(currentRoom);
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
        mSearchView = (SearchView) menuItemSearch.getActionView();
        mSearchView.setQueryHint(getString(R.string.room_name_or_number));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String _s) {
                mAdapter.filter(_s);
                if (_s.equals("")) enableFragments(true);
                else enableFragments(false);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem _item) {
        switch (_item.getItemId()) {
            case R.id.activity_main_menu_about:
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.activity_main_menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
        return true;
    }
}

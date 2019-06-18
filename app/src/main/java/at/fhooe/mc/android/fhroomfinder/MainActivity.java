package at.fhooe.mc.android.fhroomfinder;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        fillRoomList();
    }

    private void fillRoomList() {
        RoomAdapter adapter = new RoomAdapter(this);
        adapter.add(new Room(2, 4, 24, "Nectarinen Raum"));
        adapter.add(new Room(2, 0, 5, "Runtastic HS5"));
        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView _l, View _v, int _pos, long _id) {
        ListAdapter list = getListAdapter();

        Room room = (Room) list.getItem(_pos);
        Toast.makeText(this, "Clicked Room: " + room.getName() + " "
                + room.getNumber(), Toast.LENGTH_SHORT).show();
    }
}

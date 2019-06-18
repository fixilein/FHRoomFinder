package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoomAdapter extends ArrayAdapter<Room> {

    public RoomAdapter(Context _c) {
        super(_c, -1);
    }

    @Override
    public View getView(int _position, View _convertView, ViewGroup _parent) {
        if (_convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            _convertView = inflater.inflate(R.layout.list_room_item, null);
        }

        Room r = getItem(_position);

        TextView tv;
        tv = _convertView.findViewById(R.id.list_item_room_name);
        tv.setText(r.getName());

        tv = _convertView.findViewById(R.id.list_item_room_number);
        tv.setText(r.getFullNumber());

        return _convertView;
    }
}

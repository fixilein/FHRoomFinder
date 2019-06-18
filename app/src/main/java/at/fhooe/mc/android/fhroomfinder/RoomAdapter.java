package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class RoomAdapter extends ArrayAdapter<Room> {

    List<Room> completeList;

    public RoomAdapter(Context _c, List<Room> _list) {
        super(_c, -1);

        this.completeList = _list;
    }

    @Override
    public View getView(final int _position, View _convertView, ViewGroup _parent) {
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

    public void filter(String _charText) {
        _charText = _charText.toLowerCase(Locale.getDefault());
        clear();

        if (_charText.length() == 0) {
            addAll(completeList);
        } else {
            for (Room room : completeList) {
                String name = room.getName().toLowerCase(Locale.getDefault()).replace(".", "");
                String num = room.getFullNumber().replace(".", "");
                if (name.contains(_charText) || num.contains(_charText))
                    add(room);
            }
        }
        notifyDataSetChanged();
    }


}

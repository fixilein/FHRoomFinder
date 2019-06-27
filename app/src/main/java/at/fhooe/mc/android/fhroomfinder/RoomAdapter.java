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

    List<Room> mList;

    public RoomAdapter(Context _c, List<Room> _list) {
        super(_c, -1);
        mList = _list;
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
        tv.setMaxLines(1);

        tv = _convertView.findViewById(R.id.list_item_room_number);
        tv.setText(r.getToken());

        return _convertView;
    }

    public void filter(String _s) {
        _s = _s.trim().toLowerCase(Locale.getDefault()).replace(". ", ".");
        clear();

        if (_s.length() == 0) {
            addAll(mList);
        } else {
            for (Room room : mList) {
                String name = room.getName().toLowerCase(Locale.getDefault()).replace(".", "");
                String num = room.getToken().toLowerCase(Locale.getDefault());
                String num2 = num.replace(".", "");
                if (name.contains(_s) || num.contains(_s) || num2.contains(_s))
                    add(room);
            }
        }
        notifyDataSetChanged();
    }


}

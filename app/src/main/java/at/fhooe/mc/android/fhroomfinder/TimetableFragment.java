package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;

public class TimetableFragment extends Fragment implements View.OnClickListener {

    View mView;
    String mURL = "http://stundenplan.fh-ooe.at/ics/80f59376211744c879.ics";

    VEvent mEvent;
    boolean mOnGoing;
    Room mRoom;
    List<Room> mList;

    public TimetableFragment() {
    }

    public void setList(List<Room> _l) {
        mList = _l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);
        mView = _view;

        Button b = mView.findViewById(R.id.fragment_timetable_button_find);
        Button bRefresh = mView.findViewById(R.id.fragment_timetable_button_update);
        b.setOnClickListener(this);
        bRefresh.setOnClickListener(this);

        new ReceiveFileTask(getContext(), this).execute(mURL);

        try {
            readCal();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateUI();
    }

    void updateUI() {
        TextView tvAppointment = mView.findViewById(R.id.fragment_timetable_text_appointment);
        TextView tvRoom = mView.findViewById(R.id.fragment_timetable_text_room);
        Button bLocate = mView.findViewById(R.id.fragment_timetable_button_find);
        Button bRefresh = mView.findViewById(R.id.fragment_timetable_button_update);

        if (mEvent != null) {
            //ll.setVisibility(View.VISIBLE);
            if (mOnGoing)
                tvAppointment.setText(String.format("Now: %s", mEvent.getSummary().getValue()));
            else
                tvAppointment.setText(String.format("Next up: %s", mEvent.getSummary().getValue()));

            String tok = stringToTok(mEvent.getLocation().getValue());
            for (Room r : mList) {
                if (r.getToken().equals(tok)) {
                    mRoom = r;
                    break;
                }
            }

            tvRoom.setText(String.format("In: %s (%s)", mRoom.getName(), mRoom.getToken()));
            bLocate.setText(String.format("Find %s", mRoom.getName()));
            bLocate.setEnabled(true);
            bRefresh.setEnabled(true);


        } else {
            //ll.setVisibility(View.GONE);
            tvAppointment.setText("processing ... ");
            tvRoom.setText("");
            bLocate.setText("...");
            bLocate.setEnabled(false);
            bRefresh.setEnabled(false);
        }
    }

    private String stringToTok(String _s) {
        int startIndex = _s.indexOf("(FH");
        String substring = _s.substring(startIndex);

        String name = _s.replace(_s.substring(startIndex - 1), "");
        int building = Character.getNumericValue(substring.charAt(3));
        int floor = Character.getNumericValue(substring.charAt(5));
        int number = Character.getNumericValue(substring.charAt(6)) * 10 +
                Character.getNumericValue(substring.charAt(7));
        return new Room(building, floor, number, name, 0, 0).getToken();
    }

    void readCal() throws IOException {
        File file = new File(getContext().getFilesDir(), "ical.ics");

        ICalReader reader = new ICalReader(file);
        ICalendar ical;
        VEvent closestEvent = null;
        VEvent currentEvent = null;
        Date today = new Date();

        try {
            while ((ical = reader.readNext()) != null) {
                for (VEvent event : ical.getEvents()) {
                    // Log.i("FHRoomFinder", event.getDateStart().getValue() + "   " + event.getSummary().getValue() + "   " + event.getLocation().getValue());

                    // if event started before end ends after == currently running
                    if (event.getDateStart().getValue().before(today) && event.getDateEnd().getValue().after(today)) {
                        currentEvent = event;
                        break;
                    }
                    if (event.getDateStart().getValue().after(today)) {
                        closestEvent = event;
                        break;
                    }
                }
            }
        } finally {
            reader.close();
        }
        if (closestEvent != null) {
            mEvent = closestEvent;
            mOnGoing = false;
            Log.i("FHRoomFinder", "\n\n\nNext Event:  " + closestEvent.getDateStart().getValue() + "   " + closestEvent.getSummary().getValue() + "   " + closestEvent.getLocation().getValue());
        }
        if (currentEvent != null) {
            mEvent = currentEvent;
            mOnGoing = true;
            Log.i("FHRoomFinder", "\n\n\nCurrent Event:  " + currentEvent.getDateStart().getValue() + "   " + currentEvent.getSummary().getValue() + "   " + currentEvent.getLocation().getValue());
        }
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.fragment_timetable_button_find:
                if (mRoom != null) {
                    Intent i = new Intent(getContext(), LocatorActivity.class);
                    i.putExtra(getString(R.string.intent_room), mRoom);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "Sorry, cannot find this room. :(", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.fragment_timetable_button_update:
                mEvent = null;
                updateUI();
                new ReceiveFileTask(getContext(), this).execute(mURL);
                break;
        }
    }
}

package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Timetable integration.
 */
public class TimetableFragment extends Fragment implements View.OnClickListener {

    View mView;
    String mURL;

    boolean mInvalidURL;

    VEvent mEvent;
    boolean mOnGoing;
    Room mRoom;
    List<Room> mList;

    public TimetableFragment() {
    }

    public void setList(List<Room> _l) {
        mList = _l;
    }

    public void setInvalidURL(boolean _b) {
        mInvalidURL = _b;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);
        mView = _view;

        SharedPreferences sp = getContext().getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        mURL = sp.getString(getString(R.string.shared_prefs_ical_link), "");

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

    /**
     * update ui, if fragment is visible in activity. needs to be called on main/ui thread.
     */
    void updateUI() {
        if (getActivity() == null)
            return;

        TextView tvAppointment = mView.findViewById(R.id.fragment_timetable_text_appointment);
        TextView tvRoom = mView.findViewById(R.id.fragment_timetable_text_room);
        Button bLocate = mView.findViewById(R.id.fragment_timetable_button_find);
        Button bRefresh = mView.findViewById(R.id.fragment_timetable_button_update);

        if (mEvent != null) {
            if (mOnGoing)
                tvAppointment.setText(String.format(getString(R.string.appointment_now), mEvent.getSummary().getValue()));
            else
                tvAppointment.setText(String.format(getString(R.string.appointment_next), mEvent.getSummary().getValue()));

            String tok = stringToTok(mEvent.getLocation().getValue());
            for (Room r : mList) {
                if (r.getToken().equals(tok)) {
                    mRoom = r;
                    break;
                }
            }
            if (!tok.equals("undefined")) {
                tvRoom.setText(String.format(getString(R.string.appointment_location), mRoom.getName(), mRoom.getToken()));
                bLocate.setText(String.format(getString(R.string.appointment_find_button), mRoom.getName()));
                bLocate.setEnabled(true);
                bRefresh.setEnabled(true);
            } else {
                tvRoom.setText("");
                bLocate.setVisibility(View.GONE);
                bRefresh.setEnabled(true);
            }


        } else {
            tvAppointment.setText(getResources().getString(R.string.processing));
            tvRoom.setText("");
            bLocate.setText("...");
            bLocate.setEnabled(false);
            bRefresh.setEnabled(false);
        }
        if (mInvalidURL) {
            tvAppointment.setText(getResources().getString(R.string.invalid_url_error));
            tvRoom.setText("");
            bLocate.setVisibility(View.GONE);
        }

        if (mURL.equals("")) {
            tvAppointment.setText(getString(R.string.timetable_reminder));
            tvRoom.setText("");
            bLocate.setVisibility(View.GONE);
        }


    }

    /**
     * convert a string like "Hagenberg bet at home hs3 (FHX.XXX)" to "FHX.XXX"
     *
     * @param _s in
     * @return token
     */
    private String stringToTok(String _s) {
        int startIndex = _s.indexOf("(FH");
        if (startIndex == -1)
            return "undefined";
        String substring = _s.substring(startIndex);

        String name = _s.replace(_s.substring(startIndex - 1), "");
        int building = Character.getNumericValue(substring.charAt(3));
        int floor = Character.getNumericValue(substring.charAt(5));
        int number = Character.getNumericValue(substring.charAt(6)) * 10 +
                Character.getNumericValue(substring.charAt(7));
        return new Room(building, floor, number, name, 0, 0).getToken();
    }

    /**
     * read local is cs file and get next event.
     * @throws IOException
     */
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
        }
        if (currentEvent != null) {
            mEvent = currentEvent;
            mOnGoing = true;
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
                    Toast.makeText(getContext(), getString(R.string.error_cannot_find_room), Toast.LENGTH_SHORT).show();
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

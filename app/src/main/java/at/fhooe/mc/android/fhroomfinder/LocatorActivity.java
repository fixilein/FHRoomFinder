package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class LocatorActivity extends AppCompatActivity {

    static final String ROOM_FULLSCREEN = "FullscreenRoomToShow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        final Room r = getIntent().getParcelableExtra(MainActivity.ROOM_INTENT);

        Toolbar tb = findViewById(R.id.locator_toolbar);
        tb.setTitle(r.getName());
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fillTextViews(r);

        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction t = mgr.beginTransaction();
        t.replace(R.id.activity_locator_fragment_frame, FloorPlanFragment.newInstance(r));
        t.commit();

        View frame = findViewById(R.id.activity_locator_fragment_frame);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocatorActivity.this, FullscreenFloorPlanActivity.class);
                i.putExtra(ROOM_FULLSCREEN, r);
                startActivity(i);
            }
        });
    }

    private void fillTextViews(Room r) {
        TextView tv;
        tv = findViewById(R.id.activity_locator_room_number);
        tv.setText(r.getToken());

        tv = findViewById(R.id.activity_locator_room_building);
        String rBuilding = getString(R.string.building) + " " + r.getBuilding();
        tv.setText(rBuilding);

        tv = findViewById(R.id.activity_locator_room_floor);
        String rFloor = getString(R.string.floor) + " " + r.getFloor();
        tv.setText(rFloor);

        tv = findViewById(R.id.activity_locator_room_name);
        tv.setText(r.getName());
    }

}

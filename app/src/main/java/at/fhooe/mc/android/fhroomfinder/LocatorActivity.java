package at.fhooe.mc.android.fhroomfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class LocatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        Toolbar tb = findViewById(R.id.locator_toolbar);
        tb.setTitle(R.string.locator);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // TODO make the damn button work

        Room r = getIntent().getParcelableExtra(MainActivity.ROOM_INTENT);

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

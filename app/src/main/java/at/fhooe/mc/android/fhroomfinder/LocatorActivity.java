package at.fhooe.mc.android.fhroomfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class LocatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        Toolbar tb = findViewById(R.id.locator_toolbar);
        tb.setTitle(R.string.locator);
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        ImageView iv = findViewById(R.id.activity_locator_floor_plan_image_view);
        switch (r.getFloor()) {
            case 0:
                iv.setImageResource(R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_0);
                break;
            case 1:
                iv.setImageResource(R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_1);
                break;
            case 2:
                iv.setImageResource(R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_2);
                break;
            case 3:
                iv.setImageResource(R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_3);
                break;
            case 4:
                iv.setImageResource(R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_4);
                break;
        }

        ImageView circleView = findViewById(R.id.activity_locator_circle_image_view);
        circleView.setImageResource(R.drawable.ic_square);
        circleView.setScaleType(ImageView.ScaleType.FIT_START);
        circleView.setScaleX(0.2f);
        circleView.setScaleY(0.2f);
        //circleView.setX(300);

    }
}

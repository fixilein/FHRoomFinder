package at.fhooe.mc.android.fhroomfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        fillTextViews(r);

        ImageView iv = findViewById(R.id.activity_locator_floor_plan_image_view);

        Bitmap floorPlan = getFloorPlanBitmap(r.getFloor());

        Canvas canvas = new Canvas(floorPlan);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        int x = 1000;
        int y = 1000;
        canvas.drawCircle(x, y, 150, paint);

        iv.setImageBitmap(floorPlan);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LocatorActivity.this, "IV clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap getFloorPlanBitmap(int _f) {
        Bitmap b;
        switch (_f) {
            case 0:
                b = BitmapFactory.decodeResource(getResources(), R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_0).copy(Bitmap.Config.ARGB_8888, true);

                break;
            case 1:
                b = BitmapFactory.decodeResource(getResources(), R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_1).copy(Bitmap.Config.ARGB_8888, true);

                break;
            case 2:
                b = BitmapFactory.decodeResource(getResources(), R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_2).copy(Bitmap.Config.ARGB_8888, true);

                break;
            case 3:
                b = BitmapFactory.decodeResource(getResources(), R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_3).copy(Bitmap.Config.ARGB_8888, true);
                break;
            case 4:
                b = BitmapFactory.decodeResource(getResources(), R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_4).copy(Bitmap.Config.ARGB_8888, true);
                break;
            default:
                Log.e(MainActivity.TAG, "Unrecognized Floor");
                b = null;
                break;
        }
        return b;
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

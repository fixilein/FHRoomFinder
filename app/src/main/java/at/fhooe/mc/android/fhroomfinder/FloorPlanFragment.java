package at.fhooe.mc.android.fhroomfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FloorPlanFragment extends Fragment {

    private static final String FLOOR_FRAGMENT = "FloorFragmentParcelable";
    private Bitmap image;
    private Room room;

    public FloorPlanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return _inflater.inflate(R.layout.fragment_floor_plan, _container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);

        room = getArguments().getParcelable(FLOOR_FRAGMENT);
        int building = getArguments().getInt(MainActivity.BUILDING_INTENT);
        int floor = getArguments().getInt(MainActivity.FLOOR_INTENT);

        ImageView iv = _view.findViewById(R.id.fragment_floor_plan_image_view);
        if (room != null) {
            Bitmap floorPlan = getFloorPlanBitmap(room);
            float scale = getResources().getDisplayMetrics().density / 2.75f;
            float x = room.getX() * scale;
            float y = room.getY() * scale;
            drawCircle(floorPlan, x, y);
            iv.setImageBitmap(floorPlan);
            image = floorPlan;
        } else {
            Bitmap floorPlan = getFloorPlanBitmap(building, floor);
            iv.setImageBitmap(floorPlan);
            image = floorPlan;
        }
    }

    private void drawCircle(Bitmap _floorPlan, float _x, float _y) {
        Canvas canvas = new Canvas(_floorPlan);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(_x, _y, 150, paint);
    }

    private Bitmap getFloorPlanBitmap(Room _r) {
        int id = getContext().getResources().getIdentifier(
                "fhooe_hagenberg_campus_raumplan_fh" + _r.getBuilding() + "_ebene_" + _r.getFloor(),
                "mipmap", getContext().getPackageName());
        return BitmapFactory.decodeResource(getResources(), id).copy(Bitmap.Config.ARGB_8888, true);
    }

    private Bitmap getFloorPlanBitmap(int _building, int _floor) {
        int id = getContext().getResources().getIdentifier(
                "fhooe_hagenberg_campus_raumplan_fh" + _building + "_ebene_" + _floor,
                "mipmap", getContext().getPackageName());
        return BitmapFactory.decodeResource(getResources(), id)
                .copy(Bitmap.Config.ARGB_8888, true);
    }

    public static FloorPlanFragment newInstance(Parcelable _p) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FLOOR_FRAGMENT, _p);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FloorPlanFragment newInstance(int _building, int _floor) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.BUILDING_INTENT, _building);
        bundle.putInt(MainActivity.FLOOR_INTENT, _floor);

        fragment.setArguments(bundle);
        return fragment;
    }

    public Bitmap getImage() {
        return image;
    }

    public Room getRoom() {
        return room;
    }

}

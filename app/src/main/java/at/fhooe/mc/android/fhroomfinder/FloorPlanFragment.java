package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
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

/**
 * Display floor plan or floor plan with marked floor.
 */
public class FloorPlanFragment extends Fragment {
    private Bitmap mImage;
    private Room mRoom;

    public FloorPlanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return _inflater.inflate(R.layout.fragment_floor_plan, _container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);

        mRoom = getArguments().getParcelable(getString(R.string.floor_fragment_parcelable));
        int building = getArguments().getInt(getString(R.string.intent_building));
        int floor = getArguments().getInt(getString(R.string.intent_floor));

        ImageView iv = _view.findViewById(R.id.fragment_floor_plan_image_view);
        if (mRoom != null) {
            Bitmap floorPlan = getFloorPlanBitmap(mRoom);
            float scale = getResources().getDisplayMetrics().density / 2.75f;
            float x = mRoom.getX() * scale;
            float y = mRoom.getY() * scale;
            drawCircle(floorPlan, x, y);
            iv.setImageBitmap(floorPlan);
            mImage = floorPlan;
        } else {
            Bitmap floorPlan = getFloorPlanBitmap(building, floor);
            iv.setImageBitmap(floorPlan);
            mImage = floorPlan;
        }
    }

    /**
     * Draws circle on respective location on floor plan usin Canvas.
     *
     * @param _floorPlan Bitmap of floor plan.
     * @param _x         x coord
     * @param _y         y coord
     */
    private void drawCircle(Bitmap _floorPlan, float _x, float _y) {
        Canvas canvas = new Canvas(_floorPlan);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(_x, _y, 150, paint);
    }

    /**
     * Get floor plan according to building and floor.
     * @param _r Room
     * @return Bitmap of floor plan
     */
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

    public static FloorPlanFragment newInstance(Context _c, Parcelable _p) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(_c.getString(R.string.floor_fragment_parcelable), _p);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FloorPlanFragment newInstance(Context _c, int _building, int _floor) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(_c.getString(R.string.intent_building), _building);
        bundle.putInt(_c.getString(R.string.intent_floor), _floor);

        fragment.setArguments(bundle);
        return fragment;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public Room getRoom() {
        return mRoom;
    }

}

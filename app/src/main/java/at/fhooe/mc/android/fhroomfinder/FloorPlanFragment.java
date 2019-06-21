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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FloorPlanFragment extends Fragment {

    private static final String FLOOR_FRAGMENT = "FloorFragmentParcelable";

    public FloorPlanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState) {
        return _inflater.inflate(R.layout.fragment_floor_plan, _container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        super.onViewCreated(_view, _savedInstanceState);

        Room room = getArguments().getParcelable(FLOOR_FRAGMENT);

        ImageView iv = _view.findViewById(R.id.fragment_floor_plan_image_view);
        Bitmap floorPlan = getFloorPlanBitmap(room);
        drawCircle(floorPlan, room.getX(), room.getY());
        iv.setImageBitmap(floorPlan);
    }

    private void drawCircle(Bitmap _floorPlan, int _x, int _y) {
        Canvas canvas = new Canvas(_floorPlan);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(_x, _y, 150, paint);
    }

    private Bitmap getFloorPlanBitmap(Room _r) {
        Bitmap b = null;
        if (_r.getBuilding() == 2) {
            switch (_r.getFloor()) {
                case 0:
                    b = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_0)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 1:
                    b = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_1)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 2:
                    b = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_2)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 3:
                    b = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_3)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case 4:
                    b = BitmapFactory.decodeResource(getResources(),
                            R.mipmap.fhooe_hagenberg_campus_raumplan_fh2_ebene_4)
                            .copy(Bitmap.Config.ARGB_8888, true);
                    break;
                default:
                    Log.e(MainActivity.TAG, "Unrecognized Floor");
                    break;
            }
        }
        return b;
    }

    public static FloorPlanFragment newInstance(Parcelable _p) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FLOOR_FRAGMENT, _p);
        fragment.setArguments(bundle);
        return fragment;
    }

}

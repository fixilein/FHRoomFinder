package at.fhooe.mc.android.fhroomfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Select a floor plan to view.
 */
public class FloorPlanSelectionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner mFloorSpinner;

    public FloorPlanSelectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_floor_plan_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(_view, savedInstanceState);

        final Spinner buildingSpinner = _view.findViewById(R.id.fragment_floor_plan_selection_spinner_building);
        mFloorSpinner = _view.findViewById(R.id.fragment_floor_plan_selection_spinner_floor);

        ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter.createFromResource(getContext(), R.array.buildings, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.floors_fh1, android.R.layout.simple_spinner_item);

        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFloorSpinner.setAdapter(floorAdapter);

        buildingSpinner.setOnItemSelectedListener(this);
        mFloorSpinner.setOnItemSelectedListener(this);

        Button goBtn = _view.findViewById(R.id.fragment_floor_plan_selection_button_submit);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b = buildingSpinner.getSelectedItem().toString();
                int building = Character.getNumericValue(b.charAt(b.length() - 1));
                if (b.charAt(0) == 'S') // SH3 == FH 4
                    building = 4;
                String f = mFloorSpinner.getSelectedItem().toString();
                int floor = Character.getNumericValue(f.charAt(f.length() - 1));

                Intent i = new Intent(getContext(), FullscreenFloorPlanActivity.class);
                i.putExtra(getString(R.string.intent_building), building);
                i.putExtra(getString(R.string.intent_floor), floor);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> _parent, View _view, int _position, long _id) {
        ArrayAdapter<CharSequence> floorAdapter = null;
        switch (_parent.getItemAtPosition(_position).toString()) {
            case "FH1": {
                floorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.floors_fh1, android.R.layout.simple_spinner_item);
                break;
            }
            case "FH2": {
                floorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.floors_fh2, android.R.layout.simple_spinner_item);
                break;
            }
            case "FH3": {
                floorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.floors_fh3, android.R.layout.simple_spinner_item);
                break;
            }
            case "SH3": {
                floorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.floors_sh3, android.R.layout.simple_spinner_item);
                break;
            }
        }
        if (floorAdapter != null) {
            mFloorSpinner.setAdapter(floorAdapter);
            floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

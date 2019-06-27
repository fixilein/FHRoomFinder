package at.fhooe.mc.android.fhroomfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocatorActivity extends AppCompatActivity {
    FloorPlanFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        final Room r = getIntent().getParcelableExtra(getString(R.string.intent_room));

        Toolbar tb = findViewById(R.id.locator_toolbar);
        tb.setTitle(r.getName());
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillTextViews(r);

        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction t = mgr.beginTransaction();
        mFragment = FloorPlanFragment.newInstance(getApplicationContext(), r);
        t.replace(R.id.activity_locator_fragment_frame, mFragment);
        t.commit();

        View frame = findViewById(R.id.activity_locator_fragment_frame);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LocatorActivity.this, FullscreenFloorPlanActivity.class);
                i.putExtra(getString(R.string.room_fullscreen), r);
                startActivity(i);
            }
        });

        Button b = findViewById(R.id.activity_locator_button_maps);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = null;
                switch (r.getBuilding()) {
                    case 1:
                        gmmIntentUri = Uri.parse("geo:0,0?q=48.368391,14.514426(FH1)");
                        break;
                    case 2:
                        gmmIntentUri = Uri.parse("geo:0,0?q=48.368331,14.513175(FH2)");
                        break;
                    case 3:
                        gmmIntentUri = Uri.parse("geo:0,0?q=48.368248,14.512413(FH3)");
                        break;
                }
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

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

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.activity_locator_menu, _menu);

        MenuItem menuItemShare = _menu.findItem(R.id.activity_locator_menu_share);
        menuItemShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareImage();
                return true;
            }
        });
        return true;
    }

    private void shareImage() {
        Context context = getApplicationContext();
        // save bitmap to cache directory
        try {
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            mFragment.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "at.fhooe.mc.android.fhroomfinder.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.share_text),
                    mFragment.getRoom().getName(), mFragment.getRoom().getToken()));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_app)));
        }
    }

}

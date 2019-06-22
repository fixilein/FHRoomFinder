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
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class LocatorActivity extends AppCompatActivity {

    static final String ROOM_FULLSCREEN = "FullscreenRoomToShow";
    FloorPlanFragment fragment;

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
        fragment = FloorPlanFragment.newInstance(r);
        t.replace(R.id.activity_locator_fragment_frame, fragment);
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

    private ShareActionProvider shareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu _menu) {
        getMenuInflater().inflate(R.menu.activity_locator_menu, _menu);

        MenuItem menuItemShare = _menu.findItem(R.id.activity_locator_menu_share);
        // Fetch and store ShareActionProvider (FROM ANDROID DEV)
//        shareActionProvider = (ShareActionProvider) menuItemShare.getActionProvider();

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
            fragment.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, String.format(getResources().getString(R.string.share_text), fragment.getRoom().getName()));
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_app)));
        }
    }


}

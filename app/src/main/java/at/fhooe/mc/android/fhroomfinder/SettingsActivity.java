package at.fhooe.mc.android.fhroomfinder;

import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Settings
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar tb = findViewById(R.id.settings_toolbar);
        tb.setTitle(R.string.settings);
        tb.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        String url = sp.getString(getString(R.string.shared_prefs_ical_link), "undefined");

        EditText et = findViewById(R.id.activity_settings_edit_link);
        if (url != null && !url.equals("undefined")) et.setText(url);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences sp = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(getString(R.string.shared_prefs_ical_link), s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.activity_settings_button_paste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipboard.getPrimaryClip().getItemCount() > 0) {
                    TextView tv = findViewById(R.id.activity_settings_edit_link);
                    tv.setText(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
                }
            }
        });

        findViewById(R.id.activity_settings_button_levis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://levis.fh-ooe.at/schedule"));
                startActivity(i);
            }
        });
    }


}

package kz.sirius.myapplication.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.entity.UserParcel;
import kz.sirius.myapplication.entity.UserSerializable;

public class ShowDescriptionActivity extends AppCompatActivity {
    private int numberFromReg;
    private UserParcel userFromRegParc;
    private UserSerializable userFromRegSer;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_description);

        if (getIntent().getExtras() != null) {

            numberFromReg = getIntent().getExtras().getInt("DATA");
            userFromRegSer = (UserSerializable) getIntent().getExtras().getSerializable("DATA_SERIALIZE");
            userFromRegParc = (UserParcel) getIntent().getExtras().getParcelable("DATA_PARCEL");
        }

        String json = getIntent().getStringExtra("item");
        Gson gson = new Gson();
        myApp = gson.fromJson(json,MyApp.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Сведения");


        TextView description = findViewById(R.id.description);
        description.setText(myApp.getText());

        TextView downloads = findViewById(R.id.downloads);
        downloads.setText(String.valueOf(myApp.getDownloads()));

        TextView creator = findViewById(R.id.creator);
        creator.setText(myApp.getCreator());

        TextView version = findViewById(R.id.version);
        version.setText(myApp.getVersion());

        TextView lastUpdate = findViewById(R.id.lastUpdate);
        lastUpdate.setText(myApp.getLastUpdate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);



        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

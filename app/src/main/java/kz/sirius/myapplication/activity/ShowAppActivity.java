package kz.sirius.myapplication.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.adapter.MyPreviewsAdapter;
import kz.sirius.myapplication.adapter.MyRecyclerAdapter;
import kz.sirius.myapplication.adapter.OnCollaborationClickListener;
import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.entity.UserParcel;
import kz.sirius.myapplication.entity.UserSerializable;
import kz.sirius.myapplication.service.DownloadImageTask;

public class ShowAppActivity extends AppCompatActivity {
    private int numberFromReg;
    private UserParcel userFromRegParc;
    private UserSerializable userFromRegSer;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    private LinearLayoutManager layoutManager3;
    private MyApp myApp;
    private Button downloadButton;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ArrayList<MyApp> list = new ArrayList<MyApp>();
    ArrayList<MyApp> popularApps = new ArrayList<MyApp>();
    ArrayList previewsList = new ArrayList<String>();
    MyRecyclerAdapter listAdapter = new MyRecyclerAdapter();
    MyRecyclerAdapter listAdapter2 = new MyRecyclerAdapter();
    MyPreviewsAdapter listAdapter3 = new MyPreviewsAdapter();

    private ScrollView scrollView;
    private ContentLoadingProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_app);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressBar = findViewById(R.id.loader);
        scrollView = findViewById(R.id.mainScroll);

        RecyclerView uiList = findViewById(R.id.uiList);
        RecyclerView uiList2 = findViewById(R.id.uiList2);
        RecyclerView previews = findViewById(R.id.previewsList);



        int app_id = getIntent().getIntExtra("itemId", 0);
        Gson gson = new Gson();
        DatabaseReference app = FirebaseDatabase.getInstance().getReference().child("apps").child(String.valueOf(app_id));
        app.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Gson gson = new Gson();
                String Json = gson.toJson(snapshot.getValue());
                myApp = gson.fromJson(Json,MyApp.class);
                setAllInformation();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //String Json = gson.toJson(app.get);
        //list.add(gson.fromJson(Json,MyApp.class));
        //myApp = gson.fromJson(json,MyApp.class);



        ArrayAdapter adapter = new ArrayAdapter<MyApp>(this, R.layout.show_app, list);
        ArrayAdapter adapter2 = new ArrayAdapter<MyApp>(this, R.layout.show_app, popularApps);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("apps");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gson gson = new Gson();
                    String Json = gson.toJson(snapshot.getValue());
                    MyApp app = gson.fromJson(Json,MyApp.class);
                    if(app.getId() != myApp.getId()){
                        list.add(app);
                        popularApps.add(app);
                    }
                }

                Collections.sort(list, new Comparator<MyApp>(){

                    public int compare(MyApp o1, MyApp o2)
                    {
                        Date date1 = new Date();
                        Date date2 = new Date();
                        try{
                            date1 = new SimpleDateFormat("dd.MM.yyyy").parse(o1.getLastUpdate());
                            date2 = new SimpleDateFormat("dd.MM.yyyy").parse(o2.getLastUpdate());
                        } catch (ParseException e){
                            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        return date2.compareTo(date1);
                    }
                });

                listAdapter.setContent(list);

                Collections.sort(popularApps, new Comparator<MyApp>(){

                    public int compare(MyApp o1, MyApp o2)
                    {
                        if(o1.getDownloads() < o2.getDownloads())return -1;
                        else if(o1.getDownloads() > o2.getDownloads())return 1;
                        else return 0;
                    }
                });

                listAdapter2.setContent(popularApps);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();

                progressBar.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listAdapter.setContent(list);
        listAdapter2.setContent(popularApps);
        listAdapter3.setContent(previewsList, this);

        listAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = list.get(position);
                onAppClick(myApp);
            }
        });

        listAdapter2.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = popularApps.get(position);
                onAppClick(myApp);
            }
        });

        uiList.setAdapter(listAdapter);
        uiList2.setAdapter(listAdapter2);
        previews.setAdapter(listAdapter3);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        uiList.setLayoutManager(layoutManager);
        uiList2.setLayoutManager(layoutManager2);
        previews.setLayoutManager(layoutManager3);


    }

    public void setAllInformation(){
        TextView app_size = findViewById(R.id.app_size);
        app_size.setText(myApp.getSize() + " МБ");

        TextView short_description = findViewById(R.id.short_description);
        short_description.setText(myApp.getShortDescription());

        TextView app_developer = findViewById(R.id.app_developer);
        app_developer.setText(myApp.getCreator());

        TextView app_name = findViewById(R.id.app_name);
        app_name.setText(myApp.getTitle());

        TextView app_downloads = findViewById(R.id.app_downloads);
        app_downloads.setText("Более " + String.valueOf(myApp.getDownloads()));

        ImageView avatar = findViewById(R.id.imageView3);
        new DownloadImageTask(avatar)
                .execute(myApp.getImg_url());

        downloadButton = findViewById(R.id.downloadButton);
        if(myApp.isDownloaded() == true){
            downloadButton.setText("Delete");
            downloadButton.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            downloadButton.setText("Download");
            downloadButton.setBackgroundColor(getResources().getColor(R.color.green));
        }
        for( int i = 0 ; i < myApp.getPreviews().size(); i++){
            if(myApp.getPreviews().get(i) != null){
                previewsList.add(myApp.getPreviews().get(i));
            }
        }
        listAdapter3.setContent(previewsList, this);
    }

    public void downloadOrDelete(View view){
        DatabaseReference app = FirebaseDatabase.getInstance().getReference().child("apps").child(String.valueOf(myApp.getId()));

        if(myApp.isDownloaded() == false){
            downloadButton.setText("Delete");
            downloadButton.setBackgroundColor(getResources().getColor(R.color.red));
            app.child("isDownloaded").setValue(true);
        }else{
            downloadButton.setText("Download");
            downloadButton.setBackgroundColor(getResources().getColor(R.color.green));
            app.child("isDownloaded").setValue(false);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if(menuItem.getItemId() == R.id.action_search){
            goToSearchPage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void goToSearchPage() {
        Intent myIntent = new Intent(ShowAppActivity.this, AllAppsWithSearchActivity.class);
        ShowAppActivity.this.startActivity(myIntent);
    }

    public void onAppClick(MyApp myApp){
        Intent myIntent = new Intent(ShowAppActivity.this, ShowAppActivity.class);
        myIntent.putExtra("itemId", myApp.getId());
        ShowAppActivity.this.startActivity(myIntent);
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


    public void onDescriptionClick(View view){
        Intent myIntent = new Intent(ShowAppActivity.this, ShowDescriptionActivity.class);
        Gson gson = new Gson();
        String Json = gson.toJson(myApp);
        myIntent.putExtra("item", Json);
        ShowAppActivity.this.startActivity(myIntent);
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

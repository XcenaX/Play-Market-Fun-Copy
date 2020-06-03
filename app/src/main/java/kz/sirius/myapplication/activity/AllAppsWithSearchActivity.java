package kz.sirius.myapplication.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
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
import java.util.HashSet;
import java.util.Set;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.adapter.MyAppAdapter;
import kz.sirius.myapplication.adapter.OnCollaborationClickListener;
import kz.sirius.myapplication.entity.Category;
import kz.sirius.myapplication.entity.MyApp;

public class AllAppsWithSearchActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;;
    private MyAppAdapter appAdapter = new MyAppAdapter();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_apps);

        Toolbar toolbar = findViewById(R.id.allAppsToolbar);
        toolbar.setTitle("Поиск");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseApp.initializeApp(this);

        RecyclerView appsList = findViewById(R.id.appsList);

        ArrayList<MyApp> apps = new ArrayList<MyApp>();
        ArrayList<Category> categories = new ArrayList<Category>();


        appAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = apps.get(position);
                onAppClick(myApp);
            }
        });




        DatabaseReference appsReference = FirebaseDatabase.getInstance().getReference().child("apps");
        appsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(apps.size() != 0){
                    return;
                }
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gson gson = new Gson();
                    String Json = gson.toJson(snapshot.getValue());
                    MyApp app = gson.fromJson(Json,MyApp.class);
                    apps.add(app);
                }
                appAdapter.setContent(apps, null);
                appAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appsList.setAdapter(appAdapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        appsList.setLayoutManager(layoutManager);


    }


    public void onAppClick(MyApp myApp){
        Intent myIntent = new Intent(AllAppsWithSearchActivity.this, ShowAppActivity.class);
        myIntent.putExtra("itemId", myApp.getId());
        AllAppsWithSearchActivity.this.startActivity(myIntent);
    }

    public Category getCategory(int categoryId, ArrayList<Category> allCategory){
        for(int i = 0; i < allCategory.size(); i++){
            if(allCategory.get(i).getCategoryId() == categoryId)return allCategory.get(i);
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                appAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

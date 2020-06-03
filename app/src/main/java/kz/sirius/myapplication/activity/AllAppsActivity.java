package kz.sirius.myapplication.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class AllAppsActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private Category category;
    private String sortBy = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_apps);

        int category_id = getIntent().getIntExtra("itemId", 0);
        String categoryName = getIntent().getStringExtra("categoryName");
        sortBy = getIntent().getStringExtra("sortBy");
        String title = getIntent().getStringExtra("title");

        Toolbar toolbar = findViewById(R.id.allAppsToolbar);
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FirebaseApp.initializeApp(this);

        RecyclerView appsList = findViewById(R.id.appsList);

        ArrayList<MyApp> apps = new ArrayList<MyApp>();
        ArrayList<Category> categories = new ArrayList<Category>();

        MyAppAdapter appAdapter = new MyAppAdapter();
        appAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = apps.get(position);
                onAppClick(myApp);
            }
        });

        DatabaseReference categoriesReference = FirebaseDatabase.getInstance().getReference().child("categories");
        categoriesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gson gson = new Gson();
                    String Json = gson.toJson(snapshot.getValue());
                    categories.add(gson.fromJson(Json, Category.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if(category_id != 0){
            DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(String.valueOf(category_id));
            categoryReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Gson gson = new Gson();
                    String Json = gson.toJson(dataSnapshot.getValue());
                    category = gson.fromJson(Json, Category.class);
                    getSupportActionBar().setTitle(category.getName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



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

                    Set<MyApp> set = new HashSet<>(apps);
                    apps.clear();
                    apps.addAll(set);
                }
                if(category != null){
                    for(int i = 0; i < apps.size(); i++){
                        Category currentCategory = getCategory(apps.get(i).getCategoryId(), categories);
                        if(!currentCategory.getName().equals(category.getName())){
                            apps.remove(i);
                            i--;
                        }
                    }
                    appAdapter.setContent(apps, category.getName());
                }else{
                    sortApps(apps);
                    appAdapter.setContent(apps, title);
                }

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

    private void sortApps(ArrayList<MyApp> apps) {
        Collections.sort(apps, new Comparator<MyApp>(){
            public int compare(MyApp o1, MyApp o2)
            {
                if(sortBy.equals("downloads")){
                    if(o1.getDownloads() < o2.getDownloads())return -1;
                    else if(o1.getDownloads() > o2.getDownloads())return 1;
                    else return 0;
                }else if(sortBy.equals("lastUpdate")){
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
                return 0;

            }
        });
    }

    public void onAppClick(MyApp myApp){
        Intent myIntent = new Intent(AllAppsActivity.this, ShowAppActivity.class);
        myIntent.putExtra("itemId", myApp.getId());
        AllAppsActivity.this.startActivity(myIntent);
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

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

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

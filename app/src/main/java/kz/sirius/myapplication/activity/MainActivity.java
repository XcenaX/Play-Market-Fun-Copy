package kz.sirius.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kz.sirius.myapplication.adapter.MyCategoriesAdapter;
import kz.sirius.myapplication.adapter.OnCollaborationClickListener;
import kz.sirius.myapplication.entity.Category;
import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.R;
import kz.sirius.myapplication.adapter.MyRecyclerAdapter;
import kz.sirius.myapplication.entity.UserParcel;
import kz.sirius.myapplication.entity.UserSerializable;
import kz.sirius.myapplication.utils.DividerItemDecoration;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private int numberFromReg;
    private UserParcel userFromRegParc;
    private UserSerializable userFromRegSer;
    private static final String TAG = "MainActivity";
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager2;
    private LinearLayoutManager layoutManager3;
    private LinearLayoutManager layoutManager4;
    private LinearLayoutManager layoutManager5;

    private ScrollView scrollView;
    private ContentLoadingProgressBar progressBar;

    private ArrayList<Category> allCategories = new ArrayList<Category>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeGreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleIntent(getIntent());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.loader);

        scrollView = findViewById(R.id.mainScroll);

        FirebaseApp.initializeApp(this);

        RecyclerView categoriesList = findViewById(R.id.categoriesList);
        categoriesList.addItemDecoration(
                new DividerItemDecoration(getActivity(), R.drawable.divider));



        MyRecyclerAdapter listAdapter = new MyRecyclerAdapter();
        MyRecyclerAdapter listAdapter2 = new MyRecyclerAdapter();
        MyRecyclerAdapter arcadesListAdapter = new MyRecyclerAdapter();
        MyRecyclerAdapter strategyListAdapter = new MyRecyclerAdapter();
        MyCategoriesAdapter categoriesListAdapter = new MyCategoriesAdapter();


        if (getIntent().getExtras() != null) {

            numberFromReg = getIntent().getExtras().getInt("DATA");
            userFromRegSer = (UserSerializable) getIntent().getExtras().getSerializable("DATA_SERIALIZE");
            userFromRegParc = (UserParcel) getIntent().getExtras().getParcelable("DATA_PARCEL");
        }


        ArrayList<MyApp> list = new ArrayList<MyApp>();
        ArrayList<MyApp> popularApps = new ArrayList<MyApp>();
        ArrayList<MyApp> arcades = new ArrayList<MyApp>();
        ArrayList<MyApp> strategies = new ArrayList<MyApp>();

        ArrayAdapter adapter = new ArrayAdapter<MyApp>(this, R.layout.activity_main, list);
        ArrayAdapter adapter2 = new ArrayAdapter<MyApp>(this, R.layout.activity_main, popularApps);
        ArrayAdapter adapter3 = new ArrayAdapter<MyApp>(this, R.layout.activity_main, arcades);
        ArrayAdapter adapter4 = new ArrayAdapter<Category>(this, R.layout.activity_main, allCategories);
        ArrayAdapter adapter5 = new ArrayAdapter<MyApp>(this, R.layout.activity_main, strategies);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gson gson = new Gson();
                    String Json = gson.toJson(snapshot.getValue());
                    allCategories.add(gson.fromJson(Json, Category.class));
                }
                categoriesListAdapter.setContent(allCategories);
                categoriesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("apps");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gson gson = new Gson();
                    String Json = gson.toJson(snapshot.getValue());
                    list.add(gson.fromJson(Json,MyApp.class));
                    popularApps.add(gson.fromJson(Json,MyApp.class));
                    arcades.add(gson.fromJson(Json,MyApp.class));
                    strategies.add(gson.fromJson(Json,MyApp.class));
                }

                Collections.sort(list, new Comparator<MyApp>(){

                    public int compare(MyApp o1, MyApp o2)
                    {
                        if(o1 == null || o2 == null)return 1;
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

                sortByCategory(arcades, "Аркады");
                sortByCategory(strategies, "Стратегии");

                arcadesListAdapter.setContent(arcades);
                strategyListAdapter.setContent(strategies);

                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
                adapter4.notifyDataSetChanged();
                adapter5.notifyDataSetChanged();
                arcadesListAdapter.notifyDataSetChanged();


                progressBar.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RecyclerView uiList = findViewById(R.id.uiList);
        RecyclerView uiList2 = findViewById(R.id.uiList2);
        RecyclerView arcadesList = findViewById(R.id.arcadesList);
        RecyclerView strategiesList = findViewById(R.id.strategyList);
        RecyclerView recyclerCategories = findViewById(R.id.categoriesList);



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

        arcadesListAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = arcades.get(position);
                onAppClick(myApp);
            }
        });

        strategyListAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                MyApp myApp = strategies.get(position);
                onAppClick(myApp);
            }
        });

        categoriesListAdapter.setListener(new OnCollaborationClickListener() {
            @Override
            public void onItemClicked(int position) {
                Category category = allCategories.get(position);
                onCategoryClick(category);
            }
        });

        listAdapter.setContent(list);
        listAdapter2.setContent(popularApps);
        arcadesListAdapter.setContent(arcades);
        strategyListAdapter.setContent(strategies);
        categoriesListAdapter.setContent(allCategories);

        uiList.setAdapter(listAdapter2);
        uiList2.setAdapter(listAdapter);
        arcadesList.setAdapter(arcadesListAdapter);
        strategiesList.setAdapter(strategyListAdapter);
        recyclerCategories.setAdapter(categoriesListAdapter);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager5 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        uiList.setLayoutManager(layoutManager);
        uiList2.setLayoutManager(layoutManager2);
        arcadesList.setLayoutManager(layoutManager3);
        recyclerCategories.setLayoutManager(layoutManager4);
        strategiesList.setLayoutManager(layoutManager5);

    }

    public void sortByCategory(ArrayList<MyApp> apps, String categoryName){
        for(int i = 0; i < apps.size(); i++){
            Category category = getCategory(apps.get(i).getCategoryId(), allCategories);
            if(!category.getName().equals(categoryName)){
                apps.remove(i);
                i--;
            }
        }
    }

    public Category getCategory(int categoryId, ArrayList<Category> allCategory){
        for(int i = 0; i < allCategory.size(); i++){
            if(allCategory.get(i).getCategoryId() == categoryId)return allCategory.get(i);
        }
        return null;
    }

    public Category getCategoryByName(String categoryName){
        for(int i = 0; i < this.allCategories.size(); i++){
            if(allCategories.get(i).getName().equals(categoryName))return allCategories.get(i);
        }
        return null;
    }

    private Context getActivity() {
        return this;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                goToSearchPage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToSearchPage() {
        Intent myIntent = new Intent(MainActivity.this, AllAppsWithSearchActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void doSearch(String queryStr) {
        Toast.makeText(this, queryStr, Toast.LENGTH_LONG).show();
        Log.i("Your search: ",queryStr);
    }

    public void onAppClick(MyApp myApp){
        Intent myIntent = new Intent(MainActivity.this, ShowAppActivity.class);
        myIntent.putExtra("itemId", myApp.getId());
        MainActivity.this.startActivity(myIntent);
    }

    private void onCategoryClick(Category category) {
        Intent myIntent = new Intent(MainActivity.this, AllAppsActivity.class);
        myIntent.putExtra("itemId", category.getCategoryId());
        myIntent.putExtra("categoryName", category.getName());
        MainActivity.this.startActivity(myIntent);
    }

    public void onTitleClick(View view) {
        LinearLayout linearLayout = (LinearLayout)view;
        TextView textView = (TextView)linearLayout.getChildAt(0);
        String categoryName = (String) textView.getText();

        Intent myIntent = new Intent(MainActivity.this, AllAppsActivity.class);

        if(categoryName.equals("Новинки")){
            myIntent.putExtra("sortBy", "lastUpdate");
        }else if(categoryName.equals("Популярное")){
            myIntent.putExtra("sortBy", "downloads");
        } else{
            Category category = getCategoryByName(categoryName);
            myIntent.putExtra("itemId", category.getCategoryId());
            myIntent.putExtra("categoryName", category.getName());
        }
        myIntent.putExtra("title", categoryName);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }


}

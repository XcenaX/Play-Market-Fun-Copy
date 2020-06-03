package kz.sirius.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.R;
import kz.sirius.myapplication.service.DownloadImageTask;

public class MyListAdapter extends BaseAdapter {

    private ArrayList<MyApp> myApps = new ArrayList<>();

    public void setContent(ArrayList<MyApp> myApps) {
        this.myApps = myApps;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myApps.size();
    }

    @Override
    public MyApp getItem(int position) {
        return myApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_adapter, parent, false);
        }

        MyApp myApp = myApps.get(position);


        TextView uiText = convertView.findViewById(R.id.uiText);
        TextView uiAppSize = convertView.findViewById(R.id.uiAppSize);
        ImageView uiImage = convertView.findViewById(R.id.uiImage);

        uiText.setText(myApp.getTitle());
        uiAppSize.setText(myApp.getSize());
        //Picasso.get().load(myApp.getImg_url()).into(uiImage);
        new DownloadImageTask(uiImage)
                .execute(myApp.getImg_url());

        return convertView;
    }
}

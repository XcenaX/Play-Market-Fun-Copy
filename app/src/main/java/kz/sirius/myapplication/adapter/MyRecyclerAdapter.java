package kz.sirius.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.R;
import kz.sirius.myapplication.service.DownloadImageTask;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> implements Filterable {

    private OnCollaborationClickListener listener;
    private ArrayList<MyApp> myApps = new ArrayList<>();



    public void setListener(OnCollaborationClickListener listener) {
        this.listener = listener;
    }

    public void setContent(ArrayList<MyApp> myApps) {
        this.myApps = myApps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.uiApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClicked(position);
                }
            }
        });
        String title = myApps.get(position).getTitle();
        if(title.length() >= 32){
            title = title.substring(0,32);
        }
        holder.uiText.setText(title + "...");
        holder.uiAppSize.setText(String.valueOf(myApps.get(position).getSize()) + " мб");

        new DownloadImageTask(holder.uiImage)
                .execute(myApps.get(position).getImg_url());
    }



    @Override
    public int getItemCount() {
        return myApps.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<MyApp> filteredApps = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredApps.addAll(myApps);
            } else{
                for(MyApp app : myApps){
                    if(app.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredApps.add(app);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredApps;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myApps.clear();
            myApps.addAll((Collection<? extends MyApp>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView uiText;
        TextView uiTextDetails;
        TextView uiAppSize;
        ImageView uiImage;
        LinearLayout uiApp;
        public MyViewHolder(View v) {
            super(v);
            uiText = v.findViewById(R.id.uiText);
            uiAppSize = v.findViewById(R.id.uiAppSize);
            uiApp = v.findViewById(R.id.uiApp);
            uiImage = v.findViewById(R.id.uiImage);

            //uiTextDetails = v.findViewById(R.id.uiTextDetails);
        }
    }
}


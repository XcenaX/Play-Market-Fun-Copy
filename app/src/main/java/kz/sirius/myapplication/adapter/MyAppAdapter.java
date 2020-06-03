package kz.sirius.myapplication.adapter;

import android.media.Image;
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

import org.w3c.dom.Text;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.entity.Category;
import kz.sirius.myapplication.entity.MyApp;
import kz.sirius.myapplication.service.DownloadImageTask;

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyViewHolder> implements Filterable {

    private OnCollaborationClickListener listener;
    private ArrayList<MyApp> myApps = new ArrayList<>();
    private ArrayList<MyApp> allApps;
    private String categoryName;

    public void setListener(OnCollaborationClickListener listener) {
        this.listener = listener;
    }

    public void setContent(ArrayList<MyApp> myPreviews, String categoryName) {
        this.myApps = myPreviews;
        this.categoryName = categoryName;
        this.allApps = new ArrayList<>(myApps);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAppAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.appTitle.setText(myApps.get(position).getTitle());
        holder.appSize.setText(String.valueOf(myApps.get(position).getSize()) + " МБ");
        holder.appCategory.setText(categoryName);

        new DownloadImageTask(holder.appImage)
                .execute(myApps.get(position).getImg_url());
        holder.uiApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(position);
            }
        });
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

        TextView appTitle;
        TextView appSize;
        TextView appCategory;
        ImageView appImage;
        LinearLayout uiApp;

        public MyViewHolder(View v) {
            super(v);

            appTitle = v.findViewById(R.id.appTitle);
            appSize = v.findViewById(R.id.appSize);
            appCategory = v.findViewById(R.id.appCategory);
            appImage = v.findViewById(R.id.appImage);
            uiApp = v.findViewById(R.id.uiApp);

            //uiTextDetails = v.findViewById(R.id.uiTextDetails);
        }
    }
}



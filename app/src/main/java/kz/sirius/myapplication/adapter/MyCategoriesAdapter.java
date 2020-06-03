package kz.sirius.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.entity.Category;
import kz.sirius.myapplication.service.DownloadImageTask;

public class MyCategoriesAdapter extends RecyclerView.Adapter<MyCategoriesAdapter.MyViewHolder>{

    private OnCollaborationClickListener listener;
    private ArrayList<Category> myCategories = new ArrayList<>();



    public void setListener(OnCollaborationClickListener listener) {
        this.listener = listener;
    }

    public void setContent(ArrayList<Category> myPreviews) {
        this.myCategories = myPreviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyCategoriesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryName.setText(myCategories.get(position).getName());

        new DownloadImageTask(holder.categoryImage)
                .execute(myCategories.get(position).getCategoryUrl());

        holder.uiPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(position);
            }
        });
    }





    @Override
    public int getItemCount() {
        return myCategories.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView categoryName;
        LinearLayout uiPreview;
        ImageView categoryImage;
        public MyViewHolder(View v) {
            super(v);

            categoryName = v.findViewById(R.id.categoryName);
            uiPreview = v.findViewById(R.id.uiPreview);
            categoryImage = v.findViewById(R.id.categoryImage);

            //uiTextDetails = v.findViewById(R.id.uiTextDetails);
        }
    }
}



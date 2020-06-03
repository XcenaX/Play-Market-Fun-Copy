package kz.sirius.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayerView;

import kz.sirius.myapplication.R;
import kz.sirius.myapplication.service.DownloadImageTask;
import kz.sirius.myapplication.utils.YoutubeController;

public class MyPreviewsAdapter extends RecyclerView.Adapter<MyPreviewsAdapter.MyViewHolder> {

    private OnCollaborationClickListener listener;
    private ArrayList<String> myPreviews = new ArrayList<>();
    private Context context;


    public void setListener(OnCollaborationClickListener listener) {
        this.listener = listener;
    }

    public void setContent(ArrayList<String> myPreviews, Context context) {
        this.myPreviews = myPreviews;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyPreviewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.previews_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(myPreviews.get(position).contains("youtube")){
            holder.uiImage.setVisibility(View.GONE);
            //YoutubeController youtubeController = new YoutubeController(holder.youtubeView, context, myPreviews.get(position));
        }else{
            //holder.youtubeView.setVisibility(View.GONE);
            new DownloadImageTask(holder.uiImage)
                    .execute(myPreviews.get(position));
        }

    }



    @Override
    public int getItemCount() {
        return myPreviews.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView uiImage;
        LinearLayout uiApp;
        //YouTubePlayerView youtubeView;
        public MyViewHolder(View v) {
            super(v);
            uiApp = v.findViewById(R.id.uiPreview);
            uiImage = v.findViewById(R.id.categoryImage);
            //youtubeView = v.findViewById(R.id.youtubeView);

            //uiTextDetails = v.findViewById(R.id.uiTextDetails);
        }
    }
}



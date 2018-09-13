package com.shaary.movienight.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shaary.movienight.R;
import com.shaary.movienight.model.children.Result;
import com.shaary.movienight.ui.ChoiceDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    public static final String TAG = RVAdapter.class.getSimpleName();

    private List<String> posters;
    private List<String> titles;
    private Context context;
    private Listener listener;

    public interface Listener {
        void onClick(int position);
    }

    public RecyclerViewAdapter(Context context, List<String> posters, List<String> titles) {
        this.context = context;
        this.posters = posters;
        this.titles = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                // todo log exception to central service or something like that

                // important to return false so the error placeholder can be placed
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                // everything worked out, so probably nothing to do
                return false;
            }
        };

        Glide.with(context)
                .asBitmap()
                .load(posters.get(position))
                .listener(requestListener)
                .apply(new RequestOptions().placeholder(R.drawable.question_mark).error(R.drawable.question_mark))
                .into(holder.image);

        Log.d(TAG, "onBindViewHolder: poster " + posters.get(position));

        holder.imageName.setText(titles.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return posters.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.imageName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public void addResults(List<String> posters, List<String> titles) {
        this.posters.addAll(posters);
        this.titles.addAll(titles);
        notifyDataSetChanged();

    }
}

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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{

    public static final String TAG = RVAdapter.class.getSimpleName();

    private List<Result> mResults = new ArrayList<>();
    private Context mContext;
    private boolean isMovie;
    private boolean isTv;

    //TODO figure out why poster path has duplicate in it
    //TODO figure out why there's no name on TV shows

    public RVAdapter(Context context, List<Result> results, boolean isMovie, boolean isTv) {
        this.mResults.addAll(results);
        mContext = context;
        this.isMovie = isMovie;
        this.isTv = isTv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);
        return new RVAdapter.ViewHolder(view);
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

        Glide.with(mContext)
                .asBitmap()
                .load(mResults.get(position).getPoster_path())
                .listener(requestListener)
                .apply(new RequestOptions().placeholder(R.drawable.question_mark).error(R.drawable.question_mark))
                .into(holder.image);
        Log.d(TAG, "onBindViewHolder: poster path " + mResults.get(position).getPoster_path());
        if (isMovie) {
            holder.imageName.setText(mResults.get(position).getTitle());
        } else if (isTv) {
            holder.imageName.setText(mResults.get(position).getName());
        }

        //shows dialog when the image is clicked
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDialogFragment choiceDialogFragment = new ChoiceDialogFragment();
                Bundle extras = new Bundle();
                extras.putString("overview", mResults.get(holder.getAdapterPosition()).getOverview());
                extras.putFloat("rating", (float)mResults.get(holder.getAdapterPosition()).getVote_average());
                extras.putInt("vote count", mResults.get(holder.getAdapterPosition()).getVote_count());

                if (isMovie) {
                    extras.putString("title", mResults.get(holder.getAdapterPosition()).getTitle());
                    extras.putString("date", mResults.get(holder.getAdapterPosition()).getRelease_date());
                    extras.putString("type", "movie");

                    Log.d(TAG, "onClick: clicked on:  " + mResults.get(holder.getAdapterPosition()).getTitle());
                } else {
                    extras.putString("title", mResults.get(holder.getAdapterPosition()).getName());
                    extras.putString("date", mResults.get(holder.getAdapterPosition()).getFirst_air_date());
                    extras.putString("type", "tv show");
                    Log.d(TAG, "onClick: clicked on:  " + mResults.get(holder.getAdapterPosition()).getTitle());
                }
                choiceDialogFragment.setArguments(extras);
                choiceDialogFragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "choice");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResults.size();
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

    public void updateResultsList(List<Result> newResults) {
        final ResultsDiffUtil resultsDiffUtil = new ResultsDiffUtil(this.mResults, newResults, isMovie);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(resultsDiffUtil);

        this.mResults.clear();
        this.mResults.addAll(newResults);

        diffResult.dispatchUpdatesTo(this);

    }

    public void addResults (List<Result> newResults) {

        this.mResults.addAll(newResults);
        notifyDataSetChanged();
    }
}

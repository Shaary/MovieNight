package com.shaary.movienight.helpers;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil.Callback;

import com.shaary.movienight.model.children.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultsDiffUtil extends Callback {

    private List<Result> firstResults = new ArrayList<>();
    private List<Result> newResults = new ArrayList<>();
    private boolean isMovie;

    public ResultsDiffUtil(List<Result> firstResults, List<Result> newResults, boolean isMovie) {
        this.firstResults = firstResults;
        this.newResults = newResults;
        this.isMovie = isMovie;
    }

    @Override
    public int getOldListSize() {
        return firstResults.size();
    }

    @Override
    public int getNewListSize() {
        return newResults.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return firstResults.get(oldItemPosition).getId() == newResults.get(
                newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int firstItemPosition, int newItemPosition) {
        final Result firstResult = firstResults.get(firstItemPosition);
        final Result newResult = newResults.get(newItemPosition);

        boolean areEqual;

        if (isMovie) {
            areEqual = firstResult.getTitle().equals(newResult.getTitle());
        } else {
            areEqual = firstResult.getName().equals(newResult.getName());
        }
        return areEqual;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}

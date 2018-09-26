package com.shaary.movienight.model;

import android.util.Log;

import com.shaary.movienight.model.children.Result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataResults {

    private static final String TAG = DataResults.class.getSimpleName();
    private int page;
    private int total_results;
    private int total_pages;
    private List<Result> results;

    public static List<Result> SORT_BY_TITLE (List<Result> listOfData) {
       List<Result> resultsList = listOfData;
       Collections.sort(resultsList, new Comparator<Result>() {
           @Override
           public int compare(Result o1, Result o2) {
               return (o1.getTitle().compareTo(o2.getTitle()));
           }
       });
      Log.i(TAG, "SORT_BY_NAME: returning, lol" + resultsList);
       return resultsList;
   }

    public static List<Result> SORT_BY_NAME (List<Result> listOfData) {
        List<Result> resultsList = listOfData;
        Collections.sort(resultsList, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return (o1.getName().compareTo(o2.getName()));
            }
        });
        Log.i(TAG, "SORT_BY_NAME: returning, lol" + resultsList);
        return resultsList;
    }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotal_results() {
            return total_results;
        }

        public void setTotal_results(int total_results) {
            this.total_results = total_results;
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

    }


package com.shaary.movienight.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static android.support.constraint.Constraints.TAG;

public class Genres {
    private Map<String, String> genreMap = new TreeMap<>();

    public Genres() {
        genreMap.put("Action", "28");
        genreMap.put("Adventure", "12");
        genreMap.put("Animation", "16");
        genreMap.put("Comedy", "35");
        genreMap.put("Crime", "80");
        genreMap.put("Documentary", "99");
        genreMap.put("Drama", "18");
        genreMap.put("Family", "10751");
        genreMap.put("Fantasy", "14");
        genreMap.put("History", "36");
        genreMap.put("Horror", "27");
        genreMap.put("Music", "10402");
        genreMap.put("Mystery", "9648");
        genreMap.put("Romance", "10749");
        genreMap.put("Science Fiction", "878");
        genreMap.put("TV Movie", "10770");
        genreMap.put("Thriller", "53");
        genreMap.put("War", "10752");
        genreMap.put("Action & Adventure", "10759");
        genreMap.put("Kids", "10762");
        genreMap.put("News", "10763");
        genreMap.put("Reality", "10764");
        genreMap.put("Sci-Fi & Fantasy", "10765");
        genreMap.put("Soap", "Soap");
        genreMap.put("Talk", "10767");
        genreMap.put("War & Politics", "10768");
    }

    public String getId(ArrayList<String> genreNames) {
        String id = "";
        for (int i = 0; i < genreNames.size(); i++) {
            String genreName = genreNames.get(i);
            if (genreMap.containsKey(genreName)) {
                id = id + genreMap.get(genreName);
                if (i != genreNames.size() - 1) {
                    id = id + "|";
                }
            }
        }
        Log.d(TAG, "getId: " + id);
        return id;
    }
}

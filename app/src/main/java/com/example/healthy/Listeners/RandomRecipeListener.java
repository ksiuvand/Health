package com.example.healthy.Listeners;

import com.example.healthy.Models.RandomRecipeApiResponse;

public interface RandomRecipeListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}

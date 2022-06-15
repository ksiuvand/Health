package com.example.healthy.Listeners;

import com.example.healthy.Models.InstructionsResponse;

import java.util.List;

public interface InstructionListener {
    void didFetch(List<InstructionsResponse> response, String message);
    void didError(String message);
}

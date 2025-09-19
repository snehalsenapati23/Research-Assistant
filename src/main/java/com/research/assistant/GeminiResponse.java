package com.research.assistant;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;

    // --- Inner Classes ---
    public static class Candidate {
        private Content content;}

    public static class Content {
        private List<Part> parts;
    }

    public static class Part {
        private String text;
    }
}

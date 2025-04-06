package com.example.makridin.chat_bot_structured.controller;

import com.example.makridin.chat_bot_structured.excpetion.MovieNotSearchedException;
import com.example.makridin.chat_bot_structured.model.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static final String GET_MOVIES_BY_DIRECTOR_TEMPLATE = """
            "Generate a list of all the movies directed by {directorName}. If the director is unknown, return null.
            Each movie should include a title and release year. Sort by year. {format}"
            """;

    private final ChatClient chatClient;

    public MovieController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/byDirector")
    public List<Movie> getResponse(@RequestParam(value = "director", required = false) String directorName) {
        if (StringUtils.hasText(directorName)) {
            return Optional.ofNullable(chatClient.prompt()
                    .user(userSpec -> userSpec.text(directorName)
                            .param("directorName", directorName)
                            .param("format", "json"))
                    .call()
                    .entity(new ParameterizedTypeReference<List<Movie>>() {}))
                    .map(movies -> movies
                            .stream()
                            .sorted(Comparator.comparingInt(movie -> Integer.parseInt(movie.yearOfRelease())))
                            .toList()
                    )
                    .orElse(null);
        } else {
            throw new MovieNotSearchedException("There is nothing to search! please add a request parameter with the name director to your request!");
        }
    }
}

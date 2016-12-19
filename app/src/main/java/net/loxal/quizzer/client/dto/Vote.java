/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Vote {
    @NotNull
    private String id;
    @NotNull
    private String poll = "";
    @NotNull
    private String session;
    @Min(value = 0)
    @NotNull
    private List<Integer> answers = Collections.emptyList();
    @NotNull
    private String user = "anonymous";
    private Boolean correct;

    @JsonCreator
    public Vote(@JsonProperty("id") String id,
                @JsonProperty("session") String session,
                @JsonProperty("user") String user,
                @JsonProperty("poll") String poll,
                @JsonProperty("answers") List<Integer> answers
    ) {
        this.id = id;
        this.session = session;
        this.user = user;
        this.poll = poll;
        this.answers = answers;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Vote{" + "id='" + id + '\'' +
                ", poll='" + poll + '\'' +
                ", session='" + session + '\'' +
                ", answers=" + answers +
                ", user='" + user + '\'' +
                ", correct=" + correct +
                '}';
    }
}

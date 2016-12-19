/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Set;

import javax.validation.constraints.NotNull;

public class Certificate {

    private String id;
    @NotNull
    private String user;
    @NotNull
    private String session;
    @NotNull
    private String timestamp;
    @NotNull
    private Integer correctAnswers = 0;
    @NotNull
    private Integer incorrectAnswers = 0;

    private transient Integer totalAnswers = 0;

    @NotNull
    private Set<Vote> givenAnswers = Collections.emptySet();

    @JsonCreator
    public Certificate(
            @JsonProperty("id") String id, // TODO remove
            @JsonProperty("user") String user, // TODO remove
            @JsonProperty("session") String session // TODO remove
    ) {
        this.id = id;
        this.user = user;
        this.session = session;
    }

    public boolean hasPassed() {
        return 0.75 <= calculateScore();
    }

    public float calculateScore() {
        return (float) correctAnswers / (correctAnswers + incorrectAnswers);
    }

    public Integer getTotalAnswers() {
        return getCorrectAnswers() + getIncorrectAnswers();
    }

    public Set<Vote> getGivenAnswers() {
        return givenAnswers;
    }

    public void setGivenAnswers(Set<Vote> givenAnswers) {
        this.givenAnswers = givenAnswers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(Integer incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }
}

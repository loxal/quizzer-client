/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Poll {
    private String id;
    @NotNull
    private String question = "";
    @NotNull
    private List<String> options = Collections.emptyList();
    /**
     * Provide a hint for the user & UI.
     */
    @NotNull
    private Boolean multipleAnswers = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getMultipleAnswers() {
        return multipleAnswers;
    }
    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Poll{" + "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", multipleAnswers=" + multipleAnswers +
                '}';
    }
}

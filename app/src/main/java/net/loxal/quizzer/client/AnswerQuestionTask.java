/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.os.AsyncTask;
import android.util.Log;

import net.loxal.quizzer.client.dto.Vote;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class AnswerQuestionTask extends AsyncTask<Request, Void, Vote> {
    @Override
    protected Vote doInBackground(Request... request) {
        try {
            final Response response = MainActivity.CLIENT.newCall(request[0]).execute();
            final ResponseBody body = response.body();

            return MainActivity.OBJECT_MAPPER.readValue(body.bytes(), Vote.class);
        } catch (IOException e) {
            Log.w(AnswerQuestionTask.class.getName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Vote vote) {
        Log.i(AnswerQuestionTask.class.getName(), "fetched");
    }
}
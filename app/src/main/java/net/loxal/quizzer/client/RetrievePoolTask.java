/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.os.AsyncTask;
import android.util.Log;

import net.loxal.quizzer.client.dto.Poll;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class RetrievePoolTask extends AsyncTask<Request, Void, Poll> {
    @Override
    protected Poll doInBackground(Request... request) {
        try {
            final Response response = MainActivity.CLIENT.newCall(request[0]).execute();
            final ResponseBody body = response.body();

            return MainActivity.OBJECT_MAPPER.readValue(body.bytes(), Poll.class);
        } catch (Exception e) {
            Log.w(RetrievePoolTask.class.getName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Poll poll) {
        Log.i(RetrievePoolTask.class.getName(), "fetched");
    }
}
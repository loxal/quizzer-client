/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.os.AsyncTask;
import android.util.Log;

import net.loxal.quizzer.client.dto.Certificate;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class RetrieveCertificateTask extends AsyncTask<Request, Void, Certificate> {
    @Override
    protected Certificate doInBackground(Request... request) {
        try {
            final Response response = MainActivity.CLIENT.newCall(request[0]).execute();
            final ResponseBody body = response.body();

            if (response.isSuccessful()) {
                return MainActivity.OBJECT_MAPPER.readValue(body.bytes(), Certificate.class);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.w(RetrieveCertificateTask.class.getName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Certificate certificate) {
        Log.i(RetrieveCertificateTask.class.getName(), "fetched");
    }
}
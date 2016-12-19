/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;

import net.loxal.quizzer.client.dto.Certificate;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class RetrieveCertificatesTask extends AsyncTask<Request, Void, List<Certificate>> {
    private static final TypeReference<List<Certificate>> LIST_TYPE_REFERENCE = new TypeReference<List<Certificate>>() {
    };

    @Override
    protected List<Certificate> doInBackground(Request... request) {
        try {
            final Response response = MainActivity.CLIENT.newCall(request[0]).execute();
            final ResponseBody body = response.body();

            if (response.isSuccessful()) {
                return MainActivity.OBJECT_MAPPER.readValue(body.bytes(), LIST_TYPE_REFERENCE);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.w(RetrieveCertificatesTask.class.getName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Certificate> poll) {
        Log.i(RetrieveCertificatesTask.class.getName(), "fetched");
    }
}
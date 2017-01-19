/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.loxal.quizzer.client.dto.Certificate;

import java.util.List;

import okhttp3.Request;

public class CertificateListActivity extends Activity {

    private String user;
    private ListView certificateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_certificate);

        certificateList = (ListView) findViewById(R.id.certificateList);

        {
            this.user = this.getIntent().getExtras().getString("user");
        }

        List<Certificate> certificates = retrieveCertificateForUser();
        ArrayAdapter<Certificate> certificateArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, certificates);
        certificateList.setAdapter(certificateArrayAdapter);
    }

    private List<Certificate> retrieveCertificateForUser() {
        final Request request = new Request.Builder()
                .header("Authorization", "Basic " + BuildConfig.BASIC_AUTH_BASE64)
                .url(getResources().getString(R.string.serviceRootUrl)
                        + getResources().getString(R.string.certificatesEndpoint)
                        + "?user=" + user)
                .build();
        try {
            final List<Certificate> issuedCertificates = new RetrieveCertificatesTask().execute(request).get();

            return issuedCertificates;
        } catch (Exception e) {
            Log.w(MainActivity.class.getName(), e.getMessage());
        }
        return null;
    }
}

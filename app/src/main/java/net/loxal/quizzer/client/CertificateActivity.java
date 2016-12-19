/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import net.loxal.quizzer.client.dto.Certificate;

import okhttp3.Request;

public class CertificateActivity extends Activity {

    private Certificate certificate;
    private CheckBox certificateStatus;
    private TextView certificateId;
    private TextView certificateCorrectAnswers;
    private TextView certificateIncorrectAnswers;
    private TextView certificateQuestions;
    private SeekBar certificateScore;
    private String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);
        {
            this.certificateStatus = (CheckBox) findViewById(R.id.certificateStatus);
            this.certificateScore = (SeekBar) findViewById(R.id.certificateScore);
            this.certificateScore.setEnabled(false);
            this.certificateId = (TextView) findViewById(R.id.certificateId);
            this.certificateCorrectAnswers = (TextView) findViewById(R.id.certificateCorrectAnswers);
            this.certificateIncorrectAnswers = (TextView) findViewById(R.id.certificateIncorrectAnswers);
            this.certificateQuestions = (TextView) findViewById(R.id.certificateQuestions);
            this.session = this.getIntent().getExtras().getString("session");
        }

        this.certificate = retrieveCertificate();

        certificateScore.setProgress(Float.valueOf(this.certificate.calculateScore() * 100).intValue());
        certificateCorrectAnswers.setText(certificate.getCorrectAnswers().toString());
        certificateIncorrectAnswers.setText(this.certificate.getIncorrectAnswers().toString());
        certificateQuestions.setText(certificate.getTotalAnswers().toString());
        certificateId.setText(certificate.getId());

        displayStatus();
    }

    private void displayStatus() {
        if (certificate.hasPassed()) {
            certificateStatus.setText(R.string.passed);
        }

        certificateStatus.setChecked(certificate.hasPassed());
    }

    private Certificate retrieveCertificate() {
        final Request request = new Request.Builder()
                .header("Authorization", "Basic " + BuildConfig.BASIC_AUTH_BASE64)
                .url(getResources().getString(R.string.serviceRootUrl)
                        + getResources().getString(R.string.certificatesEndpoint)
                        + session)
                .build();
        try {
            final Certificate issuedCertificate = new RetrieveCertificateTask().execute(request).get();

            return issuedCertificate;
        } catch (Exception e) {
            Log.w(MainActivity.class.getName(), e.getMessage());
        }
        return null;
    }
}

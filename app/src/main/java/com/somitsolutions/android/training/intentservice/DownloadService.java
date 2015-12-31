package com.somitsolutions.android.training.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class DownloadService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public DownloadService() {
        super(DownloadService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

            /* Update UI: Download Service is Running */
            Message msg1 = MyActivity.messageHandler.obtainMessage(MyActivity.messageHandler.SHOW_PROGRESS_DIALOG);
            MyActivity.messageHandler.sendMessage(msg1);

            MyActivity.mBitmap = downloadBitmap(url);

            Message msg2 = MyActivity.messageHandler.obtainMessage(MyActivity.MessageHandler.DISMISS_PROGRESS_DIALOG);
            MyActivity.messageHandler.sendMessage(msg2);

        this.stopSelf();
    }

    private Bitmap downloadBitmap(String url) {

        final DefaultHttpClient client = new DefaultHttpClient();

        final HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            final HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    BitmapFactory.Options optionSample = new BitmapFactory.Options();
                    optionSample.inSampleSize = 4; // Or 8 for smaller image
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,optionSample);
                    return bitmap;

                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    //entity.consumeContent();
                }
            }
        } catch (Exception e) {
            getRequest.abort();
            Log.e("AsynctaskImageDownload", e.toString());
        }
        return null;
    }


}
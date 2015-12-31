package com.somitsolutions.android.training.intentservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.javatechig.intentserviceexample.R;

public class MyActivity extends Activity implements View.OnClickListener {

    Button mStartDownloadButton, mDisplayImageButton;
    EditText mURL;
    ImageView mImageView;
    Context context;
    static Bitmap mBitmap;
    Runnable mDownloadRunnable;
    String mUrl;
    static MessageHandler messageHandler;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mStartDownloadButton = (Button)findViewById(R.id.buttonDownloadImage);
        mDisplayImageButton = (Button)findViewById(R.id.buttonDisplayImage);
        mURL = (EditText)findViewById(R.id.editTextURL);
        mImageView = (ImageView)findViewById(R.id.imageView1);
        mImageView.setVisibility(View.INVISIBLE);
        mStartDownloadButton.setOnClickListener(this);
        mDisplayImageButton.setOnClickListener(this);

        context = this;

        messageHandler = new MessageHandler();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("On Progress...");
        mProgressDialog.setCancelable(true);

    }

    @Override
    public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.equals(mStartDownloadButton)){
                mUrl = mURL.getText().toString();
                mUrl = mUrl.replace(" ", "");

                if(mUrl != null && !mUrl.isEmpty()){
                    mStartDownloadButton.setEnabled(false);
                    Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                    intent.putExtra("url", mUrl);
                    startService(intent);
                }
            }
            if(v.equals(mDisplayImageButton)){
                mImageView.setImageBitmap(mBitmap);
                mImageView.setVisibility(View.VISIBLE);
            }
        }


    public class MessageHandler extends Handler {

        public static final int SHOW_PROGRESS_DIALOG = 1;
        public static final int DISMISS_PROGRESS_DIALOG = 2;

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOW_PROGRESS_DIALOG:
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(MyActivity.this);
                        mProgressDialog.setMessage("On Progress...");
                        mProgressDialog.setCancelable(true);
                    }
                    mProgressDialog.show();
                    break;

                case DISMISS_PROGRESS_DIALOG:
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                    mStartDownloadButton.setEnabled(true);
                    break;
            }
        }
    }
}
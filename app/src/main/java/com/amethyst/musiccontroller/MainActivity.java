package com.amethyst.musiccontroller;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MusicController";
    private static final boolean DBG = false;

    private static OkHttpClient sClient = null;

    public static OkHttpClient getClient() {
        if (sClient == null) {
            synchronized (OkHttpClient.class) {
                if (sClient == null) {
                    sClient = new OkHttpClient();
                }
            }
        }
        return sClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.start_button).setOnClickListener(this);
        findViewById(R.id.play_button).setOnClickListener(this);
        findViewById(R.id.last_button).setOnClickListener(this);
        findViewById(R.id.next_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String action = "";
        switch (v.getId()) {
            case R.id.start_button:
                action = Constants.ACTION_START;
                break;
            case R.id.play_button:
                action = Constants.ACTION_PLAY;
                break;
            case R.id.last_button:
                action = Constants.ACTION_LAST;
                break;
            case R.id.next_button:
                action = Constants.ACTION_NEXT;
                break;
            default:
                break;
        }

        if (DBG) {
            Log.d(TAG, "onClick, action = " + action);
        }

        if (!TextUtils.isEmpty(action)) {
            new PostAsyncTask(getClient()).execute(action);
        }
    }

    static class PostAsyncTask extends AsyncTask<String, Void, Void> {

        WeakReference<OkHttpClient> clientWeakReference;

        PostAsyncTask(OkHttpClient client) {
            clientWeakReference = new WeakReference<>(client);
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestBody formBody = new FormBody.Builder()
                    .add(Constants.ACTION, strings[0])
                    .build();
            Request request = new Request.Builder()
                    .url(Constants.SERVER_URL)
                    .post(formBody)
                    .build();

            Response response = null;

            try {
                response = clientWeakReference.get().newCall(request).execute();
                if (DBG) {
                    Log.d(TAG, "response code = " + response.code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Util.closeQuietly(response);
            }
            return null;
        }

    }

}

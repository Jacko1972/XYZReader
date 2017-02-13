package com.example.xyzreader.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.example.xyzreader.remote.Config;
import com.example.xyzreader.remote.RemoteEndpointUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE = "com.example.xyzreader.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING = "com.example.xyzreader.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //GregorianCalendar gc = new GregorianCalendar();
        //Time time = new Time();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        final ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        final Uri dirUri = ItemsContract.Items.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());
        final SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        UpdaterInterface updaterInterface = RemoteEndpointUtil.getClient().create(UpdaterInterface.class);
        final Call<List<Article>> listCall = updaterInterface.getArticles(Config.BASE_URL.toString());
        listCall.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.isSuccessful()) {
                    List<Article> articleList = response.body();
                    ContentValues cv = null;
                    for (Article articleInfo : articleList) {
                        if (cv != null) {
                            cv.clear();
                        }
                        String[] parts =articleInfo.getPublishedDate().split("T");
                        try {
                            Date date = oldDateFormat.parse(parts[0]);
                            articleInfo.setPublishedDate(newDateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cv = articleInfo.articleToContentValues();
                        cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(cv).build());
                    }
                    try {
                        getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
                    } catch (RemoteException | OperationApplicationException e) {
                        e.printStackTrace();
                    }
                }
                sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Log.d(TAG, "call: " + call);
                Log.d(TAG, "throws: " + t.toString());
                sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }
        });

//        try {
//            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
//            if (array == null) {
//                throw new JSONException("Invalid parsed item array");
//            }
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            long publishedDate = 0;
//
//            for (int i = 0; i < array.length(); i++) {
//                ContentValues values = new ContentValues();
//                JSONObject object = array.getJSONObject(i);
//                values.put(ItemsContract.Items.SERVER_ID, object.getString("id"));
//                values.put(ItemsContract.Items.AUTHOR, object.getString("author"));
//                values.put(ItemsContract.Items.TITLE, object.getString("title"));
//                values.put(ItemsContract.Items.BODY, object.getString("body"));
//                values.put(ItemsContract.Items.THUMB_URL, object.getString("thumb"));
//                values.put(ItemsContract.Items.PHOTO_URL, object.getString("photo"));
//                values.put(ItemsContract.Items.ASPECT_RATIO, object.getString("aspect_ratio"));
//                String[] parts = object.getString("published_date").split("T");
//                try {
//                    Date date = df.parse(parts[0]);
//                    publishedDate = date.getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                //time.parse3339(object.getString("published_date")); 2013-06-20
//                values.put(ItemsContract.Items.PUBLISHED_DATE, publishedDate);
//                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
//            }
//
//            getContentResolver().applyBatch(ItemsContract.CONTENT_AUTHORITY, cpo);
//
//        } catch (JSONException | RemoteException | OperationApplicationException e) {
//            Log.e(TAG, "Error updating content.", e);
//        }


    }
}

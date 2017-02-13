package com.example.xyzreader.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import okhttp3.logging.HttpLoggingInterceptor;

public class RemoteEndpointUtil {
//    private static final String TAG = "RemoteEndpointUtil";

    private RemoteEndpointUtil() {
    }

    private static Retrofit retrofit = null;
    public static Retrofit getClient() {

        if (retrofit == null) {

            OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
            //Uncomment three lines and the import above to enable logging for Retrofit API calls
            //HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            //httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //okHttpClient.addInterceptor(httpLoggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://dl.dropboxusercontent.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient.build())
                    .build();
        }
        return retrofit;
    }

//    public static JSONArray fetchJsonArray() {
//        String itemsJson = null;
//        try {
//            itemsJson = fetchPlainText(Config.BASE_URL);
//        } catch (IOException e) {
//            Log.e(TAG, "Error fetching items JSON", e);
//            return null;
//        }
//
//        // Parse JSON
//        try {
//            JSONTokener token = new JSONTokener(itemsJson);
//            Object val = token.nextValue();
//            if (!(val instanceof JSONArray)) {
//                throw new JSONException("Expected JSONArray");
//            }
//            return (JSONArray) val;
//        } catch (JSONException e) {
//            Log.e(TAG, "Error parsing items JSON", e);
//        }
//
//        return null;
//    }
//
//    static String fetchPlainText(URL url) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }
}

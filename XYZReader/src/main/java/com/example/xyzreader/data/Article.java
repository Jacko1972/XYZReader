package com.example.xyzreader.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Article implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("aspect_ratio")
    @Expose
    private Double aspectRatio;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("body")
    @Expose
    private String body;
    public final static Parcelable.Creator<Article> CREATOR = new Creator<Article>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Article createFromParcel(Parcel in) {
            Article instance = new Article();
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.photo = ((String) in.readValue((String.class.getClassLoader())));
            instance.thumb = ((String) in.readValue((String.class.getClassLoader())));
            instance.aspectRatio = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.author = ((String) in.readValue((String.class.getClassLoader())));
            instance.title = ((String) in.readValue((String.class.getClassLoader())));
            instance.publishedDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.body = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Article[] newArray(int size) {
            return (new Article[size]);
        }

    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(photo);
        dest.writeValue(thumb);
        dest.writeValue(aspectRatio);
        dest.writeValue(author);
        dest.writeValue(title);
        dest.writeValue(publishedDate);
        dest.writeValue(body);
    }

    public int describeContents() {
        return 0;
    }

    public ContentValues articleToContentValues() {
        ContentValues values = new ContentValues();
        values.put(ItemsContract.ItemsColumns._ID, id);
        values.put(ItemsContract.ItemsColumns.TITLE, title);
        values.put(ItemsContract.ItemsColumns.ASPECT_RATIO, aspectRatio);
        values.put(ItemsContract.ItemsColumns.AUTHOR, author);
        values.put(ItemsContract.ItemsColumns.BODY, body);
        values.put(ItemsContract.ItemsColumns.PHOTO_URL, photo);
        values.put(ItemsContract.ItemsColumns.PUBLISHED_DATE, publishedDate);
        values.put(ItemsContract.ItemsColumns.THUMB_URL, thumb);
        return values;
    }

    public static Article articleFromCursor(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            Article article = new Article();
            article.setId(cursor.getString(ArticleLoader.Query._ID));
            article.setTitle(cursor.getString(ArticleLoader.Query.TITLE));
            article.setThumb(cursor.getString(ArticleLoader.Query.THUMB_URL));
            article.setPublishedDate(cursor.getString(ArticleLoader.Query.PUBLISHED_DATE));
            article.setAspectRatio(cursor.getDouble(ArticleLoader.Query.ASPECT_RATIO));
            article.setBody(cursor.getString(ArticleLoader.Query.BODY));
            article.setAuthor(cursor.getString(ArticleLoader.Query.AUTHOR));
            article.setPhoto(cursor.getString(ArticleLoader.Query.PHOTO_URL));
            return article;
        } else {
            return null;
        }
    }
}
package com.example.xyzreader.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.xyzreader.R;
import com.example.xyzreader.data.Article;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ArticleDetailFragment";
    private static final String ARG_ITEM_ID = "item_id";

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView mPhotoView;
    private Context mContext;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        } else {
            mItemId = 0;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mItemId == 0) {
            return inflater.inflate(R.layout.no_detail_fragment, container, false);
        } else {
            mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);

            mPhotoView = (ImageView) mRootView.findViewById(R.id.photo);
            collapsingToolbarLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_detail_toolbar_layout);

            Boolean sw600dp = getResources().getBoolean(R.bool.sw600dp);
            toolbar = (Toolbar) mRootView.findViewById(R.id.detail_fragment_toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(!sw600dp);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.share_fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCursor != null) {
                            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                                    .setType("text/plain")
                                    .setText(mCursor.getString(ArticleLoader.Query.PHOTO_URL))
                                    .getIntent(), getString(R.string.action_share)));
                        }
                    }
                });
            }
            return mRootView;
        }
    }

    @SuppressWarnings("deprecation")
    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        final TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);

        bylineView.setMovementMethod(new LinkMovementMethod());

        TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);
        bodyView.setMovementMethod(new LinkMovementMethod());

        if (mCursor != null) {
            Article article = Article.articleFromCursor(mCursor);

            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(article.getTitle());
            } else {
                toolbar.setTitle(article.getTitle());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bylineView.setText(Html.fromHtml("<i>" + article.getAuthor() + ", " + article.getPublishedDate() + "</i>", Html.FROM_HTML_MODE_LEGACY));
                bodyView.setText(Html.fromHtml(article.getBody(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                bodyView.setText(Html.fromHtml(article.getBody()));
                bylineView.setText(Html.fromHtml("<i>" + article.getAuthor() + ", " + article.getPublishedDate() + "</i>"));
            }

            Glide.with(mContext).load(article.getPhoto())
                    .asBitmap()
                    .placeholder(R.drawable.spinner_animation)
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            mPhotoView.setImageBitmap(resource);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch dominant = palette.getDominantSwatch();
                                    if (dominant != null) {
//                                        if (mTwoPane || mStackedViews) {
//                                            header.setTextColor(dominant.getTitleTextColor());
//                                            header.setBackgroundColor(dominant.getRgb());
//                                        }
                                        bylineView.setTextColor(dominant.getTitleTextColor());
                                        bylineView.setBackgroundColor(dominant.getRgb());
                                    } else {
                                        Log.d(TAG, "dominant = null");
                                    }
                                }
                            });
                        }
                    });

        } else {
            bylineView.setText(R.string.not_applicable);
            bodyView.setText(R.string.no_data_to_display);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ItemsContract.Items.buildItemUri(mItemId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }
        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        bindViews();
    }


}

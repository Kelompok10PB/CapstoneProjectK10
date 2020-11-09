package com.example.capstoneprojectk10;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.example.capstoneprojectk10.adapter.NewsAdapter;
import com.example.capstoneprojectk10.api.newsData.NewsData;
import com.example.capstoneprojectk10.util.LoadLocale;
import com.example.capstoneprojectk10.util.SetLanguage;
import com.example.capstoneprojectk10.util.SpacesItemDecoration;
import com.example.capstoneprojectk10.vm.NewsDataViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.capstoneprojectk10.R.id;
import static com.example.capstoneprojectk10.R.layout;

public class HomeFragment extends Fragment {
    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private ArrayList<String> mNewsURL = new ArrayList<>();

    @BindView(id.shimmer_layout)
    ShimmerFrameLayout mNewsShimmer;
    @BindView(id.news_recycler_view)
    RecyclerView mNewsRecyclerView;
    @BindView(id.home_call_button)
    LinearLayout mCallButton;
    @BindView(id.home_website_button)
    LinearLayout mWebsiteButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_home, container, false);
        Toast mToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        ButterKnife.bind(this, view);

        LoadLocale loadLocale = new LoadLocale(getActivity());
        Timber.d("LoadLocale%s", loadLocale.getLocale());

        // Give the language option on the fresh install app
        if (loadLocale.getLocale().equals("-1")) {
            new SetLanguage(getContext(), getActivity());
        }

        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:119"));
                startActivity(intent);
            }
        });

        mWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("passingURL", "https://covid19.go.id");
                intent.putExtra("passingTitle", "Portal Resmi Gugus Tugas Covid-19");
                getContext().startActivity(intent);
            }
        });



        // THE DATA FETCHING PROCESS
        NewsDataViewModel newsDataViewModel;

        newsDataViewModel = ViewModelProviders.of(this).get(NewsDataViewModel.class);
        newsDataViewModel.init();

        newsDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });

        LiveData<NewsData> newsDataLiveData;

        if (loadLocale.getLocale().equals("en")) {
            newsDataLiveData = newsDataViewModel.getNewsDataEn();
        } else {
            newsDataLiveData = newsDataViewModel.getNewsData();
        }

        newsDataLiveData.observe(this, new Observer<NewsData>() {
            @Override
            public void onChanged(NewsData newsData) {
                List<NewsData.Articles> articles = newsData.getArticles();
                for (NewsData.Articles theArticle : articles) {
                    mNewsTitle.add(theArticle.getTitle());
                    mNewsImage.add(theArticle.getUrltoimage());
                    mNewsURL.add(theArticle.getUrl());
                }

                NewsAdapter newsAdapter = new NewsAdapter(mNewsImage, mNewsTitle, mNewsURL, getContext());
                mNewsRecyclerView.setAdapter(newsAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
                mNewsRecyclerView.setLayoutManager(linearLayoutManager);
                mNewsRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
                SnapHelper snapHelper = new PagerSnapHelper();
                if (mNewsRecyclerView.getOnFlingListener() == null)
                    snapHelper.attachToRecyclerView(mNewsRecyclerView);
            }
        });

        return view;

    }


    private void hideLoading() {
        mNewsShimmer.stopShimmer();
        mNewsShimmer.setVisibility(View.GONE);
        mNewsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mNewsShimmer.setVisibility(View.VISIBLE);
        mNewsRecyclerView.setVisibility(View.GONE);
    }

    private String numberSeparator(int value) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(value));
    }

}
package com.example.capstoneprojectk10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import com.example.capstoneprojectk10.api.regulerData.RegulerData;
import com.example.capstoneprojectk10.vm.RegulerDataViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.example.capstoneprojectk10.adapter.NewsAdapter;
import com.example.capstoneprojectk10.api.newsData.NewsData;
import com.example.capstoneprojectk10.util.LoadLocale;
import com.example.capstoneprojectk10.util.SpacesItemDecoration;
import com.example.capstoneprojectk10.vm.NewsDataViewModel;
import static com.example.capstoneprojectk10.R.id;
import static com.example.capstoneprojectk10.R.layout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.AlarmManager;
import android.os.SystemClock;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class HomeFragment extends Fragment {
    private ArrayList<String> mNewsImage = new ArrayList<>();
    private ArrayList<String> mNewsTitle = new ArrayList<>();
    private ArrayList<String> mNewsURL = new ArrayList<>();

    private LoadLocale loadLocale;

    public static final long INTERVAL_1Menit = 86400000L;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @BindView(R.id.stat_kasus_aktif)
    TextView mStatPositiveCases;
    @BindView(R.id.stat_kasus_meninggal) TextView mStatDeathCases;
    @BindView(R.id.stat_kasus_sumbuh) TextView mStatCuredCases;
    @BindView(R.id.stat_kasus_odp) TextView mStatMonitoringCases;
    @BindView(R.id.stat_added_pos) TextView mStatAddedPositive;
    @BindView(R.id.stat_added_men) TextView mStatAddedDeath;
    @BindView(R.id.stat_added_sem) TextView mStatAddedCured;
    @BindView(R.id.stat_updated_date) TextView mUpdatedDate;
    @BindView(R.id.stat_box_shimmer) ShimmerFrameLayout mBoxShimmer;
    @BindView(R.id.stat_box_layout)
    TableLayout mBoxLayout;

    @BindView(id.shimmer_layout)
    ShimmerFrameLayout mNewsShimmer;
    @BindView(id.news_recycler_view)
    RecyclerView mNewsRecyclerView;
    @BindView(id.home_call_button)
    LinearLayout mCallButton;
    @BindView(id.home_website_button)
    LinearLayout mWebsiteButton;
    @BindView(R.id.info_language_button)
    LinearLayout mLanguage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(layout.fragment_home, container, false);
        Toast mToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        ButterKnife.bind(this, fragmentView);
        loadLocale = new LoadLocale(getActivity());
        LoadLocale loadLocale = new LoadLocale(getActivity());
        Timber.d("LoadLocale%s", loadLocale.getLocale());

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

        // REGULAR DATA FETCHING
        loadLocale = new LoadLocale(getActivity());
        RegulerDataViewModel regulerDataViewModel;

        regulerDataViewModel = ViewModelProviders.of(this).get(RegulerDataViewModel.class);
        regulerDataViewModel.init();

        regulerDataViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showRegularDataLoading();
                } else {
                    hideRegularDataLoading();
                }
            }
        });

        regulerDataViewModel.getRegulerData().observe(this, new Observer<RegulerData>() {
            @Override
            public void onChanged(RegulerData regulerData) {
                showRegulerData(regulerData);
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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentView.getContext(), LinearLayoutManager.HORIZONTAL, false);
                mNewsRecyclerView.setLayoutManager(linearLayoutManager);
                mNewsRecyclerView.addItemDecoration(new SpacesItemDecoration(30));
                SnapHelper snapHelper = new PagerSnapHelper();
                if (mNewsRecyclerView.getOnFlingListener() == null)
                    snapHelper.attachToRecyclerView(mNewsRecyclerView);
            }
        });

        mNotificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        ToggleButton alarmToggle = fragmentView.findViewById(R.id.alarmToggle);

        Intent notifyIntent = new Intent(getContext(), AlarmReceiver.class);
        boolean alarmUp;
        alarmUp = (PendingIntent.getBroadcast(getContext(), NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);

        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), NOTIFICATION_ID, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        alarmToggle.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean isChecked) {
                        String toastMessage;
                        if (isChecked) {

                            long repeatInterval = HomeFragment.INTERVAL_1Menit;

                            long triggerTime = SystemClock.elapsedRealtime()
                                    + repeatInterval;

                            if (alarmManager != null) {
                                alarmManager.setInexactRepeating
                                        (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                                triggerTime, repeatInterval,
                                                notifyPendingIntent);
                            }

                            toastMessage = getString(R.string.alarm_on_toast);
                        }
                        else {
                            mNotificationManager.cancelAll();

                            if (alarmManager != null) {
                                alarmManager.cancel(notifyPendingIntent);
                            }
                            toastMessage = getString(R.string.alarm_off_toast);

                        }
                        Toast.makeText(getActivity(), toastMessage,Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        createNotificationChannel();
        return fragmentView;
    }


    private void createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Ayo Buka Covid-19 Untuk Berita Covid Terkini");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void showRegulerData(RegulerData regulerData) {

        int mPositif = regulerData.getUpdatedData().getTotalCases().getmPositif();
        int mMeninggal = regulerData.getUpdatedData().getTotalCases().getmMeninggal();
        int mSembuh = regulerData.getUpdatedData().getTotalCases().getmSembuh();
        int mODP = regulerData.getDerivativeData().getmODP();
        int mAddedPos = regulerData.getUpdatedData().getNewCases().getmPositif();
        int mAddedMen = regulerData.getUpdatedData().getNewCases().getmMeninggal();
        int mAddedSem = regulerData.getUpdatedData().getNewCases().getmSembuh();
        String mUpdate = regulerData.getUpdatedData().getNewCases().getmWaktuUpdate();

        mStatPositiveCases.setText(numberSeparator(mPositif));
        mStatDeathCases.setText(numberSeparator(mMeninggal));
        mStatCuredCases.setText(numberSeparator(mSembuh));
        mStatMonitoringCases.setText(numberSeparator(mODP));
        mStatAddedPositive.setText("+" + numberSeparator(mAddedPos));
        mStatAddedDeath.setText("+" + numberSeparator(mAddedMen));
        mStatAddedCured.setText("+" + numberSeparator(mAddedSem));

        if (loadLocale.getLocale().equals("en")) {
            mUpdatedDate.setText("Updated on: " + mUpdate);
        } else {
            mUpdatedDate.setText("Diperbarui pada: " + mUpdate);
        }

    }

    private void hideRegularDataLoading() {
        mBoxShimmer.setVisibility(View.GONE);
        mBoxLayout.setVisibility(View.VISIBLE);
    }

    private void showRegularDataLoading() {
        mBoxShimmer.setVisibility(View.VISIBLE);
        mBoxLayout.setVisibility(View.GONE);
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
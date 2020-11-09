package com.example.capstoneprojectk10;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.example.capstoneprojectk10.api.regulerData.RegulerData;
import com.example.capstoneprojectk10.util.LoadLocale;
import com.example.capstoneprojectk10.vm.RegulerDataViewModel;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatsFragment extends Fragment {

    private LoadLocale loadLocale;

    @BindView(R.id.stat_kasus_aktif) TextView mStatPositiveCases;
    @BindView(R.id.stat_kasus_meninggal) TextView mStatDeathCases;
    @BindView(R.id.stat_kasus_sumbuh) TextView mStatCuredCases;
    @BindView(R.id.stat_kasus_odp) TextView mStatMonitoringCases;
    @BindView(R.id.stat_kasus_pdp) TextView mStatPatientCases;
    @BindView(R.id.stat_added_pos) TextView mStatAddedPositive;
    @BindView(R.id.stat_added_men) TextView mStatAddedDeath;
    @BindView(R.id.stat_added_sem) TextView mStatAddedCured;
    @BindView(R.id.stat_updated_date) TextView mUpdatedDate;
    @BindView(R.id.stat_box_shimmer) ShimmerFrameLayout mBoxShimmer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, view);
        loadLocale = new LoadLocale(getActivity());

        // ========= REGULAR DATA FETCHING

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
        return view;
    }

    private void showRegulerData(RegulerData regulerData) {

        int mPositif = regulerData.getUpdatedData().getTotalCases().getmPositif();
        int mMeninggal = regulerData.getUpdatedData().getTotalCases().getmMeninggal();
        int mSembuh = regulerData.getUpdatedData().getTotalCases().getmSembuh();
        int mODP = regulerData.getDerivativeData().getmODP();
        int mPDP = regulerData.getDerivativeData().getmPDP();
        int mAddedPos = regulerData.getUpdatedData().getNewCases().getmPositif();
        int mAddedMen = regulerData.getUpdatedData().getNewCases().getmMeninggal();
        int mAddedSem = regulerData.getUpdatedData().getNewCases().getmSembuh();
        String mUpdate = regulerData.getUpdatedData().getNewCases().getmWaktuUpdate();

        mStatPositiveCases.setText(numberSeparator(mPositif));
        mStatDeathCases.setText(numberSeparator(mMeninggal));
        mStatCuredCases.setText(numberSeparator(mSembuh));
        mStatMonitoringCases.setText(numberSeparator(mODP));
        mStatPatientCases.setText(numberSeparator(mPDP));
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
    }

    private void showRegularDataLoading() {
        mBoxShimmer.setVisibility(View.VISIBLE);
    }

    private String numberSeparator(int value) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(value));
    }


}

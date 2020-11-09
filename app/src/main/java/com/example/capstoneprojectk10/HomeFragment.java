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

import com.example.capstoneprojectk10.util.LoadLocale;
import com.example.capstoneprojectk10.util.SetLanguage;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.capstoneprojectk10.R.id;
import static com.example.capstoneprojectk10.R.layout;

public class HomeFragment extends Fragment {

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

        return view;

    }



    private String numberSeparator(int value) {
        return String.valueOf(NumberFormat.getNumberInstance(Locale.ITALY).format(value));
    }

}
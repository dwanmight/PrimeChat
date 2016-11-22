package com.junior.dwan.primechat.ui.activities;

/**
 * Created by Might on 17.11.2016.
 */


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.junior.dwan.primechat.R;
import com.junior.dwan.primechat.ui.fragments.LoginFragment;
import com.junior.dwan.primechat.ui.fragments.RegisterFragment;
import com.junior.dwan.primechat.utils.TabsPagerAdapter;

/**
 * Created by Might on 19.10.2016.
 */

public class TabActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customizeToolbar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

    }

    public void customizeToolbar(Toolbar toolbar) {
        // Save current title and subtitle
        final CharSequence originalTitle = toolbar.getTitle();
        // Temporarily modify title and subtitle to help detecting each
        toolbar.setTitle("title");

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (textView.getText().equals("title")) {
                    // Customize title's TextView
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    textView.setLayoutParams(params);
                }
            }
            // Restore title and subtitle
            toolbar.setTitle(originalTitle);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new LoginFragment(), getString(R.string.tab_enter));
        adapter.addFragment(new RegisterFragment(), getString(R.string.tab_register));
        viewPager.setAdapter(adapter);
    }
}
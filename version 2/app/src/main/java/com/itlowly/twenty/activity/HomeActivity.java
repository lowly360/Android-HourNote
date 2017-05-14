package com.itlowly.twenty.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.itlowly.twenty.R;
import com.itlowly.twenty.fragment.AboutFragment;
import com.itlowly.twenty.fragment.HelpFragment;
import com.itlowly.twenty.fragment.SettingFragment;
import com.itlowly.twenty.fragment.home.HomeFragment;
import com.zhy.changeskin.base.BaseSkinActivity;

public class HomeActivity extends BaseSkinActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private AboutFragment aboutFragment;
    private HelpFragment helpFragment;
    private SettingFragment settingFragment;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "请选择你要添加的类型", Snackbar.LENGTH_LONG)
                        .setAction("取消", new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFragment();
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        // 设置fragment标签,方便通过标签还获取依附在此Activity上的fragment
        homeFragment = new HomeFragment();
        transaction.replace(R.id.content_home, homeFragment,
                "home");
        transaction.commit(); // 提交事务
    }

    public void changeHomeFragment(Fragment fragment) {
        transaction = fm.beginTransaction();
        transaction.replace(R.id.content_home, fragment);
        transaction.commit(); // 提交事务
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            changeHomeFragment(homeFragment);
            setToolBarTitle("HourNote");
            fab.show();
        } else if (id == R.id.nav_help) {
            if (helpFragment == null) {
                helpFragment = new HelpFragment();
            }
            changeHomeFragment(helpFragment);
            setToolBarTitle("帮助");
            fab.hide();
        } else if (id == R.id.nav_setting) {
            if (settingFragment == null) {
                settingFragment = new SettingFragment();
            }
            changeHomeFragment(settingFragment);
            setToolBarTitle("设置");
            fab.hide();
        } else if (id == R.id.nav_about) {
            if (aboutFragment == null) {
                aboutFragment = new AboutFragment();
            }
            changeHomeFragment(aboutFragment);
            setToolBarTitle("关于");
            fab.hide();
        } else if (id == R.id.nav_exit) {
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolBarTitle(String title){
        toolbar.setTitle(title);
    }
}

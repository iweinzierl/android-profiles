package de.iweinzierl.easyprofiles;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toolbar;

import de.iweinzierl.easyprofiles.navigation.NavigationAdapter;
import de.iweinzierl.easyprofiles.navigation.NavigationClickListener;

public abstract class BaseActivity extends Activity {

    private ActionBarDrawerToggle toggle;

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        final Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar_top);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        final ListView navigationDrawer = (ListView) findViewById(R.id.navigation_drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.action_settings, R.string.action_settings);

        navigationDrawer.setAdapter(new NavigationAdapter(this));
        navigationDrawer.setOnItemClickListener(new NavigationClickListener(this));

        toolbarTop.setNavigationIcon(R.drawable.ic_menu_black_36dp);

        setActionBar(toolbarTop);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }
}

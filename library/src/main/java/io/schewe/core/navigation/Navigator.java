package io.schewe.core.navigation;

import android.app.Activity;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.LinkedList;
import java.util.List;

import io.schewe.core.util.Callback.CallbackWithParameters;

public class Navigator implements NavigationView.OnNavigationItemSelectedListener {

    private final Activity context;
    private DrawerLayout layout;
    private NavigationView navigationView;
    private static SparseArray<CallbackWithParameters> navigationAction = new SparseArray<>();
    private static List<CallbackWithParameters> callbacks     = new LinkedList<>();

    public Navigator(final Activity context, DrawerLayout drawer, Toolbar toolbar, NavigationView navigationView) {
        this.context = context;
        this.layout = drawer;
        this.navigationView = navigationView;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(context, layout, toolbar, io.schewe.core.R.string.navigation_drawer_open, io.schewe.core.R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);

        layout.addDrawerListener(toggle);
        toggle.syncState();
        this.navigationView.setNavigationItemSelectedListener(this);

        for(CallbackWithParameters callback: callbacks) callback.execute(this.context, this.navigationView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Navigator.navigationAction.get(id).execute(this.context);
        this.closeDrawer();
        return true;
    }

    public static void addNavigationAction(int id, CallbackWithParameters callback){ Navigator.navigationAction.put(id, callback); }

    public void setSelectedMenu(int selectedMenu) { navigationView.setCheckedItem(selectedMenu); }

    public void closeDrawer() { if (layout.isDrawerOpen(GravityCompat.START)) layout.closeDrawer(GravityCompat.START);}

    public static void addMenuInitialisationCallback(CallbackWithParameters callback){ callbacks.add(callback);}
}


package io.schewe.core;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import io.schewe.core.navigation.Navigator;
import io.schewe.core.permission.PermissionHandler;
import io.schewe.core.theme.ThemeManager;


public abstract class ADrawerBaseActivity extends AppCompatActivity {

    private Navigator navigator;
    protected PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setzte das Theme
        this.setTheme(ThemeManager.getThemeManager(this).getThemeID());
        // Setzte das Grundlayout
        setContentView(R.layout.standard_activity_layout);
        // Setzte das jeweilige spezifische Layout
        this.getLayoutInflater().inflate(getLayout(), findViewById(R.id.rootLayout), true);
        // Toolbar anzeigen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getToolbarTitle());

        DrawerLayout layout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        navigator = new Navigator(this, layout, toolbar, navView);

        permissionHandler = new PermissionHandler(this);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){ this.permissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults); }

    @Override protected void onStart() { super.onStart(); navigator.setSelectedMenu(getActualNavigationItem()); }

    @Override protected void onRestart() { super.onRestart(); navigator.setSelectedMenu(getActualNavigationItem()); }

    @Override protected void onResume() { super.onResume(); navigator.setSelectedMenu(getActualNavigationItem()); }

    @Override public void onBackPressed() { super.onBackPressed(); navigator.closeDrawer(); }

    // Id der Activity auf der man sich befindet
    abstract protected int getActualNavigationItem();

    // das spezifische Layout
    abstract protected int getLayout();

    // Der Title der in der Toolbar angezeigt wird
    abstract protected String getToolbarTitle();
}

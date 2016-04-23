package com.epfl.computational_photography.paletizer.SlideMenu;

/*
took most of the code from there
http://codetheory.in/android-navigation-drawer/
*/

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.epfl.computational_photography.paletizer.StyleActivity;
import com.epfl.computational_photography.paletizer.TransfertActivity;
import com.epfl.computational_photography.paletizer.MainActivity;
import com.epfl.computational_photography.paletizer.R;


import java.util.ArrayList;



/**
 * You need to add the following line in the activity declaration in AndroidManifest.xml
 *
 *      android:theme="@style/SlideMenuTheme"
 *
 * because 'setTheme(R.style.SlideMenuTheme);' seems not working.
 *
 */
public abstract class SlideMenuActivity extends AppCompatActivity {
    private static String TAG = SlideMenuActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    ListView slideMenuList;
    RelativeLayout slideMenu_frame;
    RelativeLayout content_frame;

    protected ArrayList<NavItem> slideMenuItems = new ArrayList(20);


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        super.setContentView(R.layout.activity_slidemenu);




        //home
        slideMenuItems.add(new NavItem(getString(R.string.home), getString(R.string.main),android.R.drawable.ic_menu_camera, MainActivity.class));
        slideMenuItems.add(new NavItem(getString(R.string.home), getString(R.string.main),android.R.drawable.ic_menu_camera, StyleActivity.class));
        slideMenuItems.add(new NavItem(getString(R.string.home), getString(R.string.main),android.R.drawable.ic_menu_camera, TransfertActivity.class));



        mTitle = mDrawerTitle = getTitle();//


        // main layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // slide menu layout
        slideMenu_frame = (RelativeLayout) findViewById(R.id.slideMenu_frame);
        // content layout
        content_frame = (RelativeLayout) findViewById(R.id.content_frame);
        // slide menu list container
        slideMenuList = (ListView) findViewById(R.id.slideMenu_items);



        //adapter between logical NavItems and graphical representation
        DrawerListAdapter adapter = new DrawerListAdapter(this, slideMenuItems);

        //items in the slide menu will use our adapter to be displayed
        slideMenuList.setAdapter(adapter);
        //create click listeners for each menu item_drink
        slideMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromList(position);
            }
        });

        // Show the menu icon on top of the screen
        // More info: http://codetheory.in/difference-between-setdisplayhomeasupenabled-sethomebuttonenabled-and-setdisplayshowhomeenabled/
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.openSlideMenu, R.string.closeSlideMenu) {
            /** Called when a drawer has settled in a completely closed state. */
            @Override
            public void onDrawerClosed(View view) {
                Log.d(TAG, "Slide menu closed");
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d(TAG, "Slide menu opened");
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    // slide menu actions

    /**
     * Handle item_drink click, depending on its position.
     * <p>
     *     If the item_drink has a linkedActivity, the activity will be launched.
     * </p>
     * <p>
     *     If the item_drink has an action, the action will be run.
     * </p>
     *
     * @param position
     */
    private void selectItemFromList(int position) {


        //close Menu
        mDrawerLayout.closeDrawer(slideMenu_frame);
        switch (position) {
            default:
                if (slideMenuItems.get(position).action != null){
                    slideMenuItems.get(position).action.run();
                }
                if (slideMenuItems.get(position).linkedActivity != null){
                    Intent newActivity = new Intent(getApplicationContext(), slideMenuItems.get(position).linkedActivity);
                    startActivity(newActivity);
                }
        }
    }



    /**
     * Set the activity content_frame (the main content view in the drawer) from a layout resource.  The resource will be
     * inflated, adding all top-level views to the activity.
     *
     * @param layoutResID Resource ID to be inflated.
     *
     * @see #setContentView(View)
     * @see #setContentView(View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        content_frame.addView(getLayoutInflater().inflate(layoutResID, null));
    }

    // force display of the 3 bar icon on the Action Bar, the "logo" widely used for Menu
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle other action bar items...
        return super.onOptionsItemSelected(item);
    }

    // Called when invalidateOptionsMenu() is invoked
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(slideMenu_frame);
        // If the nav drawer is open, hide action items related to the content view
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        */
        return super.onPrepareOptionsMenu(menu);
    }
}


package hr.tvz.quiz.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import hr.tvz.quiz.AdminReportedQuestionsActivity;
import hr.tvz.quiz.EditProfileActivity;
import hr.tvz.quiz.LoginActivity;
import hr.tvz.quiz.MainActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.SignUpActivity;
import hr.tvz.quiz.UserLocalStore;
import hr.tvz.quiz.model.User;

public class Drawer {

    //Drawer
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private Context c;
    private Activity activity;

    private UserLocalStore userLocalStore;
    private User user;


    public Drawer(ListView mDrawerList, DrawerLayout mDrawerLayout,  Context c) {

        this.mDrawerList = mDrawerList;
        this.mDrawerLayout = mDrawerLayout;
        this.c = c;
        this.activity = (Activity) c;
        initializeDrawer();
    }

    private void initializeDrawer(){
        userLocalStore = new UserLocalStore(c);
        user = userLocalStore.getLoggedInUser();
        String[] items;
        if (user == null) {
            items = c.getResources().getStringArray(R.array.nav_drawer_items_guest);
        } else if(user.getRoleId() == 1){
            items = c.getResources().getStringArray(R.array.nav_drawer_items_admin);
        } else {
            items = c.getResources().getStringArray(R.array.nav_drawer_items_user);
        }

        mAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, items);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activityName = activity.getComponentName().getShortClassName().substring(1);
                String selected = parent.getItemAtPosition(position).toString();
                Intent intent;

                switch (selected) {
                    case "Početna":
                        intent = new Intent(c, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        getmDrawerLayout().closeDrawers();

                        //activity.finish();
                        break;
                    case "Profil":
                        intent = new Intent(c, EditProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        getmDrawerLayout().closeDrawers();
                        break;
                    case "Postavke":
                        /*intent = new Intent(c, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        activity.finish();
                        break;*/
                    case "Dodaj pitanje":
                        /*intent = new Intent(c, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        activity.finish();
                        break;*/
                    case "Odjava":
                        userLocalStore.setUserLoggedIn(false);
                        userLocalStore.clearUserData();
                        intent = new Intent(c, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(intent);
                        activity.finish();
                        break;
                    case "Korisnici":
                        /*intent = new Intent(c, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        activity.finish();
                        break;*/
                    case "Verifikacija pitanja":
                        /*intent = new Intent(c, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        activity.finish();
                        break;*/
                    case "Prijave":
                        intent = new Intent(c, AdminReportedQuestionsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        activity.finish();
                        break;
                    case "Prijava":
                        intent = new Intent(c, SignUpActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        c.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout,  R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                activity.invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    public ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }
}
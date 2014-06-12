package io.vinnie.servitetracker.android;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity implements DrawerAdapter.DrawerItemListener {

    private DrawerAdapter drawerAdapter = null;
    private ArrayList<Fragment> drawerFragments = new ArrayList<Fragment>();
    private DrawerLayout drawerLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();

        getFragmentManager().beginTransaction()
                .add(R.id.content_frame, drawerFragments.get(0))
                .commit();

        setTitle(((TitleProvider) drawerFragments.get(0)).getTitle(this));
    }

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawer = (ListView) findViewById(R.id.drawer);

        String[] classes = getResources().getStringArray(R.array.drawer_fragment_classes);

        String fragmentPackage = ((Object) this).getClass().getPackage().getName() + ".fragments.";

        for (int i = 0; i < classes.length; i++) {
            try {
                Class clz = Class.forName(fragmentPackage + classes[i]);
                drawerFragments.add((Fragment) clz.newInstance());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String[] drawerTitles = new String[drawerFragments.size()];
        for (int i = 0; i < drawerFragments.size(); i++) {
            if (drawerFragments.get(i) instanceof TitleProvider) {
                drawerTitles[i] = ((TitleProvider) drawerFragments.get(i)).getTitle(this);
            }
        }
        drawerAdapter = new DrawerAdapter(this, drawerTitles);
        drawerAdapter.addListener(this);
        drawer.setAdapter(drawerAdapter);
        drawer.setOnItemClickListener(drawerAdapter);
    }

    @Override
    public void onDrawerItemSelected(int position) {
        Fragment fragment = drawerFragments.get(position);
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

        setTitle(((TitleProvider) fragment).getTitle(this));

        drawerLayout.closeDrawer(GravityCompat.START);
    }
}

package net.uraganov.rubric.movie.collection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieCollection;
import net.uraganov.rubric.navigation.activity.FragmentDrawer;
import net.uraganov.rubric.utils.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieCollectionActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    @Bind(R.id.toolbar)
    public Toolbar mToolbar;

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position, true);
    }

    /**
     * @param position
     * @param reset    Reset collection position
     */
    private void displayView(int position, boolean reset) {
        Fragment fragment;
        String title;
        String collectionType;


        switch (position) {
            case 0:
                collectionType = MovieCollection.Types.POPULAR.name();
                title = getString(R.string.title_most_popular);
                break;
            case 1:
                collectionType = MovieCollection.Types.HIGHRATED.name();
                title = getString(R.string.title_highest_rated);
                break;
            case 2:
            default:
                collectionType =  MovieCollection.Types.FAVORITE.name();
                title = getString(R.string.title_favorite);
                break;
        }

        SharedPreferences collectionStatePref = getSharedPreferences(getString(R.string.pref_collection_state), Context.MODE_PRIVATE);

        collectionStatePref.edit().putString(getString(R.string.pref_collection_state_type), collectionType).apply();
        if (reset) {
            collectionStatePref.edit().putInt(getString(R.string.pref_collection_state_position), 0).apply();
        }

        fragment = MovieCollectionFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        // set the toolbar title
        getSupportActionBar().setTitle(title);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(Utilities.getMovieCollectionDrawerPosition(this), false);
    }
}

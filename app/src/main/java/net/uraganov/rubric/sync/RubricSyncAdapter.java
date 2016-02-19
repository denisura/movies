package net.uraganov.rubric.sync;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import net.uraganov.rubric.R;
import net.uraganov.rubric.model.MovieCollection;
import net.uraganov.rubric.network.MovieDbAPI;

public class RubricSyncAdapter extends AbstractThreadedSyncAdapter {

    private final static String LOG_TAG = RubricSyncAdapter.class.getSimpleName();

    private final static String SYNC_EXTRAS_COLLECTION_TYPE = "collection_type";
    private final static String SYNC_EXTRAS_MOVIE_ID = "movie_id";

    public RubricSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.e(LOG_TAG,"onPerformSync started");

        long movieId = extras.getLong(SYNC_EXTRAS_MOVIE_ID);
        if (movieId >0){
            new MovieDbAPI(getContext()).fetchMovieTrailers(movieId);
            new MovieDbAPI(getContext()).fetchMovieReviews(movieId);
            return;
        }

        String collectionType = extras.getString(SYNC_EXTRAS_COLLECTION_TYPE);

        if (collectionType == null){
            Log.e(LOG_TAG,"onPerformSync collectionType is null");
            return;
        }

        if (collectionType.equals(MovieCollection.Types.POPULAR.name())) {
            Log.e(LOG_TAG,"onPerformSync popular");
            new MovieDbAPI(getContext()).fetchPopularMovieCollection();
            return;
        }
        if (collectionType.equals(MovieCollection.Types.HIGHRATED.name())) {
            Log.e(LOG_TAG,"onPerformSync highrated");
            new MovieDbAPI(getContext()).fetchHighRatedMovieCollection();
        }
        Log.e(LOG_TAG, "onPerformSync completed");
    }


    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Log.e(LOG_TAG, "syncImmediately");
        SharedPreferences collectionStatePref = context.getSharedPreferences(context.getString(R.string.pref_collection_state), Context.MODE_PRIVATE);
        String collectionType = collectionStatePref.getString(context.getString(R.string.pref_collection_state_type), MovieCollection.Types.POPULAR.name());

        if (!(collectionType.equals(MovieCollection.Types.POPULAR.name())
                ||collectionType.equals(MovieCollection.Types.HIGHRATED.name()))){
            //no need to trigger sync
            Log.e(LOG_TAG, "no need to trigger sync collectionType: "+collectionType);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putString(SYNC_EXTRAS_COLLECTION_TYPE, collectionType);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
        Log.e(LOG_TAG, "requestSync");
    }


    public static void syncNowMovieDetails(Context context, long movieId) {
        Log.e(LOG_TAG, "syncNowMovieDetails Movie ID:" + movieId);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putLong(SYNC_EXTRAS_MOVIE_ID, movieId);

        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
        Log.e(LOG_TAG, "requestSync");
    }


    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        }
        return newAccount;
    }
}
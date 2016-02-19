package net.uraganov.rubric.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;


/**
 *
 */
@Database(version = RubricDatabase.VERSION,
        packageName = "net.uraganov.rubric.provider")

public final class RubricDatabase {
    private RubricDatabase() {
        Log.e("denisura","test");
    }

    public static final int VERSION = 1;

    public static class Tables {

        @Table(MovieColumns.class)
        public static final String MOVIES = "movies";
        @Table(FavoriteColumns.class)
        public static final String FAVORITE = "favorite";
        @Table(TrailerColumns.class)
        public static final String TRAILERS = "trailers";
        @Table(ReviewColumns.class)
        public static final String REVIEW = "review";
    }


    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                            int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.MOVIES);
        onCreate(context, db);
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + Tables.MOVIES;

}

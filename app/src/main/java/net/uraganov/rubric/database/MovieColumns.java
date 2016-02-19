package net.uraganov.rubric.database;


import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface MovieColumns{


    @DataType(INTEGER)
    @PrimaryKey (onConflict = ConflictResolutionType.REPLACE)
    String MOVIE_ID = "movie_id";

    @DataType(INTEGER)
    @NotNull
    String COLUMN_ADULT = "adult";

    @DataType(TEXT)
    String COLUMN_BACKDROP_PATH = "backdrop_path";
    @DataType(TEXT)
    @NotNull
    String COLUMN_ORIGINAL_LANGUAGE = "original_language";
    @DataType(TEXT)
    @NotNull
    String COLUMN_ORIGINAL_TITLE = "original_title";

    @DataType(TEXT)
    String COLUMN_OVERVIEW = "overview";
    @DataType(TEXT)
    String COLUMN_RELEASE_DATE = "release_date";
    @DataType(TEXT)
    String COLUMN_POSTER_PATH = "poster_path";

    @DataType(REAL)
    String COLUMN_POPULARITY = "popularity";

    @DataType(TEXT)
    @NotNull
    String COLUMN_TITLE = "title";
    @DataType(INTEGER)
    @NotNull
    String COLUMN_VIDEO = "video";
    @DataType(REAL)
    @NotNull
    String COLUMN_VOTE_AVERAGE = "vote_average";
    @DataType(REAL)
    @NotNull
    String COLUMN_VOTE_COUNT = "vote_count";


    String COLUMN_IS_FAVORITE = "is_favorite";
}


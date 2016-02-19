package net.uraganov.rubric.database;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;

public interface FavoriteColumns {

    @DataType(INTEGER)
    @References(table = RubricDatabase.Tables.MOVIES, column = MovieColumns.MOVIE_ID)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String MOVIE_ID = "movie_id";
}

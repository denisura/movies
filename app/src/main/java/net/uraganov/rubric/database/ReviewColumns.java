package net.uraganov.rubric.database;

import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface ReviewColumns {


    @DataType(TEXT)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)

    String COLUMN_REVIEW_ID = "review_id";

    @DataType(INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";


    @DataType(TEXT)
    @NotNull
    String COLUMN_AUTHOR = "author";

    @DataType(TEXT)
    @NotNull
    String COLUMN_CONTENT = "content";
}

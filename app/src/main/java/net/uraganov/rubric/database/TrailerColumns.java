package net.uraganov.rubric.database;


import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface TrailerColumns {

    @DataType(INTEGER)
    @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(TEXT)
    @NotNull
    String COLUMN_NAME = "name";

    @DataType(TEXT)
    @NotNull
    String COLUMN_SIZE = "size";

    @DataType(TEXT)
    @NotNull
    String COLUMN_SOURCE = "source";

    @DataType(TEXT)
    @NotNull
    String COLUMN_TYPE = "type";

    @DataType(TEXT)
    @NotNull
    String COLUMN_PLAYER = "player";
}


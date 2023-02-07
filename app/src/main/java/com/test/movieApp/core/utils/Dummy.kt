package com.test.movieApp.core.utils

import com.test.movieApp.core.data.model.GenreModel
import com.test.movieApp.core.data.model.TmDbModel

fun getDummyMovie(): MutableList<TmDbModel> {
    return mutableListOf(
        TmDbModel(),
        TmDbModel(),
        TmDbModel(),
        TmDbModel(),
    )
}
fun getDummyGenres(): MutableList<GenreModel> {
    return mutableListOf(
        GenreModel(
            id = 1,
            name = "",
            message = "",
            isSelected = false
        ),
        GenreModel(
            id = 1,
            name = "",
            message = "",
            isSelected = false
        ),
        GenreModel(
            id = 1,
            name = "",
            message = "",
            isSelected = false
        ),
        GenreModel(
            id = 1,
            name = "",
            message = "",
            isSelected = false
        ),
        GenreModel(
            id = 1,
            name = "",
            message = "",
            isSelected = false
        )

    )
}
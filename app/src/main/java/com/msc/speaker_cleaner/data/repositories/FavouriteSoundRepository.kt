package com.msc.speaker_cleaner.data.repositories

import com.msc.speaker_cleaner.data.database.AppDatabase
import com.msc.speaker_cleaner.domain.layer.DetailsSound
import javax.inject.Inject

class FavouriteSoundRepository @Inject constructor(val appDatabase: AppDatabase) {
    fun insert(sound: DetailsSound) : Long {
        return appDatabase.favouriteDao().insert(sound)
    }

    fun getFavourite(soundRes : Int) : DetailsSound? {
        return appDatabase.favouriteDao().getFavourite(soundRes)
    }

    fun remove(sound: DetailsSound) : Int{
        return appDatabase.favouriteDao().remove(sound)
    }

    fun getList() : List<DetailsSound> {
        return appDatabase.favouriteDao().getAll()
    }
}
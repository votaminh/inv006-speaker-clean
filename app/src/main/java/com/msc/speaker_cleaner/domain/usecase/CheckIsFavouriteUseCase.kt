package com.msc.speaker_cleaner.domain.usecase

import com.msc.speaker_cleaner.data.repositories.FavouriteSoundRepository
import javax.inject.Inject

class CheckIsFavouriteUseCase @Inject constructor(private val favouriteSoundRepository: FavouriteSoundRepository)
    : UseCase<CheckIsFavouriteUseCase.Param, Boolean>() {
    class Param(val soundRes : Int) : UseCase.Param()

    override suspend fun execute(param: Param): Boolean {
        return favouriteSoundRepository.getFavourite(param.soundRes) != null
    }
}
package domain.repositories

import data.model.UserAvatar

interface UserAvatarsRepository {

    suspend fun uploadPhoto(userAvatar: UserAvatar)

    suspend fun deletePhoto(userId: Int)

    suspend fun getPhotoById(userId: Int): String?
}
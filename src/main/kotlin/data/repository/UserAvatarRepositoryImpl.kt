package data.repository

import com.example.plugins.DatabaseFactory.dbQuery
import data.model.UserAvatar
import domain.repositories.UserAvatarsRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import tables.UserAvatars

class UserAvatarRepositoryImpl : UserAvatarsRepository {
    override suspend fun uploadPhoto(userAvatar: UserAvatar) {
        return dbQuery {
            val exist = UserAvatars
                .select { UserAvatars.userId eq userAvatar.userId }
                .singleOrNull()

            if (exist == null) {
                UserAvatars.insert { table ->
                    table[userId] = userAvatar.userId
                    table[image] = userAvatar.image
                }
            } else {
                UserAvatars.update({ UserAvatars.userId eq userAvatar.userId }) { table ->
                    table[image] = userAvatar.image
                }
            }
        }
    }

    override suspend fun deletePhoto(userId: Int) {
        return dbQuery {
            UserAvatars
                .deleteWhere { UserAvatars.userId eq userId }
        }
    }

    override suspend fun getPhotoById(userId: Int): String? {
        return dbQuery {
            UserAvatars.select {
                UserAvatars.userId eq userId
            }.map {
                it.toImage()
            }.singleOrNull()
        }
    }
}

fun ResultRow.toImage(): String = this[UserAvatars.image]

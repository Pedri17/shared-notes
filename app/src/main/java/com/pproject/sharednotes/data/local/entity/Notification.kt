package com.pproject.sharednotes.data.local.entity

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.pproject.sharednotes.R

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true) val notificationId: Int = 0,
    val fromUser: String = "",
    val toUser: String = "",
    val noteId: Int = 0,
    var type: Type = Type.PARTICIPATE,
) {
    enum class Type {
        PARTICIPATE,
        DISPARTICIPATE,
    }
}

data class NotificationWithNote(
    @Embedded val notification: Notification,
    @Relation(
        parentColumn = "notificationId",
        entityColumn = "noteId"
    )
    val note: Note
) {
    fun toMessage(context: Context): String {
        return when (notification.type) {
            Notification.Type.PARTICIPATE ->
                context.getString(
                    R.string.do_you_want_to_collaborate_with_the_note_from,
                    note.title,
                    notification.fromUser
                )

            Notification.Type.DISPARTICIPATE ->
                context.getString(
                    R.string.do_you_want_to_stop_collaborating_to_the_editing_of_the_note_from,
                    note.title,
                    notification.fromUser
                )
        }
    }
}
package com.pproject.sharednotes.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pproject.sharednotes.data.local.dao.FolderDao
import com.pproject.sharednotes.data.local.dao.NoteDao
import com.pproject.sharednotes.data.local.dao.NotificationDao
import com.pproject.sharednotes.data.local.dao.PreferencesDao
import com.pproject.sharednotes.data.local.dao.UserDao
import com.pproject.sharednotes.data.local.entity.Folder
import com.pproject.sharednotes.data.local.entity.Note
import com.pproject.sharednotes.data.local.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.local.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.local.entity.Notification
import com.pproject.sharednotes.data.local.entity.Preferences
import com.pproject.sharednotes.data.local.entity.User


@Database(
    entities = [
        User::class,
        Note::class,
        Folder::class,
        Preferences::class,
        Notification::class,
        NoteUserCrossRef::class,
        FolderNoteCrossRef::class,
    ],
    version = 23
)
abstract class SharedNotesDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: SharedNotesDatabase? = null

        fun getDatabase(context: Context): SharedNotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SharedNotesDatabase::class.java,
                    "sharednotes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

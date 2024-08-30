package com.pproject.sharednotes.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pproject.sharednotes.data.db.dao.FolderDao
import com.pproject.sharednotes.data.db.dao.NoteDao
import com.pproject.sharednotes.data.db.dao.NotificationDao
import com.pproject.sharednotes.data.db.dao.PreferencesDao
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.db.entity.Preferences
import com.pproject.sharednotes.data.db.entity.User


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
    version = 21
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

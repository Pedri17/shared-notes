package com.pproject.sharednotes.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.pproject.sharednotes.data.db.dao.FolderDao
import com.pproject.sharednotes.data.db.dao.NoteDao
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderPinnedNoteCrossRef
import com.pproject.sharednotes.data.db.entity.User


@Database(
    entities = [
        User::class,
        Note::class,
        NoteUserCrossRef::class,
        Folder::class,
        FolderNoteCrossRef::class,
        FolderPinnedNoteCrossRef::class,
    ],
    version = 9
)
abstract class SharedNotesDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao

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

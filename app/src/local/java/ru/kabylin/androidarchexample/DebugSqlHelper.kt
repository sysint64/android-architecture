package ru.kabylin.androidarchexample

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Хранилище данных для debug версии.
 */
class DebugSqlHelper(context: Context) : ManagedSQLiteOpenHelper(context, "debug_storage_db") {
    companion object {
        private var instance: DebugSqlHelper? = null

        @Synchronized
        fun getInstance(context: Context): DebugSqlHelper {
            if (instance == null) {
                instance = DebugSqlHelper(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.transaction {
            db.createTable("User", true,
                "id" to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                "phone" to TEXT + UNIQUE,
                "password" to TEXT,
                "accessToken" to TEXT,
                "refreshToken" to TEXT,
                "firstName" to TEXT,
                "lastName" to TEXT,
                "patronymic" to TEXT,
                "country" to TEXT,
                "city" to TEXT,
                "gender" to TEXT,
                "birthDate" to TEXT,
                "bonuses" to INTEGER,
                "personalDocument" to TEXT,
                "documentNumber" to TEXT,
                "documentSeries" to TEXT,
                "documentCountry" to TEXT)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}

val Context.database: DebugSqlHelper
    get() = DebugSqlHelper.getInstance(applicationContext)

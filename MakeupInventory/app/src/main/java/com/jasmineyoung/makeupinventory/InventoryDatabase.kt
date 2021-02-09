package com.jasmineyoung.makeupinventory

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.jasmineyoung.makeupinventory.InventoryContract.InventoryEntry
import java.lang.Exception

object InventoryContract {
    object InventoryEntry : BaseColumns {
        const val TABLE_NAME = "InventoryTable"
        const val ITEM_NAME = "ItemName"
        const val ITEM_TYPE = "ItemType"
        const val ITEM_BRAND = "ItemBrand"
        const val ITEM_SHADE = "ItemShade"
        const val ITEM_DATE = "ItemDateEntered"
        const val ITEM_EXPIRY = "ItemDateExpiry"
    }
}

private const val CREATE_TABLE = "CREATE TABLE ${InventoryEntry.TABLE_NAME}(" +
        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        "${InventoryEntry.ITEM_NAME} TEXT NOT NULL," +
        "${InventoryEntry.ITEM_TYPE} TEXT NOT NULL," +
        "${InventoryEntry.ITEM_BRAND} TEXT NOT NULL," +
        "${InventoryEntry.ITEM_SHADE} TEXT," +
        "${InventoryEntry.ITEM_DATE} TEXT NOT NULL," +
        "${InventoryEntry.ITEM_EXPIRY} INTEGER)"

private const val DROP_TABLE = "DROP TABLE IF EXISTS ${InventoryEntry.TABLE_NAME}"

class InventoryDatabase(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Inventory.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addItem(db: SQLiteDatabase, values: ContentValues):Long {

        val newRowID = db.insert(InventoryEntry.TABLE_NAME, null, values)
        Log.d("BSU", "Added item with id: $newRowID")
        return newRowID
    }

    fun clearDB(db:SQLiteDatabase) {
        db.execSQL(DROP_TABLE)
    }

    fun updateItem(db: SQLiteDatabase, values: ContentValues, itemID: Int) {
        Log.d("BSU", "Update inventory with id: $itemID")
        db.update(
            InventoryEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID}=?",
            arrayOf(itemID.toString())
        )
    }

    fun deleteItem(db:SQLiteDatabase, itemID:Int)
    {
        db.delete(InventoryEntry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf(itemID.toString()))
    }

    fun getAllItems(db: SQLiteDatabase, sort: Sorting): Cursor? {
        var cursor: Cursor? = null
        try {
            if(sort == Sorting.ORDER) {
                cursor = db.query(
                    InventoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            }
            else if(sort == Sorting.ALPHABET)
            {
                val sortOrder = "${InventoryEntry.ITEM_NAME} ASC"
                cursor = db.query(
                    InventoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    sortOrder,
                    null
                )
            }
            else if(sort == Sorting.TYPE)
            {
                val sortOrder = "${InventoryEntry.ITEM_TYPE} ASC"
                cursor = db.query(
                    InventoryEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    sortOrder,
                    null
                )
            }
        } catch (ex: Exception) {
            Log.d("BSU", "Exception getting all items" + ex.message)
        }
        return cursor
    }


}
package com.jasmineyoung.makeupinventory

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.jasmineyoung.makeupinventory.InventoryContract.InventoryEntry
import com.jasmineyoung.makeupinventory.models.ItemModel

enum class Sorting {ALPHABET, ORDER, TYPE}

object ItemManager {
    private val mItemsList = mutableListOf<ItemModel>()
    private var sort = Sorting.ORDER

    fun sortAlphabetical(){
        sort = Sorting.ALPHABET
    }
    fun sortOrder(){
        sort = Sorting.ORDER
    }
    fun sortType(){
        sort = Sorting.TYPE
    }

    fun itemsList(context: Context): List<ItemModel> {
        val dbHelper = InventoryDatabase(context)
        val db = dbHelper.readableDatabase

        val cursor = dbHelper.getAllItems(db, sort)
        if (cursor != null) {
            mItemsList.clear()
            while (cursor.moveToNext()) {
                val item = ItemModel(
                    cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)),
                    cursor.getString(cursor.getColumnIndex(InventoryEntry.ITEM_NAME)),
                    cursor.getString(cursor.getColumnIndex(InventoryEntry.ITEM_TYPE)),
                    cursor.getString(cursor.getColumnIndex(InventoryEntry.ITEM_BRAND)),
                    cursor.getString(cursor.getColumnIndex(InventoryEntry.ITEM_SHADE)),
                    cursor.getInt(cursor.getColumnIndex(InventoryEntry.ITEM_EXPIRY)),
                    cursor.getString(cursor.getColumnIndex(InventoryEntry.ITEM_DATE))
                )
                mItemsList.add(item)
            }
        }

        return mItemsList
    }

    fun addItem(context: Context, newItem: ItemModel) {
        val dbHelper = InventoryDatabase(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(InventoryEntry.ITEM_NAME, newItem.name)
            put(InventoryEntry.ITEM_TYPE, newItem.type)
            put(InventoryEntry.ITEM_BRAND, newItem.brand)
            put(InventoryEntry.ITEM_SHADE, newItem.shade)
            put(InventoryEntry.ITEM_DATE, newItem.dateEntered)
            put(InventoryEntry.ITEM_EXPIRY, newItem.expires)
        }

        dbHelper.addItem(db, values)
        itemsList(context)
        //newItem.id = id.toInt()
        //mItemsList.add(newItem)
    }

    fun updateItem(context: Context, itemToUpdate: ItemModel) {
        val dbHelper = InventoryDatabase(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(InventoryEntry.ITEM_NAME, itemToUpdate.name)
            put(InventoryEntry.ITEM_TYPE, itemToUpdate.type)
            put(InventoryEntry.ITEM_BRAND, itemToUpdate.brand)
            put(InventoryEntry.ITEM_SHADE, itemToUpdate.shade)
            put(InventoryEntry.ITEM_DATE, itemToUpdate.dateEntered)
            put(InventoryEntry.ITEM_EXPIRY, itemToUpdate.expires)
        }

        dbHelper.updateItem(db, values, itemToUpdate.id)
        itemsList(context)
    }

    fun removeItem(context: Context, itemToRemove: ItemModel) {
        val dbHelper = InventoryDatabase(context)
        val db = dbHelper.writableDatabase

        dbHelper.deleteItem(db, itemToRemove.id)
        mItemsList.remove(itemToRemove)
    }

    fun clearDB(context: Context){
        val dbHelper = InventoryDatabase(context)
        val db = dbHelper.writableDatabase

        dbHelper.clearDB(db)
    }
}
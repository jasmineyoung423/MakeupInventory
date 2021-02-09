package com.jasmineyoung.makeupinventory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jasmineyoung.makeupinventory.models.ItemModel
import com.jasmineyoung.makeupinventory.ui.itemmodelcrud.ItemModelCrudFragment

class ItemModelCrudActivity : AppCompatActivity() {

    var item:ItemModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_model_crud_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ItemModelCrudFragment.newInstance())
                .commitNow()
            val bundleExtra = intent.getSerializableExtra("itemID")
            bundleExtra?.let{
                item = it as ItemModel
            }
        }
    }
}

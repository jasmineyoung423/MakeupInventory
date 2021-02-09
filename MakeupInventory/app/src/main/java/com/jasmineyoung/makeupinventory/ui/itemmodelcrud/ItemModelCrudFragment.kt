package com.jasmineyoung.makeupinventory.ui.itemmodelcrud

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.jasmineyoung.makeupinventory.ItemManager
import com.jasmineyoung.makeupinventory.ItemModelCrudActivity
import com.jasmineyoung.makeupinventory.R
import com.jasmineyoung.makeupinventory.models.ItemModel
import kotlinx.android.synthetic.main.item_model_crud_fragment.*
import kotlinx.android.synthetic.main.item_model_crud_fragment.view.*
import java.util.*

class ItemModelCrudFragment : Fragment() {

    companion object {
        fun newInstance() = ItemModelCrudFragment()
    }

    private lateinit var viewModel: ItemModelCrudViewModel
    private var item: ItemModel? = null
    private var compareItem:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.item_model_crud_fragment, container, false)
        item = (activity as ItemModelCrudActivity).item

        item?.let {
            view.itemNameET.setText(it.name)
            view.brandNameET.setText(it.brand)
            compareItem = it.type
            view.shadeET.setText(it.shade)
            view.expireNumET.setText(it.expires.toString())
            view.dateTV.text = it.dateEntered
            view.deleteButton.visibility = View.VISIBLE
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ItemModelCrudViewModel::class.java)
        // TODO: Use the ViewModel

        val context: Context = this.context!!
        ArrayAdapter.createFromResource(
            context,
            R.array.itemTypeArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            itemTypeSpinner.adapter = adapter
            if (compareItem != null) {
                val spinnerPosition = adapter.getPosition(compareItem)
                itemTypeSpinner.setSelection(spinnerPosition)
            }
        }

        addItemButton.setOnClickListener {
            val filled: Boolean = checkForFill()
            if (filled) {
                    addItemToRepo()
            } else {
                Toast.makeText(context, getString(R.string.fillForm), Toast.LENGTH_SHORT).show()
            }
        }
        cancelButton.setOnClickListener {
            requireActivity().finish()
        }
        deleteButton.setOnClickListener {
            if (item != null) {
                ItemManager.removeItem(context, item!!)
                requireActivity().finish()
            }
        }
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dateBtn.setOnClickListener {
            val datePicker = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    dateTV.text = resources.getString(R.string.dateFormat, mMonth, mDay, mYear)
                },
                year,
                month,
                day
            )
            datePicker.show()
        }
    }

    private fun addItemToRepo() {
        if (item == null) {
            val newItem = ItemModel()

            newItem.name = itemNameET.text.toString()
            newItem.type = itemTypeSpinner.selectedItem.toString()
            newItem.brand = brandNameET.text.toString()
            if (!shadeET.text.isBlank()) {
                newItem.shade = shadeET.text.toString()
            }
            if(!expireNumET.text.isBlank()) {
                newItem.expires = expireNumET.text.toString().toInt()
            }
            newItem.dateEntered = dateTV.text.toString()
            ItemManager.addItem(this.context!!, newItem)
        } else {
            item?.name = itemNameET.text.toString()
            item?.type = itemTypeSpinner.selectedItem.toString()
            item?.brand = brandNameET.text.toString()
            if (!shadeET.text.isBlank()) {
                item?.shade = shadeET.text.toString()
            } else {
                item?.shade = "No Shade"
            }
            if(!expireNumET.text.isBlank()) {
                item?.expires = expireNumET.text.toString().toInt()
            } else {
                item?.expires = 0
            }
            item?.dateEntered = dateTV.text.toString()
            ItemManager.updateItem(this.context!!, item as ItemModel)
        }

        requireActivity().finish()
    }


    private fun checkForFill(): Boolean {
        var filled = false
        if (!itemNameET.text.isBlank() && !brandNameET.text.isBlank() && !dateTV.text.isBlank()) {
            filled = true
        }
        return filled
    }

}

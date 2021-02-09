package com.jasmineyoung.makeupinventory.models

import java.io.Serializable

data class ItemModel(
    var id: Int = 0,
    var name: String = "",
    var type: String = "",
    var brand: String = "",
    var shade: String = "No Shade",
    var expires: Int = 0,
    var dateEntered: String = ""
):Serializable
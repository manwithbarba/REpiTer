package com.repit.data.model

import com.google.gson.annotations.SerializedName

data class FormConfig(
    @SerializedName("form_name") val formName: String,
    @SerializedName("fields") val fields: List<FormField>
)

data class FormField(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("type") val type: String, // "text", "number", "boolean", "multiselect", "gps"
    @SerializedName("required") val required: Boolean = false,
    @SerializedName("options") val options: List<String>? = null,
    @SerializedName("auto_capture") val autoCapture: Boolean = false
)

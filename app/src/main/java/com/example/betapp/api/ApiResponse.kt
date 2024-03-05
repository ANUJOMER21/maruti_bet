package com.example.betapp.api

import com.google.gson.JsonObject
import org.json.JSONObject

interface ApiResponse {
      fun onSuccess(jsonObject: JsonObject)
      fun onFailure(failure:String)
}
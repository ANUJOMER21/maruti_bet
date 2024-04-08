package com.example.betapp.api

import com.google.gson.JsonObject

interface ApiResponse {
      fun onSuccess(jsonObject: JsonObject)
      fun onFailure(failure:String)
}
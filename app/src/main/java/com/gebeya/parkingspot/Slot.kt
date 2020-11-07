package com.gebeya.parkingspot

import java.io.Serializable

//what is returned for the "main/vehicle/getavailable" end point
data class Slot(
    val _id:String,
    val status:StatuS,
    val stack:String,
    val created_at:String
    ):Serializable


data class StatuS(
    val statusName:List<String>,
    val _id:String
)

data class PoResponse(val _id: String,
                      val status: StatuS,
                      val occupied_by:String,
                      val stack:String)


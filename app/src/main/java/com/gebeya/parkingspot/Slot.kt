package com.gebeya.parkingspot

import java.io.Serializable

//what is returned for the "main/vehicle/getavailable" end point
data class Slot(
    val _id:String,
    val status:StatuS,
    val stack:String,
    val created_at:String,
    val description:String
    ):Serializable


data class StatuS(
    val statusName:List<String>,
    val _id:String
)

data class PoResponse(val _id: String,
                      val plate_number:String,
                      val slot_id:String,
                      val ticket_status:String,
                      val park_at:String,
                      val exit_at:String,
                      val price_per_hour:Int,
                      val total_price:Double):Serializable

data class Stack(val floor:Double,
                 val _id:String,
                 val company:String)




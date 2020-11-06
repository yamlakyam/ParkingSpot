package com.gebeya.parkingspot

data class Slot(
    val _id:String,
    val status:StatuS,
    val stack:String
    )

data class StatuS(
    val statusName:List<String>,
    val _id:String
)

data class PoResponse(val _id: String,
                      val status: StatuS,
                      val occupied_by:String,
                      val stack:String)


package com.gebeya.parkingspot

data class Slot(
    val status:List<StatuS>,
    val _id:String,
    val stack:String
    )

data class StatuS(
    val statusName:List<String>,
    val _id:String
)
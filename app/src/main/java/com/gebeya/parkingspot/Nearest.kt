package com.gebeya.parkingspot

data class Nearest (val location: Location,
                    val company: String,
                    val floor:Int,
                    val full_status:Boolean,
                    val _id:String,
                    val parking_slots:Int,
                    val parking_lot_rank:Int,
                    val price:Int)
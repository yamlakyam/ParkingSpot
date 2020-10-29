package com.gebeya.parkingspot

 data class NearestDetail(
     val __v: Int,
     val _id: String,
     val company: List<String>,
     val created_at: String,
     val floor: Int,
     val full_status: Boolean,
     val location: Location,
     val parking_lot_rank: Int,
     val parking_slots: Int,
     val slots: List<String>,
     val updatedAt: String
 )

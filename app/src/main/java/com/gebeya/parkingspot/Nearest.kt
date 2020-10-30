package com.gebeya.parkingspot

data class Nearest (val location: Location,
                    val company: ArrayList<String>,
                    val floor:Int,
                    val full_status:Boolean,
                    val slots:ArrayList<Any>
)



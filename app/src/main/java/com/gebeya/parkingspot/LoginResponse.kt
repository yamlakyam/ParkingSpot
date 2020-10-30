package com.gebeya.parkingspot

data class LoginResponse (var token:String, var roles:List<String>, var email:String, var password:String, var name:String)
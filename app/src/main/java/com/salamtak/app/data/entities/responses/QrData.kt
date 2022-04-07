package com.salamtak.app.data.entities.responses

import com.salamtak.app.data.entities.Service

data class QrData(
    val id: String,
    val isUsed: Boolean,
    val discountCode: String,
    val qrImageUrl: String,
    val expireAt: String,
    val service: Service,
    val isOld: Boolean


//"service":{"id":"7dcaf7ed-54b0-49bf-96fc-94abea34be75","discountAmount":10.0,"serviceFees":100.0,"service":null}}

)

//    "isUsed":false,"discountCode":"uAuaoXYZZE","qrImageUrl":"/api/v1/discount/code/qrCode?code=uAuaoXYZZE&key=",
//"expireAt":"2020-06-28T14:34:41.8419555Z",
//"service":{"id":"7dcaf7ed-54b0-49bf-96fc-94abea34be75","discountAmount":10.0,"serviceFees":100.0,"service":null}},"status":true}
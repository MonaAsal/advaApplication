package com.salamtak.app.data.entities

data class PreStep2Data(
    var stepTwo: Step2,
    var attachments: NewAttachment,
    var cars: List<IdNameObject>,
    var clubs: List<IdNameObject>
)
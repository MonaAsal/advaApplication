package com.salamtak.app.data.enums

import com.salamtak.app.App
import com.salamtak.app.R
import com.salamtak.app.utils.Constants

enum class FileType(val typeId: Int) {
    Image(1),
    PDF(2),
    Document(3),
    Audio(4),
    Other(5)
}

enum class ProviderType(val typeId: Int) {
    Doctor(1),
    MedicalProvider(2),
    Insurance(3),
    GYM(4),
    Wedding(5),
    Others(6),
    Car(7),
    Finishing(8)
}

enum class OperationsFrom(val value: Int) {
    Subcategories(1),
    MedicalProvider(2),
    Doctor(3)
}
enum class EducationTypes(val value: Int) {
    School(14),
    University(15),
    Institute(16),
    Others(17),
    COURSES(32)
}


enum class InstallmentTypes(val typeId: Int) {
    OPERATION(1),
    EDUCATION(2),
    INSURANCE(3),
    WEDDING(4),
    CARS(5),
    FINISHING(6)

}

enum class MainCategories(val typeId: Int) {
    /*OPERATION(1),
    EDUCATION(2),
    INSURANCE(3),
    GYM(4),
   // WEDDING(5),
    Others(6),
    CARS(5),
    FINISHING(8)*/

    HEALTH(1),
    EDUCATION(2),
    INSURANCE(3),
    GYM(4),
    WEDDING(5),
    Others(6),
    CARS(7),
    FINISHING(8),
    TRAVEL(9),
    BILLS(10)
}


enum class ContactsType(val id: Int) {
    Facebook(1), Instagram(2), Website(3), Email(4), Phone(5), Mobile(6)
}

enum class FinishingBundleType(val id: Int) {
    Package(0),
    Product(1),
    None(2)
}

enum class PricingType(val id: Int) {
    PerMeter(0),
    FullPrice(1)
}
enum class CartType(val id: Int) {
    HEALTH(1),
    EDUCATION(2),
    INSURANCE(3),
    WEDDING(4),
    CARS(5),
    FINISHING(6)
}
enum class EducationProviderType(val id:Int)
{
    SCHOOL(3),
    COURSE(4)
}


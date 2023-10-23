package example.model

import scala.util.Try

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class Customer(
                     _id: String,
                     name: String,
                     age: Int,
                     email: String,
                     phoneNumber: String,
                     phoneWork: Option[String]
                   )

object Customer {
  def apply(
             name: String,
             age: Int,
             email: String,
             phoneNumber: String,
             workNumber: Option[String]
           ): Customer = Customer(java.util.UUID.randomUUID.toString, name, age, email, phoneNumber, workNumber)

  object FieldNames {
    def name        = "name"
    def age         = "age"
    def id          = "_id"
    def email       = "email"
    def phoneWork   = "phoneWork"
    def phoneNumber = "phoneNumber"
  }


  implicit val customerEncoder: JsonEncoder[Customer] = DeriveJsonEncoder.gen[Customer]
}
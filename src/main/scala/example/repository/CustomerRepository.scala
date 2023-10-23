package example.repository

import zio.ZIO
import zio.dynamodb.DynamoDBQuery.putItem
import zio.dynamodb.{DynamoDBExecutor, Item}
import zio.http.Response
import example.model.Customer

object CustomerRepository {
  def createCustomer(customer: Customer): ZIO[DynamoDBExecutor, Throwable, Response] = {
    val studentLogic: ZIO[DynamoDBExecutor, Throwable, Unit] = for {
      _ <- putItem("customer", Item(
        "id" -> customer._id,
        "name" -> customer.name,
        "age" -> customer.age,
      "phone" -> customer.phoneNumber,
      "email" -> customer.email,
        "phoneWork" -> customer.phoneWork)).execute
    } yield ()

    studentLogic.map(_ => Response.text("Customer created successfully"))
  }

}

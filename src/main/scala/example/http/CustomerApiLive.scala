package example.http

import example.model.CustomerBody
import zio._
import zio.dynamodb.DynamoDBExecutor
import zio.http._
import zio.http.{Http, Method, Request, Response}
import zio.dynamodb.DynamoDBQuery.{put, putItem, succeed}
import zio.dynamodb.{DynamoDBExecutor, Item, PrimaryKey, TestDynamoDBExecutor}
import example.model.Customer
import zio.http.Status.{BadRequest, Created}
import zio.json._
import example.model.CustomerBody._
import example.repository.CustomerRepository

// Create a concrete implementation of CustomerApi
case class CustomerApiLive(dynamoDbExecutorLayer: ZLayer[Any, Throwable, DynamoDBExecutor]) extends CustomerApi {
  override def httpApp: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> Root / "status" => ZIO.succeed(Response.text("Server Running: CustomerApiLive Active"))
    case Method.GET -> Root / "name" / "myname" => ZIO.succeed(Response.text("Banana"))
    case Method.POST -> Root / "students" =>
      createStudent()
        .provide(dynamoDbExecutorLayer)
        .catchAll(err => ZIO.succeed(Response.text(s"Error: ${err.getMessage}")))
    case req @ Method.POST -> Root / "customer" =>
      req.body.asString.map(_.fromJson[CustomerBody]).flatMap {
        case Left(e) =>
          ZIO
            .logErrorCause(s"Failed to parse the input: $e", Cause.fail(e))
            .as(
              Response.status(BadRequest),
            )
        case Right(customerBody) =>
          CustomerRepository.createCustomer(Customer(
              customerBody.name,
              customerBody.age,
              customerBody.email,
              customerBody.phoneNumber,
              customerBody.phoneWork
            ))
            .provide(dynamoDbExecutorLayer)
            .catchAll(err => ZIO.succeed(Response.text(s"Error: ${err.getMessage}")))
      }

}

  //  def createStudent(): ZIO[DynamoDBExecutor, Response, Response] =
  //    (for {
  //      _ <- batchWriteFromStream(ZStream(avi, adam)) { student =>
  //        put("student", student)
  //      }.runDrain
  //      _ <- put("student", avi.copy(payment = Payment.CreditCard)).execute
  //      _ <- batchReadFromStream("student", ZStream(avi, adam))(s => primaryKey(s.email, s.subject))
  //        .tap(errorOrStudent => Console.printLine(s"student=$errorOrStudent"))
  //        .runDrain
  //    } yield Response.text("Student created successfully"))
  //      .catchAll(error => ZIO.succeed(Response.text(s"Error creating student: ${error.getMessage}")))


  //

  override def createStudent(): ZIO[DynamoDBExecutor, Throwable, Response] = {
    val studentLogic: ZIO[DynamoDBExecutor, Throwable, Unit] = for {
      _ <- (putItem("table1", Item("id" -> 5, "name" -> "name1")) zip putItem(
        "table1",
        Item("id" -> 6, "name" -> "name2")
      )).execute
    } yield ()

    studentLogic.map(_ => Response.text("Students created successfully"))
}
}
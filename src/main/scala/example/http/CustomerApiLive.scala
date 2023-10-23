package example.http

import zio._
import zio.dynamodb.DynamoDBExecutor
import zio.http._
import zio.http.{Http, Method, Request, Response}
import zio.dynamodb.{DynamoDBExecutor, batchWriteFromStream}
import zio.dynamodb.DynamoDBQuery.put
import zio.dynamodb._
import example.dynamodblocal.DynamoDB._
import example.model.Student._
import example.model._
import zio.stream.ZStream
import zio.{ Console, ZIOAppDefault }


// Create a concrete implementation of CustomerApi
case class CustomerApiLive() extends CustomerApi {
  override def httpApp: Http[Any, Nothing, Request, Response] = Http.collect[Request] {
    case Method.GET -> !! / "status" => Response.text("Server Running: CustomerApiLive Active")
    case Method.GET -> !! / "name" / "myname" => Response.text("Banana")
    case Method.POST -> !! / "students" =>
      createStudent()
      Response.text("Banana")
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


  def createStudent(): ZIO[DynamoDBExecutor with Console, Throwable, Response] = {
    val studentLogic = for {
      _ <- batchWriteFromStream(ZStream(avi, adam)) { student =>
        put("student", student)
      }.runDrain

      _ <- put("student", avi.copy(payment = Payment.CreditCard)).execute

      _ <- batchReadFromStream("student", ZStream(avi, adam))(s => primaryKey(s.email, s.subject))
        .tap(errorOrStudent => Console.printLine(s"student=$errorOrStudent"))
        .runDrain

    } yield ()

    studentLogic.map(_ => Response.text("Student created successfully"))
      .catchAll(error => ZIO.succeed(Response.text(s"Error creating student: ${error.getMessage}")))
  }

}

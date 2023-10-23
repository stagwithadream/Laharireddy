package example.http

import example.model.Student
import zio._
import zio.dynamodb.{DynamoDBExecutor, batchWriteFromStream}
import zio.http._
import zio.{ Console, ZIOAppDefault }

// Define the CustomerApi trait
trait CustomerApi {
  def httpApp: Http[Any, Nothing, Request, Response]

  def createStudent(): ZIO[DynamoDBExecutor with  Console, Throwable, Response]
}

  object CustomerApi {
    lazy val live: ZLayer[Any, Nothing, CustomerApi] = ZLayer.succeed(CustomerApiLive())
}

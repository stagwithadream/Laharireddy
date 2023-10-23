package example.http

import zio._
import zio.dynamodb.{DynamoDBExecutor, batchWriteFromStream}
import example.dynamodblocal.DynamoDB
import example.dynamodblocal.DynamoDB.dynamoDBExecutorLayer
import zio.http._
import zio.{ Console, ZIOAppDefault }

// Define the CustomerApi trait
trait CustomerApi {
  def httpApp: Http[Any, Throwable, Request, Response]

  def createStudent(): ZIO[DynamoDBExecutor, Throwable, Response]
}

  object CustomerApi {
    lazy val live: ZLayer[Any, Nothing, CustomerApi] = ZLayer.succeed(CustomerApiLive(DynamoDB.dynamoDBExecutorLayer: ZLayer[Any, Throwable, DynamoDBExecutor]))
}

package example

import zio._
import zio.Console.printLine
import zio.http._
import example.http.CustomerApi
import zio.aws.core.config
import zio.aws.{dynamodb, netty}
import zio.dynamodb.DynamoDBQuery.{get, put}
import zio.dynamodb.DynamoDBExecutor
import zio.schema.{DeriveSchema, Schema}
import zio.ZIOAppDefault
import zio.dynamodb.ProjectionExpression
import zio.dynamodb.DynamoDBQuery.put
import zio.dynamodb._
import example.dynamodblocal.DynamoDB._
import example.dynamodblocal.StudentZioDynamoDBExample.program
import example.model.Student._
import example.model._
import zio.stream.ZStream
import zio.{Console, ZIOAppDefault}

object Main extends ZIOAppDefault {
  private val program = for {
    _ <- batchWriteFromStream(ZStream(avi, adam)) { student =>
      put("student", student)
    }.runDrain
    _ <- put("student", avi.copy(payment = Payment.CreditCard)).execute
  } yield ()

  val app = ZIO
    .serviceWithZIO[CustomerApi](customerApi => Server.serve(customerApi. httpApp.withDefaultErrorResponse))
    .provide(
      Server.defaultWithPort(8080),
      CustomerApi.live,
      dynamodb.DynamoDb.live,
      DynamoDBExecutor.live
    )
  override def run = program.provide(dynamoDBExecutorLayer, studentTableLayer)
}
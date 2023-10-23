package example.dynamodblocal

import zio.dynamodb.DynamoDBQuery.put
import zio.dynamodb._
import example.dynamodblocal.DynamoDB._
import example.model.Student._
import example.model._
import zio.stream.ZStream
import zio.{ Console, ZIOAppDefault }

object StudentZioDynamoDBExample extends ZIOAppDefault {

  private val program = for {
    _ <- batchWriteFromStream(ZStream(avi, adam)) { student =>
      put("student", student)
    }.runDrain
    _ <- put("student", avi.copy(payment = Payment.CreditCard)).execute
    _ <- batchReadFromStream("student", ZStream(avi, adam))(s => primaryKey(s.email, s.subject))
      .tap(errorOrStudent => Console.printLine(s"student=$errorOrStudent"))
      .runDrain
  } yield ()

  override def run = program.provide(dynamoDBExecutorLayer, studentTableLayer)
}

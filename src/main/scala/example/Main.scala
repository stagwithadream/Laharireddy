package example
import zio._
import zio.Console.printLine
import zio.http._
import example.http.CustomerApi

object Main extends ZIOAppDefault {
  val app = ZIO
    .serviceWithZIO[CustomerApi](customerApi => Server.serve(customerApi. httpApp.withDefaultErrorResponse))
    .provide(
      Server.defaultWithPort(8080),
      CustomerApi.live,
    )
  override def run:URIO[Any, ExitCode] = app.exitCode
}
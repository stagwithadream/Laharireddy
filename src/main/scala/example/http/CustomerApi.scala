package example.http

import zio._
import zio.http._

trait CustomerApi{
  def httpApp:Http[Any, Throwable, Request, Response]
}

object CustomerApi {
  lazy val live: ZLayer[Any, Nothing, CustomerApi] = ZLayer.succeed(CustomerApiLive())

}

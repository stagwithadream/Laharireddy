package example.http

import zio._
import zio.http._


case class CustomerApiLive() extends CustomerApi {

  override def httpApp:Http[Any, Nothing, Request, Response] = Http.collect[Request]{
    case Method.GET -> !! / "status" => Response.text("Server Running: CustomerApiLive Active")
    case Method.GET -> !! / "name" / "myname" => Response.text("Banana")
  }

}

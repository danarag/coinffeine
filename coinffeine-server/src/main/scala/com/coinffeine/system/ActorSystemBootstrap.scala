package com.coinffeine.system

import scala.util.control.NonFatal

import akka.actor.{Props, ActorSystem}

/** Bootstrap of an actor system whose supervisor actor receives CLI arguments and
  * whose termination stops the application.
  */
trait ActorSystemBootstrap { this: SupervisorComponent =>

  def main(args: Array[String]) {
    val system = ActorSystem("Main")
    try {
      val supervisor = system.actorOf(this.supervisorProps(args))
      system.actorOf(Props(classOf[akka.Main.Terminator], supervisor))
    } catch {
      case NonFatal(ex) =>
        system.shutdown()
        throw ex
    }
  }
}

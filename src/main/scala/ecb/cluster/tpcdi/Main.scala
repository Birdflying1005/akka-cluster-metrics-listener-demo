package ecb.cluster.tpcdi 

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.metrics.ClusterMetricsExtension
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config = ConfigFactory.load("seed")
  val system = ActorSystem("tpcdi", config)

  val roles = system.settings.config.getStringList("akka.cluster.roles")
  if(roles.contains("metric")) {
    Cluster(system).registerOnMemberUp {
      system.actorOf(Props[MetricsListener], "metrics-listener")
      //val ml = system.actorOf(Props[MetricsListener], "metrics-listener")
      //ClusterMetricsExtension(system).subscribe(ml)
    }
  }
}


package ecb.cluster.tpcdi 

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.Cluster
import akka.cluster.metrics.ClusterMetricsExtension
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config = ConfigFactory.load()
  val system = ActorSystem("tpcdi", config)

  val roles = system.settings.config.getStringList("akka.cluster.roles")
  if(roles.contains("metric")) {
    Cluster(system).registerOnMemberUp {
      system.actorOf(
        ClusterSingletonManager.props(
          singletonProps = Props(classOf[MetricsListener]),
          terminationMessage = PoisonPill,
          settings = ClusterSingletonManagerSettings(system).withRole("metric")),
        name = "metrics-listener")



      //system.actorOf(Props[MetricsListener], "metrics-listener")
      //val ml = system.actorOf(Props[MetricsListener], "metrics-listener")
      //ClusterMetricsExtension(system).subscribe(ml)
    }
  }
}


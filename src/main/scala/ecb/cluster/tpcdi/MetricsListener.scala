package ecb.cluster.tpcdi

import akka.actor.ActorLogging
import akka.actor.Actor
import akka.actor.AddressFromURIString
import akka.cluster.Cluster
import akka.cluster.metrics.ClusterMetricsEvent
import akka.cluster.metrics.ClusterMetricsChanged
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.metrics.NodeMetrics
import akka.cluster.metrics.StandardMetrics.HeapMemory
import akka.cluster.metrics.StandardMetrics.Cpu
import akka.cluster.metrics.ClusterMetricsExtension
import java.net.URLEncoder
     
class MetricsListener extends Actor with ActorLogging {
  val selfAddress = Cluster(context.system).selfAddress
  val extension = ClusterMetricsExtension(context.system)
     
  override def preStart(): Unit = extension.subscribe(self)
      
  override def postStop(): Unit = extension.unsubscribe(self)
     
  def receive = {
    case ClusterMetricsChanged(clusterMetrics) =>
      clusterMetrics.filter(_.address == AddressFromURIString("akka.tcp://tpcdi@127.0.0.1:2552")) foreach { nodeMetrics =>
        logHeap(nodeMetrics)
        logCpu(nodeMetrics)
      }
    case state: CurrentClusterState => // Ignore.
  }
     
  def logHeap(nodeMetrics: NodeMetrics): Unit = nodeMetrics match {
    case HeapMemory(address, timestamp, used, committed, max) =>
      log.info("{} Used heap: {} MB", address, used.doubleValue / 1024 / 1024)
    case _ => // No heap info.
  }
     
  def logCpu(nodeMetrics: NodeMetrics): Unit = nodeMetrics match {
    case Cpu(address, timestamp, Some(systemLoadAverage), cpuCombined, cpuStolen, processors) =>
      log.info("{} Load: {} ({} processors)", address, systemLoadAverage, processors)
    case _ => // No cpu info.
  }
}



name := "tpcdi-cluster"

version := "1.0"

organization := "com.ecb"

libraryDependencies ++= {
  val akkaVersion = "2.4.11"
  Seq(
    "com.typesafe.akka"       %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka"       %% "akka-cluster"                      % akkaVersion,
    "com.typesafe.akka"       %% "akka-cluster-metrics"              % akkaVersion, 
    "com.typesafe.akka"       %% "akka-cluster-tools"                % akkaVersion,
    "com.typesafe.akka"       %% "akka-remote"                       % akkaVersion,
    "com.typesafe.akka"       %% "akka-slf4j"                        % akkaVersion,
    "ch.qos.logback"          %  "logback-classic"                   % "1.1.7"
  )
}

// Assembly settings
mainClass in Global := Some("ecb.cluster.tpcdi.Main")

jarName in assembly := "tpcdi-node.jar"

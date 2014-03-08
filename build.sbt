name := "blogservice"

version := "1.0-SNAPSHOT"

resolvers += "jboss-releases" at "https://repository.jboss.org/nexus/content/repositories/releases/"

mainClass in Compile :=   Some("com.aevans.blog.ExampleApp")

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.6",
  "org.apache.commons" % "commons-lang3" % "3.2.1",
  "ch.qos.logback" % "logback-classic" % "1.1.1",
  "org.modeshape.bom" % "modeshape-bom-embedded" % "3.7.1.Final",
  "org.modeshape" % "modeshape-jcr" % "3.7.1.Final",
  "org.jboss.jbossts" % "jbossjta" % "4.16.6.Final",
  "org.infinispan" % "infinispan-cachestore-bdbje" % "5.3.0.Final",
  "org.scalaz" %% "scalaz-core" % "7.0.5",
  "org.specs2" %% "specs2" % "2.3.8" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)     






package com.aevans.blog.repository

import org.modeshape.jcr.{RepositoryConfiguration, ModeShapeEngine}
import javax.jcr.{Repository, PathNotFoundException, Node}
import org.slf4j.LoggerFactory

trait ModeshapeJcrRepositoryProvider  extends JcrRepositoryProvider {

  private[this] val logger = LoggerFactory.getLogger(classOf[ModeshapeJcrRepositoryProvider])
  private[this] val modeShapeEngine = new ModeShapeEngine()

  lazy val jcrRepository: Repository = {

    val repoConfigUrl = this.getClass().getClassLoader().getResource("blog-repository-config.json")
    val repositoryConfiguration = RepositoryConfiguration.read(repoConfigUrl)
    val problems = repositoryConfiguration.validate()


    if (problems.hasErrors || problems.hasWarnings) {
      logger.error("Problems starting the JCR repository engine.")
      logger.error(problems.toString)
      System.exit(-1)
    }

    modeShapeEngine.start()
    val repository = modeShapeEngine.deploy(repositoryConfiguration)

    logger.info("Started " + repository.getName + " JCR repository")
    checkAndcreateRootContentNode(repository)
    repository
  }


  private[this] def checkAndcreateRootContentNode(repository: Repository) {
    val session = repository.login()

    try {
      session.getNode("/blog/posts").asInstanceOf[Node]
    } catch {
      case e: PathNotFoundException =>
        logger.info("Creating missing /blog/post space")
        session.getRootNode.asInstanceOf[Node].addNode("blog", "nt:unstructured").addNode("posts", "nt:unstructured")
        session.save
    }

    session.logout
  }

  def shutdownJcrRepository = modeShapeEngine.shutdown()

}



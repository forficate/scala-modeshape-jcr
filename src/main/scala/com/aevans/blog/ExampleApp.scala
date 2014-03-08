package com.aevans.blog

import org.apache.commons.lang3.{RandomStringUtils}
import scalaz.Failure
import org.slf4j.LoggerFactory
import com.aevans.blog.model._


object ExampleApp extends App with BlogModuleImpl {

  val logger = LoggerFactory.getLogger("ExampleApp")

  logger.info("Blog app")

  logger.info(blogService.list.size + " blog posts found")
  logger.info("Creating new post")

  val post = BlogPost(title = "Hello World " + RandomStringUtils.randomNumeric(1000), excerpt = "",
    content = "", metaTitle = "", metaDescription = "", tags = Nil,
    slug = "/blog/posts/" + RandomStringUtils.randomAlphabetic(10).toLowerCase)


  blogService.saveOrUpdate(post) match {
    case Failure(e) => logger.error("Error saving post. " + e)
    case _ =>
      val posts = blogService.list
      logger.info("Done")
      logger.info(posts.size + " blog posts found")
      posts.foreach(post => logger.info(post.slug))
  }

  shutdownJcrRepository
}

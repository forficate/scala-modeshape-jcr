package com.aevans.blog.repository

import com.aevans.blog.model.{BlogPostSummary, BlogPost}
import com.aevans.blog.model.transformers.{BlogPostSummaryTransformer, BlogPostTransformer}
import javax.jcr.{Node, Session}
import scala.util.{Success, Failure, Try}
import scalaz.std.option.optionSyntax._
import scala.collection.mutable.ArrayBuffer
import org.slf4j.LoggerFactory


trait BlogRepositoryJCRComponent extends BlogRepositoryComponent {
  self:  JcrRepositoryProvider =>

  override lazy val blogRepository =  new BlogRepositoryJCA

  lazy val blogPostTransformer = new BlogPostTransformer
  lazy val blogPostSummaryTransformer = new BlogPostSummaryTransformer

  class BlogRepositoryJCA extends BlogRepository{
    private[this] val logger = LoggerFactory.getLogger(classOf[BlogRepositoryJCA])

    def getBySlug(slug: String): Option[BlogPost] = withSession { session =>
      blogPostTransformer.transform(session.getNode(slug)).some
    }.getOrElse(None)

    def list: Seq[BlogPostSummary] = {
      import scala.collection.JavaConversions._

      val result = withSession { session =>
        val items = ArrayBuffer[BlogPostSummary]()
        val posts = session.getNode("/blog/posts").getNodes

        for { node <- posts } try {
          items += blogPostSummaryTransformer.transform(node.asInstanceOf[Node])
        } catch {
          case e: Exception => logger.error(s"Error transforming ${node.asInstanceOf[Node].getPath} to BlogPostSummary. Skipping", e)
        }

        items.toSeq
      }

      result match {
        case Success(posts) => posts
        case Failure(exception) =>
          logger.error("Error getting list of blog posts.", exception)
          Seq.empty
      }
    }

    def save(post: BlogPost): Unit = {
      import com.aevans.blog.repository.BlogpostNodeMappings._
      val result = withSession { session =>
        val node = session.getNode("/blog/posts").addNode(post.slug.split('/').last)
        node.setProperty(title, post.title)
        node.setProperty(excerpt, post.excerpt)
        node.setProperty(content, post.content)
        node.setProperty(metaTitle, post.metaTitle)
        node.setProperty(metaDescription, post.metaDescription)
        node.setProperty(tags, post.tags.mkString(", "))
        node.setProperty(slug, post.slug)
        node.setProperty(created, post.created.toGregorianCalendar)
        node.setProperty(lastUpdated, post.lastUpdated.toGregorianCalendar)
        node.setProperty(published, post.published)
        session.save()
      }

      result match {
        case Failure(exception) => logger.error(s"Error saving post ${post.slug}", exception)
        case _ =>
      }
    }

    private[this] def withSession[T](closure: Session => T): Try[T] = {
      val session = jcrRepository.login

      Try{
        val result = closure(session)
        session.logout
        result
      } recoverWith { case  e: Exception =>
        session.logout
        Failure(e)
      }
    }
  }

}

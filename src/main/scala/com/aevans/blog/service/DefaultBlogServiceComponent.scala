package com.aevans.blog.service

import com.aevans.blog.repository.BlogRepositoryComponent
import com.aevans.blog.model.{BlogPostSummary, BlogPost}
import org.joda.time.DateTime
import scalaz.syntax.validation._
import scalaz.ValidationNel

trait DefaultBlogServiceComponent extends BlogServiceComponent {
  this: BlogRepositoryComponent =>

  override lazy val blogService = new DefaultBlogService

  class DefaultBlogService extends BlogService  {

    def getBySlug(slug: String): Option[BlogPost] = BlogPost.isValidSlug(slug) match {
      case true => blogRepository.getBySlug(slug)
      case false => None
    }

    def list: Seq[BlogPostSummary] = blogRepository.list

    def saveOrUpdate(post: BlogPost): ValidationNel[String, BlogPost] = BlogPost.isValidSlug(post.slug) match {
      case false => s"Invalid slug ${post.slug}, it must be in the format of /blog/posts/[a-z0-9-]".failNel[BlogPost]
      case true =>
        val postToSave = getBySlug(post.slug) match {
          case Some(orig) => post.copy(created = orig.created, lastUpdated = DateTime.now)
          case None => post.copy(lastUpdated = post.created)
        }

        blogRepository.save(postToSave)

        postToSave.successNel[String]
    }

  }

}

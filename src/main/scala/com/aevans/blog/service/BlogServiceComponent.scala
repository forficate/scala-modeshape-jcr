package com.aevans.blog.service

import scalaz._
import com.aevans.blog.model.BlogPostSummary
import com.aevans.blog.model.BlogPost

trait BlogServiceComponent {
  val blogService: BlogService

  trait BlogService {
    def getBySlug(slug: String): Option[BlogPost]
    def list: Seq[BlogPostSummary]
    def saveOrUpdate(blog: BlogPost) : ValidationNel[String, BlogPost]
  }

}

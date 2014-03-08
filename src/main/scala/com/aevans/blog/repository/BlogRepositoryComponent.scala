package com.aevans.blog.repository

import com.aevans.blog.model.{BlogPostSummary, BlogPost}

trait BlogRepositoryComponent {
  val blogRepository : BlogRepository

  trait BlogRepository {
    def getBySlug(slug: String): Option[BlogPost]
    def list: Seq[BlogPostSummary]
    def save(post: BlogPost)
  }

}

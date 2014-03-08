package com.aevans.blog.model

import org.joda.time.DateTime

case class BlogPost(
  title: String,
  excerpt: String,
  content: String,
  metaTitle: String,
  metaDescription: String,
  tags: Seq[String],
  slug: String,
  created: DateTime = DateTime.now,
  lastUpdated: DateTime = DateTime.now,
  published: Boolean = false
)

object BlogPost {
  def isValidSlug(slug: String) = slug.matches("/blog/posts/[a-z0-9-]*")
}
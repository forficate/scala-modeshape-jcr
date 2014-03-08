package com.aevans.blog.model

import org.joda.time.DateTime

case class BlogPostSummary(
  title: String,
  slug: String,
  excerpt: String,
  tags: Seq[String],
  created: DateTime,
  lastUpdated: DateTime,
  published: Boolean
)

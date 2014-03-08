package com.aevans.blog.model.transformers

import javax.jcr.Node
import org.joda.time.DateTime
import com.aevans.blog.model.BlogPostSummary
import org.apache.commons.lang3.StringUtils

class BlogPostSummaryTransformer extends JcrNodeTransformer[BlogPostSummary] {
  def transform(node: Node): BlogPostSummary = BlogPostSummary(
    title =  node.getProperty("title").getString,
    slug = node.getPath,
    excerpt = node.getProperty("excerpt").getString,
    created = new DateTime(node.getProperty("created").getDate),
    lastUpdated = new DateTime(node.getProperty("lastupdated").getDate),
    published = node.getProperty("published").getBoolean,
    tags = node.getProperty("tags").getString.split(",").filterNot(StringUtils.isEmpty)
  )
}

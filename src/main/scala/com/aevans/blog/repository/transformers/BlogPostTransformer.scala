package com.aevans.blog.model.transformers

import javax.jcr.Node
import org.joda.time.DateTime
import org.apache.commons.lang3.StringUtils
import com.aevans.blog.model.BlogPost
import com.aevans.blog.repository.BlogpostNodeMappings._

class BlogPostTransformer extends JcrNodeTransformer[BlogPost] {
  def transform(node: Node): BlogPost = BlogPost(
    title = node.getProperty(title).getString,
    content = node.getProperty(content).getString,
    excerpt = node.getProperty(excerpt).getString,
    slug = node.getPath,
    metaDescription = node.getProperty(metaDescription).getString,
    metaTitle = node.getProperty(metaTitle).getString,
    created = new DateTime(node.getProperty(created).getDate),
    lastUpdated = new DateTime(node.getProperty(lastUpdated).getDate),
    tags = node.getProperty(tags).getString.split(",").filterNot(StringUtils.isEmpty),
    published = node.getProperty(published).getBoolean
  )
}

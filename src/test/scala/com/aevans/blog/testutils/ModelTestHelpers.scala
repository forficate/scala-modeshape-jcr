package com.aevans.blog.testutils

import org.joda.time.DateTime
import com.aevans.blog.model.{BlogPostSummary, BlogPost}
import org.apache.commons.lang3.RandomStringUtils
import scala.util.Random


object ModelTestHelpers {

  def testBlogPost(
                    title: String = RandomStringUtils.randomAlphabetic(140),
                    excerpt: String = RandomStringUtils.randomAlphabetic(140),
                    content: String = RandomStringUtils.randomAlphabetic(500),
                    metaTitle: String = RandomStringUtils.randomAlphabetic(140),
                    metaDescription: String = RandomStringUtils.randomAlphabetic(140),
                    tags: Seq[String] = randomTags,
                    slug: String = RandomStringUtils.randomAlphabetic(25),
                    created: DateTime = DateTime.now, // Hard as this needs to be <= lastUpdated
                    lastUpdated: DateTime = DateTime.now, // Hard as this needs to be >= lastUpdated
                    published: Boolean = randomBoolean): BlogPost =
                    BlogPost(title, excerpt, content, metaTitle, metaDescription, tags, slug, created,
                      lastUpdated, published)


  def testBlogPostSummary(
                           title: String = RandomStringUtils.randomAlphabetic(140),
                           slug: String = RandomStringUtils.randomAlphabetic(25),
                           excerpt: String = RandomStringUtils.randomAlphabetic(140),
                           tags: Seq[String] = randomTags,
                           created: DateTime = DateTime.now, // Hard as this needs to be <= lastUpdated
                           lastUpdated: DateTime = DateTime.now,  // Hard as this needs to be >= lastUpdated
                           published: Boolean = randomBoolean) =
    BlogPostSummary(title = title, slug = slug, excerpt = excerpt, tags = tags, created = created,
      lastUpdated = lastUpdated, published = published)


  private def randomTags = (0 until Random.nextInt(10)) map(i=>  RandomStringUtils.randomAlphabetic(25))

  private def randomBoolean = Random.nextInt(1) match {
    case 0 => false
    case 1 => true
  }

}


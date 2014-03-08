package com.aevans.blog.transformers

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import org.joda.time.DateTime
import javax.jcr.Node
import com.aevans.blog.model.transformers.BlogPostTransformer
import com.aevans.blog.testutils.JcrTestHelpers

class BlogPostTransformerSpec extends Specification with Mockito {

  "transform" should {
    "transform a blog node to a BlogPost" in {

      val testDate = DateTime.now.toGregorianCalendar

      val node = mock[Node]
      node.getProperty("title") returns JcrTestHelpers.mockProperty(_.getString returns "Hello")
      node.getProperty("content") returns JcrTestHelpers.mockProperty(_.getString returns "Mock Content")
      node.getProperty("excerpt") returns JcrTestHelpers.mockProperty(_.getString returns "Mock excerpt")
      node.getProperty("metadescription") returns JcrTestHelpers.mockProperty(_.getString returns "desc")
      node.getProperty("metatitle") returns JcrTestHelpers.mockProperty(_.getString returns "title")
      node.getProperty("tags") returns JcrTestHelpers.mockProperty(_.getString returns "")
      node.getProperty("created") returns JcrTestHelpers.mockProperty(_.getDate returns testDate)
      node.getProperty("lastupdated") returns JcrTestHelpers.mockProperty(_.getDate returns testDate)
      node.getProperty("published") returns JcrTestHelpers.mockProperty(_.getBoolean returns true)
      node.getPath returns "/content/myblog"

      new BlogPostTransformer().transform(node) must beLike {case post =>
        post.title mustEqual("Hello")
        post.slug mustEqual("/content/myblog")
        post.content mustEqual("Mock Content")
        post.excerpt mustEqual("Mock excerpt")
        post.metaDescription mustEqual("desc")
        post.metaTitle mustEqual("title")
        post.tags must beEmpty
        post.created.getMillis mustEqual(testDate.getTimeInMillis)
        post.lastUpdated.getMillis mustEqual(testDate.getTimeInMillis)
        post.published mustEqual(true)
      }
    }
  }

}

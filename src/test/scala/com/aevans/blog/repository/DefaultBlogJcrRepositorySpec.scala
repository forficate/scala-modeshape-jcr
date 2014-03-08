package com.aevans.blog.repository

import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import javax.jcr._
import com.aevans.blog.model.BlogPost
import com.aevans.blog.model.transformers.{BlogPostSummaryTransformer, BlogPostTransformer}
import com.aevans.blog.testutils.ModelTestHelpers

class DefaultBlogJcrRepositorySpec extends Specification with Mockito {

  "getBySlug" should {
    "return None if slug is not found" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { session =>
        session.getNode(any) throws(new PathNotFoundException())
      }

      blogRepository.getBySlug("test") must beNone
    }

    "return Some[JcaBlogPost] if slug is found" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { jcrSession =>
        jcrSession.getNode(any) returns mock[Node]
      }

      blogPostTransformerExpectations {transformer =>
        transformer.transform(any) returns ModelTestHelpers.testBlogPost()
      }

      blogRepository.getBySlug("test") must beSome[BlogPost]
    }

    "the session is closed gracefully on error" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { session =>
        session.getNode(any) throws(new PathNotFoundException())
      }

      blogRepository.getBySlug("test")

      there was one(mockSession).logout()
    }

    "the session is closed when successfull" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { jcrSession =>
        jcrSession.getNode(any) returns mock[Node]
      }

      blogPostTransformerExpectations {transformer =>
        transformer.transform(any) returns ModelTestHelpers.testBlogPost()
      }

      blogRepository.getBySlug("test")
      there was one(mockSession).logout()
    }

    "the session JCR is opened" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { jcrSession =>
        jcrSession.getNode(any) returns mock[Node]
      }

      blogPostTransformerExpectations {transformer =>
        transformer.transform(any) returns ModelTestHelpers.testBlogPost()
      }

      blogRepository.getBySlug("test")
      there was one(jcrRepository).login()
    }

  }

  "list" should {
    "return a list of JCRBlogpostSummary's" in new BlogRepositoryJCAComponentTest {

      jcrSessionExpectations { jcrSession =>
        val mockPostsParentNode = mock[Node]
        val mockNodeIterator = mock[NodeIterator]

        mockPostsParentNode.getNodes returns mockNodeIterator

        mockNodeIterator.hasNext returns (true, false)
        mockNodeIterator.next.asInstanceOf[Node] returns mock[Node]

        jcrSession.getNode("/blog/posts") returns mockPostsParentNode
      }

      blogPostSummaryTransformerExpectations {transformer =>
        transformer.transform(any) returns ModelTestHelpers.testBlogPostSummary()
      }


      blogRepository.list must not beEmpty
    }
  }

  "save" should {
    "persist a given node" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { jcrSession =>
        val post = ModelTestHelpers.testBlogPost(slug="/blog/posts/hello-world")

        val mockPostsParentNode = mock[Node]
        jcrSession.getNode("/blog/posts") returns mockPostsParentNode

        val mockNewNode = mock[Node]
        mockPostsParentNode.addNode(any) returns mockNewNode

        blogRepository.save(post)

        there was one(mockNewNode).setProperty("title", post.title)
        there was one(mockNewNode).setProperty("excerpt", post.excerpt)
        there was one(mockNewNode).setProperty("content", post.content)
        there was one(mockNewNode).setProperty("metatitle", post.metaTitle)
        there was one(mockNewNode).setProperty("metadescription", post.metaDescription)
        there was one(mockNewNode).setProperty("tags", post.tags mkString(", "))
        there was one(mockNewNode).setProperty("slug", post.slug)
        there was one(mockNewNode).setProperty("created", post.created.toGregorianCalendar)
        there was one(mockNewNode).setProperty("lastupdated", post.lastUpdated.toGregorianCalendar)
        there was one(mockNewNode).setProperty("published", post.published)
        there was one(jcrSession).save()
      }

    }

    "sets the right path on persist" in new BlogRepositoryJCAComponentTest {
      jcrSessionExpectations { jcrSession =>
        val post = ModelTestHelpers.testBlogPost(slug="/blog/posts/hello-world")

        val mockPostsParentNode = mock[Node]
        jcrSession.getNode("/blog/posts") returns mockPostsParentNode

        mockPostsParentNode.addNode("hello-world") returns mock[Node]

        blogRepository.save(post)

        there was one(mockPostsParentNode).addNode("hello-world")
      }
    }
  }

  trait BlogRepositoryJCAComponentTest extends BlogRepositoryJCRComponent with JcrRepositoryProvider with Scope {
    val mockSession = mock[Session]

    val jcrRepository = mock[Repository]
    override lazy val blogPostTransformer = mock[BlogPostTransformer]
    override lazy val blogPostSummaryTransformer = mock[BlogPostSummaryTransformer]

    jcrRepository.login returns mockSession

    def jcrSessionExpectations(closure: Session => Any) {
      closure(mockSession)
    }

    def blogPostTransformerExpectations(closure: BlogPostTransformer => Any) {
      closure(blogPostTransformer)
    }

    def blogPostSummaryTransformerExpectations(closure: BlogPostSummaryTransformer => Any) {
      closure(blogPostSummaryTransformer)
    }

  }

}

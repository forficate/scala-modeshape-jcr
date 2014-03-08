package com.aevans.blog.service

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.aevans.blog.repository.BlogRepositoryComponent
import org.specs2.specification.Scope
import org.joda.time.{DateTimeUtils, DateTime}
import com.aevans.blog.testutils.ModelTestHelpers
import com.aevans.blog.model.BlogPost

class DefaultBlogServiceComponentSpec extends Specification with Mockito {

  "getBySlug" should {
    val validSlug = "/blog/posts/hello-world"

    "return None if a blog does not exist for given slug" in  new DefaultBlogServiceComponentTest {
      expect { blogRepository =>
        blogRepository.getBySlug(validSlug) returns None
      }

      blogService.getBySlug(validSlug) must beNone
      there was one(blogRepository).getBySlug(validSlug)
    }

    "return Some[BlogPost] if a blog exists for given slug" in new DefaultBlogServiceComponentTest {
      val post = ModelTestHelpers.testBlogPost()

      expect { blogRepository =>
        blogRepository.getBySlug(validSlug) returns Some(post)
      }

      blogService.getBySlug(validSlug) must beSome(post)
      there was one(blogRepository).getBySlug(validSlug)
    }

    "return None if an invalid slug is given and don't call the repository" in new DefaultBlogServiceComponentTest {
      blogService.getBySlug("blah") must beNone
      there was no(blogRepository).getBySlug(any)
    }
  }

  "list" should {
    "return a list of blog posts" in new DefaultBlogServiceComponentTest {
      val  posts = (1 until 10).map(i => ModelTestHelpers.testBlogPostSummary())

      expect { blogRepository =>
        blogRepository.list returns posts
      }

      blogService.list mustEqual posts
    }
  }

  "saveOrUpdate" should {
    "create a new post if slug does not already exist and verify created/lastupdated dates match" in new DefaultBlogServiceComponentTest{
      val postCreatedDate = new DateTime(2012, 2, 21, 8, 5)

      val newPost = ModelTestHelpers.testBlogPost(slug = "/blog/posts/test", created = postCreatedDate)

      expect { blogRepository =>
        blogRepository.getBySlug(newPost.slug) returns None
      }

      blogService.saveOrUpdate(newPost)

      there was one(blogRepository).save(newPost.copy(created = postCreatedDate, lastUpdated = postCreatedDate))
    }

    "update the last updated date if updating an existing post" in new DefaultBlogServiceComponentTest {
      val blogRepositorySaveTime = new DateTime(2012, 2, 21, 8, 5)
      val existingPost = ModelTestHelpers.testBlogPost(slug = "/blog/posts/test", created = blogRepositorySaveTime, lastUpdated = blogRepositorySaveTime)

      expect { blogRepository =>
        blogRepository.getBySlug(existingPost.slug) returns Some(existingPost)
      }

      val updateTime = blogRepositorySaveTime.plusYears(1)
      DateTimeUtils.setCurrentMillisFixed(updateTime.getMillis)

      blogService.saveOrUpdate(existingPost)

      there was one(blogRepository).save(existingPost.copy(lastUpdated = updateTime))
    }

    "return a error message if the slug is not valid" in new DefaultBlogServiceComponentTest {
      import scalaz.syntax.validation._
      val result = blogService.saveOrUpdate(ModelTestHelpers.testBlogPost(slug = "blah"))
      result mustEqual "Invalid slug blah, it must be in the format of /blog/posts/[a-z0-9-]".failNel[BlogPost]
    }

  }


  trait DefaultBlogServiceComponentTest extends Scope with DefaultBlogServiceComponent with BlogRepositoryComponent {
    val blogRepository = mock[BlogRepository]

    def expect(f: BlogRepository => Any) {
      f(blogRepository)
    }
  }


}

package com.aevans.blog.model

import org.specs2.mutable.Specification

class JcrBlogPostSpec extends Specification {

  "isValidSlug" should {
    "return false if a invalid path is used" in {
      val invalidPaths = "blog" :: "/blog" :: "/blog/posts" :: "/a/b/hello-world" :: Nil
      foreach(invalidPaths) (BlogPost.isValidSlug(_) must beFalse)
    }

    "return false if a invalid character is used"  in {
      val invalidPaths = "/blog/posts/hello!" :: "/blog/posts/hello_world" ::  Nil
      foreach(invalidPaths) (BlogPost.isValidSlug(_) must beFalse)
    }

    "return true for a valid path" in {
      BlogPost.isValidSlug("/blog/posts/hello-world") must beTrue
    }
  }

}

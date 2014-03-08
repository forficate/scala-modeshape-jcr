package com.aevans.blog.testutils

import org.specs2.mock.Mockito
import javax.jcr.Property


object JcrTestHelpers extends Mockito {

  def mockProperty(closure: (Property) => Any) : Property = {
    val prop = mock[Property]
    closure(prop)
    prop
  }

}

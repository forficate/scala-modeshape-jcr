package com.aevans.blog

import com.aevans.blog.service.{DefaultBlogServiceComponent, BlogServiceComponent}
import com.aevans.blog.repository.{ModeshapeJcrRepositoryProvider, BlogRepositoryJCRComponent,  BlogRepositoryComponent}

trait BlogModule extends BlogRepositoryComponent with BlogServiceComponent

trait BlogModuleImpl
  extends BlogModule
  with DefaultBlogServiceComponent
  with BlogRepositoryJCRComponent
  with ModeshapeJcrRepositoryProvider
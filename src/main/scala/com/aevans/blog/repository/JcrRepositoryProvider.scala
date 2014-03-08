package com.aevans.blog.repository

import javax.jcr.Repository


trait JcrRepositoryProvider {
  def jcrRepository: Repository
}

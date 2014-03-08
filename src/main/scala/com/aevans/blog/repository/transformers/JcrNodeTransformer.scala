package com.aevans.blog.model.transformers

import javax.jcr.Node

protected trait JcrNodeTransformer[T] {
  def transform(node: Node): T
}

package org.buttercoin

/**
  * Created by vu_ho on 10/27/17.
  */

import org.buttercoin.common.messages.Result

package object common {

  final case class SearchFilterParam(fieldName: String, value: String)

  sealed trait SearchSortType { val asParam: Long }
  case object AscendingSort extends SearchSortType {
    val asParam = 1L
  }
  case object DescendingSort extends SearchSortType {
    val asParam = -1L
  }
  final case class SearchSortParam(fieldName: String, direction: SearchSortType)

  final case class SearchQueryParams(
                                      skip: Option[Int] = None,
                                      limit: Option[Int] = Some(10),
                                      query: Option[String] = None,
                                      sort: Option[SearchSortParam] = None,
                                      filters: Option[List[SearchFilterParam]] = None
                                    )
  val EmptySearchQueryParams = SearchQueryParams()

  trait SearchQueryResult[T <: Result] extends Result {
    val count: Int
    val results: List[T]
  }
  case object EmptySearchQueryResult extends SearchQueryResult[Result] {
    val count = 0
    val results = List()
  }

  object query {
    sealed trait OrderQueryType
    case object OpenOrderQuery extends OrderQueryType
    case object ClosedOrderQuery extends OrderQueryType
    case object AllOrderQuery extends OrderQueryType
  }
}

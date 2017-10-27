package org.buttercoin.common

import com.typesafe.config.Config
import org.buttercoin.common.messages.CreditTrade
import org.buttercoin.common.models.money.Currency

import scalaz.{Tag, _}

/**
  * Created by vu_ho on 10/27/17.
  */
package object fees {
  trait FeeStrategyIDTag
  type FeeStrategyID = String @@ FeeStrategyIDTag
  def FeeStrategyID(uuid: String): FeeStrategyID = Tag[String, FeeStrategyIDTag](uuid)

  @SerialVersionUID(1L)
  trait FeeStrategy extends Serializable {
    type FeeInfo[T] = (T, T, BigDecimal)

    val parent: Option[FeeStrategy] = None
    def feeBreakdown[T <: Currency](msg: CreditTrade[T]): FeeInfo[T]
  }

  @SerialVersionUID(1L)
  trait StrategyFactory extends Serializable {
    protected val config: Config
    def apply(): FeeStrategy
  }
}

package org.buttercoin.common

import java.util.Date

import com.typesafe.config.ConfigFactory
import org.buttercoin.common.util.config.fallback._
import org.buttercoin.common.util.validations._

import scala.reflect.ClassTag

/**
  * Created by vu_ho on 10/27/17.
  */
package object util {
  case class CountryConfiguration(currencyCodes: Seq[String] = List()) {
    def currencyAllowed(code: String): Boolean = currencyCodes.contains(code)
  }

  private def getCountryCurrencies(code: String): Seq[String] =
    ConfigFactory.load.getConfig("countries." + code).getStringListWithFallback("currencies")

  val InvalidCountryConfiguration = CountryConfiguration()
  val US = CountryConfiguration( getCountryCurrencies("US") )

  implicit val stringToCountryConfiguration: PartialFunction[String, CountryConfiguration] = {
    case "US" => US
    case _ => InvalidCountryConfiguration
  }

  // Fail if the provided string doesn't match the regular expression
  def matchingCountry[T <: String](regexMap: Map[CountryConfiguration, String])
                                  (implicit country: CountryConfiguration): ValidatorF[T] =
    requireThat[T](_ matches regexMap(country)) orMsg "Invalid format"

  // Taken from http://stackoverflow.com/questions/6909053/enforce-type-difference
  sealed class =!=[A,B]
  trait LowerPriorityImplicits {
    /** Dummy method for type-checking support */
    implicit def equal[A]: =!=[A, A] = sys.error("should not be called")
  }
  object =!= extends LowerPriorityImplicits {
    implicit def nequal[A,B]: =!=[A,B] = new =!=[A,B]
  }
  // end

  type =?>[A, B] = PartialFunction[A, B]

  case class DateWithNanos(date: Date, nanos: Long)
  object DateWithNanos {
    def now = DateWithNanos(new Date(), System.nanoTime())
  }
  implicit val dateWithNanosOrdering: Ordering[DateWithNanos] = Ordering.by(d => (d.date, d.nanos))

  def asOpt[T](x: Any)(implicit ct: ClassTag[T]): Option[T] = x match {
    case (y: T) => Some(y)
    case _ => None
  }
}

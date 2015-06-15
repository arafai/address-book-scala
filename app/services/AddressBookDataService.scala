package services

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source
import scala.util.control.NonFatal

// generic trait for retrieving address books
// it can have multiple implementation - from file, from database, from moon
trait AddressBookDataService {
  def retrieveAll: Iterator[AddressBook]
}

// file address books service retrieval
class FileAddressBookDataServiceImpl(fileLocation: String) extends AddressBookDataService {

  private[this] def getByLine =
    try {
      Source.fromFile(fileLocation)
        .getLines()
    } catch {
      case NonFatal(e) => Iterator.empty
    }

  override def retrieveAll(): Iterator[AddressBook] =
    getByLine.map(_.split(","))
      .filter(_.length == 3)
      .map(tokens => AddressBook(tokens))
}

object FileAddressBookDataServiceImpl {
  def apply(fileLocation: String): AddressBookDataService =
    new FileAddressBookDataServiceImpl(fileLocation)
}


object AddressBook {
  lazy val formatter = DateTimeFormat.forPattern("dd/MM/yy")

  def apply(tokens: Array[String]) = {
    val fullName = tokens.head.trim.split(" ")
    new AddressBook(fullName(0), fullName(1), tokens(1).trim.equals("Male"), formatter.parseDateTime(tokens(2).trim))
  }
}

case class AddressBook(firstName: String, lastName: String, isMale: Boolean, birthDate: DateTime) {
  def fullName = s"$firstName $lastName"
}

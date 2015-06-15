package services

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source

// generic trait for retrieving address books
// it can have multiple implementation - from file, from database, from moon
trait AddressBookDataService {
  def retrieveAll(): Stream[AddressBook]
}

// file address books service retrieval
class FileAddressBookDataServiceImpl(fileLocation: String) extends AddressBookDataService {

  private[this] def getByLine =
    Source.fromFile(fileLocation)
      .getLines()

  override def retrieveAll(): Iterator[AddressBook] =
    getByLine.map(_.split(","))
      .filter(_.length == 3)
      .map(tokens => AddressBook(tokens))
}

object FileAddressBookDataServiceImpl {
  def apply(fileLocation: String) =
    new FileAddressBookDataServiceImpl(fileLocation)
}


object AddressBook {
  lazy val formatter = DateTimeFormat.forPattern("dd/MM/yyyy")

  def apply(tokens: Array[String]) = {
    val fullName = tokens.head.trim.split(" ")
    AddressBook(fullName(0), fullName(1), tokens(1).equals("Male"), formatter.parseDateTime(tokens(2)))
  }
}

case class AddressBook(firstName: String, lastName: String, isMale: Boolean, birthDate: DateTime)

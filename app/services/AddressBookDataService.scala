package services

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.io.Source

trait AddressBookDataService {
  def retrieveAll(): Stream[AddressBook]
}

class FileAddressBookDataService(fileLocation: String) extends AddressBookDataService {

  private def getByLine =
    Source.fromFile(fileLocation)
      .getLines()

  override def retrieveAll(): Iterator[AddressBook] =
    getByLine.map(_.split(","))
      .filter(_.length == 3)
      .map(tokens => AddressBook(tokens))
}


object AddressBook {
  lazy val formatter = DateTimeFormat.forPattern("dd/MM/yyyy");

  def apply(tokens: Array[String]) = {
    val fullName = tokens.head.trim.split(" ")
    AddressBook(fullName(0), fullName(1), tokens(1).equals("Male"), formatter.parseDateTime(tokens(2)))
  }
}

case class AddressBook(firstName: String, lastName: String, isMale: Boolean, birthDate: DateTime)

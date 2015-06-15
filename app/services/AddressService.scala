package services

import org.joda.time.Days

trait AddressService {
  def countMales: Int

  def getOldest: Option[AddressBook]

  //How many days older is Bill than Paul?
  //I assumed that a search by first name is intended and not a search by full name
  // that's way an address book has a fist and a last name
  def getAgeDiff(firstName1: String, firstName: String): Option[Int]
}

class AddressServiceImpl(dataService: AddressBookDataService) extends AddressService {
  //generic method for counting address books - by different criteria
  private[this] def count(f: AddressBook => Boolean): Int =
    retrieveAll
      .count(f)

  private[this] def retrieveAll: Iterator[AddressBook] =
    dataService.retrieveAll

  private[this] def findByFirstName(firstName: String): Option[AddressBook] =
    retrieveAll.find(_.firstName.equals(firstName))

  //generic method for sorting address books - by different criteria
  private[this] def sort(f: (AddressBook, AddressBook) => Boolean): Stream[AddressBook] =
    retrieveAll.toStream
      .sortWith(f)

  override def countMales(): Int =
    count((ad: AddressBook) => ad.isMale)

  override def getOldest: Option[AddressBook] =
    sort((a: AddressBook, b: AddressBook) => (a.birthDate.isBefore(b.birthDate))).headOption

  //find bood address by specified first name and calculate age difference
  override def getAgeDiff(firstName1: String, firstName2: String): Option[Int] =
    for {
      ab1 <- findByFirstName(firstName1)
      ab2 <- findByFirstName(firstName2)
    } yield Days.daysBetween(ab1.birthDate, ab2.birthDate).getDays
}

object AddressServiceImpl {
  def apply(addressBookDataService: AddressBookDataService) =
    new AddressServiceImpl(addressBookDataService)
}


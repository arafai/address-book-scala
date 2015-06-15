package services

trait AddressService {
  def countMales: Int

  def getOldest: Option[AddressBook]
}

class AddressServiceImpl(dataService: AddressBookDataService) extends AddressService {
  //generic method for counting address books - by different criteria
  private[this] def count(f: AddressBook => Boolean): Int =
    dataService.retrieveAll()
      .count(f)

  //generic method for sorting address books - by different criteria
  private[this] def sort(f: (AddressBook, AddressBook) => Boolean): Stream[AddressBook] =
    dataService.retrieveAll()
      .sortWith(f)

  override def countMales(): Int =
    count((ad: AddressBook) => ad.isMale)

  override def getOldest: Option[AddressBook] =
    sort((a: AddressBook, b: AddressBook) => (a.birthDate.isAfter(b.birthDate))).headOption
}

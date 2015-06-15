package services

object Main extends App {
  val fileService: AddressBookDataService = FileAddressBookDataServiceImpl("AddressBook")
  val addressBookService = AddressServiceImpl(fileService)

  val males = addressBookService.countMales
  println(s"number of males: $males")

  addressBookService.getOldest
    .foreach(oldest => println(s"oldest person is : ${oldest.fullName}"))

  addressBookService.getAgeDiff("Bill", "Paul")
    .foreach(ageDiff => println(s"age diff between Bill and Paul in days is : $ageDiff"))

}

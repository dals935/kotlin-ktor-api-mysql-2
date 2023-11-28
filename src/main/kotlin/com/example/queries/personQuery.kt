package com.example.queries

const val getAllPersonQuery = "SELECT personID, firstName, lastName, address, city FROM persons"

const val updatePersonQuery = "UPDATE persons SET lastName=?, firstName=?, address=?, city=? WHERE personID=?"

const val deletePersonQuery = "DELETE FROM persons WHERE personID=?"

const val insertPersonQuery = "INSERT INTO persons(firstName, lastName, address, city) VALUES(?, ?, ?, ?)"

const val insertPersonCredentialQuery = "INSERT INTO persons_credentials(employee_no, username, password) VALUES(?, ?, ?)"

const val getLoginCredsQuery = "SELECT username, password FROM persons_credentials"

const val changePasswordQuery = "UPDATE persons_credentials SET persons_credentials.password=? WHERE credential_id=?"


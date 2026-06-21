Stephen Lee (srlee16)
21 Jun 26

SER335 - Lab 5 Activity 2

Task 1 Changes

Location: SaltSingleton.writeSaltsFile
Desrciption: Added try/catch blocks so that exception can be thrown from the
source with more specific error messages.

Location: SaltSingleton.createSaltedPassword
Desrciption: Removed print stack trace call so user cannot see stack trace on 
console.
Replaced "throws Exception" in method signature with "throws IOException" and 
removed IOException try/catch block allowing IOException from writeSaltFile
method to be passed along
Replaced if/else "throw new Exception.." with more accurate "throw new 
IllegalStateException"

Location: UserSingleton.createPasswordMapping
Desrciption: Removed try/catch block for ...createSatledPassword() allowing 
IOException from writeSaltFile method to be passed along.
Removed try/catch block for writeAuthFile() allowing writeAuthFile() IOException  
to be passed along.

Location: UserSingleton.writeAuthFile
Desrciption: Added try/catch blocks so that exception can be thrown from the
source with more specific error messages.

Location: AddUserPanel.actionPerformed
Desrciption: Replaced hardcoded error message with more accurate t.getMessage



Task 2

1.
Location: SearchDialog.search
Desrciption: Replaced "throw new Exception..." with break 
Removed "throws Exception"

Location: SearchDialog.searchButton_actionPerformed
Desrciption: Replaced try/catch block surrounding search() w/try/finlly block


2.
Location: MainFrame_AboutBox.jbInit
Desrciption: Removed "throws Exception"  as aperations in method do not throw 
checked exceptions.

Location: MainFrame_AboutBox.MainFrame_AboutBox
Desrciption: Removed try/catch block as jbInit no longer throws checked 
exceptions.
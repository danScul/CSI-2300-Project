Project Title: Library Inventory System 
Team Name: DS 
Team Members: Daniel Scully 

Description: This project will be an application to aid in the management and organization of a library’s inventory.
I would like to build it because I have worked in a library for upwards of seven years.
I have found that the tool that allows a library to run at peak efficiency is a well-managed inventory.
This application will allow a user to add and remove items from their collection, sort their collection in a variety of ways (by title [ascending & descending alphabetical], 
author’s last name, and Dewey Decimal System [allowing user to manually input with a key provided for reference]), search titles for keywords, check books in and out.
The user will be prompted to add a book to their collection (providing places to input the book’s title, author,
and whether it is fiction or nonfiction [if nonfiction also prompts for dds]).
The user's collection will be displayed below with the title, author’s last name and first initial visible. 
Once an item is in the collection it can be checked out or removed from the collection entirely.
When an item is checked out, it will be marked as such and unable to be checked out until returned but not removed from the collection entirely. 

Estimate of Effort: The bulk of the effort involved in the creation of this application would be the collection system as well as the implementation of the GUI.
The program requires the use of an array of 'Book' objects. Properly maintaining this array and keeping the user data associated will require testing to ensure it is all handled properly.
The implentation of the GUI may take some more signifcant time as well due to the multiple ways of sorting and displaying the information.



How to use: Upon launch a window will open. In the top left will be where you can view your catalogue of books.

Below it is a series of four buttons: Add, Remove, Check Out, and Check In.

Add- Upon selecting Add a window will open and prompt you to fill out information to create a new catalogue entry. You will be asked to enter a title, the author's name, and whether the book is fiction or nonfiction. Once you select either fiction or non-fiction, an additional prompt is added for the genre or the dewey decimal number respectively. (If you are not familiar with the Dewey Decimal System, clicking on 'Dewey Decimal System' will open a link to a Wikipedia entry that details it).

Remove- By clicking on an entry in the catalogue then clicking the remove button will delete the book from the catalogue entirely.

Check Out- By clicking on an entry in the catalogue then clicking the check out button, you will be prompted to create a user profile The book will be unavailable to check out again until it is checked in and will be added to your user profile. If you enter an account number already in use, it will be added to the existing account.

Check In- By clicking on an entry in the catalogue then clicking the check in button, the book will be made available once again and removed from the holder's account.

In the top right is a drop down menu with options on how to organize your catalogue. You can order them alphabetically, descending alphabetically, alphabetically by author's last name, and reverse alphabetically by author's name.

In the bottom right will be a display that provides information on the currently selected book.

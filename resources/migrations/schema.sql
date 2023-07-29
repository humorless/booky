CREATE TABLE Book (
    id SERIAL PRIMARY KEY,
    Title TEXT NOT NULL,
    Author TEXT NOT NULL,
    Genre TEXT,
    Publication_Date DATE
);
--;;
CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    First_Name TEXT NOT NULL,
    Last_Name TEXT NOT NULL,
    email TEXT
);
--;;
CREATE TABLE Registry (
    id SERIAL PRIMARY KEY,
    Book_ID INT,
    Patron_ID INT,
    Borrower_ID INT,
    Borrow_Date DATE,
    Due_Date DATE,
    FOREIGN KEY (Book_ID) REFERENCES Book(id),
    FOREIGN KEY (Patron_ID) REFERENCES Person(id),
    FOREIGN KEY (Borrower_ID) REFERENCES Person(id)
);

(ns gig.sql
  (:require [honey.sql :as sql]
            [migratus.core :as migratus]
            [next.jdbc :as jdbc]
            [tick.core :as t]))

;; Database configuration
(def config {:store                :database
             :migration-dir        "resources/migrations/"
             :init-script          "schema.sql"
             :migration-table-name "booky"
             :db                   {:port     5432
                                    :dbname   "booky"
                                    :host     "localhost"
                                    :dbtype   "postgresql"
                                    :user     "booker"
                                    :password "dev"}})

;; initialize the database using the 'schema.sql' script
(comment
  (migratus/init config))

;; setup connection to database
(def db (jdbc/get-connection (:db config)))

;; helper function for db exeuction
(defn execute [q]
  (jdbc/execute! db (sql/format q {:pretty true})
                 {:return-keys true}))

;; Sample data
(def book
  {:title            "The Man Without Qualities"
   :author           "Robert Musil"
   :genre            "Philosophical Fiction"
   :publication-date (t/date "1943-11-06")})

(def patron
  {:first-name "Ulrich"
   :last-name  ""
   :email      "ulrich@kakania.at"})

(def borrower
  {:first-name "Agatha"
   :last-name  ""
   :email      "agatha@kakania.at"})

(def registry
  {:book-id     1
   :patron-id   1
   :borrower-id 2
   :borrow-date (t/date "2023-07-28")
   :due-date    (t/date "2023-10-28")})

;; Insertion
(jdbc/execute!
  db
  (sql/format
    {:insert-into :book
     :values      [book]})
  {:return-keys true})

(execute
  {:insert-into :person
   :values      [patron
                 borrower]})

(execute
  {:insert-into :registry
   :values      [registry]})

;; Querying
(execute
  {:select [:*]
   :from   :person})

(execute
  {:select [:book.title
            :registry.borrow-date
            [:borrower.first-name :borrower]
            [:patron.first-name :patron]]
   :from   [:registry]
   :join   [:book [:= :registry.book-id :book.id]
            [:person :borrower] [:= :registry.borrower-id :borrower.id]
            [:person :patron] [:= :registry.patron-id :patron.id]]
   :where  [:= :borrower.email "agatha@kakania.at"]})

(ns gig.datomic
  (:require [datomic.api :as d]
            [clojure.edn :as edn]
            [clojure.instant :as instant]))

(def db-uri "datomic:mem://booky")

(d/create-database db-uri)

(def conn (d/connect db-uri))

(def schema (edn/read-string (slurp "resources/schema.edn")))

@(d/transact conn schema)

;; Sample data
(def data
  [{:db/id "musil"
    :book/title "The Man Without Qualities"
    :book/author "Robert Musil"
    :book/genre "Philosophical Fiction"
    :book/publication-date (instant/read-instant-date "1943-11-06")}

   {:db/id "ulrich"
    :person/first-name "Ulrich"
    :person/last-name ""
    :person/email "ulrich@kakania.at"}

   {:db/id "agatha"
    :person/first-name "Agatha"
    :person/last-name ""
    :person/email "agatha@kakania.at"}

   {:registry/book "musil"
    :registry/patron "ulrich"
    :registry/borrower "agatha"
    :registry/borrow-date (instant/read-instant-date "2023-07-28")
    :registry/due-date (instant/read-instant-date "2023-10-28")}])

@(d/transact conn data)

;; TODO, re-create this relationship in the context of Datomic

(def db (d/db conn))

(d/q '[:find ?e ?first-name ?last-name ?email
       :keys id first-name last-name email
       :where
       [?e :person/first-name ?first-name]
       [?e :person/last-name ?last-name]
       [?e :person/email ?email]]
     db)

(d/q '[:find [(pull ?e [*]) ...]
       :where [?e :person/first-name]]
     db)

(let [entity (d/entity db 17592186045420)]
  (select-keys entity [:person/first-name
                       :person/last-name
                       :person/email]))

;; Query
(d/q '[:find ?title ?borrow-date ?borrower-name ?patron-name
       :keys title borrow-date borrower patron
       :where
       [?registry :registry/book ?book]
       [?registry :registry/borrow-date ?borrow-date]
       [?registry :registry/borrower ?borrower]
       [?registry :registry/patron ?patron]
       [?book :book/title ?title]
       [?borrower :person/first-name ?borrower-name]
       [?borrower :person/email "agatha@kakania.at"]
       [?patron :person/first-name ?patron-name]]
     db)

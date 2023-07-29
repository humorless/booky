(ns gig.tutorial
  (:require [datomic.api :as d]))

(def db-uri "datomic:mem://hello")

(d/create-database db-uri)
;;(d/delete-database db-uri)

(def conn (d/connect db-uri))

(def movie-schema [{:db/ident       :movie/title
                    :db/valueType   :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc         "The title of the movie"}

                   {:db/ident       :movie/genre
                    :db/valueType   :db.type/string
                    :db/cardinality :db.cardinality/one
                    :db/doc         "The genre of the movie"}

                   {:db/ident       :movie/release-year
                    :db/valueType   :db.type/long
                    :db/cardinality :db.cardinality/one
                    :db/doc         "The year the movie was released in theaters"}])

(def first-movies [{:movie/title        "The Goonies"
                    :movie/genre        "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title        "Commando"
                    :movie/genre        "action/adventure"
                    :movie/release-year 1985}
                   {:movie/title        "Repo Man"
                    :movie/genre        "punk dystopia"
                    :movie/release-year 1984}])

(comment
  @(d/transact conn movie-schema)
  @(d/transact conn first-movies)
  )

(def db (d/db conn))

;; find ids (entity ids)
(def all-movies-q '[:find ?e
                    :where [?e :movie/title]])

(d/q all-movies-q db)
;; => #{[17592186045418] [17592186045419] [17592186045420]}

(def all-titles-q '[:find ?movie-title
                    :where [_ :movie/title ?movie-title]])

;; get actual values, titles
(d/q all-titles-q db)
;; naming here is irrelevant, either ?movie-titles or ?mov-tits is fine, just like anything else
;; => #{["Commando"] ["The Goonies"] ["Repo Man"]}

(def titles-from-1985 '[:find ?title
                        :where [?e :movie/title ?title]
                        [?e :movie/release-year 1985]])

(d/q titles-from-1985 db)

(def all-data-from-1985 '[:find ?title ?year ?genre
                          :where [?e :movie/title ?title]
                          [?e :movie/release-year ?year]
                          [?e :movie/genre ?genre]
                          [?e :movie/release-year 1985]])

(d/q all-data-from-1985 db)


;; now let's transact new data for the existing entity/datum
(def commando-id
  (ffirst (d/q '[:find ?e
                 :where [?e :movie/title "Commando"]]
               db)))

@(d/transact conn [{:db/id commando-id :movie/genre "future governor"}])
;; => #{["The Goonies" 1985 "action/adventure"] ["Commando" 1985 "action/adventure"]}
;; Why? Because, we issued transaction against the connection, but we're querying the database value we've created before. Database value is a snapshot in time.


(def old-db (d/as-of db 1004))
;; t is time basis 1004 is a value of it
;; d/since is also applicable which does the opposite of as-of

(d/q all-data-from-1985 old-db)


;; get all the values for the database
(def hdb (d/history db))

(d/q '[:find ?genre
       :where [?e :movie/title "Commando"]
       [?e :movie/genre ?genre]]
     hdb)

;; This way we can see all the values we transacted
;; => #{["action/adventure"] ["future governor"]}

"
Find Spec Returns Java Type Returned
:find ?a ?b relation Collection of Lists
:find [?a …] collection Collection
:find [?a ?b] single tuple List
:find ?a . single scalar Scalar Value
"

(d/q '[:find ?genre
       :keys genre
       :where [?e :movie/title "Commando"]
       [?e :movie/genre ?genre]]
     hdb)

(ns app.server
  (:require [clojure.java.jdbc :as j]
            [maailma.core :as m]
            [app.template :as template]
            [ring.util.response :refer [response header redirect status not-found content-type]]
            [taoensso.timbre :as log]
            [hikari-cp.core :as cp]
            [honey.sql :as sql]
            [honey.sql.helpers :refer [select select-distinct from join where order-by limit offset] :as helpers]
            [clojure.core :as c]))

(set! *warn-on-reflection* true)

(def config
  (m/build-config
    (m/resource "config-defaults.edn")
    (m/resource "config-local.edn")
    (m/env "PROXY")))

(def datasource-options
  (merge (:hikaricp config)
         {:auto-commit        true
          :read-only          false
          :adapter            "postgresql"
          :connection-timeout 5000
          :validation-timeout 5000
          :idle-timeout       1200000
          :max-lifetime       3600000
          :minimum-idle       2
          :maximum-pool-size  10
          :pool-name          "db-postgresql-pool"
          :register-mbeans    false}))

(def image-datasource-options
  (merge (:db-image config)
         {:auto-commit        true
          :read-only          false
          :adapter            "postgresql"
          :connection-timeout 5000
          :validation-timeout 5000
          :idle-timeout       1200000
          :max-lifetime       3600000
          :minimum-idle       2
          :maximum-pool-size  20
          :pool-name          "db-postgresql-image-pool"
          :register-mbeans    false}))

(defonce image-datasource
  (delay (cp/make-datasource image-datasource-options)))

(defonce datasource
  (delay (cp/make-datasource datasource-options)))

(def db {:datasource @datasource})

(defn get-tags [db server]
  (->> (j/query db ["SELECT tagitem.name FROM server JOIN tag ON server.id = server_id JOIN tagitem ON tagitem.id = tagitem_id WHERE server.id = ?" (:id server)])
       (map vals)
       (flatten)))

(defn inject-tags [servers]
  (pmap #(assoc % :tags (get-tags db %)) servers))

(defn inject-banners [data]
  (doall
    (->> (pmap #(assoc % :banner (str "https://www.mcserverinfo.com/b/" (:id %))) data)
         (pmap #(assoc % :favicon (str "https://www.mcserverinfo.com/f/" (:id %))) )
         (pmap #(assoc % :url (str "https://www.serverrate.com/s/" (:id %))) ))))

(defn get-top-ranked-html [req]
  (-> (j/query db
               (-> (select :server.id :server.name :server.address :server.players :server.maxplayers :server.version)
                   (from :server)
                   (join :banners [:= :server.id :banners.server_id])
                   (where [:not= :banners.server_id nil] )
                   (order-by [:server.crank :asc])
                   (limit 20)
                   sql/format))
      (inject-banners)
      (inject-tags)
      (->> (map template/server-template))
      response
      (header "Cache-Control" "public, max-age=3600")))

(defn serve-banner [id]
  (if-let [{image :image image_type :image_type} (j/query {:datasource @image-datasource} ["SELECT image, image_type FROM banners WHERE server_id = ?" id]
                                                                                          {:result-set-fn first})]
    (-> {:status 200 :body image} 
        (content-type image_type)
        (header "Cache-Control" "public, max-age=432000"))
    (not-found nil)))

(defn serve-favicon [id]
  (if-let [{icon :icon} (j/query db ["SELECT icon FROM favicons WHERE server_id = ?" id] {:result-set-fn first})]
    (header (content-type {:status 200 :body icon} "image/png") "Cache-Control" "public, max-age=86400")
    (not-found nil)))


(ns app.route
  (:require [compojure.core :refer :all]
            [compojure.coercions :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response header redirect status not-found content-type]]
            [ring.logger :as logger]
            [taoensso.timbre :as log]))

(use 'ring.middleware.resource
     'ring.middleware.content-type
     'ring.middleware.not-modified)

(require '[ring.middleware.defaults :refer :all])
(require '[app.server :as server])
(require '[app.template :as template])

(defroutes app-routes
  (GET "/" _ (-> (response (template/index (server/get-top-ranked-html nil)))
                 (content-type "text/html; charset=utf-8")
                 (header "Cache-Control" "public, max-age=3600")))
  (GET "/b/:id" [id :<< as-int] (server/serve-banner id))
  (GET "/f/:id" [id :<< as-int] (server/serve-favicon id))
  (HEAD "/" _ {:status 200 :body nil})
  (route/not-found "Page Not Found"))

(def app
  (-> app-routes
      (ring.logger/wrap-log-response {:log-fn (fn [{:keys [level throwable message]}] (log/log level throwable message))})
      (wrap-defaults (assoc site-defaults :session false :cookies false :params false) )))

(ns app.template
  (:require [ring.util.response :refer [response header redirect status not-found content-type]]
            [clojure.string :refer [split]]
            [taoensso.timbre :as log]))

(use 'hiccup.core)
(use 'hiccup.page)

(defn index [{:keys [body] :as all}]
  (html5 {:lang "en"}
    [:head
     [:meta {:charset "UTF-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1, shrink-to-fit=no"}]
     [:meta {:name "description" :content "We list the top, best and popular Minecraft servers on the web to play on in the world today. Find the perfect server you are looking for right now!"}]
     [:meta {:name "author" :content "mcserverinfo.com"}]
     [:meta {:name "keywords" :content "best top popular minecraft servers listing"}]
     [:title "The Best and Top Minecraft Servers"]
     [:link {:rel "apple-touch-icon" :sizes "180x180" :href "/assets/apple-touch-icon.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/assets/favicon-32x32.png"}]
     [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/assets/favicon-16x16.png"}]
     [:link {:rel "manifest" :href "/assets/site.webmanifest"}]
     (include-css "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css")
     (include-css "https://use.fontawesome.com/releases/v5.8.1/css/all.css")
     (include-css "/css/site.css")]
    [:body
     [:br]
     [:div.container
      [:div.card.shadow.bg-light
       [:div.card-body
        [:h1.text-center "The Best and Top Minecraft Servers"]
        [:p.lead.d-none.d-sm-block.mt-3.mb-4.col-10.offset-1 "We list the top, best and popular Minecraft servers in the world.
                Scroll down our list and discover awesome servers. Find one you like the look of or a tag that stands out?
                Click on a server to learn more about it or just press the copy button and paste the address into your Minecraft client and check it out for yourself."]
        [:section
         (for [x body]
          [:div.row.mt-4 x])]]]]
     [:br]
     [:div.container
      [:div.card.shadow
       [:div.card-body.center.row
        [:div.col-12
         [:p.text-center [:small "\"Minecraft\" is a trademark of Mojang Synergies AB and is not affiliated in any way with this site. &copy; 2022 All Rights Reserved."]]]]]]
     (include-js "https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.8/clipboard.min.js")
     (include-js "/site.js")]))

(defn server-template [{:keys [url favicon name banner version tags address players maxplayers] :as server}]
  (html
   [:div.col-md-3.col-sm-4.hidden-xs.d-flex.align-items-center.justify-content-center
    [:p.h4.d-none.d-sm-block name]]
   [:div.col-md-6.col-sm-8
    [:a {:href url} [:img.img-fluid.w-100 {:src banner :alt name}]]
    [:br]
    [:span.badge.bg-primary.cat version]
    (map #(-> [:span.badge.bg-dark.cat %]) tags)
    [:div.mt-2.d-grid.gap-2.col-12.mx-auto
      [:button.copy.btn.btn-outline-primary.btn-lg {:data-clipboard-text address} [:i.far.fa-fw.fa-clipboard {:aria-hidden "true"}] (first (split address #":"))]]]
   [:div.col-md-3.col-sm-4.hidden-xs.d-flex.align-items-center.justify-content-center
    [:span.h4 (format "%,d" players)]
    [:small.text-muted (format "/%,d" maxplayers)]]))

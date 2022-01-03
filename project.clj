(defproject mcserverinfo-ssr "0.1.0-SNAPSHOT"
  :description "mcseverinfo ssr version"
  :url "https://www.mcserverinfo.com/"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [compojure "1.6.2"]
                 [ring "1.9.4"]
                 [ring/ring-defaults "0.3.3"]
                 [metosin/maailma "1.1.0"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.postgresql/postgresql "42.2.22"]
                 [hiccup "1.0.5"]
                 [ring-logger "1.0.1"]
                 [com.github.seancorfield/honeysql "2.2.840"]
                 [hikari-cp "2.13.0"]
                 [com.taoensso/timbre "5.1.2"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler app.route/app}
  :repl-options {:init-ns app.route}
  :profiles {:uberjar {:aot :all :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

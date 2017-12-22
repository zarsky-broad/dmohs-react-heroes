(defproject org.broadinstitute/dmohs-react-heroes "0.0.1"
  :dependencies
  [
   [dmohs/react "1.3.0"]
   [org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.9.946"]
   ]
  :plugins [[lein-cljsbuild "1.1.7"] [lein-figwheel "0.5.14"] [lein-resource "17.06.1"]]
  :profiles {:dev
             {:dependencies [[binaryage/devtools "0.9.7"]]
              :cljsbuild
              {:builds
               {:client
                {:source-paths ["src/cljs/main"]
                 :figwheel true
                 :compiler
                 {:main "dmohs_react.heroes.main"
                  :optimizations :none
                  :asset-path "target/build"
                  :pretty-print true
                  :anon-fn-naming-policy :mapped
                  :preloads [devtools.preload]
                  :external-config {:devtools/config {:features-to-install
                                                      [:formatters :hints]}}}}}}}
             :deploy
             {:cljsbuild
              {:builds
               {:client
                {:compiler
                 {:optimizations :simple
                  :static-fns true
                  :fn-invoke-direct true
                  :elide-asserts true
                  :language-out :ecmascript5
                  :optimize-constants true
                  :output-dir "build"}}}}}}
  :target-path "resources/public/target"
  :clean-targets ^{:protect false} [:target-path]
  :cljsbuild {:builds {:client {:source-paths ["src/cljs/main"]
                                :compiler {:main "broadfcui.main"
                                           :output-dir "resources/public/target/build"
                                           :output-to "resources/public/target/compiled.js"}}}}
  :resource {:resource-paths ["src/static"]
             :target-path "resources/public"
             :excludes [#".*\.DS_Store"]
             :skip-stencil [#"src/static/assets/.*"]
             :extra-values {:vtag ~(.getTime (java.util.Date.))}})

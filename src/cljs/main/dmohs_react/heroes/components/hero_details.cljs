(ns dmohs-react.heroes.components.hero-details
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.nav :as nav]
   ))

(react/defc HeroDetails
  {:render
   (fn [])})

(defn add-nav-paths []
  (nav/defpath
   :details
   {:component HeroDetails
    :regex #"details/([^/]+)"
    :make-props (fn [id]
                  {:hero-id id})
    :make-path (fn [id]
                 (str "details/" id))}))

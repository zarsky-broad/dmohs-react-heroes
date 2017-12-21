(ns dmohs-react.heroes.components.hero-details
  (:require
   [clojure.string :as string]
   [dmohs.react :as react]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc HeroDetails
  {:component-will-mount
   (fn [{:keys [state props]}]
     (hero-service/get-hero (:hero-id props) #(swap! state assoc :hero %)))
   :render
   (fn [{:keys [state]}]
     (let [{:keys [id name]} (:hero @state)]
       [:div {}
        [:h2 {:style (:h2-and-h3 style/elements)}
         (string/upper-case name) " Details"]
        "id: " id
        ]))})

(defn add-nav-paths []
  (nav/defpath
   :details
   {:component HeroDetails
    :regex #"details/([^/]+)"
    :make-props (fn [id]
                  {:hero-id (int id)})
    :make-path (fn [id]
                 (str "details/" id))}))

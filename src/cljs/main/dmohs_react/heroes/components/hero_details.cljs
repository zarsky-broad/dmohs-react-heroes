(ns dmohs-react.heroes.components.hero-details
  (:require
   [clojure.string :as string]
   [dmohs.react :as react]
   [dmohs-react.heroes.elements :as elements]
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
     (let [{:keys [id name] :as hero} (:hero @state)
           make-button (fn [button-props label]
                         [elements/Hover
                          {:element-key :button
                           :props (utils/deep-merge
                                   {:style (assoc (:button style/elements)
                                             :marginTop 20 :padding "5px 10px")}
                                   button-props)
                           :hover-props {:style {:backgroundColor style/light-bluish}}
                           :child label}])]
       [:div {}
        [:h2 {:style (:h2-and-h3 style/elements)}
         (string/upper-case name) " Details"]
        "id: " id
        [:label {:style {:display "block"
                         :color style/dark-bluish :fontWeight "bold"
                         :margin "0.5em 0" :width "3em"}}
         "name:"
         [:input {:style {:height "2em" :fontSize "1em" :paddingLeft "0.4em"}
                  :defaultValue name
                  :onChange #(swap! state assoc :new-name (.. % -target -value))}]]
        (make-button {:style {:marginRight "0.5em"}
                      :onClick #(js/history.back)}
                     "go back")
        (make-button {:onClick #(do
                                  (hero-service/update-hero id (:new-name @state))
                                  (js/history.back))}
                     "save")]))})

(defn add-nav-paths []
  (nav/defpath
   :details
   {:component HeroDetails
    :regex #"details/([^/]+)"
    :make-props (fn [id]
                  {:hero-id (int id)})
    :make-path (fn [id]
                 (str "details/" id))}))

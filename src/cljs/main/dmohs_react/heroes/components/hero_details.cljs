(ns dmohs-react.heroes.components.hero-details
  (:require
   [dmohs.react :as react]
   [org.broadinstitute.uicomps.nav :as nav]
   [clojure.string :as string]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc HeroDetails
  ":hero-id - ID of hero to display."
  {:component-will-mount
   (fn [{:keys [state props]}]
     (hero-service/get-hero (:hero-id props) #(swap! state assoc :hero %)))
   :render
   (fn [{:keys [state]}]
     (let [{:keys [id name] :as hero} (:hero @state)
           make-button (fn [button-props label]
                         (style/add-hover-style
                          [:button (utils/deep-merge
                                    {:style (assoc (:button style/elements)
                                              :marginTop 20 :padding "5px 10px")
                                     :hover-style {:backgroundColor (:light-bluish style/colors)}}
                                    button-props)
                           label]))]
       [:div {}
        [:h2 {:style (:h2-and-h3 style/elements)}
         (string/upper-case name) " Details"]
        "id: " id
        [:label {:style {:display "block"
                         :color (:dark-bluish style/colors) :fontWeight "bold"
                         :margin "0.5em 0" :width "3em"}}
         "name:"
         [:input {:style {:height "2em" :fontSize "1em" :paddingLeft "0.4em"}
                  :value name
                  :onChange #(swap! state assoc-in [:hero :name] (.. % -target -value))}]]
        (make-button {:style {:marginRight "0.5em"}
                      :onClick #(js/history.back)}
                     "go back")
        (make-button {:onClick #(do
                                  (hero-service/update-hero id name)
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

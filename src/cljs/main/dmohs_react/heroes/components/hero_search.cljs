(ns dmohs-react.heroes.components.hero-search
  (:require
   [clojure.string :as string]
   [dmohs.react :as react]
   [dmohs-react.heroes.elements :as elements]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc HeroSearch
  {:render
   (fn [{:keys [state]}]
     (let [{:keys [query]} @state]
       [:div {}
        [:h4 {:style {:marginTop 0}} "Hero Search"]
        [:input {:style {:width 200 :height 20}
                 :onChange (fn [e]
                             (hero-service/search-heroes
                              (.. e -target -value)
                              #(swap! state assoc :filtered-heroes %)))}]
        [:div {:style {:marginBottom "1em"}}
         (map (fn [{:keys [id name]}]
                [elements/Hover
                 {:element-key :a
                  :props {:href (nav/get-link :details id)
                          :style {:textDecoration "none" :display "block"
                                  :color "#888"
                                  :border "1px solid #808080" :borderTop "none" :padding 5
                                  :width 195 :height 16}}
                  :hover-props {:style {:backgroundColor "#607D8B" :color "#fff"}}
                  :child name}])
              (:filtered-heroes @state))]]))})

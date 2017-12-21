(ns dmohs-react.heroes.components.hero-list
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc- HeroCreator
  {:render
   (fn [{:keys [state]}]
     (let [{:keys [value]} @state]
       [:div {}
        [:label {} "Hero name:"
         [:input {:style {:margin "0 0.5em"}
                  :value (or value "")
                  :onChange #(swap! state assoc :value (.. % -target -value))}]]
        [:button {:style (:button style/elements)
                  :onClick (fn []
                             (hero-service/add-hero value)
                             (swap! state assoc :value ""))}
         "add"]]))})

(react/defc HeroList
  {:component-will-mount
   (fn [{:keys [this]}]
     (add-watch hero-service/local-heroes :dashboard
                (fn []
                  (.forceUpdate this))))
   :render
   (fn [{:keys [state]}]
     [:div {}
      [:h2 {:style (:h2-and-h3 style/elements)} "My Heroes"]
      [HeroCreator]
      [:ul {}
       (map (fn [{:keys [id name]}]
              [:li {}
               [:a {:href (nav/get-link :details id)}
                [:span {} id]
                name]])
            @hero-service/local-heroes)]])
   :component-will-unmount
   (fn []
     (remove-watch hero-service/local-heroes :dashboard))})

(defn add-nav-paths []
  (nav/defpath
   :list
   {:component HeroList
    :regex #"heroes"
    :make-props (fn [_] {})
    :make-path (fn [] "heroes")}))

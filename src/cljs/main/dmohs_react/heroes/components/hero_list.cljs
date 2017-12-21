(ns dmohs-react.heroes.components.hero-list
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc HeroList
  {:component-will-mount
   (fn [{:keys [this]}]
     (add-watch hero-service/local-heroes :dashboard
                (fn []
                  (.forceUpdate this))))
   :render
   (fn [{:keys [state]}]
     [:div {}
      [:h3 {} "My Heroes"]
      [:div {}]
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

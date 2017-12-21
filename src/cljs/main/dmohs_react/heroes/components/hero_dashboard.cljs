(ns dmohs-react.heroes.components.hero-dashboard
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.components.hero-search :refer [HeroSearch]]
   [dmohs-react.heroes.elements :as elements]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc HeroDashboard
  {:component-will-mount
   (fn [{:keys [this]}]
     (add-watch hero-service/local-heroes :dashboard
                (fn []
                  (.forceUpdate this))))
   :render
   (fn [{:keys [state]}]
     [:div {}
      [:h3 {:style (merge
                    {:textAlign "center" :marginBottom 0}
                    (:h2-and-h3 style/elements))}
       "Top Heroes"]
      [:div {:style {:display "flex" :paddingTop 10}}
       (map (fn [{:keys [id name]}]
              [:a {:href (nav/get-link :details id)
                   :style {:width "25%" :paddingBottom 20 :paddingRight 20
                           :textDecoration "none"}}
               [elements/Hover
                {:element-key :div
                 :props {:style {:borderRadius 2 :padding 20
                                 :color "#eee" :backgroundColor "#607D8B"}}
                 :hover-props {:style {:color "#607d8b" :backgroundColor "#eee"}}
                 :children [:h4 {:style {:textAlign "center"}} name]}]])
            (subvec @hero-service/local-heroes 1 5))]
      [HeroSearch]])
   :component-will-unmount
   (fn []
     (remove-watch hero-service/local-heroes :dashboard))})

(defn add-nav-paths []
  (nav/defredirect {:regex #"" :make-path (fn [] "dashboard")})
  (nav/defpath
   :dashboard
   {:component HeroDashboard
    :regex #"dashboard"
    :make-props (fn [_] {})
    :make-path (fn [] "dashboard")}))

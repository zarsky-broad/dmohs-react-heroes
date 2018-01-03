(ns dmohs-react.heroes.components.hero-dashboard
  (:require
   [dmohs.react :as react]
   [org.broadinstitute.uicomps.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc- HeroSearch
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
                (utils/add-hover-style
                 [:a {:href (nav/get-link :details id)
                      :style {:textDecoration "none" :display "block"
                              :color "#888"
                              :border "1px solid #808080" :borderTop "none" :padding 5
                              :width 195 :height 16}
                      :hover-style {:backgroundColor (:dark-bluish style/colors) :color "#fff"}}
                  name]))
              (:filtered-heroes @state))]]))})

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
               (utils/add-hover-style
                [:div {:style {:borderRadius 2 :padding 20
                               :color "#eee" :backgroundColor (:dark-bluish style/colors)}
                       :hover-style {:color (:dark-bluish style/colors) :backgroundColor "#eee"}}
                 [:h4 {:style {:textAlign "center"}} name]])])
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

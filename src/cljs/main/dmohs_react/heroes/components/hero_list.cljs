(ns dmohs-react.heroes.components.hero-list
  (:require
   [dmohs.react :as react]
   [org.broadinstitute.uicomps.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.style :as style]
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
      (map (fn [{:keys [id name]}]
             (style/add-hover-style
              [:a {:style {:display "block"
                           :backgroundColor "#eee" :color "#888"
                           :margin "0.5em" :padding "0.3em 0" :borderRadius 4
                           :height "1.6em" :width "15em"
                           :textDecoration "none"}
                   :hover-style {:backgroundColor "#ddd" :color (:dark-bluish style/colors)
                                 :position "relative" :left "0.1em"}
                   :href (nav/get-link :details id)}
               [:span {:style {:display "inline-block"
                               :backgroundColor (:dark-bluish style/colors) :color "#fff"
                               :font-size "small" :textAlign "right" :lineHeight "1em"
                               :margin "-0.3em 0.8em 0 -1px" :padding "0.8em 0.7em 0 0.7em"
                               :borderRadius "4px 0 0 4px"
                               :height "1.8em" :width 16}}
                id]
               name
               [:button {:style (merge (:button style/elements)
                                       {:backgroundColor "#808080" :color "#fff"
                                        :float "right" :marginRight 4})
                         :onClick (fn [e]
                                    (.preventDefault e)
                                    (hero-service/delete-hero id))}
                "x"]]))
           @hero-service/local-heroes)])
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

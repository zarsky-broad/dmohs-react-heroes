(ns dmohs-react.heroes.main
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.components.hero-dashboard :as dashboard]
   [dmohs-react.heroes.components.hero-details :as details]
   [dmohs-react.heroes.components.hero-list :as list]
   [org.broadinstitute.uicomps.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.utils :as utils]
   [dmohs-react.heroes.style :as style]
   ))

(defn- init-nav-paths []
  (nav/clear-paths)
  (list/add-nav-paths)
  (dashboard/add-nav-paths)
  (details/add-nav-paths))

(react/defc App
  {:handle-hash-change
   (fn [{:keys [state]}]
     (let [window-hash (aget js/window "location" "hash")]
       (when-not (nav/execute-redirects window-hash)
         (swap! state assoc :window-hash window-hash))))
   :component-will-mount
   (fn [{:keys [this state]}]
     (init-nav-paths)
     (this :handle-hash-change)
     (hero-service/get-heroes #(swap! state assoc :loaded? true)))
   :render
   (fn [{:keys [props state]}]
     (let [{:keys [window-hash loaded?]} @state
           {:keys [component make-props]} (nav/find-path-handler (str window-hash))
           make-nav-link (fn [props label]
                           (utils/add-hover-style
                            [:a (merge
                                 {:style {:display "inline-block"
                                          :padding "5px 10px" :marginTop 10 :marginRight 10
                                          :backgroundColor "#eee" :borderRadius 4
                                          :textDecoration "none"}
                                  :hover-style {:color "#039be5" :backgroundColor (:light-bluish style/colors)}}
                                 props)
                             label]))]
       [:div {}
        [:h1 {:style {:fontSize "1.2em" :color "#999" :marginBottom 0}}
         (:title props)]
        [:nav {:style {:paddingTop 10}}
         (make-nav-link {:href (nav/get-link :dashboard)} "Dashboard")
         (make-nav-link {:href (nav/get-link :list)} "Heroes")]
        [:div {:style {:marginTop "1rem"}}
         (cond
           (not loaded?) [:h2 {} "Loading heroes..."]
           component [component (make-props)])]]))
   :component-did-mount
   (fn [{:keys [locals this]}]
     (swap! locals assoc :hash-change-listener (partial this :handle-hash-change))
     (.addEventListener js/window "hashchange" (:hash-change-listener @locals)))
   :component-will-receive-props
   (fn []
     (init-nav-paths))
   :component-will-unmount
   (fn [{:keys [locals]}]
     (.removeEventListener js/window "hashchange" (:hash-change-listener @locals)))})

(defn render-application []
  (react/render (react/create-element App {:title "Tour of Heroes"}) (.. js/document (getElementById "app"))))

(render-application)

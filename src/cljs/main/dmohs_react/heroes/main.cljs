(ns dmohs-react.heroes.main
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.components.hero-dashboard :as dashboard]
   [dmohs-react.heroes.components.hero-details :as details]
   [dmohs-react.heroes.components.hero-list :as list]
   [dmohs-react.heroes.elements :as elements]
   [dmohs-react.heroes.nav :as nav]
   [dmohs-react.heroes.services.hero-service :as hero-service]
   [dmohs-react.heroes.utils :as utils]
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
   :get-default-props
   (fn []
     {:title "Tour of Heroes"})
   :component-will-mount
   (fn [{:keys [this state]}]
     (init-nav-paths)
     (this :handle-hash-change)
     (hero-service/get-heroes #(swap! state assoc :loaded? true)))
   :render
   (fn [{:keys [props state]}]
     (let [{:keys [window-hash loaded?]} @state
           {:keys [component make-props]} (nav/find-path-handler (str window-hash))]
       [:div {}
        [:h1 {:style {:fontSize "1.2em" :color "#999" :marginBottom 0}}
         (:title props)]
        [:nav {}
         (elements/make-nav-link {:href (nav/get-link :dashboard)} "Dashboard")
         (elements/make-nav-link {:href (nav/get-link :list)} "Heroes")]
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
  (react/render (react/create-element App) (.. js/document (getElementById "app"))))

(render-application)

(ns dmohs-react.heroes.elements
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc Hover
  {:render
   (fn [{:keys [props state]}]
     (let [{:keys [element-key props hover-props children]} props
           {:keys [hovering?]} @state]
       [element-key
        (assoc (if hovering?
                 (utils/deep-merge props hover-props)
                 props)
          :onMouseOver #(swap! state assoc :hovering? true)
          :onMouseOut #(swap! state dissoc :hovering?))
        children]))})

(defn make-button [props & contents]
  [:button (utils/deep-merge
            {:style {:color "#888" :backgroundColor "#eee"
                     :border "none" :borderRadius 4
                     :padding "5px 10px"
                     :cursor "pointer"}}
            props)
   contents])

(defn make-text-input [props & contents]
  [:input (utils/deep-merge
           {:style {:color "#888" :backgroundColor "#eee"
                    :border "none" :borderRadius 4
                    :padding "5px 10px"
                    :cursor "pointer"}}
           props)
   contents])

(defn make-nav-link [props label]
  [Hover {:element-key :a
          :props (utils/deep-merge
                  {:style {:padding "5px 10px"
                           :marginTop 10 :marginRight 10
                           :textDecoration "none"
                           :display "inline-block"
                           :backgroundColor "#eee"
                           :borderRadius 4}}
                  props)
          :hover-props {:style {:color "#039be5" :backgroundColor "#cfd8dc"}}
          :children label}])

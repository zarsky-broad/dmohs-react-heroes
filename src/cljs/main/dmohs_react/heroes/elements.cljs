(ns dmohs-react.heroes.elements
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.style :as style]
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
        (seq children)]))})

(defn make-nav-link [props label]
  [Hover {:element-key :a
          :props (utils/deep-merge
                  {:style (:nav>a style/elements)}
                  props)
          :hover-props {:style {:color "#039be5" :backgroundColor "#cfd8dc"}}
          :children label}])

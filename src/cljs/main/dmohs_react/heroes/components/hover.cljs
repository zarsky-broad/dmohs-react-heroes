(ns dmohs-react.heroes.components.hover
  (:require
   [dmohs.react :as react]
   [dmohs-react.heroes.utils :as utils]
   ))

(react/defc Hover
  {:render
   (fn [{:keys [props state]}]
     (let [{:keys [element-key props hover-props child]} props
           {:keys [hovering?]} @state]
       [element-key
        (assoc (if hovering?
                 (utils/deep-merge props hover-props)
                 props)
          :onMouseOver #(swap! state assoc :hovering? true)
          :onMouseOut #(swap! state dissoc :hovering?))
        child]))})

(ns dmohs-react.heroes.style
  (:require
   [clojure.string :as string]
   ))

(def colors
  {:dark-bluish "#607d8b"
   :light-bluish "#cfd8dc"})

(def elements
  {:h2-and-h3 {:color "#444" :fontWeight "lighter"}
   :button {:color "#888" :backgroundColor "#eee"
            :border "none" :borderRadius 4
            :padding "5px 10px"
            :cursor "pointer"}})

(defn transform-style-map-to-css [styles & [!important?]]
  (reduce
   (fn [prev [k v]]
     (str prev
          (string/replace (name k) #"[A-Z]" #(str "-" (string/lower-case %))) ":"
          v (when (number? v) "px") (when !important? " !important") ";"))
   ""
   styles))

(defn add-hover-style
  "Takes an element with :hover-style in its props and causes
  those styles to be applied on hover."
  [[element-key {:keys [hover-style] :as props} :as element]]
  (let [hover-id (name (gensym "hover"))
        cleaned-props (-> props (assoc :data-hover-style-id hover-id) (dissoc :hover-style))
        css-string (str "[data-hover-style-id=\"" hover-id "\"]:hover {"
                        (transform-style-map-to-css hover-style true)
                        "}")]
    (into [element-key cleaned-props
           [:style {} css-string]]
          (subvec element 2))))

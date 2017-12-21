(ns dmohs-react.heroes.services.hero-service
  (:require
   [clojure.string :as string]
   [dmohs-react.heroes.utils :as utils]
   ))

(defonce ^:private mock-heroes
         [{:id 11 :name "Mr. Nice"}
          {:id 12 :name "Narco"}
          {:id 13 :name "Bombasto"}
          {:id 14 :name "Celeritas"}
          {:id 15 :name "Magneta"}
          {:id 16 :name "RubberMan"}
          {:id 17 :name "Dynama"}
          {:id 18 :name "Dr IQ"}
          {:id 19 :name "Magma"}
          {:id 20 :name "Tornado"}])

(defonce local-heroes (atom (if @utils/use-live-data? false mock-heroes)))

(defn get-heroes
  ([]
   (get-heroes (constantly nil)))
  ([on-done]
   (if @utils/use-live-data?
     (utils/ajax {:url nil
                  :on-done (fn [{:keys [get-parsed-response]}]
                             (on-done (reset! local-heroes (get-parsed-response))))})
     (on-done @local-heroes))))

(defn search-heroes [query on-done]
  (let [query (string/trim query)]
    (if (string/blank? query)
      (on-done [])
      (get-heroes
       (fn [result]
         (on-done
          (filter
           #(re-find
             (re-pattern (string/lower-case query))
             (string/lower-case (:name %)))
           result)))))))

(defn add-hero [hero-name]
  (when-not (string/blank? hero-name)
    (if @utils/use-live-data?
      (utils/ajax {:url nil
                   :method :post
                   :data hero-name
                   :on-done (fn [{:keys [get-parsed-response]}]
                              (get-heroes))})
      (swap! local-heroes conj
             {:id (inc (:id (apply max-key :id @local-heroes))) :name hero-name}))))

(defn delete-hero [hero-id]
  (if @utils/use-live-data?
    (utils/ajax {:url (str nil "/" hero-id)
                 :method :delete
                 :on-done (fn [{:keys [get-parsed-response]}]
                            (get-heroes))})
    (swap! local-heroes (partial filter #(not= hero-id (:id %))))))

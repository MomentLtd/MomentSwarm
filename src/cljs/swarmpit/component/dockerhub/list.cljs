(ns swarmpit.component.dockerhub.list
  (:require [material.component :as comp]
            [material.icon :as icon]
            [swarmpit.component.state :as state]
            [swarmpit.routes :as routes]
            [clojure.string :as string]
            [rum.core :as rum]))

(def cursor [:page :dockerhub :list :filter])

(def headers ["Username" "Name" "Company"])

(def render-item-keys
  [[:username] [:name] [:company]])

(defn- render-item
  [item]
  (val item))

(defn- onclick-handler
  [item]
  (routes/path-for-frontend :dockerhub-user-info {:id (:_id item)}))

(defn- filter-items
  "Filter list items based on given predicate"
  [items predicate]
  (filter #(string/includes? (:username %) predicate) items))

(rum/defc dockeruser-list < rum/reactive [items]
  (let [{:keys [username]} (state/react cursor)
        filtered-items (filter-items items username)]
    [:div
     [:div.form-panel
      [:div.form-panel-left
       (comp/panel-text-field
         {:hintText "Filter by username"
          :onChange (fn [_ v]
                      (state/update-value [:username] v cursor))})]
      [:div.form-panel-right
       (comp/mui
         (comp/raised-button
           {:href    (routes/path-for-frontend :dockerhub-user-create)
            :label   "Add User"
            :primary true}))]]
     (comp/list-table headers
                      filtered-items
                      render-item
                      render-item-keys
                      onclick-handler)]))

(defn- init-state
  []
  (state/set-value {:username ""} cursor))

(defn mount!
  [items]
  (init-state)
  (rum/mount (dockeruser-list items) (.getElementById js/document "content")))
(ns aws-client-component.sqs.producer
  (:require [aws-client-component.sqs.operations :as sqs.operations]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [medley.core :as medley]))

(defmethod ig/init-key ::sqs-producer
  [_ {:keys [components]}]
  (log/info :starting ::sqs-producer)

  (when (= (-> components :config :current-env) :prod)
    (try (sqs.operations/list-queues (:aws-sqs-client components))
         (catch Exception ex
           (log/error :invalid-credentials :exception ex)
           (throw ex)))
    (sqs.operations/create-sqs-queues! (-> components :config :queues keys) (:aws-sqs-client components)))

  (medley/assoc-some {:current-env    (-> components :config :current-env)
                      :aws-sqs-client (:aws-sqs-client components)}
                     :produced-messages (when (= (-> components :config :current-env) :test)
                                          (atom []))))

(defmethod ig/halt-key! ::sqs-producer
  [_ _sqs-producer]
  (log/info :stopping ::sqs-producer))

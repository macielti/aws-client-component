(ns aws-client-component.sqs.client
  (:require [clojure.tools.logging :as log]
            [cognitect.aws.client.api :as aws]
            [cognitect.aws.credentials :as credentials]
            [cognitect.aws.http.cognitect :as aws.http]
            [integrant.core :as ig]))

(defmethod ig/init-key ::aws-sqs-client
  [_ {:keys [components]}]
  (log/info :starting ::aws-sqs-client)
  (let [{:keys [access-key secret-key region]} (-> components :config :aws-credentials)]
    (aws/client {:api                  :sqs
                 :region               region
                 :http-client          (aws.http/create)
                 :credentials-provider (credentials/basic-credentials-provider {:access-key-id     access-key
                                                                                :secret-access-key secret-key})})))

(defmethod ig/halt-key! ::aws-sqs-client
  [_ _aws-client]
  (log/info :stopping ::aws-sqs-client))

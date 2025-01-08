(ns aws-client-component.sqs.operations
  (:require [cognitect.aws.client.api :as aws]
            [common-clj.traceability.core :as common-traceability]
            [schema.core :as s]))

(defn fetch-queue-url
  [queue-name
   aws-sqs-client]
  (aws/invoke aws-sqs-client {:op      :GetQueueUrl
                              :request {:QueueName queue-name}}))

(defn fetch-messages
  [queue-url
   aws-sqs-client]
  (aws/invoke aws-sqs-client {:op      :ReceiveMessage
                              :request {:QueueUrl        queue-url
                                        :WaitTimeSeconds 20}}))

(defn create-queue!
  [queue-name
   aws-sqs-client]
  (aws/invoke aws-sqs-client {:op      :CreateQueue
                              :request {:QueueName queue-name}}))

(s/defn create-sqs-queues!
  [queues-names :- [s/Str]
   aws-sqs-client]
  (doseq [queue queues-names]
    (create-queue! queue aws-sqs-client)))

(defn delete-message!
  [receipt-handle
   queue-url
   aws-sqs-client]
  (aws/invoke aws-sqs-client {:op      :DeleteMessage
                              :request {:QueueUrl      queue-url
                                        :ReceiptHandle receipt-handle}}))

(defn list-queues
  [aws-sqs-client]
  (aws/invoke aws-sqs-client {:op :ListQueues}))

(defmulti send-message!
  (fn [_ {:keys [current-env]}]
    current-env))

(defmethod send-message! :prod
  [{:keys [queue payload]}
   {:keys [aws-sqs-client]}]
  (let [payload' (assoc payload :meta {:correlation-id (-> (common-traceability/current-correlation-id)
                                                           common-traceability/correlation-id-appended)})
        queue-url (:QueueUrl (fetch-queue-url queue aws-sqs-client))
        message-body (prn-str payload')]
    (aws/invoke aws-sqs-client {:op      :SendMessage
                                :request {:QueueUrl    queue-url
                                          :MessageBody message-body}})))

(defmethod send-message! :test
  [{:keys [queue payload]}
   {:keys [produced-messages]}]
  (let [payload' (assoc payload :meta {:correlation-id (-> (common-traceability/current-correlation-id)
                                                           common-traceability/correlation-id-appended)})]
    (swap! produced-messages conj {:queue   queue
                                   :payload payload'})))

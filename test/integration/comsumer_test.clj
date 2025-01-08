(ns comsumer-test
  (:require [aws-client-component.sqs.client :as component.sqs.client]
            [aws-client-component.sqs.consumer :as component.sqs.consumer]
            [aws-client-component.sqs.operations :as sqs.operations]
            [aws-client-component.sqs.producer :as component.sqs.producer]
            [clojure.test :refer [is testing]]
            [common-clj.integrant-components.config :as component.config]
            [integrant.core :as ig]
            [schema.core :as s]
            [schema.test :as st]
            [taoensso.timbre.tools.logging]))

(taoensso.timbre.tools.logging/use-timbre)

(def test-state (atom nil))

(s/defn message-handler
  [{message     :message
    _components :components}]
  (reset! test-state message))

(def consumers
  {"message_handler_test" {:schema     {:test s/Keyword}
                           :handler-fn message-handler}})

(def config
  {::component.config/config             {:path "resources/config.example.edn"
                                          :env  :test}
   ::component.sqs.client/aws-sqs-client {:components {:config (ig/ref ::component.config/config)}}
   ::component.sqs.producer/sqs-producer {:components {:config         (ig/ref ::component.config/config)
                                                       :aws-sqs-client (ig/ref ::component.sqs.client/aws-sqs-client)}}
   ::component.sqs.consumer/sqs-consumer {:consumers  consumers
                                          :components {:producer (ig/ref ::component.sqs.producer/sqs-producer)
                                                       :config   (ig/ref ::component.config/config)}}})

(st/deftest create-and-fetch-one-menu-test
  (let [system (ig/init config)]
    (sqs.operations/send-message! {:queue   "message_handler_test"
                                   :payload {:test :ok}}
                                  (::component.sqs.producer/sqs-producer system))

    (Thread/sleep 1000)

    (testing "That we are able to consume produced messages"
      (is (= {:test :ok}
             @test-state)))

    (ig/halt! system)))

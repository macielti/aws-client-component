(defproject net.clojars.macielti/aws-client-component "0.1.0-17"

  :description "AWS Client Integrant component"

  :url "https://github.com/macielti/aws-client-component"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.0"]
                 [integrant "0.13.1"]
                 [com.cognitect.aws/api "0.8.692"]
                 [com.cognitect.aws/endpoints "1.1.12.772"]
                 [com.cognitect.aws/sqs "871.2.29.35"]
                 [net.clojars.macielti/common-clj "41.74.74-2"]
                 [overtone/at-at "1.4.65"]]

  :profiles {:dev {:test-paths   ^:replace ["test/unit" "test/integration" "test/helpers"]

                   :plugins      [[lein-cloverage "1.2.4"]
                                  [com.github.clojure-lsp/lein-clojure-lsp "1.4.15"]
                                  [com.github.liquidz/antq "RELEASE"]]

                   :dependencies [[hashp "0.2.2"]]

                   :injections   [(require 'hashp.core)]

                   :aliases      {"clean-ns"     ["clojure-lsp" "clean-ns" "--dry"] ;; check if namespaces are clean
                                  "format"       ["clojure-lsp" "format" "--dry"] ;; check if namespaces are formatted
                                  "diagnostics"  ["clojure-lsp" "diagnostics"]
                                  "lint"         ["do" ["clean-ns"] ["format"] ["diagnostics"]]
                                  "clean-ns-fix" ["clojure-lsp" "clean-ns"]
                                  "format-fix"   ["clojure-lsp" "format"]
                                  "lint-fix"     ["do" ["clean-ns-fix"] ["format-fix"]]}}})

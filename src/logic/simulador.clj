(ns yes-she-codes.simulador
  (:require [clojure.string :as str]
            [java-time :as t]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn csv-data[caminho-arquivo]
  (with-open [reader (io/reader caminho-arquivo)]
    (doall (next
      (csv/read-csv reader)))))

(def repositorio-de-compras (atom []))

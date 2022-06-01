(ns yes-she-codes.logic.compra
  (:require [java-time :as t]
            [clojure.string :as str]
            [yes-she-codes.util.listas :refer :all]
            [yes-she-codes.util.arquivo :refer :all]
            [yes-she-codes.logic.cartao :refer :all]
            [yes-she-codes.models.compra :refer :all])
  (:import (yes_she_codes.models.compra Compra)))

(defn data->compra [data valor estabelecimento categoria cartao]
  (Compra.
    (t/local-date data)
    (bigdec valor )
    (str/lower-case estabelecimento)
    (str/lower-case categoria)
    (Long/parseLong (str/replace cartao " " ""))))

(defn csv->lista-compras [lista-compra caminho-arquivo]
  (doseq [[data valor estabelecimento categoria cartao] (csv-data caminho-arquivo)]
    (->> (data->compra data valor estabelecimento categoria cartao)
         (insere-item! lista-compra))))

(defmethod valida-item Compra [compra]
  (if-let [cartao (first (item-lista? (:cartao compra)))]
    (and
      (t/before?  (:data compra) (t/local-date))
      (decimal? (:valor compra))
      (> (:valor compra) 0)
      (> (count (:estabelecimento compra)) 2)
      (not-every? #(= % (:categoria compra)) ["alimentação" "automóvel" "casa" "educação" "lazer" "saúde"])
      (valida-item cartao))))

(defn total-gasto [lista-compras]
  (double (apply + (map :valor lista-compras))))

(defn busca-compras-mes [mes lista-compras]
  (filter #(= (t/year-month (:data %)) (t/year-month mes )) lista-compras))

(defn busca-compras-estabelecimento [estabelecimento lista-compras]
  (filter #(= (:estabelecimento %) (str/lower-case estabelecimento)) lista-compras))

(defn total-gasto-no-mes [cartao mes lista-compras]
  (total-gasto (busca-compras-mes mes (filter #(= (:cartao %) cartao) lista-compras))))

(defn intervalo-compras [minimo maximo lista-compras]
  (filter #(and (<= minimo (:valor %) ) (>= maximo (:valor %))) lista-compras))

(defn gasto-categoria [lista-compras]
  (map
    (fn [[grp-key values]]
      {:categoria grp-key
       :total (total-gasto values)})
    (group-by :categoria lista-compras)))
(ns yes-she-codes.compra
  (:require [java-time :as t]
            [clojure.string :as str]
            [yes-she-codes.cartao :refer :all]
            [yes-she-codes.cliente :refer :all]
            [yes-she-codes.simulador :refer :all]))

(defrecord compra [data valor estabelecimento categoria numero])

(defn compra? [compra]
    (if-let [cartao (first (cartao-lista? (:numero compra)))]
      (and
        (not (nil? (:data compra)))
        (not (nil? (:valor compra)))
        (not (nil? (:estabelecimento compra)))
        (not (nil? (:categoria compra)))
        (cartao? [(:numero cartao) (:cvv cartao) (:validade cartao) (:limite cartao) (:cliente cartao)]))))

(defn insere-compra [lista-compra compra]
  (if (compra? compra)
    (conj lista-compra compra)
    (throw (ex-info "Compra invalida: cliente não possui cpf" {:compra compra}))))

(defn lista-compras [parametro-compra]
  (map nova-compra parametro-compra))

(defn data->nova-compra [data]
  (if-let [[data valor estabelecimento categoria numero] data]
    (nova-compra [(t/local-date "yyyy-MM-dd" data)
                  (Double/parseDouble valor)
                  (str/lower-case estabelecimento)
                  (str/lower-case categoria)
                  (Long/parseLong (str/replace numero " " ""))
                  ])))

(defn csv->lista-compras [caminho-arquivo]
  (map data->nova-compra (csv-data caminho-arquivo)))


; ------ PESQUISA NAS COMPRAS
(defn total-gasto [lista-compras]
  (double (apply + (map :valor lista-compras))))

(defn busca-compras-mes [mes lista-compras]
  (filter #(= (t/year-month (:data %)) (t/year-month mes )) lista-compras))

(defn busca-compras-estabelecimento [estabelecimento lista-compras]
  (filter #(= (:estabelecimento %) (str/lower-case estabelecimento)) lista-compras))

(defn total-gasto-no-mes [cartao mes lista-compras]
  (->> lista-compras
       (filter #(= (:cartao %) cartao) ,,,)
       (busca-compras-mes mes ,,,)
       (total-gasto ,,,)))

(defn intervalo-compras [minimo maximo lista-compras]
  (filter #(and (<= minimo (:valor %) ) (>= maximo (:valor %))) lista-compras))

(defn gasto-categoria [lista-compras]
  (map
    (fn [[grp-key values]]
      {:categoria grp-key
       :total (total-gasto values)})
    (group-by :categoria lista-compras)))
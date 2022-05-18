(ns logic.cartao
  (:require [java-time :as t]
            [logic.cliente :refer :all]
            [models.cartao :refer :all]
            [logic.arquivo :refer :all]
            [clojure.string :as str])
  (:import (models.cartao Cartao)))


(defn cartao? [cartao]
    (let [cliente (first (cliente-lista? (:cliente cartao)))]
      (and
        (not (nil? (:numero cartao)))
        (not (nil? (:cvv cartao)))
        (not (nil? (:validade cartao)))
        (not (nil? (:limite cartao)))
        (cliente? cliente))))

(defn insere-cartao [id cartao]
  (assoc cartao :id id)
  )

(defn insere-cartao! [lista-cartao cartao]
  (if (cartao? cartao)
    (swap! lista-cartao conj (insere-cartao (count @lista-cartao) cartao))
    (throw (ex-info "Cartão invalido" {:cartao cartao}))))

(defn lista-cartao! [lista-cartao]
  (println @lista-cartao))

(defn data->cartao [numero cvv validade limite cpf]
  (Cartao.
    (Long/parseLong (str/replace numero " " ""))
    (Integer/parseInt cvv)
    (t/year-month validade)
    (Double/parseDouble (str/replace limite "." ""))
    cpf))

(defn csv->lista-cartoes [lista-cartao caminho-arquivo]
  (doseq [[numero cvv validade limite cpf] (csv-data caminho-arquivo)]
    (->> (data->cartao numero cvv validade limite cpf)
         (insere-cartao! lista-cartao)))
  lista-cartao)

(defmulti cartao-lista? (fn [i & other] (coll? i)))

(defmethod cartao-lista? true [lista-cartao numero]
  (filter #(= (:numero %) numero) lista-cartao))

(defmethod cartao-lista? false [numero]
  (filter #(= (:numero %) numero) @(csv->lista-clientes  (atom []) "arquivos/cartoes.csv")))

(defn pesquisa-cartao-por-id [lista-cartao id]
  (nth lista-cartao id))

(defn pesquisa-cartao-por-id! [lista-cartao id]
  (pesquisa-cartao-por-id @lista-cartao id))

(defn exclui-cartao [lista-cartao id]
  (concat (subvec lista-cartao 0 id)
          (subvec lista-cartao (inc id))))

(defn exclui-cartao! [lista-cartao id]
  (reset! lista-cartao (atom (exclui-cartao @lista-cartao id))))

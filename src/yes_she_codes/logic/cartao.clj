(ns yes-she-codes.logic.cartao
  (:require [java-time :as t]
            [yes-she-codes.models.cartao :refer :all]
            [yes-she-codes.util.arquivo :refer :all]
            [yes-she-codes.util.listas :refer :all]
            [yes-she-codes.logic.cliente :refer :all]
            [clojure.string :as str])
  (:import (yes_she_codes.models.cartao Cartao)))

(defn data->cartao [numero cvv validade limite cpf]
  (Cartao.
    (Long/parseLong (str/replace numero " " ""))
    (Integer/parseInt cvv)
    (t/year-month validade)
    (bigdec (str/replace limite "." ""))
    cpf))

(defn csv->lista-cartoes [lista-cartao caminho-arquivo]
  (doseq [[numero cvv validade limite cpf] (csv-data caminho-arquivo)]
    (->> (data->cartao numero cvv validade limite cpf)
         (insere-item! lista-cartao)))
  lista-cartao)

(defmethod item-lista? Long [numero]
  (filter #(= (:numero %) numero) @(csv->lista-cartoes  (atom []) "arquivos/cartoes.csv")))

(defmethod valida-item Cartao [cartao]
  (let [cliente (first (item-lista? (:cliente cartao)))]
    (and
      (not (nil? (:numero cartao)))
      (not (nil? (:cvv cartao)))
      (not (nil? (:validade cartao)))
      (not (nil? (:limite cartao)))
      (valida-item cliente))))


(ns yes-she-codes.logic.cliente
  (:require [clojure.string :as str]
            [yes-she-codes.models.cliente :refer :all]
            [yes-she-codes.util.arquivo :refer :all]
            [yes-she-codes.util.listas :refer :all])
  (:import (yes_she_codes.models.cliente Cliente)))

(defn data->cliente [nome cpf email]
  (Cliente.
    (str/lower-case nome)
    cpf
    (str/lower-case email)))

(defn csv->lista-clientes [lista-cliente caminho-arquivo]
  (doseq [[nome cpf email] (csv-data caminho-arquivo)]
    (->> (data->cliente nome cpf email)
        (insere-item! lista-cliente)))
  lista-cliente)

(defmethod item-lista? String [cpf]
  (filter #(= (:cpf %) cpf) @(csv->lista-clientes  (atom []) "arquivos/clientes.csv")))

(defmethod valida-item Cliente [cliente]
  (and (not (nil? (:nome cliente)))
       (not (nil? (:cpf cliente)))
       (not (nil? (:email cliente)))))
(ns logic.cliente
  (:require [clojure.string :as str]
            [models.cliente :refer :all]
            [util.arquivo :refer :all]
            [util.listas :refer :all])
  (:import (models.cliente Cliente)))

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
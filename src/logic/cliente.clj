(ns logic.cliente
  (:require [clojure.string :as str]
            [models.cliente :refer :all]
            [logic.arquivo :refer :all])
  (:import (models.cliente Cliente)))

(defn cliente? [cliente]
  (and (not (nil? (:nome cliente)))
       (not (nil? (:cpf cliente)))
       (not (nil? (:email cliente)))))

(defn insere-cliente [id cliente]
  (assoc cliente :id id)
  )

(defn insere-cliente! [lista-cliente cliente]
  (if (cliente? cliente)
    (swap! lista-cliente conj (insere-cliente (count @lista-cliente) cliente))
    (throw (ex-info "Cliente invalido" {:cliente cliente}))))

(defn data->cliente [nome cpf email]
  (Cliente.
    (str/lower-case nome)
    cpf
    (str/lower-case email)))

(defn csv->lista-clientes [lista-cliente caminho-arquivo]
  (doseq [[nome cpf email] (csv-data caminho-arquivo)]
    (->> (data->cliente nome cpf email)
        (insere-cliente! lista-cliente)))
  lista-cliente
  )

(defmulti cliente-lista? (fn [i & other] (coll? i)))

(defmethod cliente-lista? true [lista-cliente cpf]
  (filter #(= (:cpf %) cpf) lista-cliente))

(defmethod cliente-lista? false [cpf]
  (filter #(= (:cpf %) cpf) @(csv->lista-clientes  (atom []) "arquivos/clientes.csv")))

(defn lista-cliente! [lista-cliente]
  (println @lista-cliente))

(defn pesquisa-cliente-por-id [lista-cliente id]
  (nth lista-cliente id))

(defn pesquisa-cliente-por-id! [lista-cliente id]
  (pesquisa-cliente-por-id @lista-cliente id))

(defn exclui-cliente [lista-cliente id]
  (concat (subvec lista-cliente 0 id)
          (subvec lista-cliente (inc id))))

(defn exclui-cliente! [lista-cliente id]
  (reset! lista-cliente (atom (exclui-cliente @lista-cliente id))))
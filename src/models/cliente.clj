(ns models.cliente
  (:require [schema.core :as s]))

(s/defrecord Cliente [
                      nome  :- (s/pred #(> (count %) 2) 'str-nome->-2-caracteres)
                      cpf   :- (s/pred #(re-matches #"(\d{3}\.){2}\d{3}\-\d{2}" %) 'str-cpf-valido)
                      email :- (s/pred #(re-matches #".+@.+\.com(\..+)?" %) 'str-email-valido)])


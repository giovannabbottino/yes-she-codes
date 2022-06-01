(ns yes-she-codes.models.cartao
  (:require [java-time :as t]
            [schema.core :as s])
  (:import (java.time YearMonth)))

(s/defrecord Cartao [numero   :- (s/constrained long #(<= 0 % 10000000000000000) 'long-0-a-1T)
                     cvv      :- (s/constrained int #(<= 0 % 999) 'int-0-a-999)
                     validade :- YearMonth
                     limite   :- (s/constrained BigDecimal #(<= 0 %) 'bigdec-0)
                     cliente  :- (s/pred #(re-matches #"(\d{3}\.){2}\d{3}\-\d{2}" %) 'str-cpf-valido)])
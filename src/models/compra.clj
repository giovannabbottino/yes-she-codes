(ns models.compra
  (:require [schema.core :as s]
            [java-time :as t])
  (:import (java.time LocalDate)))

(s/defrecord Compra [data            :- (s/pred #(t/before? % (t/local-date)))
                     valor           :- (s/constrained BigDecimal #(<= 0 %) 'bigdec-0)
                     estabelecimento :- (s/pred #(> (count %) 2) 'str-nome->-2-caracteres)
                     categoria       :- (s/enum "alimentação" "automóvel" "casa" "educação" "lazer" "saúde")
                     cartao          :- (s/constrained long #(<= 0 % 10000000000000000) 'long-0-a-1T)])
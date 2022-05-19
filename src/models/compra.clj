(ns models.compra
  (:import (java.time LocalDate)))

(defrecord Compra [^LocalDate data
                   ^BigDecimal valor
                   ^String estabelecimento
                   ^String categoria
                   ^BigInteger cartao])
(ns models.cartao
  (:import (java.time YearMonth)))

(defrecord Cartao [^BigInteger numero
                   ^Integer cvv
                   ^YearMonth validade
                   ^BigDecimal limite
                   ^String cliente])

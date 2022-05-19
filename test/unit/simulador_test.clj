(ns yes-she-codes.simulador-test
  (:require [clojure.test :refer :all]
            [yes-she-codes.simulador :refer :all]
            [yes-she-codes.compra :refer :all]
            [yes-she-codes.cartao :refer :all]
            [yes-she-codes.cliente :refer :all]))

(deftest repositorio-de-compras-test
  (testing "Testando simbolo repositorio de compras atom"
    (is (instance? clojure.lang.Atom repositorio-de-compras))))



(ns unit.cartao-test
  (:require [clojure.test :refer :all]
            [util.listas :refer :all]
            [logic.cartao :refer :all]
            [models.cartao :refer :all]
            [java-time :as t]))

(deftest insere-item!-test
  (testing "Testando insere-item! multiplas vezes"
    (are [parametro-cartao esperado] (= esperado (insere-item! (atom []) parametro-cartao))
                                     (->Cartao 1234123412341234 111 (t/year-month "2023-01") 1000.0 "000.111.222-33")
                                     [(insere-item
                                        0 (->Cartao 1234123412341234 111 (t/year-month "2023-01") 1000.0 "000.111.222-33"))]
                                     (->Cartao 4321432143214321 222 (t/year-month "2024-02") 2000.0 "333.444.555-66")
                                     [(insere-item
                                        0 (->Cartao 4321432143214321 222 (t/year-month "2024-02") 2000.0 "333.444.555-66"))]
                                     (->Cartao 1598159815981598 333 (t/year-month "2021-03") 3000.0 "666.777.888-99")
                                     [(insere-item
                                        0 (->Cartao 1598159815981598 333 (t/year-month "2021-03") 3000.0 "666.777.888-99"))]
                                     (->Cartao 6655665566556655 444 (t/year-month "2025-04") 4000.0 "666.777.888-99")
                                     [(insere-item
                                        0 (->Cartao 6655665566556655 444 (t/year-month "2025-04") 4000.0 "666.777.888-99"))]
                                     (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78")
                                     [(insere-item
                                        0 (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78"))]))
  (testing "Testando com parametro invalido"
    (are [parametro-cartao] (thrown? Exception (insere-item! (atom []) parametro-cartao))
                            nil
                            (->Cartao 4321432143214321 222 "2024-02" 2000 nil))))

(deftest cartao-lista?-test
  (testing "Testando se o cartao esta na lista"
    (is (= (item-lista? 3939393939393939) [(insere-item
                                             4
                                             (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78") )]))))

(deftest pesquisa-cartao-por-id!-test
  (testing "Testando se a pesquisa do cartao na lista por id"
    (let [repositorio-de-cartao (atom [])]
      (csv->lista-cartoes repositorio-de-cartao "arquivos/cartoes.csv")
      (is (= (pesquisa-item-por-id! repositorio-de-cartao 4) (insere-item
                                                               4
                                                               (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78") ))))))

(deftest exclui-cartao!-test
  (testing "Testando a exclusao do cartao na lista por id"
    (let [repositorio-de-cartao (atom [])]
      (csv->lista-cartoes repositorio-de-cartao "arquivos/cartoes.csv")
      (is (= @(exclui-item! repositorio-de-cartao 4) [ (insere-item
                                                         0
                                                         (->Cartao 1234123412341234 111 (t/year-month "2023-01") 1000.0 "000.111.222-33"))
                                                      (insere-item
                                                        1
                                                        (->Cartao 4321432143214321 222 (t/year-month "2024-02") 2000.0 "333.444.555-66"))
                                                      (insere-item
                                                        2
                                                        (->Cartao 1598159815981598 333 (t/year-month "2021-03") 3000.0 "666.777.888-99"))
                                                      (insere-item
                                                        3
                                                        (->Cartao 6655665566556655 444 (t/year-month "2025-04") 4000.0 "666.777.888-99"))])))))

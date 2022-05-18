(ns unit.cartao-test
  (:require [clojure.test :refer :all]
            [logic.cartao :refer :all]
            [models.cartao :refer :all]
            [java-time :as t]))

(deftest novo-cartao-test
  (testing "Testando novo cartao multiplas vezes"
    (are [parametro-cartao esperado] (= esperado (insere-cartao! (atom []) parametro-cartao))
                                     (->Cartao 1234123412341234 111 (t/year-month "2023-01") 1000.0 "000.111.222-33")
                                     [{:numero   1234123412341234
                                      :cvv      111
                                      :validade (t/year-month "2023-01")
                                      :limite   1000.0
                                      :cliente  "000.111.222-33"
                                      :id 0}]
                                     (->Cartao 4321432143214321 222 (t/year-month "2024-02") 2000.0 "333.444.555-66")
                                     [{:numero   4321432143214321
                                      :cvv      222
                                      :validade (t/year-month "2024-02")
                                      :limite   2000.0
                                      :cliente  "333.444.555-66"
                                      :id 0}]
                                     (->Cartao 1598159815981598 333 (t/year-month "2021-03") 3000.0 "666.777.888-99")
                                     [{:numero   1598159815981598
                                      :cvv      333
                                      :validade (t/year-month "2021-03")
                                      :limite   3000.0
                                      :cliente  "666.777.888-99"
                                      :id 0}]
                                     (->Cartao 6655665566556655 444 (t/year-month "2025-04") 4000.0 "666.777.888-99")
                                     [{:numero   6655665566556655
                                      :cvv      444
                                      :validade (t/year-month "2025-04")
                                      :limite   4000.0
                                      :cliente  "666.777.888-99"
                                      :id 0}]
                                     (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78")
                                     [{:numero   3939393939393939
                                      :cvv      555
                                      :validade (t/year-month "2026-05")
                                      :limite   5000.0
                                      :cliente  "999.123.456-78"
                                      :id 0}]) )
  (testing "Testando com cliente invalido"
    (are [parametro-cartao] (thrown? Exception (insere-cartao! (atom []) parametro-cartao))
                            nil
                            (->Cartao 4321432143214321 222 "2024-02" 2000 nil))))

(deftest cartao-lista?-test
  (testing "Testando se o cartao esta na lista"
    (let [repositorio-de-cartao (atom [])]
      (csv->lista-cartoes repositorio-de-cartao "arquivos/cartoes.csv")
      (is (= (cartao-lista? @repositorio-de-cartao 3939393939393939) [(insere-cartao
                                                                        4
                                                                        (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78") )])))))

(deftest pesquisa-cartao-por-id!-test
  (testing "Testando se a pesquisa do cartao na lista por id"
    (let [repositorio-de-cartao (atom [])]
      (csv->lista-cartoes repositorio-de-cartao "arquivos/cartoes.csv")
      (is (= (pesquisa-cartao-por-id! repositorio-de-cartao 4) (insere-cartao
                                                                        4
                                                                        (->Cartao 3939393939393939 555 (t/year-month "2026-05") 5000.0 "999.123.456-78") ))))))

(deftest exclui-cartao!-test
  (testing "Testando a exclusao do cartao na lista por id"
    (let [repositorio-de-cartao (atom [])]
      (csv->lista-cartoes repositorio-de-cartao "arquivos/cartoes.csv")
      (is (= (exclui-cartao! repositorio-de-cartao 4) [ (insere-cartao
                                                          0
                                                          (->Cartao 1234123412341234 111 (t/year-month "2023-01") 1000.0 "000.111.222-33"))
                                                       (insere-cartao
                                                         1
                                                         (->Cartao 4321432143214321 222 (t/year-month "2024-02") 2000.0 "333.444.555-66"))
                                                       (insere-cartao
                                                         2
                                                         (->Cartao 1598159815981598 333 (t/year-month "2021-03") 3000.0 "666.777.888-99"))
                                                       (insere-cartao
                                                         3
                                                         (->Cartao 6655665566556655 444 (t/year-month "2025-04") 4000.0 "666.777.888-99"))])))))

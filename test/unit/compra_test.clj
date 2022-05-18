(ns unit.compra-test
  (:require [clojure.test :refer :all]
            [logic.compra :refer :all]
            [models.compra :refer :all]
            [java-time :as t]))

(deftest insere-compra!-test
  (testing "Testando insere compra!  multiplas vezes"
    (are [parametro-compra esperado] (= esperado (insere-compra! (atom []) parametro-compra))
                                     (->Compra(t/local-date "yyyy-MM-dd" "2022-01-01")	129.90 "outback" "alimentação" 1234123412341234)
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-01") :valor 129.90 :estabelecimento "outback" :categoria "alimentação" :cartao 1234123412341234 :id 0}
                                     (->Compra(t/local-date "yyyy-MM-dd" "2022-01-02")	260.00 "dentista" "saúde" 1234123412341234)
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-02") :valor 260.00 :estabelecimento "dentista" :categoria "saúde" :cartao 1234123412341234 :id 0}
                                     (->Compra(t/local-date "yyyy-MM-dd" "2022-02-01")	20.00	 "cinema" "lazer" 1234123412341234)
                                     {:data (t/local-date "yyyy-MM-dd" "2022-02-01") :valor 20.00 :estabelecimento "cinema" :categoria "lazer" :cartao 1234123412341234 :id 0}
                                     (->Compra(t/local-date "yyyy-MM-dd" "2022-01-10")	150.00 "show" "lazer" 4321432143214321)
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-10") :valor 150.00 :estabelecimento "show" :categoria "lazer" :cartao 4321432143214321 :id 0}))
  (testing "Testando com cartao invalido"
    (are [parametro-compra] (thrown? Exception (insere-compra! (atom []) parametro-compra))
                            (->Compra "2022-01-02"	260.00 "Dentista" "Saúde" nil))))

(deftest csv->lista-compras-test
  (testing "Testando csv para lista-compras"
    (let [repositorio-de-compras (atom [])]
      (csv->lista-compras repositorio-de-compras "arquivos/compras.csv")
      (is (= @repositorio-de-compras
             [(insere-compra 0 (->Compra (t/local-date "2022-01-01") 129.9 "outback" "alimentação" 1234123412341234))
              (insere-compra 1 (->Compra (t/local-date "2022-01-02") 260.0 "dentista" "saúde" 1234123412341234))
              (insere-compra 2 (->Compra (t/local-date "2022-02-01") 20.0  "cinema" "lazer" 1234123412341234))
              (insere-compra 3 (->Compra (t/local-date "2022-01-10") 150.0  "show" "lazer" 4321432143214321))
              (insere-compra 4 (->Compra (t/local-date "2022-02-10") 289.99 "posto de gasolina" "automóvel " 4321432143214321))
              (insere-compra 5 (->Compra (t/local-date "2022-02-20") 79.9 "ifood" "alimentação" 4321432143214321))
              (insere-compra 6 (->Compra (t/local-date "2022-03-01") 85.0 "alura" "educação" 4321432143214321))
              (insere-compra 7 (->Compra (t/local-date "2022-01-30") 85.0 "alura" "educação" 1598159815981598))
              (insere-compra 8 (->Compra (t/local-date "2022-01-31") 350.0 "tok&stok" "casa" 1598159815981598))
              (insere-compra 9 (->Compra (t/local-date "2022-02-01") 400.0 "leroy merlin" "casa" 1598159815981598))
              (insere-compra 10 (->Compra (t/local-date "2022-03-01") 50.0 "madero" "alimentação" 6655665566556655 ))
              (insere-compra 11 (->Compra (t/local-date "2022-03-01") 70.0 "teatro" "lazer" 6655665566556655 ))
              (insere-compra 12 (->Compra (t/local-date "2022-03-04") 250.0 "hospital" "saúde" 6655665566556655 ))
              (insere-compra 13 (->Compra (t/local-date "2022-04-10") 130.0 "drogaria" "saúde" 6655665566556655 ))
              (insere-compra 14 (->Compra (t/local-date "2022-03-10") 100.0 "show de pagode" "lazer" 3939393939393939 ))
              (insere-compra 15 (->Compra (t/local-date "2022-03-11") 25.9 "dogão" "alimentação" 3939393939393939 ))
              (insere-compra 16 (->Compra (t/local-date "2022-03-12") 215.87 "praia" "lazer" 3939393939393939 ))
              (insere-compra 17 (->Compra (t/local-date "2022-04-01") 976.88 "oficina" "automóvel" 3939393939393939 ))
              (insere-compra 18 (->Compra (t/local-date "2022-04-10") 85.0 "alura" "educação" 3939393939393939 ))])))))

(def repositorio-de-compras (atom []))

(csv->lista-compras repositorio-de-compras "arquivos/compras.csv")

(deftest total-gasto-test
  (testing "Testando total gasto"
    (is (= (total-gasto @repositorio-de-compras)
           3753.44))))

(deftest busca-compras-mes-test
  (testing "Testando buscar compras por mes"
    (is (= (busca-compras-mes  "2022-01" @repositorio-de-compras)
           [(insere-compra
              0
              (->Compra (t/local-date "2022-01-01") 129.9 "outback" "alimentação" 1234123412341234))
            (insere-compra
              1
              (->Compra (t/local-date "2022-01-02") 260.0 "dentista" "saúde" 1234123412341234))
            (insere-compra
              3
              (->Compra (t/local-date "2022-01-10") 150.0 "show" "lazer" 4321432143214321))
            (insere-compra
              7
              (->Compra (t/local-date "2022-01-30") 85.0 "alura" "educação" 1598159815981598))
            (insere-compra
              8
              (->Compra (t/local-date "2022-01-31") 350.0 "tok&stok" "casa" 1598159815981598))]))))

(deftest busca-compras-estabelecimento-test
  (testing "Testando buscar compras por estabelecimento"
    (is (= (busca-compras-estabelecimento  "outback" @repositorio-de-compras)
           [(insere-compra
              0
              (->Compra (t/local-date "2022-01-01") 129.9 "outback" "alimentação" 1234123412341234))]))))

(deftest total-gasto-no-mes-test
  (testing "Testando total gasto no mes"
    (is (= (total-gasto-no-mes 1234123412341234 "2022-01"  @repositorio-de-compras)
           389.9 ))))

(deftest intervalo-compras-test
  (testing "Testando intervalo de valor de compras"
    (is (= (intervalo-compras 0 50 @repositorio-de-compras)
           [
            (insere-compra
              2
              (->Compra  (t/local-date "2022-02-01") 20.0 "cinema" "lazer" 1234123412341234))
            (insere-compra
              10
              (->Compra  (t/local-date "2022-03-01") 50.0 "madero" "alimentação" 6655665566556655))
            (insere-compra
              15
              (->Compra  (t/local-date "2022-03-11") 25.9 "dogão" "alimentação" 3939393939393939))]))))

(deftest gasto-categoria-test
  (testing "Testando categoria por teste"
    (is (= (gasto-categoria @repositorio-de-compras)
           [{:categoria "alimentação"
             :total 285.7}
            {:categoria "saúde"
             :total 640.0}
            {:categoria "lazer"
             :total 555.87}
            {:categoria "automóvel"
             :total 1266.87}
            {:categoria "educação"
             :total 255.0}
            {:categoria "casa"
             :total 750.0}]))))

(deftest pesquisa-compra-por-id!-test
  (testing "Testando se a pesquisa de compra na lista por id"
    (is (= (pesquisa-compra-por-id! repositorio-de-compras 0) (insere-compra
                                                                0
                                                                (->Compra (t/local-date "2022-01-01") 129.9 "outback" "alimentação" 1234123412341234))))))

(deftest exclui-compra!-test
  (testing "Testando a exclusao de compra na lista por id"
    (is (= @(exclui-compra! repositorio-de-compras 0) [(insere-compra 1 (->Compra (t/local-date "2022-01-02") 260.0 "dentista" "saúde" 1234123412341234))
                                                       (insere-compra 2 (->Compra (t/local-date "2022-02-01") 20.0  "cinema" "lazer" 1234123412341234))
                                                       (insere-compra 3 (->Compra (t/local-date "2022-01-10") 150.0  "show" "lazer" 4321432143214321))
                                                       (insere-compra 4 (->Compra (t/local-date "2022-02-10") 289.99 "posto de gasolina" "automóvel " 4321432143214321))
                                                       (insere-compra 5 (->Compra (t/local-date "2022-02-20") 79.9 "ifood" "alimentação" 4321432143214321))
                                                       (insere-compra 6 (->Compra (t/local-date "2022-03-01") 85.0 "alura" "educação" 4321432143214321))
                                                       (insere-compra 7 (->Compra (t/local-date "2022-01-30") 85.0 "alura" "educação" 1598159815981598))
                                                       (insere-compra 8 (->Compra (t/local-date "2022-01-31") 350.0 "tok&stok" "casa" 1598159815981598))
                                                       (insere-compra 9 (->Compra (t/local-date "2022-02-01") 400.0 "leroy merlin" "casa" 1598159815981598))
                                                       (insere-compra 10 (->Compra (t/local-date "2022-03-01") 50.0 "madero" "alimentação" 6655665566556655 ))
                                                       (insere-compra 11 (->Compra (t/local-date "2022-03-01") 70.0 "teatro" "lazer" 6655665566556655 ))
                                                       (insere-compra 12 (->Compra (t/local-date "2022-03-04") 250.0 "hospital" "saúde" 6655665566556655 ))
                                                       (insere-compra 13 (->Compra (t/local-date "2022-04-10") 130.0 "drogaria" "saúde" 6655665566556655 ))
                                                       (insere-compra 14 (->Compra (t/local-date "2022-03-10") 100.0 "show de pagode" "lazer" 3939393939393939 ))
                                                       (insere-compra 15 (->Compra (t/local-date "2022-03-11") 25.9 "dogão" "alimentação" 3939393939393939 ))
                                                       (insere-compra 16 (->Compra (t/local-date "2022-03-12") 215.87 "praia" "lazer" 3939393939393939 ))
                                                       (insere-compra 17 (->Compra (t/local-date "2022-04-01") 976.88 "oficina" "automóvel" 3939393939393939 ))
                                                       (insere-compra 18 (->Compra (t/local-date "2022-04-10") 85.0 "alura" "educação" 3939393939393939 ))]))))
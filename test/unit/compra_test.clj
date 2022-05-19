(ns unit.compra-test
  (:require [clojure.test :refer :all]
            [logic.listas :refer :all]
            [logic.compra :refer :all]
            [models.compra :refer :all]
            [java-time :as t]))

(deftest insere-item!-test
  (testing "Testando insere compra!  multiplas vezes"
    (are [parametro-compra esperado] (= esperado (insere-item! (atom []) parametro-compra))
                                     (->Compra(t/local-date "2022-01-01")	129.90M "outback" "alimentação" 1234123412341234)
                                     [(insere-item
                                        0 (->Compra(t/local-date "2022-01-01")	129.90M "outback" "alimentação" 1234123412341234))]
                                     (->Compra(t/local-date "2022-01-02")	260.00M "dentista" "saúde" 1234123412341234)
                                     [(insere-item
                                        0
                                        (->Compra(t/local-date "2022-01-02")	260.00M "dentista" "saúde" 1234123412341234))]
                                     (->Compra(t/local-date "2022-02-01")	20.00M	 "cinema" "lazer" 1234123412341234)
                                     [(insere-item
                                        0
                                        (->Compra(t/local-date "2022-02-01")	20.00M	 "cinema" "lazer" 1234123412341234))]
                                     (->Compra(t/local-date "2022-01-10")	150.00M "show" "lazer" 4321432143214321)
                                     [(insere-item
                                        0
                                        (->Compra(t/local-date "2022-01-10")	150.00M "show" "lazer" 4321432143214321))]))
  (testing "Testando com cartao invalido"
    (are [parametro-compra] (thrown? Exception (insere-item! (atom []) parametro-compra))
                            (->Compra "2022-01-02"	260.00M "Dentista" "Saúde" nil))))

(deftest csv->lista-compras-test
  (testing "Testando csv para lista-compras"
    (let [repositorio-de-compras (atom [])]
      (csv->lista-compras repositorio-de-compras "arquivos/compras.csv")
      (is (= @repositorio-de-compras
             [(insere-item 0 (->Compra (t/local-date "2022-01-01") 129.90M "outback" "alimentação" 1234123412341234))
              (insere-item 1 (->Compra (t/local-date "2022-01-02") 260.00M "dentista" "saúde" 1234123412341234))
              (insere-item 2 (->Compra (t/local-date "2022-02-01") 20.00M  "cinema" "lazer" 1234123412341234))
              (insere-item 3 (->Compra (t/local-date "2022-01-10") 150.00M  "show" "lazer" 4321432143214321))
              (insere-item 4 (->Compra (t/local-date "2022-02-10") 289.99M "posto de gasolina" "automóvel" 4321432143214321))
              (insere-item 5 (->Compra (t/local-date "2022-02-20") 79.90M "ifood" "alimentação" 4321432143214321))
              (insere-item 6 (->Compra (t/local-date "2022-03-01") 85.00M "alura" "educação" 4321432143214321))
              (insere-item 7 (->Compra (t/local-date "2022-01-30") 85.00M "alura" "educação" 1598159815981598))
              (insere-item 8 (->Compra (t/local-date "2022-01-31") 350.00M "tok&stok" "casa" 1598159815981598))
              (insere-item 9 (->Compra (t/local-date "2022-02-01") 400.00M "leroy merlin" "casa" 1598159815981598))
              (insere-item 10 (->Compra (t/local-date "2022-03-01") 50.00M "madero" "alimentação" 6655665566556655 ))
              (insere-item 11 (->Compra (t/local-date "2022-03-01") 70.00M "teatro" "lazer" 6655665566556655 ))
              (insere-item 12 (->Compra (t/local-date "2022-03-04") 250.00M "hospital" "saúde" 6655665566556655 ))
              (insere-item 13 (->Compra (t/local-date "2022-04-10") 130.00M "drogaria" "saúde" 6655665566556655 ))
              (insere-item 14 (->Compra (t/local-date "2022-03-10") 100.00M "show de pagode" "lazer" 3939393939393939 ))
              (insere-item 15 (->Compra (t/local-date "2022-03-11") 25.90M "dogão" "alimentação" 3939393939393939 ))
              (insere-item 16 (->Compra (t/local-date "2022-03-12") 215.87M "praia" "lazer" 3939393939393939 ))
              (insere-item 17 (->Compra (t/local-date "2022-04-01") 976.88M "oficina" "automóvel" 3939393939393939 ))
              (insere-item 18 (->Compra (t/local-date "2022-04-10") 85.00M "alura" "educação" 3939393939393939 ))])))))


(def repositorio-de-compras (atom []))

(csv->lista-compras repositorio-de-compras "arquivos/compras.csv")

(deftest total-gasto-test
  (testing "Testando total gasto"
    (is (= (total-gasto @repositorio-de-compras)
           3753.44))))

(deftest busca-compras-mes-test
  (testing "Testando buscar compras por mes"
    (is (= (busca-compras-mes  "2022-01" @repositorio-de-compras)
           [(insere-item
              0
              (->Compra (t/local-date "2022-01-01") 129.90M "outback" "alimentação" 1234123412341234))
            (insere-item
              1
              (->Compra (t/local-date "2022-01-02") 260.00M "dentista" "saúde" 1234123412341234))
            (insere-item
              3
              (->Compra (t/local-date "2022-01-10") 150.00M "show" "lazer" 4321432143214321))
            (insere-item
              7
              (->Compra (t/local-date "2022-01-30") 85.00M "alura" "educação" 1598159815981598))
            (insere-item
              8
              (->Compra (t/local-date "2022-01-31") 350.00M "tok&stok" "casa" 1598159815981598))]))))

(deftest busca-compras-estabelecimento-test
  (testing "Testando buscar compras por estabelecimento"
    (is (= (busca-compras-estabelecimento  "outback" @repositorio-de-compras)
           [(insere-item
              0
              (->Compra (t/local-date "2022-01-01") 129.90M "outback" "alimentação" 1234123412341234))]))))

(deftest total-gasto-no-mes-test
  (testing "Testando total gasto no mes"
    (is (= (total-gasto-no-mes 1234123412341234 "2022-01"  @repositorio-de-compras)
           389.9))))

(deftest intervalo-compras-test
  (testing "Testando intervalo de valor de compras"
    (is (= (intervalo-compras 0 50 @repositorio-de-compras)
           [
            (insere-item
              2
              (->Compra  (t/local-date "2022-02-01") 20.00M "cinema" "lazer" 1234123412341234))
            (insere-item
              10
              (->Compra  (t/local-date "2022-03-01") 50.00M "madero" "alimentação" 6655665566556655))
            (insere-item
              15
              (->Compra  (t/local-date "2022-03-11") 25.90M "dogão" "alimentação" 3939393939393939))]))))

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
    (is (= (pesquisa-item-por-id! repositorio-de-compras 0) (insere-item
                                                              0
                                                              (->Compra (t/local-date "2022-01-01") 129.90M "outback" "alimentação" 1234123412341234))))))

(deftest exclui-compra!-test
  (testing "Testando a exclusao de compra na lista por id"
    (is (= @(exclui-item! repositorio-de-compras 0) [(insere-item 1 (->Compra (t/local-date "2022-01-02") 260.00M "dentista" "saúde" 1234123412341234))
                                                     (insere-item 2 (->Compra (t/local-date "2022-02-01") 20.00M  "cinema" "lazer" 1234123412341234))
                                                     (insere-item 3 (->Compra (t/local-date "2022-01-10") 150.00M  "show" "lazer" 4321432143214321))
                                                     (insere-item 4 (->Compra (t/local-date "2022-02-10") 289.99M "posto de gasolina" "automóvel" 4321432143214321))
                                                     (insere-item 5 (->Compra (t/local-date "2022-02-20") 79.90M "ifood" "alimentação" 4321432143214321))
                                                     (insere-item 6 (->Compra (t/local-date "2022-03-01") 85.00M "alura" "educação" 4321432143214321))
                                                     (insere-item 7 (->Compra (t/local-date "2022-01-30") 85.00M "alura" "educação" 1598159815981598))
                                                     (insere-item 8 (->Compra (t/local-date "2022-01-31") 350.00M "tok&stok" "casa" 1598159815981598))
                                                     (insere-item 9 (->Compra (t/local-date "2022-02-01") 400.00M "leroy merlin" "casa" 1598159815981598))
                                                     (insere-item 10 (->Compra (t/local-date "2022-03-01") 50.00M "madero" "alimentação" 6655665566556655 ))
                                                     (insere-item 11 (->Compra (t/local-date "2022-03-01") 70.00M "teatro" "lazer" 6655665566556655 ))
                                                     (insere-item 12 (->Compra (t/local-date "2022-03-04") 250.00M "hospital" "saúde" 6655665566556655 ))
                                                     (insere-item 13 (->Compra (t/local-date "2022-04-10") 130.00M "drogaria" "saúde" 6655665566556655 ))
                                                     (insere-item 14 (->Compra (t/local-date "2022-03-10") 100.00M "show de pagode" "lazer" 3939393939393939 ))
                                                     (insere-item 15 (->Compra (t/local-date "2022-03-11") 25.90M "dogão" "alimentação" 3939393939393939 ))
                                                     (insere-item 16 (->Compra (t/local-date "2022-03-12") 215.87M "praia" "lazer" 3939393939393939 ))
                                                     (insere-item 17 (->Compra (t/local-date "2022-04-01") 976.88M "oficina" "automóvel" 3939393939393939 ))
                                                     (insere-item 18 (->Compra (t/local-date "2022-04-10") 85.00M "alura" "educação" 3939393939393939 ))]))))
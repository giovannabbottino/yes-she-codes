(ns yes-she-codes.compra-test
  (:require [clojure.test :refer :all]
            [yes-she-codes.compra :refer :all]
            [yes-she-codes.cartao :refer :all]
            [yes-she-codes.cliente :refer :all]
            [java-time :as t]))

(deftest nova-compra-test
  (testing "Testando nova compra multiplas vezes"
    (are [parametro-compra esperado] (= esperado (nova-compra parametro-compra))
                                     [(t/local-date "yyyy-MM-dd" "2022-01-01")	129.90 "outback" "alimentação" 1234123412341234]
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-01") :valor 129.90 :estabelecimento "outback" :categoria "alimentação" :cartao 1234123412341234}
                                     [(t/local-date "yyyy-MM-dd" "2022-01-02")	260.00 "dentista" "saúde" 1234123412341234]
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-02") :valor 260.00 :estabelecimento "dentista" :categoria "saúde" :cartao 1234123412341234}
                                     [(t/local-date "yyyy-MM-dd" "2022-02-01")	20.00	 "cinema" "lazer" 1234123412341234]
                                     {:data (t/local-date "yyyy-MM-dd" "2022-02-01") :valor 20.00 :estabelecimento "cinema" :categoria "lazer" :cartao 1234123412341234}
                                     [(t/local-date "yyyy-MM-dd" "2022-01-10")	150.00 "show" "lazer" 4321432143214321]
                                     {:data (t/local-date "yyyy-MM-dd" "2022-01-10") :valor 150.00 :estabelecimento "show" :categoria "lazer" :cartao 4321432143214321}))
  (testing "Testando com cartao invalido"
    (are [parametro-compra] (thrown? Exception (nova-compra parametro-compra))
                            ["2022-01-02"	260.00 "Dentista" "Saúde" nil])))

(deftest lista-compras-test
  (testing "Testando lista de compras"
    (is (= (csv->lista-compras "arquivos/compras.csv")
           [{:data (t/local-date "yyyy-MM-dd" "2022-01-01")
             :valor 129.9
             :estabelecimento "outback"
             :categoria "alimentação"
             :cartao 1234123412341234}
            {:data (t/local-date "yyyy-MM-dd" "2022-01-02")
             :valor 260.0
             :estabelecimento "dentista"
             :categoria "saúde"
             :cartao 1234123412341234}
            {:data (t/local-date "yyyy-MM-dd" "2022-02-01")
             :valor 20.0
             :estabelecimento "cinema"
             :categoria "lazer"
             :cartao 1234123412341234}
            {:data (t/local-date "yyyy-MM-dd" "2022-01-10")
             :valor 150.0
             :estabelecimento "show"
             :categoria "lazer"
             :cartao 4321432143214321}
            {:data (t/local-date "yyyy-MM-dd" "2022-02-10")
             :valor 289.99
             :estabelecimento "posto de gasolina"
             :categoria "automóvel"
             :cartao 4321432143214321}
            {:data (t/local-date "yyyy-MM-dd" "2022-02-20")
             :valor 79.9
             :estabelecimento "ifood"
             :categoria "alimentação"
             :cartao 4321432143214321}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-01")
             :valor 85.0
             :estabelecimento "alura"
             :categoria "educação"
             :cartao 4321432143214321}
            {:data (t/local-date "yyyy-MM-dd" "2022-01-30")
             :valor 85.0
             :estabelecimento "alura"
             :categoria "educação"
             :cartao 1598159815981598}
            {:data (t/local-date "yyyy-MM-dd" "2022-01-31")
             :valor 350.0
             :estabelecimento "tok&stok"
             :categoria "casa"
             :cartao 1598159815981598}
            {:data (t/local-date "yyyy-MM-dd" "2022-02-01")
             :valor 400.0
             :estabelecimento "leroy merlin"
             :categoria "casa"
             :cartao 1598159815981598}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-01")
             :valor 50.0
             :estabelecimento "madero"
             :categoria "alimentação"
             :cartao 6655665566556655}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-01")
             :valor 70.0
             :estabelecimento "teatro"
             :categoria "lazer"
             :cartao 6655665566556655}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-04")
             :valor 250.0
             :estabelecimento "hospital"
             :categoria "saúde"
             :cartao 6655665566556655}
            {:data (t/local-date "yyyy-MM-dd" "2022-04-10")
             :valor 130.0
             :estabelecimento "drogaria"
             :categoria "saúde"
             :cartao 6655665566556655}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-10")
             :valor 100.0
             :estabelecimento "show de pagode"
             :categoria "lazer"
             :cartao 3939393939393939}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-11")
             :valor 25.9
             :estabelecimento "dogão"
             :categoria "alimentação"
             :cartao 3939393939393939}
            {:data (t/local-date "yyyy-MM-dd" "2022-03-12")
             :valor 215.87
             :estabelecimento "praia"
             :categoria "lazer"
             :cartao 3939393939393939}
            {:data (t/local-date "yyyy-MM-dd" "2022-04-01")
             :valor 976.88
             :estabelecimento "oficina"
             :categoria "automóvel"
             :cartao 3939393939393939}
            {:data (t/local-date "yyyy-MM-dd" "2022-04-10")
             :valor 85.0
             :estabelecimento "alura"
             :categoria "educação"
             :cartao 3939393939393939}]))))

(deftest total-gasto-test
  (testing "Testando total gasto"
    (is (= (total-gasto (csv->lista-compras "arquivos/compras.csv"))
           3753.44))))

(deftest busca-compras-mes-test
  (testing "Testando buscar compras por mes"
    (is (= (busca-compras-mes  "2022-01" (csv->lista-compras "arquivos/compras.csv"))

           [{:cartao          1234123412341234
             :categoria       "alimentação"
             :data            (t/local-date "2022-01-01")
             :estabelecimento "outback"
             :valor           129.9}
            {:cartao          1234123412341234
             :categoria       "saúde"
             :data            (t/local-date "2022-01-02")
             :estabelecimento "dentista"
             :valor           260.0}
            {:cartao          4321432143214321
             :categoria       "lazer"
             :data            (t/local-date "2022-01-10")
             :estabelecimento "show"
             :valor           150.0}
            {:cartao          1598159815981598
             :categoria       "educação"
             :data            (t/local-date "2022-01-30")
             :estabelecimento "alura"
             :valor           85.0}
            {:cartao          1598159815981598
             :categoria       "casa"
             :data            (t/local-date "2022-01-31")
             :estabelecimento "tok&stok"
             :valor           350.0}]))))

(deftest busca-compras-estabelecimento-test
  (testing "Testando buscar compras por estabelecimento"
    (is (= (busca-compras-estabelecimento  "outback" (csv->lista-compras "arquivos/compras.csv"))
           [{:cartao          1234123412341234
             :categoria       "alimentação"
             :data            (t/local-date "2022-01-01")
             :estabelecimento "outback"
             :valor           129.9}]))))

(deftest total-gasto-no-mes-test
  (testing "Testando total gasto no mes"
    (is (= (total-gasto-no-mes 1234123412341234 "2022-01" (csv->lista-compras "arquivos/compras.csv"))
           389.9 ))))

(deftest intervalo-compras-test
  (testing "Testando intervalo de valor de compras"
    (is (= (intervalo-compras 0 50 (csv->lista-compras "arquivos/compras.csv"))
           [{:cartao          1234123412341234
             :categoria       "lazer"
             :data            (t/local-date "2022-02-01")
             :estabelecimento "cinema"
             :valor           20.0}
            {:cartao          6655665566556655
             :categoria       "alimentação"
             :data            (t/local-date "2022-03-01")
             :estabelecimento "madero"
             :valor           50.0}
            {:cartao          3939393939393939
             :categoria       "alimentação"
             :data            (t/local-date "2022-03-11")
             :estabelecimento "dogão"
             :valor           25.9}] ))))

(deftest gasto-categoria-test
  (testing "Testando categoria por teste"
    (is (= (gasto-categoria (csv->lista-compras "arquivos/compras.csv"))
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
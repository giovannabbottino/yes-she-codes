(ns unit.cliente_test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [schema.core :as s]
            [yes-she-codes.util.listas :refer :all]
            [yes-she-codes.logic.cliente :refer :all]
            [yes-she-codes.models.cliente :refer :all])
  (:import (yes_she_codes.models.cliente Cliente)))

(deftest insere-item-test
  (testing "Testando insere-cliente multiplas vezes"
    (are [parametro-cliente esperado] (= esperado (insere-item! (atom []) parametro-cliente))
                                      (->Cliente "feiticeira escarlate" "000.111.222-33" "feiticeira.poderosa@vingadoras.com.br")
                                      [#yes_she_codes.models.cliente.Cliente{:nome "feiticeira escarlate"
                                                                             :cpf "000.111.222-33"
                                                                             :email "feiticeira.poderosa@vingadoras.com.br"
                                                                             :id 0}]
                                      (->Cliente "viúva negra" "333.444.555-66" "viuva.casca.grossa@vingadoras.com.br")
                                      [#yes_she_codes.models.cliente.Cliente{:nome "viúva negra"
                                                                             :cpf "333.444.555-66"
                                                                             :email "viuva.casca.grossa@vingadoras.com.br"
                                                                             :id 0}]
                                      (->Cliente "hermione granger" "666.777.888-99" "hermione.salvadora@hogwarts.com")
                                      [#yes_she_codes.models.cliente.Cliente{:nome "hermione granger"
                                                                             :cpf "666.777.888-99"
                                                                             :email "hermione.salvadora@hogwarts.com"
                                                                             :id 0}]
                                      (->Cliente "daenerys targaryen" "999.123.456-78" "mae.dos.dragoes@got.com")
                                      [#yes_she_codes.models.cliente.Cliente{:nome "daenerys targaryen"
                                                                             :cpf "999.123.456-78"
                                                                             :email "mae.dos.dragoes@got.com"
                                                                             :id 0}]))
  (testing "Testando com cliente invalido"
    (is (thrown? Exception (insere-item! [] nil)))))

(deftest lista-clientes-test
  (testing "Testando lista de clientes"
    (let [repositorio-de-clientes (atom [])]
      (is (= @(csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv") [
                                                                                     #yes_she_codes.models.cliente.Cliente{:nome "feiticeira escarlate"
                                                                                                                           :cpf "000.111.222-33"
                                                                                                                           :email "feiticeira.poderosa@vingadoras.com.br"
                                                                                                                           :id 0}
                                                                                     #yes_she_codes.models.cliente.Cliente{:nome"viúva negra"
                                                                                                                           :cpf "333.444.555-66"
                                                                                                                           :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                                                           :id 1}
                                                                                     #yes_she_codes.models.cliente.Cliente{:nome "hermione granger"
                                                                                                                           :cpf "666.777.888-99"
                                                                                                                           :email "hermione.salvadora@hogwarts.com"
                                                                                                                           :id 2}
                                                                                     #yes_she_codes.models.cliente.Cliente{:nome "daenerys targaryen"
                                                                                                                           :cpf "999.123.456-78"
                                                                                                                           :email "mae.dos.dragoes@got.com"
                                                                                                                           :id 3}])))))

(deftest item-lista?-test
  (testing "Testando se o cliente esta na lista"
    (is (= (item-lista? "333.444.555-66") [#yes_she_codes.models.cliente.Cliente{:nome"viúva negra"
                                                                                 :cpf "333.444.555-66"
                                                                                 :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                 :id 1}]))))

(deftest pesquisa-cliente-por-id!-test
  (testing "Testando se a pesquisa do cliente pelo id"
    (let [repositorio-de-clientes (atom [])]
      (csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv")
      (is (= (pesquisa-item-por-id! repositorio-de-clientes 1) #yes_she_codes.models.cliente.Cliente{:nome"viúva negra"
                                                                                                     :cpf "333.444.555-66"
                                                                                                     :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                                     :id 1})))))

(deftest exclui-cliente!-test
  (testing "Testando excluir cliente pelo id"
    (let [repositorio-de-clientes (atom [])]
      (csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv")
      (is (= @(exclui-item! repositorio-de-clientes 1) [
                                                        #yes_she_codes.models.cliente.Cliente{:nome "feiticeira escarlate"
                                                                                              :cpf "000.111.222-33"
                                                                                              :email "feiticeira.poderosa@vingadoras.com.br"
                                                                                              :id 0}
                                                        #yes_she_codes.models.cliente.Cliente{:nome "hermione granger"
                                                                                              :cpf "666.777.888-99"
                                                                                              :email "hermione.salvadora@hogwarts.com"
                                                                                              :id 2}
                                                        #yes_she_codes.models.cliente.Cliente{:nome "daenerys targaryen"
                                                                                              :cpf "999.123.456-78"
                                                                                              :email "mae.dos.dragoes@got.com"
                                                                                              :id 3}])))))

(deftest validate-test
  (testing "Testa se o cliente enviado é realmente valido"
    (is (s/validate Cliente (->Cliente
                              (str/lower-case "Viúva Negra")
                              "333.444.555-66"
                              (str/lower-case "viuva.casca.grossa@vingadoras.com.br")))))
  (testing "Teste multipla vezes para cliente invalido"
    (are [parametro-cliente] (thrown? Exception (s/validate Cliente parametro-cliente))
                             [ (->Cliente
                                 nil
                                 "333.444.555-66"
                                 (str/lower-case "viuva.casca.grossa@vingadoras.com.br")
                                 )
                              (->Cliente
                                (str/lower-case "Viúva Negra")
                                "333-444-555-66"
                                (str/lower-case "viuva.casca.grossa@vingadoras.com.br")
                                )
                              (->Cliente
                                (str/lower-case "Viúva Negra")
                                "333.444.555-66"
                                (str/lower-case "viuva.casca.grossa.com.br"))])))
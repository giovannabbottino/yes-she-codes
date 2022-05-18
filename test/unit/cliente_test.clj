(ns unit.cliente_test
  (:require [clojure.test :refer :all]
            [logic.cliente :refer :all]
            [models.cliente :refer :all]))

(deftest insere-cliente-test
  (testing "Testando insere-cliente multiplas vezes"
    (are [parametro-cliente esperado] (= esperado (insere-cliente! (atom []) parametro-cliente))
                                      (->Cliente "feiticeira escarlate" "000.111.222-33" "feiticeira.poderosa@vingadoras.com.br")
                                      [#models.cliente.Cliente{:nome "feiticeira escarlate"
                                                               :cpf "000.111.222-33"
                                                               :email "feiticeira.poderosa@vingadoras.com.br"
                                                               :id 0}]
                                      (->Cliente "viúva negra" "333.444.555-66" "viuva.casca.grossa@vingadoras.com.br")
                                      [#models.cliente.Cliente{:nome "viúva negra"
                                                               :cpf "333.444.555-66"
                                                               :email "viuva.casca.grossa@vingadoras.com.br"
                                                               :id 0}]
                                      (->Cliente "hermione granger" "666.777.888-99" "hermione.salvadora@hogwarts.com")
                                      [#models.cliente.Cliente{:nome "hermione granger"
                                                               :cpf "666.777.888-99"
                                                               :email "hermione.salvadora@hogwarts.com"
                                                               :id 0}]
                                      (->Cliente "daenerys targaryen" "999.123.456-78" "mae.dos.dragoes@got.com")
                                      [#models.cliente.Cliente{:nome "daenerys targaryen"
                                                               :cpf "999.123.456-78"
                                                               :email "mae.dos.dragoes@got.com"
                                                               :id 0}]))
  (testing "Testando com cliente invalido"
    (is (thrown? Exception (insere-cliente! [] nil)))))


(deftest lista-clientes-test
  (testing "Testando lista de clientes"
    (let [repositorio-de-clientes (atom [])]
      (is (= @(csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv") [
                                                                                    #models.cliente.Cliente{:nome "feiticeira escarlate"
                                                                                                            :cpf "000.111.222-33"
                                                                                                            :email "feiticeira.poderosa@vingadoras.com.br"
                                                                                                            :id 0}
                                                                                    #models.cliente.Cliente{:nome"viúva negra"
                                                                                                            :cpf "333.444.555-66"
                                                                                                            :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                                            :id 1}
                                                                                    #models.cliente.Cliente{:nome "hermione granger"
                                                                                                            :cpf "666.777.888-99"
                                                                                                            :email "hermione.salvadora@hogwarts.com"
                                                                                                            :id 2}
                                                                                    #models.cliente.Cliente{:nome "daenerys targaryen"
                                                                                                            :cpf "999.123.456-78"
                                                                                                            :email "mae.dos.dragoes@got.com"
                                                                                                            :id 3}])))))

(deftest cliente-lista?-test
  (testing "Testando se o cliente esta na lista"
    (let [repositorio-de-clientes (atom [])]
      (csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv")
      (is (= (cliente-lista? @repositorio-de-clientes "333.444.555-66") [#models.cliente.Cliente{:nome"viúva negra"
                                                                                                 :cpf "333.444.555-66"
                                                                                                 :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                                 :id 1}])))))

(deftest pesquisa-cliente-por-id!-test
  (testing "Testando se a pesquisa do cliente pelo id"
    (let [repositorio-de-clientes (atom [])]
      (csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv")
      (is (= (pesquisa-cliente-por-id! repositorio-de-clientes 1) #models.cliente.Cliente{:nome"viúva negra"
                                                                                          :cpf "333.444.555-66"
                                                                                          :email "viuva.casca.grossa@vingadoras.com.br"
                                                                                          :id 1})))))

(deftest exclui-cliente!-test
  (testing "Testando excluir cliente pelo id"
    (let [repositorio-de-clientes (atom [])]
      (csv->lista-clientes repositorio-de-clientes "arquivos/clientes.csv")
      (is (= @(exclui-cliente! repositorio-de-clientes 1) [
                                                          #models.cliente.Cliente{:nome "feiticeira escarlate"
                                                                                  :cpf "000.111.222-33"
                                                                                  :email "feiticeira.poderosa@vingadoras.com.br"
                                                                                  :id 0}
                                                          #models.cliente.Cliente{:nome "hermione granger"
                                                                                  :cpf "666.777.888-99"
                                                                                  :email "hermione.salvadora@hogwarts.com"
                                                                                  :id 2}
                                                          #models.cliente.Cliente{:nome "daenerys targaryen"
                                                                                  :cpf "999.123.456-78"
                                                                                  :email "mae.dos.dragoes@got.com"
                                                                                  :id 3}])))))
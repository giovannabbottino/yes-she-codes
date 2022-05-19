(ns logic.simulador
  (:require [logic.listas :refer :all]
            [logic.cliente :refer :all]
            [logic.cartao :refer :all]
            [logic.compra :refer :all]
            [java-time :as t]))


(def repositorio-de-clientes (atom []))

(csv->lista-clientes repositorio-de-clientes "../../arquivos/clientes.csv")

(lista! repositorio-de-clientes)

(def repositorio-de-cartao (atom []))

(csv->lista-cartoes repositorio-de-cartao "../../arquivos/cartoes.csv")

(lista! repositorio-de-cartao)

(def repositorio-de-compras (atom []))

(csv->lista-compras repositorio-de-compras "../../arquivos/compras.csv")

(println "Total gasto" (total-gasto @repositorio-de-compras))

(println "Compras por mes" (busca-compras-mes (t/year-month "2022-01") @repositorio-de-compras))

(println "Busca compras por estabelecimento" (busca-compras-estabelecimento "OUTBACK" @repositorio-de-compras))

(println "Total gasto no mes" (total-gasto-no-mes 1234123412341234 "2022-01" @repositorio-de-compras))

(println "Intervalo de compras" (intervalo-compras 0 50 @repositorio-de-compras))

(println "Gasto por categoria" (gasto-categoria @repositorio-de-compras))
(ns models.cartao)

(defrecord Cartao [numero
                   cvv
                   validade
                   limite
                   cliente])

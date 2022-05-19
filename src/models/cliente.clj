(ns models.cliente)

(defrecord Cliente [^String nome
                    ^String cpf
                    ^String email])

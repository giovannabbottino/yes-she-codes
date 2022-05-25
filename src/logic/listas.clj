(ns logic.listas)

(defmulti item-lista? (fn [parametro] (class parametro)))

(defmulti valida-item (fn [parametro] (class parametro)))

(defn insere-item [id item]
  (assoc item :id id))

(defn insere-item! [lista item]
  (if (valida-item item)
    (swap! lista conj (insere-item (count @lista) item))
    (throw (ex-info "Invalido" {:item item}))))

(defn lista! [lista]
  (println @lista))

(defn pesquisa-item-por-id [lista id]
  (nth lista id))

(defn pesquisa-item-por-id! [lista id]
  (pesquisa-item-por-id @lista id))

(defn exclui-item [lista id]
  (concat (subvec lista 0 id)
          (subvec lista (inc id))))

(defn exclui-item! [lista id]
  (reset! lista (atom (exclui-item @lista id))))


(ns user)

(defn addPair [a b] (+ a b))
(defn identity2 [a] a)
(println ((identity2 addPair) 1 2))
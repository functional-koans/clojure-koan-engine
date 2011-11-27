(def one-tuple [[1]])

(meditations
 "We shall contemplate truth by testing reality, via equality."
 (= __ [["truth."]])

 "Queries produce results..."
 (?= __ (<- [?x]
            (one-tuple ?x)))

 "All sorts of sequences do the job."
 (?= __ (<- [?x]
            ([[1]] ?x)))

 "Lists, anyone?"
 (?= __ (<- [?x]
            ((list [1]) ?x)))

 "From whence do these tuples come?"
 (?= [[4]] (<- [?x]
               (__ ?x)))

 "Name something well to give it power."
 (?= one-tuple (<- [?x]
                   (one-tuple __)))

 "The name itself doesn't matter, just that it match."
 (?= one-tuple (<- [__]
                   (one-tuple __))))

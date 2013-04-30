-- Nonsence function which used explicit forall
g :: forall b c. (b -> c -> b)
g b _ = b

-- just define main for compiling
main = putStrLn ""
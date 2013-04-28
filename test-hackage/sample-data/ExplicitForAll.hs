-- Nonsence function which used explicit forall
g :: forall b. (b -> c -> b)
g b _ = b

-- just define main for compiling
main = putStrLn ""
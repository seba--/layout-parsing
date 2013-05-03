f :: Eq a => a -> Bool
f x = (x == x) || g True
  
g :: Ord a => a -> Bool
g y = (y <= y) || f True

main = putStrLn ""
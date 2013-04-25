module Main (main) where
 
listP :: [(Int, Int)]
listP = [(i, j) | i <- [1 .. 5], | j <- [1 .. 4]]
 
listP2 :: [((Int, Int), (Int, Int), (Int, Int))]
listP2
  = [((a, b), (c, d), (e, f)) | a <- [1 .. 2], |
     b <- [2 .. 3], | c <- [3 .. 4], | d <- [4 .. 5], |
     e <- [5 .. 6], | f <- [6 .. 7]]

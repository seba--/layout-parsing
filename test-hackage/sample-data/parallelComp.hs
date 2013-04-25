--listToString :: (a -> String) -> [a] -> String
--listToString f [] = []
--listToString f (x:xs) = (f x) ++ (listToString f xs)

--pairToString :: (a -> String) -> (a,a) -> String
--pairToString f (a,b) = "(" ++ (f a) ++ "," ++ (f b) ++ ")"

--intListToString :: [Int] -> String
--intListToString i = (listToString show i)

--intPairListToString :: [(Int, Int)] -> String
--intPairListToString p = (listToString (pairToString show) p)

--list :: [Int]
--list = [i | i <- [1..5]]
listP :: [(Int,Int)]
listP = [(i,j) | i <- [1..5] | j <- [1..4] ]
listP2 :: [((Int, Int), (Int, Int), (Int,Int))]
listP2 = [((a,b),(c,d),(e,f)) | a <- [1..2], b <- [2..3] | c <- [3..4], d <- [4..5] | e <- [5..6], f <- [6..7]]

main = do
       putStrLn("List   " ++ (intListToString list))
--        putStrLn("ListP  " ++ (intPairListToString listP))
        --putStrLn("ListP2 " ++ (listToString (tripleToString (pairToString show))) listP2)
                --where
                        --tripleToString :: (a -> String) -> ((a),(a),(a)) -> String
                        --tripleToString f (a,b,c) = "(" ++ (f a) ++ "," ++ (f b) ++ "," ++ (f c) ++ ")"
        
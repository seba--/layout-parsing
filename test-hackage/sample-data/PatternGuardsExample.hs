{-
    Funny function is an exmaple using default guards. What should the function do?
    If the half of the first double is equal to the fourth of the second, the function
    evaluates to the some of these numbers.
	Otherwise it should just evaluate to the sum of both numbers.
-}
funnySum :: Double -> Double -> Double
funnySum i j 	| halfEqualsFourth = halfi + fourthj
				| otherwise		   = i + j
	where
		halfi = i / 2
		fourthj = j / 4
		halfEqualsFourth = halfi == fourthj
		
{-
	Now we define funnySum with pattern guards as funnySum2.
	This function can only be compiled using -XPatternGuards
-}
funnySum2 :: Double -> Double -> Double
funnySum2 i j	| halfi <- i / 2
				, fourthj <- j / 4
				, halfi == fourthj
				= halfi + fourthj
funnySum2 i j	= i + j

-- Do Some examples
main = do
	putStrLn ("Funny(3,4)  = " ++ show (funnySum 3 4))
	putStrLn ("Funny(2,4)  = " ++ show (funnySum 2 4))
	putStrLn ("Funny2(3,4) = " ++ show (funnySum2 3 4))
	putStrLn ("Funny2(2,4) = " ++ show (funnySum2 2 4))
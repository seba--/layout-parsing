module Test (mean) where
mean :: [Double] -> Double
mean d = sum / length
	where
		calcSumLength :: [Double] -> (Double,Double)
		calcSumLength [] = (0,0)
		calcSumLength (x:xs) = (s+x,l+1)
			where
				(s,l) = calcSumLength xs
		(sum, length) = calcSumLength d
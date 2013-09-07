force :: Any -> IO (Capture Any)
force x@(Any xx) = do
   case y of
        _ | Left (_ :: RecConError) <- res -> return $ Missing x
        Right r -> return r
        Left f | not $ isAlgType x -> return $ f $ Value x
               | otherwise -> do
            cs <- mapM force $ children x
            return $ f $ Ctor x cs


{-- case y of
        _ | Left (_ :: RecConError) <- res -> return $ Missing x
        Right r -> return r
        Left f | not $ isAlgType x -> return $ f $ Value x
               | otherwise -> do
            cs <- mapM force $ children x
            return $ f $ Ctor x cs--}
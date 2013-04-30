f1 :: forall a b. a -> b -> a
f1 a _ = a


g1 :: forall a b. (Ord a, Eq  b) => a -> b -> a
g1 a _ = a


f2 :: (forall a. a->a) -> Int -> Int
f2 _ _= 0

g2 :: (forall a. Eq a => [a] -> a -> Bool) -> Int -> Int
g2 _ _ = 0

f3 :: ((forall a. a->a) -> Int) -> Bool -> Bool
f3 _ _ = False

f4 :: Int -> (forall a. a -> a)
f4 _ = f1

data T a = T1 (forall b. b -> b -> b) a
{-
data MonadT m = MkMonad { return :: forall a. a -> m a,
                          bind   :: forall a b. m a -> (a -> m b) -> m b
                        }-}

--newtype Swizzle = MkSwizzle (Ord a => [a] -> [a])

--T1 :: forall a. (forall b. b -> b -> b) -> a -> T a
-- MkMonad :: forall m. (forall a. a -> m a)
--                 -> (forall a b. m a -> (a -> m b) -> m b)
--                 -> MonadT m
--MkSwizzle :: (Ord a => [a] -> [a]) -> Swizzle

main = putStrLn ""
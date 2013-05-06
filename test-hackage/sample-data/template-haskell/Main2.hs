module Main2 where

import Expr

main :: IO ()
main = do { print $ eval [$expr|1 + 2|]
          ; case IntExpr 1 of
              { [$expr|'int:n|] -> print n
              ;  _              -> return ()
              }
          }
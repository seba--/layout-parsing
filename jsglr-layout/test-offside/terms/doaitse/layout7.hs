-- layout should be further indented than the enclosing context, but
-- is allowed within explicit contexts again
f x = let f x = 
            let{ f x = let 
        f x = 2 
                       in 3 
               } 
            in 4
      in 5

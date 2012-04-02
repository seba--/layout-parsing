-- layout can still have explicit semicolons
f x = let a = 1
          ; b = 2
          c = 3
      in x

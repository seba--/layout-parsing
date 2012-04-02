-- layout should be further indented than the enclosing context
f x = let h y = let
     p z = z
                in p
      in h
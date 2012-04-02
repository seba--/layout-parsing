-- layout can extend over multiple lines
test = let x = 1; y = x 
           z = f x
            y
           f x y = z in y

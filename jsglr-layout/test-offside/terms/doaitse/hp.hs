module Hp where

import Parsec hiding (Parser)
import qualified ParsecToken as P
import ParsecLanguage (haskellDef)



-----------------------------------------------------------
-- 
-----------------------------------------------------------
testFile fname
  = do{ input <- readFile ("c:\\docs\\cvs\\hp\\test\\" ++ fname ++ ".hs")
      ; test input
      }

test input
  = case (runParser pprogram (0,0) "" input) of
      Left err -> do{ putStr "parse error at "
                    ; print err
                    }
      Right x  -> print x



type Parser a = GenParser Char (Int,Int) a

-----------------------------------------------------------
-- 
-----------------------------------------------------------
pprogram :: Parser ()
pprogram
  = do{ whiteSpace
      ; pmodule
      ; eof
      }

pmodule :: Parser ()
pmodule
  = do{ reserved "module"
      ; identifier
      ; reserved "where"
      ; decls
      }
  <|> decls

decl :: Parser ()
decl
  = do{ identifier
      ; many identifier
      ; symbol "="
      ; expr
      }
  <?> "declaration"  

expr :: Parser ()
expr  
  = do{ reserved "let"
      ; decls
      ; reserved "in"
      ; expr
      }
  <|>
    do{ reserved "case"
      ; expr
      ; reserved "of"
      ; expr
      ; symbol "->"
      ; expr
      }
  <|> fexpr
  <?> "expression"
  
fexpr 
  = do{ many1 atom; return (); }

atom  
  =   do{ identifier; return () }
  <|> do{ natural; return () }
  <|> do{ symbol "("
        ; expr
        ; symbol ","
        ; expr
        ; symbol ")"
        ; return ()
        }
  <?> "atom"

-----------------------------------------------------------
-- 
-----------------------------------------------------------
decls
  = do{ symbol "{"
      ; withcontext (0,0) (do{ sepBy decl (symbol ";")
                             ; symbol "}"
                             })
      ; return ()
      }
  <|>
    do{ p@(c,l) <- pos
      ; (dc,dl) <- context
      ; if (c <= dc)
         then pzero
         else do{ withcontext p decl
                ; many (layoutSemiDecl p) 
                ; return () <?> "layout '}'"
                }
      }

layoutSemiDecl p@(dc,dl)
  = do{ (c,l) <- pos
      ; if (c < dc)
         then pzero
         else if (c == dc)
               then do{ optional (symbol ";"); withcontext (dc,l) decl }
               else do{ symbol ";"; withcontext (dc,l) decl }
      }


-----------------------------------------------------------
-- 
-----------------------------------------------------------
onside p
  = do{ (c,l)   <- pos
      ; (dc,dl) <- context
      ; if (dc == 0 || c > dc || dl == l) 
         then p 
         else fail ("token not onside (at context " ++ show dc ++ "," ++ show dl ++ ")")
      }

pos
  = do{ p <- getPosition
      ; return (sourceColumn p,sourceLine p)
      }

context
  = getState

setcontext c
  = setState c

withcontext c p
  = do{ dc <- context
      ; setcontext c 
      ; x <- p
      ; setcontext dc
      ; return x
      }


-----------------------------------------------------------
-- Tokens
-- Use qualified import to have token parsers on toplevel
-----------------------------------------------------------
haskell         = P.makeTokenParser haskellDef
    
whiteSpace      = P.whiteSpace haskell    

xsymbol         = P.symbol haskell    
xreserved       = P.reserved haskell    

symbol s        = onside $ xsymbol s
reserved s      = onside $ xreserved s
identifier      = onside $ P.identifier haskell    
natural         = onside $ P.natural haskell    


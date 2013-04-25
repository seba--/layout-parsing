-- Define a binary tree structure
data TNode a = Node (TNode a) (TNode a) | Leaf a

-- Define a structure which removes the content in the leafs
data TNodeView a = Branch (TNode a) (TNode a) | End

-- Define a function to convert a TNode to a TNodeView
-- Yes this is quite wrong (because the content of the Branch is not reduced, but anyway, we only need the syntax in examples)
view :: TNode a -> TNodeView a
view (Leaf a) = End
view (Node t1 t2) = (Branch t1 t2)

-- Calculate the maximum depth of the tree using ViewPatterns
maxDepth :: TNode a -> Int
maxDepth (view -> End) = 0
maxDepth (view -> Branch n1 n2)  = (max (maxDepth n1) (maxDepth n2)) + 1

-- Define an example tree
exampleTree :: TNode Char
exampleTree = (Node (Node (Leaf 'A') (Node (Leaf 'B') (Leaf 'C'))) (Node (Leaf 'D') (Leaf 'E')))

-- Output the result
main = do
	putStrLn (show (maxDepth exampleTree))
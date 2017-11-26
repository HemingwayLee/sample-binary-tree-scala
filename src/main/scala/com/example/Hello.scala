package com.example

object Hello {
	def Find(elem: Int, t: TreeNode): TreeNode = {
        if (t == null)
        {
            return null
        }

        if (elem < t.value)
        {
            return Find(elem, t.left)
        }
        else if (elem > t.value)
        {
            return Find(elem, t.right)
        }
        else
        {
            return t
        }
    }

    def FindMin(t: TreeNode): TreeNode = {
        if (t == null)
        {
            return null
        }
        else if (t.left == null)
        {
            return t
        }
        else
        {
            return FindMin(t.left)
        }
    }


    //Insert i into the tree t, duplicate will be discarded
    //Return a pointer to the resulting tree.                 
    def Insert(value: Int, t: TreeNode): TreeNode = {
        if (t == null)
        {
            var new_node: TreeNode = new TreeNode(value)
            return new_node
        }

        if (value < t.value)
        {
            t.left = Insert(value, t.left)
        }
        else if (value > t.value)
        {
            t.right = Insert(value, t.right)
        }
        else //duplicate, ignore it
        {
            return t
        }

        return t
    }

    //Deletes node from the tree
    // Return a pointer to the resulting tree
    def Remove(value: Int, tt: TreeNode): TreeNode = {
        if (tt == null)
        {
            return null
        }

        var t: TreeNode = tt
        var tmp_cell: TreeNode = null
        if (value < t.value)
        {
            t.left = Remove(value, t.left)
        }
        else if (value > t.value)
        {
            t.right = Remove(value, t.right)
        }
        else if (t.left != null && t.right != null)
        {
            tmp_cell = FindMin(t.right)
            t.value = tmp_cell.value
            t.right = Remove(t.value, t.right)
        }
        else
        {
            tmp_cell = t
            if (t.left == null)
                t = t.right
            else if (t.right == null)
                t = t.left
        }

        return t
    }

  	def main(args: Array[String]): Unit = {
	    var root: TreeNode = null

        val ascii: AsciiPrinter = new AsciiPrinter

        print("\nAfter inserting val 10..\n")
        root = Insert(10, root)
        ascii.PrintAsciiTree(root)

        print("\nAfter inserting val 5..\n")
        root = Insert(5, root)
        ascii.PrintAsciiTree(root)

        print("\nAfter inserting val 15..\n")
        root = Insert(15, root)
        ascii.PrintAsciiTree(root)

        print("\nAfter inserting vals 9, 13..\n")
        root = Insert(9, root)
        root = Insert(13, root)
        ascii.PrintAsciiTree(root)

        print("\nAfter inserting vals 2, 6, 12, 14, ..\n")
        root = Insert(2, root)
        root = Insert(6, root)
        root = Insert(12, root)
        root = Insert(14, root)
        ascii.PrintAsciiTree(root)

        print("\n\nAfter deleting a node (14) with no child..\n")
        root = Remove(14, root)
        ascii.PrintAsciiTree(root)

        print("\n\nAfter deleting a node (13) with left child..\n")
        root = Remove(13, root)
        ascii.PrintAsciiTree(root)

        print("\n\nAfter deleting a node (5) with left and right children..\n")
        root = Remove(5, root)
        ascii.PrintAsciiTree(root)

        print("\n\nAfter deleting a node (10, root node) with left and right children..\n")
        root = Remove(10, root)
        ascii.PrintAsciiTree(root)
  	}
}

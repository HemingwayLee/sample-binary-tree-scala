package com.example

class AsciiPrinter
{
    class AsciiNode
    {
        var left: AsciiNode = null
        var right: AsciiNode = null
        //length of the edge from this node to its children
        var edgeLength: Int = 0
        var height: Int = 0
        
        //-1=I am left, 0=I am root, 1=right   
        var parentDir: Int = 0

        var label: String = ""
    }

    private val INFINITY_NUM: Int = (1 << 20)
    
    //used for printing next node in the same level, this is the x coordinate of the next char printed
    private var m_printNext: Int = 0

    private val MAX_HEIGHT: Int = 1000

    private val m_lProfile = Array.fill(MAX_HEIGHT){0}
    private val m_rProfile = Array.fill(MAX_HEIGHT){0}

    //adjust gap between left and right nodes
    private val GAP: Int = 3

    //prints ascii tree for given Tree structure
    def PrintAsciiTree(t: TreeNode) {
        if (t == null) {
            return
        }

        var proot: AsciiNode = BuildAsciiTree(t)
        ComputeEdgeLengths(proot)

        for (i <- 0 to proot.height if i < MAX_HEIGHT) {
            m_lProfile(i) = INFINITY_NUM
        }

        ComputeLProfile(proot, 0, 0)

        var xMin: Int = 0
        for (i <- 0 to proot.height if i < MAX_HEIGHT) {
            xMin = Math.min(xMin, m_lProfile(i))
        }

        for (i <- 0 until proot.height)
        {
            m_printNext = 0
            PrintLevel(proot, -xMin, i)
            print("\n")
        }

        if (proot.height >= MAX_HEIGHT) {
            print("(This tree is taller than %d, and may be drawn incorrectly.)\n", MAX_HEIGHT)
        }
    }

    def BuildAsciiTreeRecursive(t: TreeNode): AsciiNode = {
        if (t == null) {
            return null
        }

        var node: AsciiNode = new AsciiNode
        node.left = BuildAsciiTreeRecursive(t.left)
        node.right = BuildAsciiTreeRecursive(t.right)

        if (node.left != null) {
            node.left.parentDir = -1
        }

        if (node.right != null) {
            node.right.parentDir = 1
        }

        node.label = t.value.toString
        
        return node
    }

    //Copy the tree into the ascii node structre
    def BuildAsciiTree(t: TreeNode): AsciiNode = {
        if (t == null) {
            return null
        }

        var node: AsciiNode = BuildAsciiTreeRecursive(t)
        node.parentDir = 0

        return node
    }

    //The following function fills in the m_lProfile array for the given tree.
    //It assumes that the center of the label of the root of this tree
    //is located at a position (x,y).  It assumes that the edgeLength
    //fields have been computed for this tree.
    def ComputeLProfile(node: AsciiNode, x: Int, y: Int) {
        if (node != null) {
            val isleft: Int = if (node.parentDir == -1) 1 else 0
            m_lProfile(y) = Math.min(m_lProfile(y), x - ((node.label.length - isleft) / 2))
            if (node.left != null)
            {
                for (i <- 1 to node.edgeLength if y + i < MAX_HEIGHT) {
                    m_lProfile(y + i) = Math.min(m_lProfile(y + i), x - i)
                }
            }

            ComputeLProfile(node.left, x - node.edgeLength - 1, y + node.edgeLength + 1)
            ComputeLProfile(node.right, x + node.edgeLength + 1, y + node.edgeLength + 1)
        }
    }

    def ComputeRProfile(node: AsciiNode, x: Int, y: Int) {
        if (node != null) {
            val notleft: Int = if (node.parentDir != -1) 1 else 0
            m_rProfile(y) = Math.max(m_rProfile(y), x + ((node.label.length - notleft) / 2))
            if (node.right != null)
            {
                for (i <- 1 to node.edgeLength if y + i < MAX_HEIGHT) {
                    m_rProfile(y + i) = Math.max(m_rProfile(y + i), x + i)
                }
            }

            ComputeRProfile(node.left, x - node.edgeLength - 1, y + node.edgeLength + 1)
            ComputeRProfile(node.right, x + node.edgeLength + 1, y + node.edgeLength + 1)
        }
    }

    //This function fills in the edgeLength and 
    //height fields of the specified tree
    def ComputeEdgeLengths(node: AsciiNode) {
        if (node == null) {
            return
        }

        ComputeEdgeLengths(node.left)
        ComputeEdgeLengths(node.right)

        /* first fill in the edgeLength of node */
        if (node.right == null && node.left == null)
        {
            node.edgeLength = 0
        }
        else
        {
            var hmin: Int = 0
            if (node.left != null)
            {
                for (i <- 0 to node.left.height if i < MAX_HEIGHT) {
                    m_rProfile(i) = -INFINITY_NUM
                }
                ComputeRProfile(node.left, 0, 0)
                hmin = node.left.height
            }
            else
            {
                hmin = 0
            }
            if (node.right != null)
            {
                for (i <- 0 to node.right.height if i < MAX_HEIGHT) {
                    m_lProfile(i) = INFINITY_NUM
                }
                ComputeLProfile(node.right, 0, 0)
                hmin = Math.min(node.right.height, hmin)
            }
            else
            {
                hmin = 0
            }

            var delta: Int = 4
            for (i <- 0 until hmin) {
                delta = Math.max(delta, GAP + 1 + m_rProfile(i) - m_lProfile(i))
            }

            //If the node has two children of height 1, then we allow the
            //two leaves to be within 1, instead of 2 
            if (((node.left != null && node.left.height == 1) ||
                (node.right != null && node.right.height == 1)) && delta>4)
            {
                delta = delta - 1
            }

            node.edgeLength = ((delta + 1) / 2) - 1
        }

        //now fill in the height of node
        var h: Int = 1
        if (node.left != null)
        {
            h = Math.max(node.left.height + node.edgeLength + 1, h)
        }
        if (node.right != null)
        {
            h = Math.max(node.right.height + node.edgeLength + 1, h)
        }

        node.height = h
    }

    //This function prints the given level of the given tree, assuming
    //that the node has the given x cordinate.
    def PrintLevel(node: AsciiNode, x: Int, level: Int) {
        if (node == null) {
            return
        }

        val isleft = if (node.parentDir == -1) 1 else 0
        if (level == 0)
        {
            var i: Int = 0
            while (i < (x - m_printNext - ((node.label.length - isleft) / 2))) {
                i = i + 1
                print(" ")
            }
            m_printNext += i
            print(node.label)
            m_printNext += node.label.length
        }
        else if (node.edgeLength >= level)
        {
            if (node.left != null)
            {
                var i: Int = 0
                while (i < (x - m_printNext - (level))) {
                    i = i + 1
                    print(" ")
                }
                m_printNext += i
                print("/")
                m_printNext += 1
            }
            if (node.right != null)
            {
                var i: Int = 0
                while (i < (x - m_printNext + (level))) {
                    i = i + 1
                    print(" ")
                }
                m_printNext += i
                print("\\")
                m_printNext += 1
            }
        }
        else
        {
            PrintLevel(node.left,
                x - node.edgeLength - 1,
                level - node.edgeLength - 1)
            PrintLevel(node.right,
                x + node.edgeLength + 1,
                level - node.edgeLength - 1)
        }
    }
}
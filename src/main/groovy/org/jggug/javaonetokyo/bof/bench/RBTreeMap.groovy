@Typed
package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    private Node root = Node.EMPTY

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
        root.toRootColor()
    }

    String get(String key) {
        if (key == null) throw new IAE("key is null")
        root.get(key)
    }

    int height() {
        root.height()
    }

    @Override
    String toString() {
        root.toString()
    }

    static newInstance() {
        new RBTreeMap()
    }
}

class Node {
    protected final static int BLACK = 1
    protected final static int RED = 0
    protected final static Node EMPTY = new Node(color:BLACK)

    int color
    String key
    String value
    Node left = EMPTY
    Node right = EMPTY

    String get(String key) {
        if (this == EMPTY) {
            return null
        }
        switch (this.key <=> key) {
            case  0: return value
            case  1: return left.get(key)
            case -1: return right.get(key)
        }
        assert false
    }

    int height() {
        if (this == EMPTY) {
            return 0
        }
        left.height() + color // because of BLACK = 1 and RED = 0
    }

    Node put(String key, String value) {
        //println ">"*10 + "put($key, $value)"
        //println this
        if (this == EMPTY) {
            return new Node(color:RED, key:key, value:value)
        }
        switch (this.key <=> key) {
            case  0:
                this.value = value
                return this
            case  1:
                left = left.put(key, value)
                return balanceLeft(this)
            case -1:
                right = right.put(key, value)
                return balanceRight(this)
        }
        assert false
    }

    void toRootColor() { setColor(BLACK) }

    @Override
    String toString() {
        toTreeString(0)
    }

    String toTreeString(int level) {
        def indent = " "
        def buff = []
        if (right != null && right != EMPTY) {
            buff << right.toTreeString(level + 1)
        }
        buff << indent * level + (color == BLACK ? 'B' : 'R') + "($key)"
        if (left != null && left != EMPTY) {
            buff << left.toTreeString(level + 1)
        }
        buff.join(System.getProperty("line.separator"))
    }

    private static Node rotateRight(Node node) {
        def left = node.left
        node.setLeft(left.right)
        left.setRight(node)
        left.setColor(node.color)
        node.setColor(RED)
        //println ">"*10 + "rotateRight"
        //println node
        return left
    }

    private static Node rotateLeft(Node node) {
        def right = node.right
        node.setRight(right.left)
        right.setLeft(node)
        right.setColor(node.color)
        node.setColor(RED)
        //println ">"*10 + "rotateLeft"
        //println node
        return right
    }

    private static Node balanceLeft(Node node) {
        //println ">"*10 + "balanceLeft:BEFORE"
        //println node
        Node right = node.right
        Node left = node.left
        if (node.color == BLACK && left.color == RED) {
            if (right.color == BLACK) {
                if (left.right.color == RED) {
                    node.setLeft(rotateLeft(left))
                }
                // both are black
                else if (left.left.color == BLACK) {
                    return node
                }
                node = rotateRight(node)
            }
            right = node.right
            left = node.left
            if (node.color == BLACK && right.color == RED && left.color == RED) {
                node.setColor(RED)
                left.setColor(BLACK)
                right.setColor(BLACK)
            }
        }
        //println ">"*10 + "balanceLeft:AFTER"
        //println node
        return node
    }

    private static Node balanceRight(Node node) {
        //println ">"*10 + "balanceRight:BEFORE"
        //println node
        Node right = node.right
        Node left = node.left
        if (node.color == BLACK && right.color == RED) {
            if (left.color == BLACK) {
                if (right.left.color == RED) {
                    node.setRight(rotateRight(right))
                }
                // both are black
                else if (right.right.color == BLACK) {
                    return node
                }
                node = rotateLeft(node)
            }
            right = node.right
            left = node.left
            if (node.color == BLACK && right.color == RED && left.color == RED) {
                node.setColor(RED)
                left.setColor(BLACK)
                right.setColor(BLACK)
            }
        }
        //println ">"*10 + "balanceRight:AFTER"
        //println node
        return node
    }
}

package br.pucrs.alest1.colecoes;

import java.util.LinkedList;
import java.util.Queue;

public class BinaryTreeOfString {

    public static final class Node {
        public Node father;
        public Node left;
        public Node right;
        public String element;

        public Node(String element) {
            this.father = null;
            this.left = null;
            this.right = null;
            this.element = element;
        }
    }

    private int count;
    private Node root;

    public BinaryTreeOfString() {
        this.count = 0;
        this.root = null;
    }

    public Node getRootNode() { return this.root; }
    public boolean isEmpty() { return root == null; }
    public int size() { return count; }

    public boolean addRoot(String element) {
        if (root != null) return false;
        root = new Node(element);
        count++;
        return true;
    }

    // Método de busca adaptado para String
    public Node searchNodeRef(String element, Node target) {
        if (target == null) return null;
        if (element != null && element.equals(target.element)) return target;

        Node aux = searchNodeRef(element, target.left);
        if (aux == null) {
            aux = searchNodeRef(element, target.right);
        }
        return aux;
    }

    public boolean addLeft(String element, String elemFather) {
        Node aux = searchNodeRef(elemFather, root);
        if (aux == null || aux.left != null) return false;

        Node n = new Node(element);
        n.father = aux;
        aux.left = n;
        count++;
        return true;
    }

    public boolean addRight(String element, String elemFather) {
        Node aux = searchNodeRef(elemFather, root);
        if (aux == null || aux.right != null) return false;

        Node n = new Node(element);
        n.father = aux;
        aux.right = n;
        count++;
        return true;
    }

    // --- Métodos Solicitados nas Consultas Estruturais ---

    public int height() {
        return heightAux(root);
    }

    private int heightAux(Node n) {
        if (n == null) return -1;
        return 1 + Math.max(heightAux(n.left), heightAux(n.right));
    }

    public int countLeaves() {
        return countLeavesAux(root);
    }

    private int countLeavesAux(Node n) {
        if (n == null) return 0;
        if (n.left == null && n.right == null) return 1;
        return countLeavesAux(n.left) + countLeavesAux(n.right);
    }

    public int countInternalNodes() {
        return size() - countLeaves();
    }

    // --- Percursos (Retornando strings nativas do Java para facilitar) ---

    public java.util.LinkedList<String> positionsPre() {
        java.util.LinkedList<String> lista = new java.util.LinkedList<>();
        positionsPreAux(root, lista);
        return lista;
    }

    private void positionsPreAux(Node n, java.util.LinkedList<String> lista) {
        if (n != null) {
            lista.add(n.element != null ? n.element : "[Vazio]");
            positionsPreAux(n.left, lista);
            positionsPreAux(n.right, lista);
        }
    }
}
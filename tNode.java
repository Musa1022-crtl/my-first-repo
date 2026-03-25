/*
 * tryBST.java
 * Author: Musa Mbhungana
 * Purpose: Construct a balanced binary search tree (BST), test it, remove even nodes, 
 *          and compute timing statistics.
 * Note: Consulted ChatGPT (GPT-5) for algorithm guidance.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class tNode {
    int data;
    tNode left, right;

    tNode(int data) {
        this.data = data;
        left = right = null;
    }
}

class BinarySearchTree {
    tNode root;

    // Insert a node into the BST
    void insert(int data) {
        root = insertRec(root, data);
    }

    private tNode insertRec(tNode node, int data) {
        if (node == null) return new tNode(data);
        if (data < node.data) node.left = insertRec(node.left, data);
        else if (data > node.data) node.right = insertRec(node.right, data);
        return node; // duplicates ignored
    }

    // Delete a node from the BST
    void delete(int data) {
        root = deleteRec(root, data);
    }

    private tNode deleteRec(tNode node, int data) {
        if (node == null) return null;

        if (data < node.data) node.left = deleteRec(node.left, data);
        else if (data > node.data) node.right = deleteRec(node.right, data);
        else {
            // Node with only one child or no child
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;

            // Node with two children: get inorder successor
            node.data = minValue(node.right);
            node.right = deleteRec(node.right, node.data);
        }
        return node;
    }

    private int minValue(tNode node) {
        int minVal = node.data;
        while (node.left != null) {
            minVal = node.left.data;
            node = node.left;
        }
        return minVal;
    }

    // Check if tree is a valid BST
    boolean isBST() {
        return isBSTUtil(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBSTUtil(tNode node, int min, int max) {
        if (node == null) return true;
        if (node.data <= min || node.data >= max) return false;
        return isBSTUtil(node.left, min, node.data) && isBSTUtil(node.right, node.data, max);
    }

    // Build balanced BST from sorted array using middle-element strategy
    void buildBalancedBST(int[] arr) {
        root = buildBalancedBSTRec(arr, 0, arr.length - 1);
    }

    private tNode buildBalancedBSTRec(int[] arr, int start, int end) {
        if (start > end) return null;
        int mid = start + (end - start) / 2;
        tNode node = new tNode(arr[mid]);
        node.left = buildBalancedBSTRec(arr, start, mid - 1);
        node.right = buildBalancedBSTRec(arr, mid + 1, end);
        return node;
    }

    // Remove all even numbers
    void removeEvenNodes() {
        root = removeEvenRec(root);
    }

    private tNode removeEvenRec(tNode node) {
        if (node == null) return null;
        node.left = removeEvenRec(node.left);
        node.right = removeEvenRec(node.right);
        if (node.data % 2 == 0) return deleteNodeReturnReplacement(node);
        return node;
    }

    private tNode deleteNodeReturnReplacement(tNode node) {
        if (node.left == null) return node.right;
        if (node.right == null) return node.left;
        // Node with two children: replace with inorder successor
        int successorVal = minValue(node.right);
        node.data = successorVal;
        node.right = deleteRec(node.right, successorVal);
        return node;
    }

    // In-order traversal (for debugging)
    void inorder() {
        inorderRec(root);
        System.out.println();
    }

    private void inorderRec(tNode node) {
        if (node == null) return;
        inorderRec(node.left);
        System.out.print(node.data + " ");
        inorderRec(node.right);
    }
}

public class tryBST {
    public static void main(String[] args) {
        final int n = 20; // change n<=7 first for testing
        int numKeys = 2 * n - 1;
        int repetitions = 30;

        // Prepare the sorted array [1..2n-1]
        int[] sortedArr = new int[numKeys];
        for (int i = 0; i < numKeys; i++) sortedArr[i] = i + 1;

        long totalPopulateTime = 0;
        long totalRemoveTime = 0;
        List<Long> populateTimes = new ArrayList<>();
        List<Long> removeTimes = new ArrayList<>();

        for (int i = 0; i < repetitions; i++) {
            BinarySearchTree bst = new BinarySearchTree();

            // Populate tree
            long startPopulate = System.currentTimeMillis();
            bst.buildBalancedBST(sortedArr);
            long endPopulate = System.currentTimeMillis();
            long populateDuration = endPopulate - startPopulate;
            populateTimes.add(populateDuration);
            totalPopulateTime += populateDuration;

            // Remove even numbers
            long startRemove = System.currentTimeMillis();
            bst.removeEvenNodes();
            long endRemove = System.currentTimeMillis();
            long removeDuration = endRemove - startRemove;
            removeTimes.add(removeDuration);
            totalRemoveTime += removeDuration;
        }

        // Compute averages
        double avgPopulate = totalPopulateTime / (double) repetitions;
        double avgRemove = totalRemoveTime / (double) repetitions;

        // Compute standard deviations
        double sdPopulate = computeSD(populateTimes, avgPopulate);
        double sdRemove = computeSD(removeTimes, avgRemove);

        // Display results
        System.out.println("Number of keys n: " + n);
        System.out.println("Method: Balanced BST build with middle insertion");
        System.out.printf("Populate tree -> Average time: %.2f ms, Standard deviation: %.2f ms%n", avgPopulate, sdPopulate);
        System.out.printf("Remove even numbers -> Average time: %.2f ms, Standard deviation: %.2f ms%n", avgRemove, sdRemove);
    }

    private static double computeSD(List<Long> times, double mean) {
        double sum = 0;
        for (long t : times) sum += Math.pow(t - mean, 2);
        return Math.sqrt(sum / times.size());
    }
}
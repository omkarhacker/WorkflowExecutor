# APIwiz Workflow Execution Assignment

## Problem Summary

Given a workflow as a directed graph (tree-like with possible converging paths), where each node performs a simple task (printing its name), we need to:

- Traverse the graph starting from the root node.
- Ensure a node is executed only **after all of its parent nodes** have been executed.
- If a node has **multiple children**, they must be executed **in parallel**.
- Each node should be executed only once.
- Output the printed node names in the order they are executed, followed by the total number of nodes.

---

## Approach

### 1. Graph Construction

- Parsed input into a map `nodeMap` from node IDs to `Node` objects.
- Created directed edges by connecting parents and children.
- Each node tracks how many of its parent nodes are still pending using `AtomicInteger pendingParents`.

### 2. Execution Strategy

- Used `ExecutorService` (`newCachedThreadPool`) for parallel execution of nodes.
- A node is only executed when its `pendingParents` reaches 0.
- Used `ConcurrentHashMap.newKeySet()` to track executed nodes to avoid duplicates.
- Used `CountDownLatch` to wait until all nodes finish execution before shutting down.

---

## Tools / Libraries Used

- Java Standard Library only.
- `java.util.concurrent`: `ExecutorService`, `CountDownLatch`, `AtomicInteger`, `ConcurrentHashMap`.

---

## Assumptions

- Input is read via standard input as described in the prompt.
- Node with ID `1` is always the starting (root) node.
- The structure is a DAG (Directed Acyclic Graph).
- Output order may vary where parallelism is involved, but dependencies are respected.

---

## How to Run

1. Compile:
   ```bash
   javac WorkflowExecutor.java

2. Run:
   ```bash
   java WorkflowExecutor.java

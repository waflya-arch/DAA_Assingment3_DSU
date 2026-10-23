# DAA Assignment 3: Graph Algorithms & Empirical Complexity Analysis

## Overview
This repository contains a complete Java implementation of foundational graph algorithms and data structures developed for **Assignment 3** in *Design and Analysis of Algorithms*.

The goal of this project is to implement, test, and empirically analyze core graph algorithms using hardware-independent **operation counters** (`edgeScans`, `dsuOps`, `comparisons`, `maxDepth`) alongside traditional execution metrics.

---

## Implemented Algorithms & Data Structures

1. **Graph Representation (`Graph`, `Edge`)**:
   * Custom Adjacency List supporting both directed and undirected graphs, self-loops, parallel edges, and disconnected vertices.
   * File parser for reading graphs in standard format (`n m directed`).

2. **Disjoint Set Union (`DisjointSetUnion`)**:
   * Features **Union by Rank** and **Path Compression** heuristics.
   * Maintains active component counts and operation metrics.

3. **Minimum Spanning Trees (MST)**:
   * **Kruskal's Algorithm (`Kruskal`)**: Edge sorting with DSU cycle detection and an early-exit optimization upon collecting $N-1$ spanning tree edges.
   * **Prim's Algorithm (`Prim`)**: Min-Heap PriorityQueue implementation supporting disconnected forest processing.

4. **Strongly Connected Components (SCC) & Topological Sorting**:
   * **Kosaraju's Algorithm (`Kosaraju`)**: Two-pass iterative DFS using an explicit user stack to ensure stack-safety on deep graph paths ($N = 100\,000$).
   * **Kahn's Algorithm (`Kahn`)**: In-degree queue processing with cycle detection capabilities.

5. **DAG Shortest Paths (`DagShortestPath`)**:
   * $O(V+E)$ single-pass relaxation following topological order.
   * Virtual super-source handling for disconnected DAGs, support for negative edge weights, and full path restoration via parent references.

---

## Project Structure

```text
├── data/                  # Sample input graph text files
├── results/
│   ├── results.csv        # Benchmark execution metrics (50 benchmark runs)
│   └── plots/             # Generated performance charts (.png)
├── src/
│   ├── main/java/         # Source code for algorithms, graph classes, generator, benchmark
│   │   ├── Benchmark.java
│   │   ├── DagShortestPath.java
│   │   ├── DisjointSetUnion.java
│   │   ├── Edge.java
│   │   ├── Graph.java
│   │   ├── GraphGenerator.java
│   │   ├── Kahn.java
│   │   ├── Kosaraju.java
│   │   ├── Kruskal.java
│   │   └── Prim.java
│   └── test/java/         # JUnit 5 unit, equivalence, and stack-safety tests
│       ├── DSUTest.java
│       ├── MSTTest.java
│       └── StackSafetyTest.java
├── generate_plots.py      # Python script for rendering performance plots
├── README.md              # Project setup and user guide
└── REPORT.md              # Analytical report with proofs and decision matrix
```

---

## Requirements & Prerequisites

* **Java Development Kit (JDK)**: Version 17 or higher
* **Build Tool**: Apache Maven (or Gradle)
* **Python**: Version 3.8+ with `pandas` and `matplotlib` installed for plot generation

To install Python dependencies:
```bash
pip install pandas matplotlib
```

---

## How to Build and Run

### 1. Build & Run Unit Tests
To compile the source code and run all JUnit 5 correctness, equivalence, and stack-safety tests:

```bash
mvn clean test
```

### 2. Run Benchmarks
To execute the benchmark suite across all graph configurations ($N \in [1000, 100000]$, $M \in [4N, 20N]$) and generate `results/results.csv`:

```bash
mvn exec:java -Dexec.mainClass="Benchmark"
```

> **Artifact Generated**: `results/results.csv` containing operation counts and exact results matching the required benchmark baseline.

### 3. Generate Performance Charts
To process `results/results.csv` and render high-resolution PNG plots:

```bash
python generate_plots.py
```

> **Artifacts Generated in `results/plots/`**:
> * `operations_vs_n.png` — Edge scans vs $N$ on Log-Log scale confirming linear slope $1.00$.
> * `scans_per_edge.png` — Ratio `edgeScans / m` vs $N$ showing flat lines at theoretical bounds ($2.0$ for Kosaraju, $1.0$ for Kahn/DAG).
> * `kruskal_edge_share.png` — Proportion of total edges inspected by Kruskal before MST completion.

---

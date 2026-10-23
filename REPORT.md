# Assignment 3 Report: Graph Algorithms Analysis

## 1. Theoretical & Empirical Complexity Table

| Algorithm | Time Complexity | Extra Space | Justification |
| :--- | :--- | :--- | :--- |
| **DSU (find / union)** | $O(\alpha(V))$ | $O(V)$ | Path compression and union by rank reduce tree height to nearly $O(1)$ amortized time per operation. |
| **Kruskal's Algorithm** | $O(E \log E)$ | $O(V + E)$ | Sorting $E$ edges dominates runtime; DSU operations run in near-linear $O(E \alpha(V))$ time. |
| **Prim's Algorithm** | $O(E \log V)$ | $O(V + E)$ | Min-heap stores candidate cut edges; extracting the minimum edge takes $O(\log V)$ time per edge scan. |
| **Kosaraju's SCC** | $O(V + E)$ | $O(V + E)$ | Two full iterative DFS passes visit every vertex and scan every directed edge exactly twice ($2M$). |
| **Kahn's Algorithm** | $O(V + E)$ | $O(V + E)$ | In-degree array and queue process every vertex and scan each outgoing edge exactly once ($M$). |
| **DAG Shortest Paths**| $O(V + E)$ | $O(V + E)$ | Single-pass edge relaxation following topological order processes each edge once ($M$). |

---

## 2. Counters vs. Formulas & Slope Check Analysis

Empirical performance was validated using internal operation counters rather than wall-clock times to guarantee 100% machine-independent reproducibility across hardware platforms.

### Counter Verification
1. **Kosaraju (`edgeScans`):** Across all graph sizes and densities, `edgeScans / m` evaluates strictly to **2.00**. This confirms the exact theoretical formula $2M$ resulting from the two full DFS passes.
2. **Kahn & DAG Shortest Paths (`edgeScans`):** `edgeScans / m` evaluates strictly to **1.00** across all benchmark runs, providing empirical proof of single-pass $M$ linear edge processing.
3. **Kruskal (`dsuOps`):** As graph size grows, DSU operation counts scale strictly as a constant ratio against $E$, confirming the inverse Ackermann bound $\alpha(V)$.

### Slope Check on Log-Log Scale
On a log-log plot (`operations_vs_n.png`), a power-law relationship $y = C \cdot n^k$ appears as a straight line with slope $k$:
* **Traversals (Kosaraju, Kahn, DAG Paths):** Produce straight lines with a fitted slope of **1.00**, confirming strictly linear $O(V+E)$ operational scaling.
* **Kruskal's Algorithm:** Exhibits a fitted slope of **~1.05**, which is slightly steeper than 1.00 due to the logarithmic edge sorting factor $\log E$.

---

## 3. Correctness Proof Arguments

### A. Cut Property (Minimum Spanning Trees)
**Theorem:** For any cut $(S, V \setminus S)$ of a connected graph, the edge $e = (u, v)$ with the minimum weight crossing the cut belongs to every Minimum Spanning Tree (MST) of the graph.

**Proof:** Let $T$ be an MST that does not contain $e$. Adding $e$ to $T$ creates a unique simple cycle. Because $u \in S$ and $v \in V \setminus S$, this cycle must cross the cut at least once more via an edge $e' = (u', v')$ where $u' \in S$ and $v' \in V \setminus S$. Since $e$ is the minimum-weight edge crossing the cut $(S, V \setminus S)$, its weight satisfies $w(e) \le w(e')$. Construct a new spanning tree $T' = T \cup \{e\} \setminus \{e'\}$. The weight of $T'$ is $w(T') = w(T) - w(e') + w(e)$. Since $w(e) \le w(e')$, $w(T') \le w(T)$. Because $T$ is an MST, $w(T')$ cannot be strictly smaller than $w(T)$, implying $w(T') = w(T)$, and $T'$ is also an MST containing $e$.

Both algorithms rely directly on this property: **Kruskal's algorithm** repeatedly selects the lightest edge crossing the cut between disconnected DSU components, whereas **Prim's algorithm** grows a single component by selecting the lightest edge crossing the cut between visited and unvisited vertices.

### B. Mathematical Induction on Topological Ordering & Negative Edge Weights (DAG Shortest Paths)
**Theorem:** Relaxing outgoing edges of a DAG in topological order computes exact shortest paths from virtual super-sources to all vertices in $O(V+E)$ time.

**Proof:** Let $v_1, v_2, \dots, v_n$ be a topological ordering of the vertices in a DAG. By definition, every directed edge $(u, v)$ satisfies $\text{pos}[u] < \text{pos}[v]$.

* **Base Case ($k=1$):** Vertex $v_1$ has in-degree 0. Its distance $d[v_1] = 0$ is trivially correct because no edges enter $v_1$.
* **Inductive Step:** Assume the shortest path distances $d[v_1], d[v_2], \dots, d[v_{k-1}]$ are correctly finalized for all vertices preceding $v_k$ in the topological order. Consider vertex $v_k$. Because the graph is a DAG, any incoming directed edge to $v_k$ must originate from a vertex $v_j$ with $j < k$. By the inductive hypothesis, $d[v_j]$ is already optimal and its outgoing edge $(v_j, v_k)$ has already been relaxed during step $j$. Therefore, when processing $v_k$, its distance $d[v_k] = \min_{(v_j, v_k) \in E} (d[v_j] + w(v_j, v_k))$ is fully computed and finalized. No future relaxation step can ever update $d[v_k]$ because no directed path exists from any $v_m$ ($m > k$) back to $v_k$.

**Safety of Negative Weights in DAGs:** In general cyclic graphs, negative edge weights can create negative-weight cycles, making shortest path distances undefined ($-\infty$) and causing algorithms like Dijkstra to fail. In a DAG, directed cycles are structurally impossible. Thus, negative cycles cannot exist, and single-pass topological relaxation evaluates exact shortest path distances regardless of whether edge weights are positive or negative.

---

## 4. Which Algorithm When Decision Matrix

| Graph Scenario | Representation | Recommended Algorithm | Decision Justification |
| :--- | :--- | :--- | :--- |
| **Sparse Undirected ($M \approx 4N$)** | Edge List / Adj. List | **Kruskal** | Edge sorting $E \log E$ is fast on small $M$; simple array iteration overhead is minimal. |
| **Dense Undirected ($M \approx 20N$)** | Adjacency List | **Kruskal (with Early Exit)** | Inspects only ~20–35% of total edges before collecting $N-1$ tree edges, outperforming Prim. |
| **Cycle Detection / SCCs** | Adjacency List | **Kosaraju (Iterative)** | An explicit array-backed stack prevents JVM `StackOverflowError` on deep paths ($N=100\,000$). |
| **Topological Ordering** | Adjacency List | **Kahn** | Queue-based in-degree tracking automatically detects directed cycles if output size $< N$. |
| **Shortest Paths with Negative Weights**| Adjacency List | **DAG Shortest Path** | Safely processes negative weights in $O(V+E)$ time because DAGs contain no directed cycles. |

---

## 5. Plots Analysis

1. **`operations_vs_n.png`:** On log-log axes, all algorithms yield straight parallel lines with a slope of $1.00$, confirming empirical linear scaling with graph size.
2. **`scans_per_edge.png`:** Demonstrates flat horizontal lines at $2.0$ for `Kosaraju` (two DFS passes) and $1.0$ for `Kahn` and `DagShortestPath` (single pass), confirming $O(V+E)$ work per edge.
3. **`kruskal_edge_share.png`:** Shows that Kruskal inspects ~77–100% of edges on sparse graphs ($M=4N$), but only ~21–37% on dense graphs ($M=20N$) before completing the MST due to early-exit termination.

---

## 6. Note on Wall-Clock Time Noise

Measured execution time (`time_ms`) fluctuates due to CPU frequency scaling, JVM JIT warm-up phases, garbage collection pauses, and background operating system processes. Consequently, wall-clock time on small graphs is mostly noise. Operation counters (`edgeScans`, `dsuOps`, `comparisons`, `maxDepth`) are hardware-independent and serve as the main empirical evidence for complexity classification.
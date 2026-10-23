import pandas as pd
import matplotlib.pyplot as plt
import os

os.makedirs('results/plots', exist_ok=True)
df = pd.read_csv('results/results.csv')

# --- GRAPH 1: Operations vs n (Разделение Sparse и Dense без зигзагов) ---
plt.figure(figsize=(9, 6))
# Фильтруем редкие графы m=4n для чистых линий без скачков
df_sparse = df[df['m'] == 4 * df['n']]

markers = {'Kruskal': 'o', 'Prim': 's', 'Kosaraju': '^', 'Kahn': 'v', 'DagShortestPath': 'D'}
linestyles = {'Kruskal': '-', 'Prim': '--', 'Kosaraju': '-.', 'Kahn': ':', 'DagShortestPath': '-'}

for alg in df['algorithm'].unique():
    sub = df_sparse[df_sparse['algorithm'] == alg]
    plt.plot(sub['n'], sub['edge_scans'], 
             label=alg, 
             marker=markers.get(alg, 'o'), 
             linestyle=linestyles.get(alg, '-'),
             linewidth=2)

plt.xscale('log')
plt.yscale('log')
plt.xlabel('n (Vertices)', fontsize=12)
plt.ylabel('Edge Scans', fontsize=12)
plt.title('Operations vs n (Sparse Graphs m=4n)', fontsize=14)
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend(fontsize=10)
plt.tight_layout()
plt.savefig('results/plots/operations_vs_n.png', dpi=300)
plt.close()

# --- GRAPH 2: Scans per Edge vs n ---
plt.figure(figsize=(9, 6))
for alg in df['algorithm'].unique():
    sub = df_sparse[df_sparse['algorithm'] == alg]
    scans_per_edge = sub['edge_scans'] / sub['m']
    plt.plot(sub['n'], scans_per_edge, 
             label=alg, 
             marker=markers.get(alg, 'o'), 
             linestyle=linestyles.get(alg, '-'),
             linewidth=2)

plt.xscale('log')
plt.xlabel('n (Vertices)', fontsize=12)
plt.ylabel('edgeScans / m', fontsize=12)
plt.title('Scans per Edge vs n (Flat Line = Linear Work)', fontsize=14)
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend(fontsize=10)
plt.tight_layout()
plt.savefig('results/plots/scans_per_edge.png', dpi=300)
plt.close()

# --- GRAPH 3: Share of Edges Kruskal Examines ---
plt.figure(figsize=(9, 6))
kruskal_df = df[df['algorithm'] == 'Kruskal']

for density in [4, 20]:
    group = kruskal_df[kruskal_df['m'] == density * kruskal_df['n']]
    label = f'Sparse (m=4n)' if density == 4 else f'Dense (m=20n)'
    plt.plot(group['n'], group['edge_scans'] / group['m'], 
             marker='o', linewidth=2, label=label)

plt.xscale('log')
plt.xlabel('n (Vertices)', fontsize=12)
plt.ylabel('edgeScans / m', fontsize=12)
plt.title('Share of Edges Kruskal Examines before MST Complete', fontsize=14)
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend(fontsize=11)
plt.tight_layout()
plt.savefig('results/plots/kruskal_edge_share.png', dpi=300)
plt.close()

print("Новые графики успешно сохранены в results/plots/")
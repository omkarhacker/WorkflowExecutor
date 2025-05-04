import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkflowExecutor {

    static class Node {
        int id;
        String name;
        List<Node> children = new ArrayList<>();
        AtomicInteger pendingParents = new AtomicInteger(0);

        Node(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static Map<Integer, Node> nodeMap = new HashMap<>();
    static Set<Integer> visited = ConcurrentHashMap.newKeySet();
    static ExecutorService executor = Executors.newCachedThreadPool();
    static CountDownLatch done;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());
        done = new CountDownLatch(n);

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().split(":");
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            nodeMap.put(id, new Node(id, name));
        }

        int m = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < m; i++) {
            String[] edge = sc.nextLine().split(":");
            int from = Integer.parseInt(edge[0].trim());
            int to = Integer.parseInt(edge[1].trim());

            Node parent = nodeMap.get(from);
            Node child = nodeMap.get(to);

            parent.children.add(child);
            child.pendingParents.incrementAndGet();
        }

        executeNode(nodeMap.get(1));

        try {
            done.await(); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        System.out.println(n);
    }

    static void executeNode(Node node) {
        executor.execute(() -> {
            
            if (!visited.add(node.id)) return;

        
            System.out.println(node.name);

        
            for (Node child : node.children) {
                if (child.pendingParents.decrementAndGet() == 0) {
                    executeNode(child);
                }
            }

            done.countDown();
        });
    }
}

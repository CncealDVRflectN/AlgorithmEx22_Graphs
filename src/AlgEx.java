import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class AlgEx {
    private static class Station {
        int index;
        int minIndexFrom;
        int type;
        double minCost;

        public Station() {
            index = -1;
            minIndexFrom = -1;
            type = -1;
            minCost = Double.MAX_VALUE;
        }

        public Station(int index, int minIndexFrom, int type, double minCost) {
            this.index = index;
            this.minIndexFrom = minIndexFrom;
            this.type = type;
            this.minCost = minCost;
        }
    }

    private static class Town {
        Station[] stations;
        List<Road> roads;

        public Town() {
            stations = new Station[2];
            stations[0] = new Station();
            stations[1] = new Station();
            roads = new ArrayList<>();
        }

        public void addRoad(Road road, int index) {
            roads.add(road);
            stations[road.type].index = index;
            stations[road.type].type = road.type;
        }
    }

    private static class Road {
        int indexTownA;
        int indexTownB;
        int type;
        int cost;

        public Road() {
            indexTownA = -1;
            indexTownB = -1;
            cost = -1;
            type = -1;
        }

        public Road(int indexTownA, int indexTownB, int type, int cost) {
            this.indexTownA = indexTownA;
            this.indexTownB = indexTownB;
            this.type = type;
            this.cost = cost;
        }
    }

    private static Town[] towns;
    private static int from;
    private static int to;
    private static Station stationCur;
    private static int indexTownTo;


    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        PrintWriter writer = new PrintWriter("output.txt");
        String[] buf;
        Road roadBuf;
        Queue<Station> stations = new PriorityQueue<>((a, b) -> Double.compare(a.minCost, b.minCost));
        int townsNum = Integer.parseInt(reader.readLine());
        int waysNum = Integer.parseInt(reader.readLine());
        towns = new Town[townsNum];
        for (int i = 0; i < townsNum; i++) {
            towns[i] = new Town();
        }
        for (int i = 0; i < waysNum; i++) {
            buf = reader.readLine().split(" ");
            from = Integer.parseInt(buf[0]) - 1;
            to = Integer.parseInt(buf[1]) - 1;
            roadBuf = new Road(from, to, Integer.parseInt(buf[2]), Integer.parseInt(buf[3]));
            towns[from].addRoad(roadBuf, from);
            towns[to].addRoad(roadBuf, to);
        }
        buf = reader.readLine().split(" ");
        from = Integer.parseInt(buf[0]) - 1;
        to = Integer.parseInt(buf[1]) - 1;
        if (from == to) {
            writer.println("Yes");
            writer.print(0);
            writer.close();
            reader.close();
            return;
        }
        towns[from].roads.forEach(road -> {
            indexTownTo = (road.indexTownA != from) ? road.indexTownA : road.indexTownB;
            stations.add(new Station(indexTownTo, from, road.type, 1.1 * road.cost));
        });
        towns[from].stations[0].minIndexFrom = from;
        towns[from].stations[1].minIndexFrom = from;
        while ((stationCur = stations.poll()) != null) {
            if (towns[stationCur.index].stations[stationCur.type].minCost == Double.MAX_VALUE) {
                towns[stationCur.index].stations[stationCur.type] = stationCur;
                towns[stationCur.index].roads.forEach(road -> {
                    indexTownTo = (road.indexTownA != stationCur.index) ? road.indexTownA : road.indexTownB;
                    if (towns[indexTownTo].stations[road.type].minCost == Double.MAX_VALUE) {
                        stations.add(new Station(indexTownTo, stationCur.index, road.type,
                                (stationCur.type == road.type) ? stationCur.minCost + road.cost : stationCur.minCost + road.cost * 1.1));
                    }
                });
            }
            if (stationCur.index == to) {
                break;
            }
        }
        if (towns[to].stations[0].minIndexFrom != -1 || towns[to].stations[1].minIndexFrom != -1) {
            writer.println("Yes");
            writer.format(Locale.US, "%.2f", Math.min(towns[to].stations[0].minCost, towns[to].stations[1].minCost));
        } else {
            writer.print("No");
        }
        reader.close();
        writer.close();
    }
}

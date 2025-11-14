package consistenthash;

public class Main {
    public static void main(String[] args) {
        ConsistentHash<String, String> consistentHash = new ConsistentHash<>();

        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);
        consistentHash.addRealNode(ConsistentHash.randomGenerateChar(),1500);


        System.out.println(consistentHash.getVirtualNodeHash(ConsistentHash.randomGenerateChar()));
        System.out.println(consistentHash.getVirtualNodeHash(ConsistentHash.randomGenerateChar()));
        System.out.println(consistentHash.getVirtualNodeHash(ConsistentHash.randomGenerateChar()));
        System.out.println(consistentHash.getVirtualNodeHash(ConsistentHash.randomGenerateChar()));
        System.out.println(consistentHash.getVirtualNodeHash(ConsistentHash.randomGenerateChar()));



    }
}

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class encoder {
	static Map<Integer, String> codetable = new HashMap<Integer, String>();
	static File file = null;

	public static void main(String args[]) throws IOException {
		file = new File(args[0]);
		HashMap<String, Integer> frequencytable = new HashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String a = line.trim();
			if (a.equals(""))
				continue;
			Integer count = frequencytable.get(a);
			if (count == null)
				frequencytable.put(a, 1);
			else {
				frequencytable.put(a, count + 1);
			}
		}
		long sftime = System.currentTimeMillis();
		FourWayCache fheap = null;
		BinaryHeap bheap = null;
		PairHeap pheap = null;
		for (int i = 0; i < 10; i++) {
			fheap = new FourWayCache(frequencytable.size());
			for (Map.Entry<String, Integer> entry : frequencytable.entrySet()) {
				fheap.insert(new Node(entry.getValue(), Integer.parseInt(entry.getKey())));
			}
			while (fheap.getSize() > 4) {
				Node n1 = fheap.delete(3);
				Node n2 = fheap.delete(3);
				Node n3 = new Node(n1.getFreq() + n2.getFreq(), -1);
				n3.addChild(n1);
				n3.addChild(n2);
				fheap.insert(n3);
			}
			genct(fheap.getStartNode(), "");
			fheap.setCodeTable(codetable);
		}
		long eftime = System.currentTimeMillis();
		long eTotaltime = eftime - sftime;
		System.out.println("total time for fourwayheap:" + eTotaltime);
		for (int i = 0; i < 10; i++) {
			bheap = new BinaryHeap(frequencytable.size());
			for (Map.Entry<String, Integer> entry : frequencytable.entrySet()) {
				bheap.insert(new Node(entry.getValue(), Integer.parseInt(entry.getKey())));
			}
			while (bheap.getSize() > 1) {
				Node n1 = bheap.delete(0);
				Node n2 = bheap.delete(0);
				Node n3 = new Node(n1.getFreq() + n2.getFreq(), -1);
				n3.addChild(n1);
				n3.addChild(n2);
				bheap.insert(n3);
			}
			genct(bheap.getStartNode(), "");
			bheap.setCodeTable(codetable);
		}
		long ebtime = System.currentTimeMillis();
		long ebTotaltime = ebtime - eftime;
		System.out.println("total time for binaryheap :" + ebTotaltime);
		for (int i = 0; i < 10; i++) {
			pheap = new PairHeap();
			for (Map.Entry<String, Integer> entry : frequencytable.entrySet()) {
				pheap.insert(new Node(entry.getValue(), Integer.parseInt(entry.getKey())));
			}
			pheap.encode();
			genct(pheap.getStartNode(), "");
			pheap.setCodeTable(codetable);
		}
		long eptime = System.currentTimeMillis();
		long epTotaltime = eptime - ebtime;
		System.out.println("total time for pairheap :" + epTotaltime);
		if ((eTotaltime < ebTotaltime) && (eTotaltime < epTotaltime)) {
			System.out.println("4way Heap");
			encode(fheap.getCodetable());
		} else if((ebTotaltime < eTotaltime) && (ebTotaltime < epTotaltime)){
			System.out.println("Binary Heap");
			encode(fheap.getCodetable());
		}else{
			System.out.println("pairing Heap");
			encode(fheap.getCodetable());
		}

	}

	private static void genct(Node n, String c) {
		if (n.isChildEmpty()) {
			codetable.put(n.getKey(), c);

		} else {
			genct(n.getChild(0), c + "0");
			genct(n.getChild(1), c + "1");
		}

	}

	private static void encode(Map<Integer, String> codeTable) throws IOException {
		String line;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter o = new BufferedWriter(new FileWriter(new File("code_table.txt")));
		for (Integer i : codeTable.keySet()) {
			o.write(i + " " + codeTable.get(i));
			o.newLine();
		}
		o.close();
		BufferedOutputStream encodedFile = new BufferedOutputStream(new FileOutputStream(new File("encoded.bin")));
		Hashtable<String, Byte> encodemap = buildencodebitmap();
		StringBuffer buffer = new StringBuffer();
		int cnt, remainder;
		while ((line = reader.readLine()) != null) {
			if (line.trim().equals("")) {
				continue;
			}
			buffer.append(codeTable.get(Integer.parseInt(line)));
			if (buffer.length() > 64000) {
				remainder = buffer.length() % 8;
				for (cnt = 0; cnt < buffer.length() - remainder; cnt += 8) {
					String strBits = buffer.substring(cnt, cnt + 8);
					byte realBits = encodemap.get(strBits);
					encodedFile.write(realBits);
				}
				if (remainder == 0) {
					buffer = new StringBuffer();
				} else {
					buffer = new StringBuffer(buffer.substring(cnt));
				}
			}
		}

		for (cnt = 0; cnt < buffer.length(); cnt += 8) {
			String strBits = buffer.substring(cnt, cnt + 8);
			if (strBits != null) {
				byte realBits = encodemap.get(strBits);
				encodedFile.write(realBits);
			}
		}

		encodedFile.close();
		reader.close();
	}

	private static Hashtable<String, Byte> buildencodebitmap() {
		Hashtable<String, Byte> encodingBitMap = new Hashtable<String, Byte>();
		for (int cnt = 0; cnt <= 255; cnt++) {
			StringBuffer workingBuf = new StringBuffer();
			int val = 128;
			while (val >= 1) {
				if ((cnt & val) > 0) {
					workingBuf.append("1");
				} else {
					workingBuf.append("0");
				}
				val = val / 2;
			}
			encodingBitMap.put(workingBuf.toString(), (byte) (cnt));
		}
		return encodingBitMap;
	}
}

class CodeTable {
	int value;
	String code;

	public CodeTable(int value, String code) {
		// TODO Auto-generated constructor stub
		this.value = value;
		this.code = code;
	}
}

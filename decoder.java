

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class decoder {

	private String backup = "";
	private static final String outfile = "decoded.txt";
	public static void main(String args[]) throws IOException {
		long startTime=System.currentTimeMillis();
		decoder dec = new decoder();
		dec.decode(args);
		System.out.println("time for decoding: "+ (System.currentTimeMillis()-startTime));
	}

	private void decode(String[] args) throws IOException, FileNotFoundException {
		Map<Byte, String> decodedbm = new HashMap<Byte, String>();
		String encodefile = args[0];
		String codetabfile = args[1];
		File file = new File(encodefile);
		Parse codetabparser = new Parse(codetabfile);
		Decnode root = codetabparser.parsectfile();
		BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
		decodebm(decodedbm);
		String path = "";
		if (file.getParentFile() != null) {
			path=file.getParentFile().getAbsolutePath()+"/";
		}
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(path + outfile));
		int i = 0, count;
		boolean flag = true;
		while (flag) {
			count = 0;
			byte[] byteData = new byte[64000];
			while (count < 64000 && (i = reader.read()) != -1) {
				byte b = (byte) i;
				byteData[count++] = b;
			}
			if (i == -1) {
				byteData = Arrays.copyOf(byteData, count);
				flag = false;
			}
			String decodedstr= bittobin(byteData, decodedbm);
			writer.write(bintostr(root, decodedstr));
		}
		writer.close();
		reader.close();
	}

	private void decodebm(Map<Byte, String> decodedbm) {
		for (int j = 0; j <= 255; j++) {
			StringBuffer workingBuf = new StringBuffer();
			int val = 128;
			while (val >= 1) {
				if ((j & val) > 0) {
					workingBuf.append("1");
				} else {
					workingBuf.append("0");
				}
				val = val / 2;
			}
			decodedbm.put((byte) (j), workingBuf.toString());
		}
	}

	private String bittobin(byte[] bdata, Map<Byte, String> decodedbm) {
		StringBuffer buffer = new StringBuffer();
		for (Byte element : bdata) {
			buffer.append(decodedbm.get(element));
		}
		return buffer.toString();
	}

	private String bintostr(Decnode r, String decstr) {
		int i = 0, cnt;
		Decnode n=r;
	StringBuffer outbuf = new StringBuffer();
	decstr=backup+decstr;
		for (cnt = 0; cnt < decstr.length(); cnt++) {
			char c = decstr.charAt(cnt);
			if (c == '0') {
				n = n.getLeft();
			} else
				n = n.getRight();

			if (n.getLeft() == null && n.getRight() == null) {
				outbuf.append(n.getElement()).append("\n");
				n = r;
				i = cnt;
			}
		}
	backup = decstr.substring(i + 1);
	return outbuf.toString();
	}

}
	

class Parse{
	String fileName;
	public Parse(String fileName) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
	}
	public Decnode parsectfile() throws IOException {
		String line;
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Map<String, String> codeTable = new HashMap<String, String>();
		while ((line = reader.readLine()) != null) {
			if (line.trim().equals("")) {
				continue;
			}
			String[] code = line.split(" ");
			codeTable.put(code[1].trim(), code[0].trim());
		}
		reader.close();
		Decnode r=new Decnode(0);
		for(String key:codeTable.keySet()){
			insert(r,key,codeTable.get(key),0,key.length()-1);
		}
		return r;
	}



	private Object insert(Decnode node, String key, String value, int i, int l) {
		if (i == l) {
			if (key.charAt(i) == '0') {
				node.setLeft(new Decnode(Integer.parseInt(value)));
			} else {
				node.setRight(new Decnode(Integer.parseInt(value)));
			}
			return 0;
		} else {
			if (key.charAt(i) == '0') {
				if (node.getLeft() == null) {
					node.setLeft(new Decnode(0));
				}
				return insert(node.getLeft(), key, value, i + 1, l);
			} else {
				if (node.getRight() == null) {
					node.setRight(new Decnode(0));
				}
				return insert(node.getRight(), key, value, i + 1, l);
			}

		}

	}
}

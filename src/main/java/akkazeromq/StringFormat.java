package akkazeromq;

public class StringFormat implements Writer<Object>, Reader<Object> {
	public byte[] write(Object obj) {
		return obj == null ? new byte[0] : obj.toString().getBytes();
	}

	public Object read(byte[] bytes) {
		return bytes == null ? "" : new String(bytes);
	}
}

package akkazeromq;

public interface Reader<T> {
	public T read(byte[] bytes);
}

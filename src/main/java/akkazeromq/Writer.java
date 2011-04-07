package akkazeromq;

public interface Writer<T> {
	public byte[] write(T t);
}
